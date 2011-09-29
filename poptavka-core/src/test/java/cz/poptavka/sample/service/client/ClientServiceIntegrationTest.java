package cz.poptavka.sample.service.client;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.domain.settings.Settings;
import cz.poptavka.sample.domain.user.BusinessUserData;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.service.user.ClientService;
import cz.poptavka.sample.service.user.UserSearchCriteria;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 *
 * @author Juraj Martinka
 *         Date: 20.12.10
 */
@DataSet(path = "classpath:cz/poptavka/sample/domain/user/UsersDataSet.xml", dtd = "classpath:test.dtd")
public class ClientServiceIntegrationTest extends DBUnitBaseTest {

    @Autowired
    private ClientService clientService;


    @Test
    public void testSearchByEmail() {
        final List<Client> clients = this.clientService.searchByCriteria(
                UserSearchCriteria.Builder.userSearchCriteria()
                        .withEmail("elvira@email.com")
                        .withPassword(null)
                        .build());
        Assert.assertEquals(1, clients.size());
        Assert.assertEquals("ahoj", clients.get(0).getBusinessUser().getPassword());
        Assert.assertEquals("Elv\u00edra",
                clients.get(0).getBusinessUser().getBusinessUserData().getPersonFirstName());
    }

    @Test
    public void testGetAllClients() {
        final List<Client> allClients = clientService.getAll();
        for (Client client : allClients) {
            System.out.println(client.getBusinessUser().getBusinessUserData().getPersonFirstName());
        }
        System.out.println();
        Assert.assertEquals(4, allClients.size());
    }


    @Test
    public void testSearchClientByName() {
        final List<Client> clients = clientService.searchByCriteria(
                UserSearchCriteria.Builder.userSearchCriteria()
                        .withName("Elv\u00edra")
                        .withSurName("Vytret\u00e1")
                        .build());
        Assert.assertNotNull(clients);
        Assert.assertEquals(1, clients.size());
        final Client client = clients.get(0);
        Assert.assertTrue("Elv\u00edra".equals(client.getBusinessUser().getBusinessUserData().getPersonFirstName()));
        Assert.assertTrue("Vytret\u00e1".equals(client.getBusinessUser().getBusinessUserData().getPersonLastName()));


        final List<Client> clients2 = clientService.searchByCriteria(
                UserSearchCriteria.Builder.userSearchCriteria()
                        .withName("Elv\u00edra")
                        .build());
        Assert.assertNotNull(clients);
        Assert.assertEquals(2, clients2.size());
        Assert.assertTrue("Elv\u00edra".equals(
                clients2.get(0).getBusinessUser().getBusinessUserData().getPersonFirstName()));
        Assert.assertTrue("Elv\u00edra".equals(
                clients2.get(1).getBusinessUser().getBusinessUserData().getPersonFirstName()));


        final Client hovnoClient = clientService.getById(111111114L);
        Assert.assertTrue("hovna".equals(hovnoClient.getBusinessUser().getBusinessUserData().getTaxId()));
    }


    @Test
    public void testUpdateClient() {
        final List<Client> clients = clientService.searchByCriteria(
                UserSearchCriteria.Builder.userSearchCriteria()
                        .withName("Velislav")
                        .withSurName("Kroko")
                        .build());
        Assert.assertNotNull(clients);
        Assert.assertEquals(1, clients.size());

        final Client clientToModify = clients.get(0);
        clientToModify.getBusinessUser().getBusinessUserData().setPersonLastName("Krokovic");
        this.clientService.update(clientToModify);

        final List<Client> persistedClient = this.clientService.searchByCriteria(
                UserSearchCriteria.Builder.userSearchCriteria()
                        .withName("Velislav")
                        .withSurName("Krokovic")
                        .build());
        Assert.assertNotNull(persistedClient);
        Assert.assertNotNull(persistedClient.get(0).getId());
        Assert.assertEquals("My Second Company",
                persistedClient.get(0).getBusinessUser().getBusinessUserData().getCompanyName());
    }


    @Test
    public void testCreateClient() {
        final Client newClient = new Client();
        newClient.getBusinessUser().setEmail("new@client.com");
        newClient.getBusinessUser().setBusinessUserData(
                new BusinessUserData.Builder().personFirstName("New").personLastName("Client").build());
        newClient.getBusinessUser().setSettings(new Settings());
        this.clientService.create(newClient);

        final List<Client> persistedClient = this.clientService.searchByCriteria(
                UserSearchCriteria.Builder.userSearchCriteria()
                        .withName("New")
                        .withSurName("Client")
                        .build());
        Assert.assertNotNull(persistedClient);
        Assert.assertNotNull(persistedClient.get(0).getId());
        Assert.assertEquals("new@client.com", persistedClient.get(0).getBusinessUser().getEmail());
    }
}
