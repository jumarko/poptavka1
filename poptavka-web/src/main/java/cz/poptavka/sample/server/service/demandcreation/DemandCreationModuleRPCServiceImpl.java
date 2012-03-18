/*
 * RPC trieda, ktora tvori rozhranie medzi GWT a Hibernate. Stara sa o metody z modulu
 * DemandCreation. Vsetky metody pre tento modul budu v tejto RPC metode. Detail objekty
 * sa mozu zdielat medzi viacerymi RPC servisami.
 */
package cz.poptavka.sample.server.service.demandcreation;

import com.google.common.base.Preconditions;
import com.googlecode.genericdao.search.Search;
import cz.poptavka.sample.client.service.demand.DemandCreationModuleRPCService;
import cz.poptavka.sample.domain.address.Address;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.demand.DemandStatus;
import cz.poptavka.sample.domain.user.BusinessUser;
import cz.poptavka.sample.domain.user.BusinessUserData;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.exception.MessageException;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.address.LocalityService;
import cz.poptavka.sample.service.demand.CategoryService;
import cz.poptavka.sample.service.demand.DemandService;
import cz.poptavka.sample.service.user.ClientService;
import cz.poptavka.sample.service.user.UserSearchCriteria;
import cz.poptavka.sample.shared.domain.AddressDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author praso
 */
public class DemandCreationModuleRPCServiceImpl extends AutoinjectingRemoteService
        implements DemandCreationModuleRPCService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DemandCreationModuleRPCServiceImpl.class);
    private DemandService demandService;
    private GeneralService generalService;
    private LocalityService localityService;
    private CategoryService categoryService;
    private ClientService clientService;

    @Autowired
    public void setDemandService(DemandService demandService) {
        this.demandService = demandService;
    }

    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    @Autowired
    public void setLocalityService(LocalityService localityService) {
        this.localityService = localityService;
    }

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Dorobit komentar.
     *
     * TODO - skontrolovat ci si butbe musime predavat parametre ako Long clientId.
     * BOlo by lepsie optimalizovat toto riesenie s vyuzitim GWT SPRING Security?
     *
     * @param detail
     * @param cliendId
     * @return
     */
    @Override
    public FullDemandDetail createNewDemand(FullDemandDetail detail, Long cliendId) {
        final Demand demand = new Demand();
        demand.setTitle(detail.getTitle());
        demand.setDescription(detail.getDescription());
        demand.setType(this.demandService.getDemandType(detail.getDemandType()));
        demand.setPrice(detail.getPrice());
        // if max suppliers has not been specified, default value is used. @See Demand#DEFAULT_MAX_SUPPLIERS
        if (maxOffersSpecified(detail)) {
            demand.setMaxSuppliers(detail.getMaxOffers());
        }
        demand.setMinRating(detail.getMinRating());
        demand.setStatus(DemandStatus.TEMPORARY);
        demand.setEndDate(detail.getEndDate());
        demand.setValidTo(detail.getValidToDate());
        demand.setClient(this.generalService.find(Client.class, cliendId));

        /** localities **/
        List<Locality> locs = new ArrayList<Locality>();
        for (String localityCode : detail.getLocalities().keySet()) {
            locs.add(getLocality(localityCode));
        }
        demand.setLocalities(locs);
        /** categories **/
        List<Category> categories = new ArrayList<Category>();
        for (Long categoryID : detail.getCategories().keySet()) {
            categories.add(getCategory(categoryID));
        }
        demand.setCategories(categories);

        Demand newDemandFromDB = demandService.create(demand);
        // TODO ivlcek - test sending demand to proper suppliers
        sendDemandToSuppliersTest(newDemandFromDB);
        return (FullDemandDetail) FullDemandDetail.createDemandDetail(
                newDemandFromDB);
    }

    private boolean maxOffersSpecified(FullDemandDetail detail) {
        return detail.getMaxOffers() > 0;
    }

    // TODO FIX this, it's not working nullPointerException. -- who use it anyway???
    public Locality getLocality(String code) {
        System.out.println("Locality code value: " + code + ", localityService is null? " + (localityService == null));
        return localityService.getLocality(code);
//        return localityService.getById(10);
    }

    public Category getCategory(Long id) {
        return categoryService.getById(id);
    }

    /**
     * Method creates a message that is associated with created demand. Message
     * is sent to all suppliers that complies with the demand criteria
     *
     * TODO design some heuristic to choose suitable suppliers to whom the
     * demand messages should be sent and possibly separate this heuristic
     *
     * @param demand
     */
    // TODO should send messages as we're sending messages to display potential demands. Beho
    //
    private void sendDemandToSuppliersTest(Demand demand) {
        // send message and handle exception if any
        try {
            this.demandService.sendDemandToSuppliers(demand);
        } catch (MessageException e) {
            LOGGER.error("Demand " + demand + " has not been sent to suppliers. "
                    + "The next try will be made by regular job.");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public boolean checkFreeEmail(String email) {
        // try to find user with given email
        final BusinessUser userByEmail = (BusinessUser) generalService.searchUnique(
                new Search(BusinessUser.class).addFilterEqual("email", email));

        // email is free if no such user exists
        return userByEmail == null;
    }

    /**
     * Vytvorenie noveho klienta.
     *
     */
    @Override
    public UserDetail createNewClient(UserDetail clientDetail) {
        Preconditions.checkNotNull(clientDetail);
        final Client newClient = new Client();
        /** Person is mandatory for person client and for company client as well. **/
        final BusinessUserData businessUserData = new BusinessUserData.Builder().companyName(
                clientDetail.getCompanyName()).
                personFirstName(clientDetail.getFirstName()).
                personLastName(clientDetail.getLastName()).
                phone(clientDetail.getPhone()).
                identificationNumber(clientDetail.getIdentificationNumber()).
                // TODO in builder crete setWebsite(String website)
                taxId(clientDetail.getTaxId()).build();
        newClient.getBusinessUser().setBusinessUserData(businessUserData);
        /** Address. **/
        final List<Address> addresses = new ArrayList<Address>();
        // TODO are addresses really required - if yes add check for not null,
        // otherwise following for-each throws NPE if clienDetail#getAddresses returns null
        if (clientDetail.getAddresses() != null) {
            for (AddressDetail detail : clientDetail.getAddresses()) {
                Locality city = this.getLocality(detail.getCityName());
                Address address = new Address();
                address.setCity(city);
                address.setStreet(detail.getStreet());
                address.setZipCode(detail.getZipCode());
                addresses.add(address);
            }
        }

        newClient.getBusinessUser().setAddresses(addresses);
        /** Login & pwd information. **/
        newClient.getBusinessUser().setEmail(clientDetail.getEmail());
        newClient.getBusinessUser().setPassword(clientDetail.getPassword());
        final Client newClientFromDB = clientService.create(newClient);
        return this.toUserDetail(newClientFromDB.getBusinessUser().getId(),
                newClientFromDB.getBusinessUser().getBusinessUserRoles());
    }

    @Override
    public UserDetail verifyClient(UserDetail clientDetail) {
        final List<Client> clientFromDB = this.clientService.searchByCriteria(
                UserSearchCriteria.Builder.userSearchCriteria().withEmail(
                clientDetail.getEmail()).withPassword(clientDetail.getPassword()).build());
        if (clientFromDB.isEmpty()) {
            return new UserDetail();
        } else {
            final Client user = clientFromDB.get(0);
            return toUserDetail(user.getBusinessUser().getId(), user.getBusinessUser().getBusinessUserRoles());
        }
    }

}
