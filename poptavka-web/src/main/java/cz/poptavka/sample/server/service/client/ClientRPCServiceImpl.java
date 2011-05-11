package cz.poptavka.sample.server.service.client;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cz.poptavka.sample.client.service.demand.ClientRPCService;
import cz.poptavka.sample.domain.address.Address;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.user.BusinessUser;
import cz.poptavka.sample.domain.user.BusinessUserData;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.address.LocalityService;
import cz.poptavka.sample.service.user.ClientService;
import cz.poptavka.sample.shared.domain.ClientDetail;

public class ClientRPCServiceImpl extends AutoinjectingRemoteService implements ClientRPCService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientRPCServiceImpl.class);
    private static final String HULICE_CODE = "529737";

    private ClientService clientService;

    private LocalityService localityService;

    public ArrayList<ClientDetail> getAllClients() {
        // TODO Auto-generated method stub
        LOGGER.info("Getting fake clients");
//        return clientService.getAll();
        return null;
    }

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }


    @Autowired
    public void setLocalityService(LocalityService localityService) {
        this.localityService = localityService;
    }

    public void sendClientId(long id) {
        // TODO Auto-generated method stub

    }

    /**
     * Create new Client - person or company.
     *
     * TODO: website assignation in backend
     */
    public long createNewClient(ClientDetail clientDetail) {
        Client newClient = new Client();
        /** Person is mandatory for person client and for company client as well. **/
        final BusinessUserData businessUserData = new BusinessUserData.Builder()
                .companyName(clientDetail.getCompanyName())
                .personFirstName(clientDetail.getFirstName())
                .personLastName(clientDetail.getLastName())
                .phone(clientDetail.getPhone())
                .identificationNumber(clientDetail.getIdentifiacationNumber())
                .taxId(clientDetail.getTaxId())
                .build();
        newClient.getBusinessUser().setBusinessUserData(businessUserData);

        /** Address. **/
        /** Need to fix City selection for frontEnd - think about it **/
        String addressName = clientDetail.getAddress().getCityName();

        /** need for fix this **/
//        LocalityRPCServiceImpl localityService = new LocalityRPCServiceImpl();
//        Locality city = localityService.getLocality("Brno");
        final Locality city = this.localityService.getLocality(HULICE_CODE);
        final Address address = new Address();
        address.setCity(city);
        address.setStreet(clientDetail.getAddress().getStreet());
        address.setZipCode(clientDetail.getAddress().getZipCode());
        List<Address> addresses = new ArrayList<Address>();
        addresses.add(address);
        newClient.getBusinessUser().setAddresses(addresses);
        /** Login & pwd information. **/
        newClient.getBusinessUser().setEmail(clientDetail.getEmail());
        newClient.getBusinessUser().setPassword(clientDetail.getPassword());

        Client newClient2 = clientService.create(newClient);
        return newClient2.getId();
    }

    public long verifyClient(ClientDetail client) {
        List<Client> clients = clientService.getAll();

        //lebo inteligenti nasetuju null heslo do db...
        for (Client cl : clients) {
            if (cl.getBusinessUser().getEmail().equals(client.getEmail())) {
                return cl.getId();
            }
        }
//        for (Client cl : clients) {
//            if (cl.getBusinessUser().getEmail().equals(client.getEmail())
//                    && cl.getBusinessUser().getPassword().equals(client.getPassword())) {
//                return cl.getId();
//            }
//        }
        return -1;
//        return 1;
    }

    @Override
    public boolean checkFreeEmail(String email) {
        Client example = new Client();
        BusinessUser user = new BusinessUser();
        user.setEmail(email);
        example.setBusinessUser(user);
        List<Client> resultList = clientService.findByExample(example);
        return resultList.size() == 0;
    }

}
