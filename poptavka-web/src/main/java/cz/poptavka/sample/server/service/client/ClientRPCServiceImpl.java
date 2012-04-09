package cz.poptavka.sample.server.service.client;

import com.google.common.base.Preconditions;
import com.google.gwt.core.client.GWT;
import com.googlecode.genericdao.search.Search;
import cz.poptavka.sample.client.service.demand.ClientRPCService;
import cz.poptavka.sample.domain.address.Address;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.user.BusinessUserData;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.domain.user.Verification;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.server.service.ConvertUtils;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.address.LocalityService;
import cz.poptavka.sample.service.user.ClientService;
import cz.poptavka.sample.shared.domain.AddressDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.domain.adminModule.ClientDetail;
import cz.poptavka.sample.shared.exceptions.CommonException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

public class ClientRPCServiceImpl extends AutoinjectingRemoteService implements ClientRPCService {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -5905531608577218017L;

    private GeneralService generalService;
    private ClientService clientService;
    private LocalityService localityService;

    public ArrayList<UserDetail> getAllClients() {
        // TODO do we need this method?
        return null;
    }

    @Override
    public ArrayList<ClientDetail> getClients(int start, int count) throws CommonException {
        final Search search = new Search(Client.class);
        search.setFirstResult(start);
        search.setMaxResults(count);
        return this.createClientDetailList(generalService.search(search));
    }

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    @Autowired
    public void setLocalityService(LocalityService localityService) {
        this.localityService = localityService;
    }

    /**
     * Vytvorenie noveho klienta.
     *
     */
    public UserDetail createNewClient(UserDetail clientDetail) throws CommonException {
        Preconditions.checkNotNull(clientDetail);
        final Client newClient = new Client();
        /** Person is mandatory for person client and for company client as well. **/
        final BusinessUserData businessUserData = new BusinessUserData.Builder()
                .companyName(clientDetail.getCompanyName()).
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
        return ConvertUtils.toUserDetail(newClientFromDB.getBusinessUser().getId(),
                newClientFromDB.getBusinessUser().getBusinessUserRoles());
    }


    // TODO FIX this, it's not working nullPointerException.
    public Locality getLocality(String code) throws CommonException {
        System.out.println("Locality code value: " + code + ", localityService is null? " + (localityService == null));
        return localityService.getLocality(code);
    }

    /**
     * Get All clients count.
     */
    @Override
    public Integer getClientsCount() throws CommonException {
        return (int) clientService.getCount();
    }

    /**
     * Method updates client object in database.
     *
     * @param detail - updated ClientDetail from front end
     * @return clientDetail
     */
    @Override
    public ClientDetail updateClient(ClientDetail detail) throws CommonException {
        Client client = clientService.getById(detail.getId());

        //Client
        if (detail.getOveralRating() == -1) {
            client.setOveralRating(null);
        } else {
            client.setOveralRating(detail.getOveralRating());
        }
        client.setVerification(Verification.valueOf(detail.getVerification()));

        //TODO Martin - how to update addresses???
        List<Address> newAddresses = new ArrayList<Address>();
        for (AddressDetail addr : detail.getUserDetail().getAddresses()) {
//            Address address = new Address();
//            supplier.getBusinessUser().getAddresses()
        }

        //Busines data
        client.getBusinessUser().setEmail(detail.getUserDetail().getEmail());

        client.getBusinessUser().getBusinessUserData().setDescription(detail.getUserDetail().getDescription());
        client.getBusinessUser().getBusinessUserData().setCompanyName(detail.getUserDetail().getCompanyName());
        client.getBusinessUser().getBusinessUserData().
                setIdentificationNumber(detail.getUserDetail().getIdentificationNumber());

        //-- Contact
        client.getBusinessUser().getBusinessUserData().setPersonFirstName(detail.getUserDetail().getFirstName());
        client.getBusinessUser().getBusinessUserData().setPersonLastName(detail.getUserDetail().getLastName());
        client.getBusinessUser().getBusinessUserData().setPhone(detail.getUserDetail().getPhone());

        clientService.update(client);
        return detail;
    }

    @Override
    public ArrayList<ClientDetail> getSortedClients(int start, int count, Map<String, OrderType> orderColumns)
        throws CommonException {
        final ResultCriteria resultCriteria = new ResultCriteria.Builder()
                .firstResult(start).maxResults(count)
                .orderByColumns(orderColumns)
                .build();
        return this.createClientDetailList(clientService.getAll(resultCriteria));
    }

    private ArrayList<ClientDetail> createClientDetailList(Collection<Client> clients) {
        ArrayList<ClientDetail> clientDetails = new ArrayList<ClientDetail>();
        for (Client client : clients) {
            clientDetails.add(ClientDetail.createClientDetail(client));
        }
        GWT.log("clientDetailList created: " + clientDetails.size());
        return clientDetails;
    }
}
