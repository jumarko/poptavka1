package cz.poptavka.sample.service.demand;

import cz.poptavka.sample.application.ApplicationContextHolder;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.service.common.TreeItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 12.2.11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class PrintCategories {

    @Test
    public void main() {
        final TreeItemService treeItemService =
                (TreeItemService) ApplicationContextHolder.getApplicationContext().getBean("treeItemService");
        final List<Category> allCategories = treeItemService.getAllChildren(null, Category.class);
        for (Category category   : allCategories) {
            System.out.println(category);
        }

    }
}
