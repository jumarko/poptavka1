package com.eprovement.poptavka.dao.demand;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.demand.Demand;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Juraj Martinka
 *         Date: 9/29/11
 *         Time: 7:50 PM
 */
@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/CategoryDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/RatingDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/demand/DemandDataSet.xml" },
        dtd = "classpath:test.dtd")
public class DemandDaoIntegrationTest extends DBUnitIntegrationTest {

    @Autowired
    private DemandDao demandDao;


    @Test
    public void testGetAllNewDemands() {
        final List<Demand> allNewDemands = this.demandDao.getAllNewDemands(ResultCriteria.EMPTY_CRITERIA);
        Assert.assertThat("Incorrect count of all new demands", allNewDemands.size(), Is.is(5));
        checkDemandExist(allNewDemands, 1L);
        checkDemandExist(allNewDemands, 7L);
        checkDemandExist(allNewDemands, 8L);
        checkDemandExist(allNewDemands, 9L);
        checkDemandExist(allNewDemands, 10L);
    }

    private void checkDemandExist(List<Demand> allNewDemands, final Long demandId) {
        Assert.assertNotNull(demandId);
        Assert.assertTrue(CollectionUtils.exists(allNewDemands, new Predicate() {
            @Override
            public boolean evaluate(Object o) {
                return demandId.equals(((Demand) o).getId());
            }
        }));
    }
}

