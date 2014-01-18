package com.eprovement.poptavka.rest.externalsource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.rest.ResourceIntegrationTest;
import org.junit.Test;

@DataSet(path = { "classpath:com/eprovement/poptavka/domain/demand/CategoryDataSet.xml" },
        dtd = "classpath:test.dtd")
public class ExternalSourceResourceIntegrationTest extends ResourceIntegrationTest {

    @Test
    public void getCategoriesMappingForExternalSource() throws Exception {
        mockMvc.performRequest(get("/sources/FBOGOV/categorymapping"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.source").value("FBOGOV"))

                // first mapping
                .andExpect(jsonPath("$.mappings[0].externalCategoryId").value("111110"))
                .andExpect(jsonPath("$.mappings[0].categories[0].id").value(11))
                .andExpect(jsonPath("$.mappings[0].categories[0].name").value("Category 11"))
                .andExpect(jsonPath("$.mappings[0].categories[1].id").value(111))
                .andExpect(jsonPath("$.mappings[0].categories[2].id").value(1131))

                // second mapping
                .andExpect(jsonPath("$.mappings[1].externalCategoryId").value("112111"))
                .andExpect(jsonPath("$.mappings[1].categories[0].id").value(112))

                // third mapping
                .andExpect(jsonPath("$.mappings[2].externalCategoryId").value("311211"))
                .andExpect(jsonPath("$.mappings[2].categories[0].id").value(311))

                .andExpect(jsonPath("$.links.self").value("/api/sources/FBOGOV/categorymapping"));
    }


}
