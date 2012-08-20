package com.eprovement.poptavka.util.orm;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.service.demand.CategoryService;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @deprecated {@link AdditionalInfoLoadListener is not used anymore}
 *
 * @author Juraj Martinka
 *         Date: 13.2.11
 */
@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/CategoryDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/RatingDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/DemandDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/SupplierDataSet.xml" },
        dtd = "classpath:test.dtd")
@Ignore // this test is deprecated
public class AdditionalInfoLoadListenerTest extends DBUnitIntegrationTest {

    private static final String ROOT_CATEGORY_CODE = "0";
    private static final String ROOT_LOCALITY_CODE = "CZ";

    @Autowired
    private LocalityService localityService;

    @Autowired
    private CategoryService categoryService;



    @Test
    public void testCategories() {
        checkCategoryCounts(ROOT_CATEGORY_CODE, 0L, 4L);
        checkCategoryCounts("cat1", 0L, 2L);
        checkCategoryCounts("cat2", 0L, 0L);
        checkCategoryCounts("cat3", 0L, 2L);
        checkCategoryCounts("cat11", 0L, 2L);
        checkCategoryCounts("cat111", 0L, 0L);
        checkCategoryCounts("cat113", 0L, 1L);
        checkCategoryCounts("cat31", 0L, 1L);
        checkCategoryCounts("cat312", 0L, 1L);
    }


    @Test
    public void testLocalities() {
        checkLocalityCounts(ROOT_LOCALITY_CODE, 10L, 4L);
    }


    //-------------------------- HELPTER METHODS -----------------------------------------------------------------------
    private void checkCategoryCounts(String categoryCode, Long expectedDemandCount, Long expectedSupplierCount) {
        final Category category = this.categoryService.getCategory(categoryCode);
        Assert.assertEquals(Long.valueOf(expectedDemandCount), category.getAdditionalInfo().getDemandsCount());
        Assert.assertEquals(Long.valueOf(expectedSupplierCount), category.getAdditionalInfo().getSuppliersCount());
    }

    private void checkLocalityCounts(String localityCode, Long expectedDemandCount, Long expectedSupplierCount) {
        final Locality locality = this.localityService.getLocality(localityCode);
        Assert.assertEquals(Long.valueOf(expectedDemandCount), locality.getAdditionalInfo().getDemandsCount());
        Assert.assertEquals(Long.valueOf(expectedSupplierCount), locality.getAdditionalInfo().getSuppliersCount());
    }

}
