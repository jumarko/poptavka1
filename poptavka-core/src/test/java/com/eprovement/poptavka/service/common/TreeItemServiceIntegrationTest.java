package com.eprovement.poptavka.service.common;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.TreeItem;
import com.eprovement.poptavka.domain.demand.Category;
import java.util.List;

import com.eprovement.poptavka.service.GeneralService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;

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
public class TreeItemServiceIntegrationTest extends DBUnitIntegrationTest {

    @Autowired
    private TreeItemService treeItemService;
    @Autowired
    private GeneralService generalService;


    @Test
    public void testGetAllLeavesIdsForLocalities() {
        final Long[] expectedLocalityLeaves = {211L, 212L, 213L, 214L, 111L, 121L};
        checkLeaves(Locality.class, 6, expectedLocalityLeaves);
        checkIsLeafForLocality(expectedLocalityLeaves);
    }




    @Test
    public void testGetAllLeavesIdsForCategories() {
        final Long[] expectedCategoryLeaves = {9L, 111L, 112L, 1131L, 1132L, 21L, 22L, 23L, 311L, 312L};
        checkLeaves(Category.class, 10, expectedCategoryLeaves);
        checkIsLeafForCategories(expectedCategoryLeaves);
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
            assertTrue(allLocalitiesLeavesIds.contains(expectedId));
        }
    }


    private void checkIsLeafForLocality(Long[] expectedLocalityLeavesIds) {
        for (Long localityId : expectedLocalityLeavesIds) {
            checkIsLeaf(generalService.find(Locality.class, localityId));
        }
    }

    private void checkIsLeafForCategories(Long[] expectedCategoryLeavesIds) {
        for (Long categoryId : expectedCategoryLeavesIds) {
            checkIsLeaf(generalService.find(Category.class, categoryId));
        }
    }


    /**
     * Checks whether method {@link com.eprovement.poptavka.domain.common.TreeItem#isLeaf()} returns true
     * for tree item id which is really a leaf.
     * @param leafTreeIItemId
     */
    private void checkIsLeaf(TreeItem treeItem) {
        assertTrue("TreeItem=" + treeItem + " is expected to be a leaf but TreeItem.isLeaf returned false ",
                treeItem.isLeaf());
    }
}
