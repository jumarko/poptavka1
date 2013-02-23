package com.eprovement.poptavka.service.demand;

import com.eprovement.poptavka.base.integration.BasicIntegrationTest;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.service.common.TreeItemService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 12.2.11
 */
public class PrintCategories extends BasicIntegrationTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TreeItemService treeItemService;



    @Test
    public void printRootCategories() {
        final List<Category> rootCategories = categoryService.getRootCategories();
        System.out.println("Root categories count: " + rootCategories.size());
        for (Category category   : rootCategories) {
            System.out.println("Root category: [" + category + "]");
        }
    }


    @Test
    public void printAllCategories() {
        final List<Category> allCategories = treeItemService.getAllDescendants(null, Category.class);
        for (Category category   : allCategories) {
            System.out.println(category);
        }

    }
}
