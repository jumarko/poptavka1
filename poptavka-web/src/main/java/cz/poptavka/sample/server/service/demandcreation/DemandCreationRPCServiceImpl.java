/*
 * RPC trieda, ktora tvori rozhranie medzi GWT a Hibernate. Stara sa o metody z modulu
 * DemandCreation. Vsetky metody pre tento modul budu v tejto RPC metode. Detail objekty
 * sa mozu zdielat medzi viacerymi RPC servisami.
 */
package cz.poptavka.sample.server.service.demandcreation;

import com.google.common.base.Preconditions;
import cz.poptavka.sample.client.service.demand.DemandCreationRPCService;
import cz.poptavka.sample.domain.address.Address;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.demand.DemandStatus;
import cz.poptavka.sample.domain.user.BusinessUserData;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.exception.MessageException;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.server.service.ConvertUtils;
import cz.poptavka.sample.service.address.LocalityService;
import cz.poptavka.sample.service.demand.CategoryService;
import cz.poptavka.sample.service.demand.DemandService;
import cz.poptavka.sample.service.user.ClientService;
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
 *
 * TODO Praso - doplnit komentare k metodam a optimalizovat na stranke backendu
 */
public class DemandCreationRPCServiceImpl extends AutoinjectingRemoteService
        implements DemandCreationRPCService {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DemandCreationRPCServiceImpl.class);
    private DemandService demandService;
    private LocalityService localityService;
    private CategoryService categoryService;
    private ClientService clientService;

    @Autowired
    public void setDemandService(DemandService demandService) {
        this.demandService = demandService;
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
     * Create new entity Demand based on the passed FullDemandDetail object.
     *
     * @param detail object created by user in DemandCreation view
     * @param cliendId is used in case the Demand is created by registered and
     * logged in client
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
        demand.setClient(this.clientService.getById(cliendId));

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
        sendDemandToSuppliers(newDemandFromDB);
        return FullDemandDetail.createDemandDetail(newDemandFromDB);
    }

    private boolean maxOffersSpecified(FullDemandDetail detail) {
        return detail.getMaxOffers() > 0;
    }

    // TODO FIX this, it's not working nullPo interException. -- who use it anyway???
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
     * demand messages should be sent and possibly separate this heuristic
     *
     * @param demand
     */
    private void sendDemandToSuppliers(Demand demand) {
        // send message and handle exception if any
        try {
            this.demandService.sendDemandToSuppliers(demand);
        } catch (MessageException e) {
            LOGGER.error("Demand " + demand + " has not been sent to suppliers. "
                    + "The next try will be made by regular job.");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
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
                taxId(clientDetail.getTaxId())
                .website(clientDetail.getWebsite())
                .build();

        newClient.getBusinessUser().setBusinessUserData(businessUserData);

        setAddresses(clientDetail, newClient);

        /** Login & pwd information. **/
        newClient.getBusinessUser().setEmail(clientDetail.getEmail());
        newClient.getBusinessUser().setPassword(clientDetail.getPassword());

        final Client newClientFromDB = clientService.create(newClient);

        return ConvertUtils.toUserDetail(newClientFromDB.getBusinessUser().getId(),
                newClientFromDB.getBusinessUser().getBusinessUserRoles());
    }


    //--------------------------------------------------- HELPER METHODS -----------------------------------------------
    private void setAddresses(UserDetail clientDetail, Client newClient) {
        final List<Address> addresses = new ArrayList<Address>();
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
    }

}
