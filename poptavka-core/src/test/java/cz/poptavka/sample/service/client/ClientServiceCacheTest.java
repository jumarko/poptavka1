package cz.poptavka.sample.service.client;

import static org.mockito.Mockito.times;
import cz.poptavka.sample.base.integration.BasicIntegrationTest;
import cz.poptavka.sample.dao.user.ClientDao;
import cz.poptavka.sample.service.user.ClientService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

/**
 * Tests of correct caching when calling {@link ClientService} methods.
 * @author Juraj Martinka
 *         Date: 8.1.11
 */
// this class "corrupts" the clientService bean with mocking dao, therefore this bean must be reinitialize
@DirtiesContext
public class ClientServiceCacheTest extends BasicIntegrationTest {

    @Autowired
    private ClientService clientService;


    @Before
    public void setUp() {
        final ClientDao mockDao = Mockito.mock(ClientDao.class);
        this.clientService.setDao(mockDao);
    }


    @Test
    public void testGetClientByIdCache() {
        clientService.getCachedClientById(2L);
        Mockito.verify(clientService.getDao(), times(1))
                .findById(Mockito.anyLong());

        clientService.getCachedClientById(2L);
        Mockito.verify(clientService.getDao(), times(1))
                .findById(Mockito.anyLong());


        clientService.getCachedClientById(3L);
        Mockito.verify(clientService.getDao(), times(2))
                .findById(Mockito.anyLong());
    }
}

