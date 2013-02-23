package com.eprovement.poptavka.service.client;

import com.eprovement.poptavka.base.RealDbTest;
import com.eprovement.poptavka.domain.user.BusinessUserData;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.service.user.ClientService;
import com.eprovement.poptavka.service.user.UserSearchCriteria;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Test of {@link com.eprovement.poptavka.service.user.ClientService} that works
 * with real DB!
 * Watch out for consequences - e.g. methods that create/update/delete data should
 * not be a permanent part of this tests - such tests should be marked as @Ignored to prevent
 * from modifications of live data.
 *
 * @author Juraj Martinka
 *         Date: 24.4.11
 */
public class ClientServiceDbTest extends RealDbTest {

    @Autowired
    private ClientService clientService;

    @Test
    @Ignore // do not create clients in live DB every time this test is run
    @Transactional(propagation = Propagation.REQUIRED)
    public void testCreateClient() {
        final Client newClient = new Client();
        newClient.getBusinessUser().setEmail("test@poptavam.com");
        newClient.getBusinessUser().setBusinessUserData(
                new BusinessUserData.Builder().personFirstName("Test").personLastName("Client").build());
        this.clientService.create(newClient);

        final List<Client> peristedClient = this.clientService.searchByCriteria(
                UserSearchCriteria.Builder.userSearchCriteria()
                .withName("Test")
                .withSurName("Test")
                .build());
        Assert.assertNotNull(peristedClient);
        Assert.assertNotNull(peristedClient.get(0).getId());
        Assert.assertEquals("test@poptavam.com", peristedClient.get(0).getBusinessUser().getEmail());
    }
}
