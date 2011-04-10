package cz.poptavka.sample.service.jobs;

import com.google.common.base.Preconditions;
import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.AdditionalInfo;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.service.address.LocalityService;
import cz.poptavka.sample.service.demand.CategoryService;
import cz.poptavka.sample.service.jobs.base.JobTask;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author Juraj Martinka
 *         Date: 3.4.11
 */
@DataSet(path = {
        "classpath:cz/poptavka/sample/domain/address/LocalityDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/CategoryDataSet.xml",
        "classpath:cz/poptavka/sample/domain/user/SupplierDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/DemandDataSet.xml" },
        dtd = "classpath:test.dtd")
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
