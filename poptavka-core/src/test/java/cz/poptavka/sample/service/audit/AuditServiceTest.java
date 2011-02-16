package cz.poptavka.sample.service.audit;

import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.domain.user.Person;
import cz.poptavka.sample.service.user.ClientService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.persistence.Query;
import java.util.List;

/**
 * Test of {@link AuditService}.
 *
 * * <p>
 * <strong>PLEASE, DO NOT INSPIRE YOURSELF BY THIS TEST!</strong>.
 * It is very specific in a way in which it must handle the transactions
 * and DO the cleaning process. For most other tests this is not needed and suitable.
 *
 * <p>
 * This class uses custom "tearDown" method and manual transactions handling
 * to manually delete created entries from DB, because standard
 * procedure with rolling back the transaction does not work - in such a case, the auditService is not capable
 * to retrieve any audit informations from DB, because audit information are stored only after commiting of an active
 * transaction.
 *
 * @author Juraj Martinka
 *         Date: 8.1.11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
// MUST NOT BE TRANSACTIONAL
//@Transactional
@Ignore // Maven has strange problem when running this test - it must be ignored, at least for this moment :(
public class AuditServiceTest {

    @Autowired
    private AuditService auditService;

    @Autowired
    private ClientService clientService;

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;


    private Client client1;
    private Client client2;
    private Client client3;

    @Before
    public void setUp() {
        this.client1 = createClient("ClientF1", "ClientL1");
        this.client2 = createClient("ClientF2", "ClientF2");
        this.client3 = createClient("ClientF3", "ClientL3");
    }

    private Client createClient(String firstName, String lastName) {
        final Client newClient = new Client();
        newClient.setPerson(new Person(firstName, lastName));
        clientService.create(newClient);
        return newClient;
    }

    @Test
    public void testGetRevisions1() {
        Assert.assertEquals(1, getClientRevisions(client1).size());
        Assert.assertEquals(1, getClientRevisions(client2).size());
        Assert.assertEquals(1, getClientRevisions(client3).size());

        client1.setPerson(new Person("Client1", "Client1"));
        this.clientService.update(client1);

        Assert.assertEquals(2, this.auditService.getRevisions(Client.class, client1.getId()).size());
        Assert.assertEquals(1, getClientRevisions(client2).size());
        Assert.assertEquals(1, getClientRevisions(client3).size());

    }


    @After
    public void deleteTestingRecords() {
        // custom transaction handling is neccessary otherwise the TransactionRequiredException is thrown
        final EntityManager entityManager = this.entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        if (client1 == null || client2 == null || client3 == null) {
            // avoid strange NPE when running multiple tests at once with maven
            return;
        }

        deleteClientsRevisions(entityManager);
        deleteClients(entityManager);

        entityManager.getTransaction().commit();
    }

    private void deleteClientsRevisions(EntityManager entityManager) {
        final Query deleteQuery = entityManager.createNativeQuery("delete from Client_AUD where id IN (?, ?, ?)");
        deleteQuery.setParameter(1, client1.getId());
        deleteQuery.setParameter(2, client2.getId());
        deleteQuery.setParameter(3, client3.getId());

        deleteQuery.executeUpdate();
    }

    private void deleteClients(EntityManager entityManager) {
        entityManager.remove(entityManager.find(Client.class, client1.getId()));
        entityManager.remove(entityManager.find(Client.class, client2.getId()));
        entityManager.remove(entityManager.find(Client.class, client3.getId()));
    }


    private List<Number> getClientRevisions(Client client) {
        return this.auditService.getRevisions(Client.class, client.getId());
    }
}
