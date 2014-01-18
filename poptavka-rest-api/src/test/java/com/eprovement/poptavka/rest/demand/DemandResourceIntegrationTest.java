package com.eprovement.poptavka.rest.demand;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.common.Origin;
import com.eprovement.poptavka.rest.ResourceIntegrationTest;
import com.eprovement.poptavka.rest.client.ClientDto;
import com.eprovement.poptavka.rest.common.dto.CategoryDto;
import com.eprovement.poptavka.rest.common.dto.LocalityDto;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/CategoryDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/RatingDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/register/RegisterDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/DemandDataSet.xml" },
        dtd = "classpath:test.dtd")
public class DemandResourceIntegrationTest extends ResourceIntegrationTest {

    private static final String DEMAND_TITLE = "New demand from external system";
    private static final String DEMAND_DESCRIPTION = "Description of New demand from external system";
    private static final int DEMAND_PRICE = 1000;
    private static final CategoryDto DEMAND_CATEGORY = new CategoryDto().setId(11L);
    private static final LocalityDto DEMAND_LOCALITY = new LocalityDto().setId(11L);

    @Test
    public void listDemands() throws Exception {
        this.mockMvc.performRequest(get("/demands"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.collection[0].title").value("Demand 1"))
                .andExpect(jsonPath("$.collection[1].title").value("Demand 2"))
                .andExpect(jsonPath("$.collection[14].title").value("Demand 11"))
                .andExpect(jsonPath("$.links.self").value("/api/demands"))
                .andExpect(jsonPath("$.paging.offset").value(0))
                .andExpect(jsonPath("$.paging.count").value(15));
    }

    @Test
    public void getById() throws Exception {
        this.mockMvc.performRequest(get("/demands/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Demand 3"))
                .andExpect(jsonPath("$.status").value("INVALID"))
                .andExpect(jsonPath("$.price").value(1200.00))
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.createdDate").value(startsWith("2001-01-01T00:00:01")))

                // demand's categories
                .andExpect(jsonPath("$.categories").exists())
                .andExpect(jsonPath("$.categories[0].id").value(2))
                .andExpect(jsonPath("$.categories[0].name").value("Category 2"))
                .andExpect(jsonPath("$.categories[0].description").value("Category desc 2"))

                // demand's localities
                .andExpect(jsonPath("$.localities").exists())
                .andExpect(jsonPath("$.localities[0].id").value(2))
                .andExpect(jsonPath("$.localities[0].region").value("locality2"))
                .andExpect(jsonPath("$.localities[0].district").doesNotExist())
                .andExpect(jsonPath("$.localities[0].city").doesNotExist())
                .andExpect(jsonPath("$.localities[0].street").doesNotExist())
                .andExpect(jsonPath("$.links.self").value("/api/demands/3"))

                // demand's client
                .andExpect(jsonPath("$.client").exists())
                .andExpect(jsonPath("$.client.id").value(111111113))
                .andExpect(jsonPath("$.client.email").value("lisohlavka@email.com"))
                .andExpect(jsonPath("$.client.personFirstName").value("Lisohlávka"))
                .andExpect(jsonPath("$.client.personLastName").value("Čínska"))
                .andExpect(jsonPath("$.client.companyName").value("My Third Company"))
                .andExpect(jsonPath("$.client.password").doesNotExist());
    }

    @Test
    public void getByIdNotExist() throws Exception {
        this.mockMvc.performRequest(get("/demands/123456789067"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createValidDemand() throws Exception {
        createDemandAndCheck(validDemand());
    }

    @Test
    public void createExternalDemand() throws Exception {
        final Map<String, Object> demand = validDemand();
        demand.put("origin", Origin.EXTERNAL_ORIGIN_CODE);
        final DemandDto createdDemand = createDemandAndCheck(demand);
        assertThat(createdDemand.getOrigin(), is(Origin.EXTERNAL_ORIGIN_CODE));
    }


    /**
     * Sometimes the price is unknown which is perfectly valid case.
     */
    @Test
    public void createDemandWithoutPriceValid() throws Exception {
        final Map<String, Object> demandWithoutPrice = validDemand();
        demandWithoutPrice.remove("price");
        // create demand and check only id - createDemandAndCheck cannot be used because it's checking price too
        this.mockMvc.performRequest(post("/demands")
               .content(toJsonString(demandWithoutPrice)))
                .andExpect(status().isCreated())
                // it is important to check that id has been generated and returned
                .andExpect(jsonPath("$.id").exists());
    }


    @Test
    public void createDemandWithoutTitleInvalid() throws Exception {
        createDemandWithoutFieldAndCheckResponse("title");
    }

    @Test
    public void createDemandWithoutDescriptionInvalid() throws Exception {
        createDemandWithoutFieldAndCheckResponse("description");
    }


    @Test
    public void createDemandWithoutCategoryInvalid() throws Exception {
        createDemandWithoutFieldAndCheckResponse("categories");
    }

    @Test
    public void createDemandWithUnknownCategoryInvalid() throws Exception {
        final Map<String, Object> demandWithUnknownCategory = validDemand();
        demandWithUnknownCategory.put("categories",
                CollectionUtils.union((Collection) demandWithUnknownCategory.get("categories"),
                        newArrayList(new CategoryDto().setId(1999L))));
        createAndExpectValidationError(demandWithUnknownCategory);
    }

    @Test
    public void createDemandWithoutLocalityInvalid() throws Exception {
        createDemandWithoutFieldAndCheckResponse("localities");
    }

    @Test
    public void createDemandWithUnknownLocalityInvalid() throws Exception {
        final Map<String, Object> demandWithUnknownLocality = validDemand();
        demandWithUnknownLocality.put("localities",
                CollectionUtils.union((Collection) demandWithUnknownLocality.get("localities"),
                        newArrayList(new LocalityDto().setId(1999L))));
        createAndExpectValidationError(demandWithUnknownLocality);
    }

    @Test
    public void createDemandWithoutClientInvalid() throws Exception {
        createDemandWithoutFieldAndCheckResponse("client");
    }

    @Test
    public void createDemandWithUnknownClientInvalid() throws Exception {
        final Map<String, Object> demandWithUnknownClient = validDemand();
        demandWithUnknownClient.put("client", new ClientDto().setId(-1L));
        this.mockMvc.performRequest(post("/demands").content(toJsonString(demandWithUnknownClient)))
                .andExpect(status().isNotFound());
    }

    //--------------------------------------------------- HELPER METHODS -----------------------------------------------

        // TODO: move to the common place
    private String toJsonString(Map<?, ?> jsonObject) {
        try {
            return jsonObjectMapper.writeValueAsString(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException("Cannot serialize json!");
        }
    }

    private Map<String, Object> validDemand() {
        return new HashMap<String, Object>() {
            {
                put("title", DEMAND_TITLE);
                put("description", DEMAND_DESCRIPTION);
                put("price", DEMAND_PRICE);
                put("categories", newArrayList(DEMAND_CATEGORY));
                put("localities", newArrayList(DEMAND_LOCALITY));
                put("client", new ClientDto().setId(111111113L));
            }
        };
    }

    private DemandDto createDemandAndCheck(Map<?, ?> demand) throws Exception {
        final MvcResult createdDemandResult = this.mockMvc.performRequest(post("/demands")
               .content(toJsonString(demand)))
                .andExpect(status().isCreated())
                // it is important to check that id has been generated and returned
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value(DEMAND_TITLE))
                .andExpect(jsonPath("$.description").value(DEMAND_DESCRIPTION))
                .andExpect(jsonPath("$.price").value(DEMAND_PRICE))
                .andExpect(jsonPath("$.categories[0]").exists())
                .andExpect(jsonPath("$.categories[0].id").value(11))
                .andExpect(jsonPath("$.categories[0].name").value("Category 11"))
                .andExpect(jsonPath("$.categories[0].description").exists())
                .andExpect(jsonPath("$.localities[0]").exists())
                .andExpect(jsonPath("$.localities[0].id").value(11))
                .andExpect(jsonPath("$.localities[0].region").value("locality1"))
                .andExpect(jsonPath("$.localities[0].district").value("locality11"))
                .andExpect(jsonPath("$.client.id").value(111111113))
                .andExpect(jsonPath("$.links.self").exists())
                // createdDate should never be null even if it was not explicitly set when POSTing new demand
                .andExpect(jsonPath("$.createdDate").exists())
                .andReturn();

        final DemandDto createDemandDto =
                jsonObjectMapper.readValue(createdDemandResult.getResponse().getContentAsString(), DemandDto.class);
        assertNotNull(createDemandDto);
        final String demandId = createDemandDto.getId();
        this.mockMvc.performRequest(get("/demands/" + demandId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(DEMAND_TITLE));
        return createDemandDto;
    }

    private void createDemandWithoutFieldAndCheckResponse(String fieldName) throws Exception {
        final Map demandWithoutField = validDemand();
        demandWithoutField.remove(fieldName);
        createAndExpectValidationError(demandWithoutField);
    }

    private void createAndExpectValidationError(Map demand) throws Exception {
        this.mockMvc.performRequest(post("/demands")
                .content(toJsonString(demand)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(notNullValue()));
    }

}
