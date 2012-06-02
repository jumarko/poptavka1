package com.eprovement.poptavka.service.jobs;

import com.google.common.base.Preconditions;
import com.eprovement.poptavka.base.integration.DBUnitBaseTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.AdditionalInfo;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.service.demand.CategoryService;
import com.eprovement.poptavka.service.jobs.base.JobTask;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Test of job that fills counts of demands and suppliers in db.
 *
 * @author Juraj Martinka
 *         Date: 3.4.11
 */
@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/CategoryDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/SupplierDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/RatingDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/DemandDataSet.xml" },
        dtd = "classpath:test.dtd")
@Ignore
public class AdditionalInfoFillerTest extends DBUnitBaseTest {

    @Autowired
    @Qualifier(value = "additionalInfoFiller")
    private JobTask additionalInfoFiller;

    @Autowired
    private LocalityService localityService;

    @Autowired
    private CategoryService categoryService;


    @Test
    public void testExecute() throws Exception {
        // fill localities' and categories' additional info
        this.additionalInfoFiller.execute();

        // ... and check it
        checkCategory("cat0", new AdditionalInfo(10L, 4L));
        checkCategory("cat11", new AdditionalInfo(4L, 2L));

        checkLocality("CZ", new AdditionalInfo(10L, 4L));
        checkLocality("loc121", new AdditionalInfo(2L, 0L));
    }


    //----------------------------------- HELPER METHODS ---------------------------------------------------------------
    /**
     * Check if category with given <code>categoryCode</code> has assigned additional info with expected counts.
     *
     * @param categoryCode unique code of category
     * @param expectedAdditionalInfo additional info that should be set for given category
     */
    private void checkCategory(String categoryCode, AdditionalInfo expectedAdditionalInfo) {
        final Category category = this.categoryService.getCategory(categoryCode);
        Preconditions.checkState(category != null);
        checkCounts(expectedAdditionalInfo, category.getAdditionalInfo());
    }


    /**
     * Check if locality with given <code>localityCode</code> has assigned additional info with expected counts.
     *
     * @param localityCode
     * @param expectedAdditionalInfo
     */
    private void checkLocality(String localityCode, AdditionalInfo expectedAdditionalInfo) {
        final Locality locality = this.localityService.getLocality(localityCode);
        Preconditions.checkState(locality != null);
        checkCounts(expectedAdditionalInfo, locality.getAdditionalInfo());
    }


    private void checkCounts(AdditionalInfo expectedAdditionalInfo, AdditionalInfo additionalInfo) {
        Assert.assertNotNull(expectedAdditionalInfo);
        Assert.assertNotNull(additionalInfo);
        Assert.assertEquals(expectedAdditionalInfo.getDemandsCount(), additionalInfo.getDemandsCount());
        Assert.assertEquals(expectedAdditionalInfo.getSuppliersCount(), additionalInfo.getSuppliersCount());
    }
}
