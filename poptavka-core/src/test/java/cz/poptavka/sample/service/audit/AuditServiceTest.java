package cz.poptavka.sample.service.audit;

import cz.poptavka.sample.domain.user.BusinessUserData;
import cz.poptavka.sample.domain.user.Client;
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
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;
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
 * This class uses custom "tearDown" method and programmatic transactions handling
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
// TODO: jumar fix "cannot inser null value into non-nullable settings_id" by creating the new client
@Ignore
public class AuditServiceTest {

    /**
     * Template used for programmatic transaction handling, because audited data must be commited before loading
     * audit information.
     */
    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private AuditService auditService;

    @Autowired
    private ClientService clientService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DataSource dataSource;

    private Client client1;
    private Client client2;
    private Client client3;


    @Before
    public void setUp() {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                client1 = createClient("ClientF1", "ClientL1");
                client2 = createClient("ClientF2", "ClientF2");
                client3 = createClient("ClientF3", "ClientL3");
            }
        });
    }


    @Test
    public void testGetRevisions1() {
        Assert.assertEquals(1, getClientRevisions(client1).size());
        Assert.assertEquals(1, getClientRevisions(client2).size());
        Assert.assertEquals(1, getClientRevisions(client3).size());

        // after udpate the new revision must be created
        updateClient(client1);
        Assert.assertEquals(2, this.auditService.getRevisions(Client.class, client1.getId()).size());
        Assert.assertEquals(1, getClientRevisions(client2).size());
        Assert.assertEquals(1, getClientRevisions(client3).size());
    }


    @After
    public void deleteTestingRecords() {
        // custom transaction handling is neccessary otherwise the TransactionRequiredException is thrown
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                deleteClientsRevisions(entityManager);
                deleteClients(entityManager);
            }
        });
    }



    //----------------------------------- HELPER METHODS _--------------------------------------------------------------

    private Client createClient(String firstName, String lastName) {
        final Client newClient = new Client();
        newClient.setEmail(firstName + "." + lastName + "@poptavam.com");
        newClient.setBusinessUserData(
                new BusinessUserData.Builder().personFirstName(firstName).personLastName(lastName).build());
        clientService.create(newClient);
        return newClient;
    }


    private void updateClient(final Client clientForUpdate) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                clientForUpdate.setBusinessUserData(
                        new BusinessUserData.Builder().personFirstName("Client1").personLastName("Client1").build());
                clientService.update(clientForUpdate);
            }
        });
    }


    private List<Number> getClientRevisions(Client client) {
        return this.auditService.getRevisions(Client.class, client.getId());
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
}
