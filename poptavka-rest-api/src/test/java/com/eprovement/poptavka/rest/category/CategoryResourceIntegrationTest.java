package com.eprovement.poptavka.rest.category;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.rest.ResourceIntegrationTest;
import org.junit.Test;

@DataSet(path = { "classpath:com/eprovement/poptavka/domain/demand/CategoryDataSet.xml" },
        dtd = "classpath:test.dtd")
public class CategoryResourceIntegrationTest extends ResourceIntegrationTest {

    @Test
    public void listCategories() throws Exception {
        mockMvc.performRequest(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.collection[0].id").value(0))
                .andExpect(jsonPath("$.collection[0].name").value("Root Category"))
                .andExpect(jsonPath("$.collection[0].links.self").value("/api/categories/0"))
                .andExpect(jsonPath("$.collection[0].description").value("Common ancestor for all categories"))
                .andExpect(jsonPath("$.collection[16].name").value("Category 1132"))
                .andExpect(jsonPath("$.links.self").value("/api/categories"))
                .andExpect(jsonPath("$.paging.offset").value(0))
                .andExpect(jsonPath("$.paging.count").value(17));
    }

    @Test
    public void getCategoryById() throws Exception {
        mockMvc.performRequest(get("/categories/1132"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1132))
                .andExpect(jsonPath("$.name").value("Category 1132"))
                .andExpect(jsonPath("$.links.self").value("/api/categories/1132"))
                .andExpect(jsonPath("$.description").value("Category desc 1132"));
    }

}
