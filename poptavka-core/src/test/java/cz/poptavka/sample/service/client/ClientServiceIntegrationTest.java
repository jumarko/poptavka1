package cz.poptavka.sample.service.client;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.domain.settings.Settings;
import cz.poptavka.sample.domain.user.BusinessUserData;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.service.user.ClientSearchCriteria;
import cz.poptavka.sample.service.user.ClientService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * TODO: this test must be implemented correctly by complete test methods.
 *
 * @author Juraj Martinka
 *         Date: 20.12.10
 */
@DataSet(path = "classpath:cz/poptavka/sample/domain/user/UsersDataSet.xml", dtd = "classpath:test.dtd")
public class ClientServiceIntegrationTest extends DBUnitBaseTest {

    @Autowired
    private ClientService clientService;


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
                new ClientSearchCriteria("Elv\u00edra", "Vytret\u00e1"));
        Assert.assertNotNull(clients);
        Assert.assertEquals(1, clients.size());
        final Client client = clients.get(0);
        Assert.assertTrue("Elv\u00edra".equals(client.getBusinessUser().getBusinessUserData().getPersonFirstName()));
        Assert.assertTrue("Vytret\u00e1".equals(client.getBusinessUser().getBusinessUserData().getPersonLastName()));


        final List<Client> clients2 = clientService.searchByCriteria(new ClientSearchCriteria("Elv\u00edra", null));
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
        final List<Client> clients = clientService.searchByCriteria(new ClientSearchCriteria("Velislav", "Kroko"));
        Assert.assertNotNull(clients);
        Assert.assertEquals(1, clients.size());

        final Client clientToModify = clients.get(0);
        clientToModify.getBusinessUser().getBusinessUserData().setPersonLastName("Krokovic");
        this.clientService.update(clientToModify);

        final List<Client> peristedClient = this.clientService.searchByCriteria(
                new ClientSearchCriteria("Velislav", "Krokovic"));
        Assert.assertNotNull(peristedClient);
        Assert.assertNotNull(peristedClient.get(0).getId());
        Assert.assertEquals("My Second Company",
                peristedClient.get(0).getBusinessUser().getBusinessUserData().getCompanyName());
    }


    @Test
    public void testCreateClient() {
        final Client newClient = new Client();
        newClient.getBusinessUser().setEmail("new@client.com");
        newClient.getBusinessUser().setBusinessUserData(
                new BusinessUserData.Builder().personFirstName("New").personLastName("Client").build());
        newClient.getBusinessUser().setSettings(new Settings());
        this.clientService.create(newClient);

        final List<Client> peristedClient = this.clientService.searchByCriteria(
                new ClientSearchCriteria("New", "Client"));
        Assert.assertNotNull(peristedClient);
        Assert.assertNotNull(peristedClient.get(0).getId());
        Assert.assertEquals("new@client.com", peristedClient.get(0).getBusinessUser().getEmail());
    }
}
