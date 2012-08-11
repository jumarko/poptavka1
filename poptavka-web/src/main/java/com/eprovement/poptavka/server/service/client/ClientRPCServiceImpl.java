package com.eprovement.poptavka.server.service.client;

import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.server.converter.Converter;
import com.google.common.base.Preconditions;
import com.google.gwt.core.client.GWT;
import com.googlecode.genericdao.search.Search;
import com.eprovement.poptavka.client.service.demand.ClientRPCService;
import com.eprovement.poptavka.domain.address.Address;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.user.BusinessUserData;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.enums.Verification;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.service.user.ClientService;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.adminModule.ClientDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

@Configurable
public class ClientRPCServiceImpl extends AutoinjectingRemoteService implements ClientRPCService {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -5905531608577218017L;

    private GeneralService generalService;
    private ClientService clientService;
    private LocalityService localityService;
    private Converter<Client, ClientDetail> clientConverter;
    private Converter<BusinessUser, BusinessUserDetail> businessUserConverter;

    public ArrayList<BusinessUserDetail> getAllClients() throws RPCException {
        // TODO do we need this method?
        return null;
    }

    @Override
    public ArrayList<ClientDetail> getClients(int start, int count) throws RPCException {
        final Search search = new Search(Client.class);
        search.setFirstResult(start);
        search.setMaxResults(count);
        //mohlo by byt nie?
        //return clientConverter.convertToTargetList(generalService.search(search));
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

    @Autowired
    public void setClientConverter(@Qualifier("clientConverter") Converter<Client, ClientDetail> clientConverter) {
        this.clientConverter = clientConverter;
    }

    @Autowired
    public void setBusinessUserConverter(@Qualifier("businessUserConverter") Converter<BusinessUser,
            BusinessUserDetail> businessUserConverter) {
        this.businessUserConverter = businessUserConverter;
    }


    /**
     * Vytvorenie noveho klienta.
     *
     */
    public BusinessUserDetail createNewClient(BusinessUserDetail clientDetail) throws RPCException {
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
                Locality city = this.getLocality(detail.getCity());
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
        //use UserConverter???
        return businessUserConverter.convertToTarget(newClientFromDB.getBusinessUser());
    }


    // TODO FIX this, it's not working nullPointerException.
    public Locality getLocality(String code) throws RPCException {
        System.out.println("Locality code value: " + code + ", localityService is null? " + (localityService == null));
        return localityService.getLocality(code);
    }

    /**
     * Get All clients count.
     */
    @Override
    public Integer getClientsCount() throws RPCException {
        return (int) clientService.getCount();
    }

    /**
     * Method updates client object in database.
     *
     * @param detail - updated ClientDetail from front end
     * @return clientDetail
     */
    @Override
    public ClientDetail updateClient(ClientDetail detail) throws RPCException {
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
        throws RPCException {
        final ResultCriteria resultCriteria = new ResultCriteria.Builder()
                .firstResult(start).maxResults(count)
                .orderByColumns(orderColumns)
                .build();
        return this.createClientDetailList(clientService.getAll(resultCriteria));
    }

    private ArrayList<ClientDetail> createClientDetailList(Collection<Client> clients) {
        ArrayList<ClientDetail> clientDetails = new ArrayList<ClientDetail>();
        for (Client client : clients) {
            clientDetails.add(clientConverter.convertToTarget(client));
        }
        GWT.log("clientDetailList created: " + clientDetails.size());
        return clientDetails;
    }
}
