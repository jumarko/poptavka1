package cz.poptavka.sample.service.client;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
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
            System.out.println(client.getPerson().getFirstName());
        }
        System.out.println();
        Assert.assertEquals(4, allClients.size());
    }


    @Test
    public void testSearchClientByName() {
        final List<Client> clients = clientService.searchByCriteria(new ClientSearchCriteria("Elvíra", "Vytretá"));
        Assert.assertNotNull(clients);
        Assert.assertEquals(1, clients.size());
        final Client client = clients.get(0);
        Assert.assertTrue("Elvíra".equals(client.getPerson().getFirstName()));
        Assert.assertTrue("Vytretá".equals(client.getPerson().getLastName()));


        final List<Client> clients2 = clientService.searchByCriteria(new ClientSearchCriteria("Elvíra", null));
        Assert.assertNotNull(clients);
        Assert.assertEquals(2, clients2.size());
        Assert.assertTrue("Elvíra".equals(clients2.get(0).getPerson().getFirstName()));
        Assert.assertTrue("Elvíra".equals(clients2.get(1).getPerson().getFirstName()));


        final Client hovnoClient = clientService.getById(4L);
        Assert.assertTrue("hovna".equals(hovnoClient.getCompany().getTaxId()));
    }


    @Test
    public void testUpdateClient() {
        final List<Client> clients = clientService.searchByCriteria(new ClientSearchCriteria("Velislav", "Kroko"));
        Assert.assertNotNull(clients);
        Assert.assertEquals(1, clients.size());

        final Client clientToModify = clients.get(0);
        clientToModify.getPerson().setLastName("Krokovic");
        this.clientService.update(clientToModify);
    }
}
