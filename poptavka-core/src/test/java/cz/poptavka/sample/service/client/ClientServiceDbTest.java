package cz.poptavka.sample.service.client;

import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.domain.user.Person;
import cz.poptavka.sample.service.user.ClientSearchCriteria;
import cz.poptavka.sample.service.user.ClientService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Test of {@link cz.poptavka.sample.service.user.ClientService} that works
 * with real DB!
 * Watch out for consequences - e.g. methods that create/update/delete data should
 * not be a permanent part of this tests - such tests should be marked as @Ignored to prevent
 * from modifications of live data.
 *
 * @author Juraj Martinka
 *         Date: 24.4.11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@Transactional
public class ClientServiceDbTest {

    @Autowired
    private ClientService clientService;

    @Test
    @Ignore // do not create clients in live DB every time this test is run
    public void testCreateClient() {
        final Client newClient = new Client();
        newClient.setEmail("test@poptavam.com");
        newClient.setPerson(new Person("Test", "Client"));
        this.clientService.create(newClient);

        final List<Client> peristedClient = this.clientService.searchByCriteria(
                new ClientSearchCriteria("Test", "Client"));
        Assert.assertNotNull(peristedClient);
        Assert.assertNotNull(peristedClient.get(0).getId());
        Assert.assertEquals("test@poptavam.com", peristedClient.get(0).getEmail());
    }
}
