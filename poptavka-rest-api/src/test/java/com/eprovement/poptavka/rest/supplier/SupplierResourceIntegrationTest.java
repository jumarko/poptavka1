package com.eprovement.poptavka.rest.supplier;

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
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.rest.ResourceIntegrationTest;
import com.eprovement.poptavka.service.user.SupplierService;
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
        "classpath:com/eprovement/poptavka/domain/demand/CategoryDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/SupplierDataSet.xml" },
        dtd = "classpath:test.dtd")
public class SupplierResourceIntegrationTest extends ResourceIntegrationTest {

    @Autowired
    private SupplierService supplierService;

    @Test
    public void listSuppliers() throws Exception {
        this.mockMvc.performRequest(get("/suppliers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.collection[0].email").value("elvira11@email.com"))
                .andExpect(jsonPath("$.collection[1].email").value("velislav12@email.com"))
                .andExpect(jsonPath("$.collection[2].email").value("lisohlavka12@email.com"))
                .andExpect(jsonPath("$.collection[3].email").value("elvira14@email.com"))
                .andExpect(jsonPath("$.links.self").value("/api/suppliers"))
                .andExpect(jsonPath("$.paging.offset").value(0))
                .andExpect(jsonPath("$.paging.count").value(4));
    }

    @Test
    public void getSupplierById() throws Exception {
        performRequestAndCheckElvira(this.mockMvc.performRequest(get("/suppliers/{id}", 11L)));
    }

    @Test
    public void getSupplierByIdNotFound() throws Exception {
        this.mockMvc.performRequest(get("/suppliers/{id}", 333L))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getSupplierByEmail() throws Exception {
        performRequestAndCheckElvira(this.mockMvc.performRequest(get("/suppliers/search")
                .param("email", "elvira11@email.com")));
    }

    @Test
    public void getSupplierByEmailNotFound() throws Exception {
        this.mockMvc.performRequest(get("/suppliers/search").param("email", "elvira@email.com"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createSupplier() throws Exception {
        // sample data downloaded from http://uscompanydatabase.com/database_us.aspx
        MvcResult createdSupplierResult =
                performRequestAndCheckNewSupplier(HttpStatus.CREATED,
                        this.mockMvc.performRequest(post("/suppliers")
                                .content("{\"email\":\"import@mailinator.com\","
                                        + "\"personFirstName\":\"Scot\","
                                        + "\"personLastName\":\"Teasdale\","
                                        + "\"companyName\":\"Quad City Port Svc\","
                                        + "\"phone\":\"123465789\","
                                        + "\"website\":\"www.qcps.com\","
                                        + "\"password\":\"kreslo\","
                                        + "\"categories\":[{\"id\":11}],"
                                        + "\"localities\":[{\"region\":\"locality2\"}],"
                                        + "\"addresses\":"
                                            + "[{\"region\":\"locality1\","
                                                + "\"district\":\"locality11\","
                                                + "\"city\":\"locality111\","
                                                + "\"zipCode\":\"52722-4938\","
                                                + " \"street\":\"1634 State St\"}]}")));

        final SupplierDto supplierDto =
                jsonObjectMapper.readValue(createdSupplierResult.getResponse().getContentAsString(), SupplierDto.class);

        performRequestAndCheckNewSupplier(HttpStatus.OK,
                this.mockMvc.performRequest(get("/suppliers/{id}", supplierDto.getId())));

        final Supplier createdSupplier = supplierService.getById(supplierDto.getId());
        Assert.assertNotNull("No corresponding supplier found in database!", createdSupplier);

        // check that activation email has been sent to the supplier
        assertThat("No activation email should be sent to the external supplier",
                createdSupplier.getBusinessUser().getActivationEmail(), notNullValue());

        // check that external supplier has notifications set
        final List<NotificationItem> notifications =
                createdSupplier.getBusinessUser().getSettings().getNotificationItems();
        assertTrue("Notifications should be set for classic external supplier, but found:" + notifications,
                CollectionUtils.isNotEmpty(notifications));
    }

    @Test
    public void createExternalSupplier() throws Exception {
        MvcResult createdSupplierResult =
                performRequestAndCheckNewSupplier(HttpStatus.CREATED, this.mockMvc.performRequest(post("/suppliers")
                        .content("{\"email\":\"import@mailinator.com\","
                                + "\"personFirstName\":\"Scot\","
                                + "\"personLastName\":\"Teasdale\","
                                + "\"companyName\":\"Quad City Port Svc\","
                                + "\"origin\":\""
                                + Origin.EXTERNAL_ORIGIN_CODE + "\","
                                + "\"phone\":\"123465789\","
                                + "\"website\":\"www.qcps.com\","
                                + "\"password\":\"kreslo\","
                                // sicCode 20 == id 11
                                + "\"categories\":[{\"sicCode\":20}],"
                                + "\"localities\":[{\"region\":\"MM\"}],"
                                + "\"addresses\":"
                                    + "[{\"region\":\"LL\","
                                        + "\"district\":\"locality11\","
                                        + "\"city\":\"locality111\","
                                        + "\"zipCode\":\"52722-4938\","
                                        + " \"street\":\"1634 State St\"}]}")));

        final SupplierDto supplierDto =
                jsonObjectMapper.readValue(createdSupplierResult.getResponse().getContentAsString(), SupplierDto.class);

        performRequestAndCheckNewSupplier(HttpStatus.OK,
                this.mockMvc.performRequest(get("/suppliers/{id}", supplierDto.getId())));

        final Supplier createdSupplier = supplierService.getById(supplierDto.getId());
        Assert.assertNotNull("No corresponding supplier found in database!", createdSupplier);

        // check that no activation email has been sent to the external supplier
        assertThat("No activation email should be sent to the external supplier",
                createdSupplier.getBusinessUser().getActivationEmail(), nullValue());

        // check that external supplier has proper notifications set
        final List<NotificationItem> notifications =
                createdSupplier.getBusinessUser().getSettings().getNotificationItems();
        assertThat("Only one notification should be set for external supplier, but found:" + notifications,
                notifications.size(), is(1));
        assertThat("Only 'new.message' notification should be set for external supplier, but found:"
                    + notifications.get(0),
                notifications.get(0).getNotification().getCode(), is(Registers.Notification.NEW_MESSAGE.getCode()));
    }

    @Test
    public void createSupplierMissingEmail() throws Exception {
        this.mockMvc.performRequest(post("/suppliers").content("{\"personFirstName\":\"Janko\", "
                + "\"personLastName\":\"Hraško\", "
                + "\"password\":\"kreslo\",\n"
                + "\"addresses\":"
                    + "[{\"region\":\"locality1\","
                    + "\"city\":\"locality111\","
                    + " \"street\":\"Main road\"}]}"))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void createSupplierMissingAddress() throws Exception {
        this.mockMvc.performRequest(post("/suppliers").content("{\"email\":\"poptavka3@mailinator.com\","
                + "\"personFirstName\":\"Janko\", "
                + "\"personLastName\":\"Hraško\", "
                + "\"password\":\"kreslo\"}\n"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createSupplierMissingCategory() throws Exception {
        this.mockMvc.performRequest(post("/suppliers").content("{\"email\":\"poptavka3@mailinator.com\","
                + "\"personFirstName\":\"Janko\", "
                + "\"personLastName\":\"Hraško\", "
                + "\"password\":\"kreslo\"}\n"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createSupplierMissingLocality() throws Exception {
        this.mockMvc.performRequest(post("/suppliers").content("{\"email\":\"poptavka3@mailinator.com\","
                + "\"personFirstName\":\"Janko\", "
                + "\"personLastName\":\"Hraško\", "
                + "\"password\":\"kreslo\"}\n"))
                .andExpect(status().isBadRequest());
    }


    //--------------------------------------------------- HELPER METHODS -----------------------------------------------
    private MvcResult performRequestAndCheckNewSupplier(HttpStatus expectedStatusCode, ResultActions request)
        throws Exception {
        return request
                .andExpect(status().is(expectedStatusCode.value()))
                .andExpect(jsonPath("$.email").value("import@mailinator.com"))
                .andExpect(jsonPath("$.personFirstName").value("Scot"))
                .andExpect(jsonPath("$.personLastName").value("Teasdale"))
                .andExpect(jsonPath("$.phone").value("123465789"))
                .andExpect(jsonPath("$.companyName").value("Quad City Port Svc"))
                .andExpect(jsonPath("$.website").value("www.qcps.com"))
                .andExpect(jsonPath("$.localities[0].id").value(2))
                .andExpect(jsonPath("$.categories[0].id").value(11))
                .andExpect(jsonPath("$.addresses[0].region").value("locality1"))
                .andExpect(jsonPath("$.addresses[0].district").value("locality11"))
                .andExpect(jsonPath("$.addresses[0].city").value("locality111"))
                .andExpect(jsonPath("$.addresses[0].zipCode").value("52722-4938"))
                .andExpect(jsonPath("$.addresses[0].street").value("1634 State St"))
                .andExpect(jsonPathMissing("$.password")).andReturn();
    }

    private MvcResult performRequestAndCheckElvira(ResultActions request)
        throws Exception {
        return request
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(11))
                .andExpect(jsonPath("$.email").value("elvira11@email.com"))
                .andExpect(jsonPath("$.personFirstName").value("Elvíra"))
                .andExpect(jsonPath("$.personLastName").value("Vytretá"))
                .andExpect(jsonPath("$.companyName").value("My First Company"))
                .andExpect(jsonPath("$.localities[0].id").value(1))
                .andExpect(jsonPath("$.localities[1].id").value(213))
                .andExpect(jsonPath("$.categories[0].id").value(3))

                .andExpect(jsonPathMissing("$.password"))
                .andExpect(jsonPathMissing("$.overallRating")).andReturn();
    }

}
