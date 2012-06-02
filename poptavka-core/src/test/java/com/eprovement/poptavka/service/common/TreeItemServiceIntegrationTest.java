package com.eprovement.poptavka.service.common;

import com.eprovement.poptavka.base.integration.DBUnitBaseTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.TreeItem;
import com.eprovement.poptavka.domain.demand.Category;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 12.2.11
 */
@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/CategoryDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/RatingDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/DemandDataSet.xml" },
        dtd = "classpath:test.dtd")
public class TreeItemServiceIntegrationTest extends DBUnitBaseTest {

    @Autowired
    private TreeItemService treeItemService;


    @Test
    public void testGetAllLeavesIdsForLocalities() {
        checkLeaves(Locality.class, 6, new Long[] {5L, 6L, 9L, 10L, 11L, 12L});
    }




    @Test
    public void testGetAllLeavesIdsForCategories() {
        checkLeaves(Category.class, 10, new Long[]{10L, 11L, 15L, 16L, 6L, 7L, 8L, 13L, 14L, 111L});
    }


    //---------------------------- HELPER METHODS ----------------------------------------------------------------------
    /**
     * Check if leaves in hierarchic structure of all entities' IDs of type <code>treeItemClass</code>
     * contains exactly <code>expectedLeavesNumber</code> and contains all of IDs from <code>expectedIds</code>.
     * @param treeItemClass
     * @param expectedLeavesNumber
     * @param expectedIds
     */
    private void checkLeaves(Class<? extends TreeItem> treeItemClass, int expectedLeavesNumber, Long[] expectedIds) {
        final List<Long> allLocalitiesLeavesIds = this.treeItemService.getAllLeavesIds(treeItemClass);
        Assert.assertEquals(expectedLeavesNumber, allLocalitiesLeavesIds.size());

        // check all leaves
        for (Long expectedId : expectedIds) {
            Assert.assertTrue(allLocalitiesLeavesIds.contains(expectedId));
        }
    }
}
