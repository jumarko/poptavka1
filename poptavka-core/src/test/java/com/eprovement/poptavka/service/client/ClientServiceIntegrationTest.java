package com.eprovement.poptavka.service.client;

import com.eprovement.poptavka.domain.address.Address;
import com.eprovement.poptavka.domain.user.rights.AccessRole;
import com.eprovement.poptavka.service.address.LocalityService;
import com.googlecode.genericdao.search.Search;
import com.eprovement.poptavka.base.integration.DBUnitBaseTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.product.UserService;
import com.eprovement.poptavka.domain.register.Registers;
import com.eprovement.poptavka.domain.settings.Notification;
import com.eprovement.poptavka.domain.enums.Period;
import com.eprovement.poptavka.domain.settings.Settings;
import com.eprovement.poptavka.domain.user.BusinessUserData;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.register.RegisterService;
import com.eprovement.poptavka.service.user.ClientService;
import com.eprovement.poptavka.service.user.UserSearchCriteria;
import com.eprovement.poptavka.util.user.UserTestUtils;
import java.util.Arrays;
import static org.hamcrest.core.Is.is;
import org.junit.Assert;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 *
 * @author Juraj Martinka
 *         Date: 20.12.10
 */
@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/register/RegisterDataSet.xml" },
         dtd = "classpath:test.dtd")
public class ClientServiceIntegrationTest extends DBUnitBaseTest {

    @Autowired
    private ClientService clientService;

    @Autowired
    private GeneralService generalService;

    @Autowired
    private RegisterService registerService;

    @Autowired
    private LocalityService localityService;


    @Test
    public void testSearchByEmail() {
        final List<Client> clients = this.clientService.searchByCriteria(
                UserSearchCriteria.Builder.userSearchCriteria()
                        .withEmail("elvira@email.com")
                        .withPassword(null)
                        .build());
        Assert.assertEquals(1, clients.size());
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
        Assert.assertEquals(5, allClients.size());
    }


    @Test
    public void testSearchClientByName() {
        final List<Client> clients = clientService.searchByCriteria(
                UserSearchCriteria.Builder.userSearchCriteria()
                        .withName("Elv\u00edra")
                        .withSurName("Vytret\u00e1")
                        .build());
        Assert.assertNotNull(clients);
        Assert.assertEquals(2, clients.size());
        final Client client = clients.get(0);
        Assert.assertTrue("Elv\u00edra".equals(client.getBusinessUser().getBusinessUserData().getPersonFirstName()));
        Assert.assertTrue("Vytret\u00e1".equals(client.getBusinessUser().getBusinessUserData().getPersonLastName()));


        final List<Client> clients2 = clientService.searchByCriteria(
                UserSearchCriteria.Builder.userSearchCriteria()
                        .withName("Elv\u00edra")
                        .build());
        Assert.assertNotNull(clients);
        Assert.assertEquals(3, clients2.size());
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
        newClient.getBusinessUser().setPassword("myPassword");
        newClient.getBusinessUser().setAccessRoles(Arrays.asList(this.generalService.find(AccessRole.class, 1L)));
        final Address clientAddress = new Address();
        clientAddress.setCity(this.localityService.getLocality("loc211"));
        clientAddress.setStreet("Gotham city");
        clientAddress.setZipCode("12");
        newClient.getBusinessUser().setAddresses(Arrays.asList(clientAddress));
        newClient.getBusinessUser().setBusinessUserData(
                new BusinessUserData.Builder().personFirstName("New").personLastName("Client").build());
        newClient.getBusinessUser().setSettings(new Settings());
        this.clientService.create(newClient);

        final List<Client> persistedClients = this.clientService.searchByCriteria(
                UserSearchCriteria.Builder.userSearchCriteria()
                        .withName("New")
                        .withSurName("Client")
                        .build());
        Assert.assertNotNull(persistedClients);
        final Client createdClient = persistedClients.get(0);
        Assert.assertNotNull(createdClient.getId());
        Assert.assertEquals("new@client.com", createdClient.getBusinessUser().getEmail());

        // check BusinessUserRole-s
        Assert.assertFalse(createdClient.getBusinessUser().getBusinessUserRoles().isEmpty());
        Assert.assertThat(createdClient.getBusinessUser().getBusinessUserRoles().get(0).getId(),
                is(createdClient.getId()));

        // check if new client has automatically assigned service "classic client" with status unverified
        final Search userServiceSearch = new Search(UserService.class);
        userServiceSearch.addFilterEqual("user", createdClient.getBusinessUser());
        final List<UserService> serviceAssignedToClient = this.generalService.search(userServiceSearch);
        Assert.assertNotNull(serviceAssignedToClient.get(0));
        Assert.assertThat(serviceAssignedToClient.get(0).getUser().getEmail(), is("new@client.com"));
        Assert.assertThat(serviceAssignedToClient.get(0).getService().getCode(), is(Registers.Service.CLASSIC));

        // check if new client has also all client notifications set to the sensible values
        Assert.assertNotNull(createdClient.getBusinessUser().getSettings());
        UserTestUtils.checkHasNotification(createdClient.getBusinessUser(),
                this.registerService.getValue(Registers.Notification.CLIENT_NEW_MESSAGE, Notification.class),
                true, Period.INSTANTLY);
        UserTestUtils.checkHasNotification(createdClient.getBusinessUser(),
                this.registerService.getValue(Registers.Notification.CLIENT_NEW_OPERATOR, Notification.class),
                true, Period.INSTANTLY);
        UserTestUtils.checkHasNotification(createdClient.getBusinessUser(),
                this.registerService.getValue(Registers.Notification.CLIENT_NEW_INFO, Notification.class),
                false, Period.INSTANTLY);
        UserTestUtils.checkHasNotification(createdClient.getBusinessUser(),
                this.registerService.getValue(Registers.Notification.CLIENT_NEW_OFFER, Notification.class),
                false, Period.INSTANTLY);
        UserTestUtils.checkHasNotification(createdClient.getBusinessUser(),
                this.registerService.getValue(Registers.Notification.CLIENT_DEMAND_STATUS_CHANGED, Notification.class),
                false, Period.INSTANTLY);
    }


    @Test
    public void testCheckFreeEmail() {
        assertFalse(this.clientService.checkFreeEmail("elvira@email.com"));
        assertTrue(this.clientService.checkFreeEmail("elvira11@email.com"));
        assertFalse(this.clientService.checkFreeEmail("elviraM@email.com"));
        assertFalse(this.clientService.checkFreeEmail("lisohlavka@email.com"));
    }


}
