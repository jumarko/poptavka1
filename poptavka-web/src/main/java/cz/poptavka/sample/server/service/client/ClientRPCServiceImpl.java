package cz.poptavka.sample.server.service.client;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cz.poptavka.sample.client.service.demand.ClientRPCService;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.domain.user.Company;
import cz.poptavka.sample.domain.user.Person;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.GenericService;
import cz.poptavka.sample.service.user.ClientService;
import cz.poptavka.sample.shared.domain.ClientDetail;

public class ClientRPCServiceImpl extends AutoinjectingRemoteService implements ClientRPCService {

    private ClientService clientService;
    private GenericService genericService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientRPCServiceImpl.class);

    @Override
    public ArrayList<ClientDetail> getAllClients() {
        // TODO Auto-generated method stub
        LOGGER.info("Getting fake clients");
//        return clientService.getAll();
        return null;
    }

//    @Autowired
//    public void setClientService(ClientService clientService) {
//        this.clientService = clientService;
//    }

    @Autowired
    public void setGenericService(GenericService genericService) {
        this.genericService = genericService;
    }

    @Override
    public void sendClientId(long id) {
        // TODO Auto-generated method stub

    }

    @Override
    public long createNewClient(ClientDetail clientDetail) {
        Client newClient = new Client();
        /** Person is mandatory for person client and for company client as well. **/
        Person person = new Person(clientDetail.getFirstName(), clientDetail.getLastName());
        person.setPhone(clientDetail.getPhone());
        person = (Person) genericService.create(person);
        newClient.setPerson(person);
        /** Company will stay null for person client. **/
        Company company = null;
        if (!(clientDetail.getCompanyName() == null)) {
            company = new Company();
            company.setName(clientDetail.getCompanyName());
            company.setIdentificationNumber(clientDetail.getIdentifiacationNumber());
            company.setTaxId(clientDetail.getTaxId());
            company = (Company) genericService.create(company);
        }
        newClient.setCompany(company);
        /** Address. **/
        /** Need to fix City selection for frontEnd - think about it **/
//        long addressId = Long.parseLong(clientDetail.getAddress().getCityCode());
//        //getting city from selected
//        Locality city = (Locality) genericService.getById(addressId);
//        Address address = new Address();
//        address.setCity(city);
//        address.setStreet(clientDetail.getAddress().getStreet());
//        address.setZipCode(clientDetail.getAddress().getZipCode());
//        List<Address> addresses = new ArrayList<Address>();
//        addresses.add(address);
//        newClient.setAddresses(addresses);
        newClient.setEmail(clientDetail.getEmail());
        newClient.setLogin(clientDetail.getLogin());
        newClient.setPassword(clientDetail.getPassowrd());
        newClient = clientService.create(newClient);
        return newClient.getId();
    }

    @Override
    public long verifyClient(ClientDetail client) {
//        List<Client> clients = clientService.getAll();
//        for (Client cl : clients) {
//            if (cl.getLogin().equals(client.getLogin()) && cl.getPassword().equals(client.getPassowrd())) {
//                return cl.getId();
//            }
//        }
//        return -1;
        return 1;
    }

}
