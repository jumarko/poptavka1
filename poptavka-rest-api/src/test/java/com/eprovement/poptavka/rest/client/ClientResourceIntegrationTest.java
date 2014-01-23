package com.eprovement.poptavka.rest.client;

import static com.eprovement.poptavka.rest.matchers.JsonMatchers.jsonPathMissing;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.common.Origin;
import com.eprovement.poptavka.domain.register.Registers;
import com.eprovement.poptavka.domain.settings.NotificationItem;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.rest.ResourceIntegrationTest;
import com.eprovement.poptavka.service.user.ClientService;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/register/RegisterDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml" },
        dtd = "classpath:test.dtd")
public class ClientResourceIntegrationTest extends ResourceIntegrationTest {

    @Autowired
    private ClientService clientService;

    @Test
    public void listClients() throws Exception {
        this.mockMvc.performRequest(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.collection[0].email").value("elvira@email.com"))
                .andExpect(jsonPath("$.collection[1].email").value("velislav@email.com"))
                .andExpect(jsonPath("$.links.self").value("/api/clients"))
                .andExpect(jsonPath("$.paging.offset").value(0))
                .andExpect(jsonPath("$.paging.count").value(5));
    }

    @Test
    public void getClientById() throws Exception {
        performRequestAndCheckElvira(mockMvc.performRequest(get("/clients/{id}", 111111111L)));
    }

    @Test
    public void getClientByIdNotFound() throws Exception {
        mockMvc.performRequest(get("/clients/{id}", 333L)).andExpect(status().isNotFound());
    }

    @Test
    public void getClientByEmail() throws Exception {
        performRequestAndCheckElvira(mockMvc.performRequest(get("/clients/search").param("email", "elvira@email.com")));
    }

    @Test
    public void getClientByEmailNotFound() throws Exception {
        mockMvc.performRequest(get("/clients/search").param("email", "elvira111@email.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createClient() throws Exception {
        MvcResult createdClientResult =
                performRequestAndCheckNewClient(HttpStatus.CREATED,
                        mockMvc.performRequest(post("/clients").content("{\"email\":\"poptavka3@mailinator.com\","
                                + "\"personFirstName\":\"Janko\","
                                + "\"personLastName\":\"Hraško\","
                                + "\"password\":\"kreslo\","
                                + "\"addresses\":"
                                    + "[{\"region\":\"locality1\","
                                    + "\"city\":\"locality111\","
                                    + " \"street\":\"Main road\"}]}")));

        final ClientDto clientDto =
                jsonObjectMapper.readValue(createdClientResult.getResponse().getContentAsString(), ClientDto.class);

        performRequestAndCheckNewClient(HttpStatus.OK, mockMvc.performRequest(get("/clients/{id}", clientDto.getId())));

        final Client createdClient = clientService.getById(clientDto.getId());
        Assert.assertNotNull("No corresponding client found in database!", createdClient);

        // check that activation email has been sent to the client
        assertThat("No activation email should be sent to the external client",
                createdClient.getBusinessUser().getActivationEmail(), notNullValue());

        // check that external client has notifications set
        final List<NotificationItem> notifications =
                createdClient.getBusinessUser().getSettings().getNotificationItems();
        assertTrue("No notifications should be set for external client, but found:" + notifications,
                CollectionUtils.isNotEmpty(notifications));
    }

    @Test
    public void createExternalClient() throws Exception {
        MvcResult createdClientResult =
                performRequestAndCheckNewClient(HttpStatus.CREATED,
                        mockMvc.performRequest(post("/clients").content("{\"email\":\"poptavka3@mailinator.com\","
                                + "\"personFirstName\":\"Janko\","
                                + "\"personLastName\":\"Hraško\","
                                + "\"password\":\"kreslo\","
                                + "\"origin\":\"" + Origin.EXTERNAL_ORIGIN_CODE
                                + "\"," + "\"addresses\":"
                                    + "[{\"region\":\"locality1\","
                                    + "\"city\":\"locality111\","
                                    + " \"street\":\"Main road\"}]}")));

        final ClientDto clientDto =
                jsonObjectMapper.readValue(createdClientResult.getResponse().getContentAsString(), ClientDto.class);

        performRequestAndCheckNewClient(HttpStatus.OK, mockMvc.performRequest(get("/clients/{id}", clientDto.getId())));

        final Client createdClient = clientService.getById(clientDto.getId());
        Assert.assertNotNull("No corresponding client found in database!", createdClient);

        // check that no activation email has been sent to the external client
        assertThat("No activation email should be sent to the external client",
                createdClient.getBusinessUser().getActivationEmail(), nullValue());

        // check that external client has no notifications set
        final List<NotificationItem> notifications =
                createdClient.getBusinessUser().getSettings().getNotificationItems();
        assertThat("Only one notification should be set for external client, but found:" + notifications,
                notifications.size(), is(1));
        assertThat("Only 'new.message' notification should be set for external client, but found:"
                    + notifications.get(0),
                notifications.get(0).getNotification().getCode(), is(Registers.Notification.NEW_MESSAGE.getCode()));
    }

    @Test
    public void createClientMissingEmail() throws Exception {
        mockMvc.performRequest(post("/clients").content("{\"personFirstName\":\"Janko\", "
                + "\"personLastName\":\"Hraško\", "
                + "\"password\":\"kreslo\",\n"
                + "\"addresses\":"
                    + "[{\"region\":\"locality1\","
                    + "\"city\":\"locality111\","
                    + " \"street\":\"Main road\"}]}"))
                .andExpect(status().isBadRequest());

    }


    @Test
    public void createClientMissingAddress() throws Exception {
        this.mockMvc.performRequest(post("/clients").content("{\"email\":\"poptavka3@mailinator.com\","
                + "\"personFirstName\":\"Janko\", "
                + "\"personLastName\":\"Hraško\", "
                + "\"password\":\"kreslo\"}\n"))
                .andExpect(status().isBadRequest());
    }


    //--------------------------------------------------- HELPER METHODS -----------------------------------------------
    private MvcResult performRequestAndCheckNewClient(HttpStatus expectedStatusCode, ResultActions request)
        throws Exception {
        return request
                .andExpect(status().is(expectedStatusCode.value()))
                .andExpect(jsonPath("$.email").value("poptavka3@mailinator.com"))
                .andExpect(jsonPath("$.personFirstName").value("Janko"))
                .andExpect(jsonPath("$.personLastName").value("Hraško"))
                .andExpect(jsonPath("$.addresses[0].region").value("locality1"))
                .andExpect(jsonPath("$.addresses[0].city").value("locality111"))
                .andExpect(jsonPath("$.addresses[0].street").value("Main road"))
                .andExpect(jsonPathMissing("$.password")).andReturn();
    }

    private MvcResult performRequestAndCheckElvira(ResultActions request)
        throws Exception {
        return request
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(111111111))
                .andExpect(jsonPath("$.email").value("elvira@email.com"))
                .andExpect(jsonPath("$.personFirstName").value("Elvíra"))
                .andExpect(jsonPath("$.personLastName").value("Vytretá"))
                .andExpect(jsonPath("$.companyName").value("My First Company"))
                .andExpect(jsonPath("$.addresses[0].region").value("locality2"))
                .andExpect(jsonPath("$.addresses[0].city").value("locality211"))
                .andExpect(jsonPath("$.addresses[0].street").value("Test street 9"))
                .andExpect(jsonPath("$.addresses[0].zipCode").value("60200"))

                .andExpect(jsonPathMissing("$.password"))
                .andExpect(jsonPathMissing("$.overallRating")).andReturn();
    }

}
