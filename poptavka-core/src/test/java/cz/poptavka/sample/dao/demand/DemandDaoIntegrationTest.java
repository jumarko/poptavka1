package cz.poptavka.sample.dao.demand;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.demand.Demand;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 9/29/11
 *         Time: 7:50 PM
 */
@DataSet(path = {
        "classpath:cz/poptavka/sample/domain/address/LocalityDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/CategoryDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/RatingDataSet.xml",
        "classpath:cz/poptavka/sample/domain/user/UsersDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/DemandDataSet.xml" },
        dtd = "classpath:test.dtd")
public class DemandDaoIntegrationTest extends DBUnitBaseTest {

    @Autowired
    private DemandDao demandDao;


    @Test
    public void testGetAllNewDemands() {
        final List<Demand> allNewDemands = this.demandDao.getAllNewDemands(ResultCriteria.EMPTY_CRITERIA);
        Assert.assertThat("Incorrect count of all new demands", allNewDemands.size(), Is.is(6));
        checkDemandExist(allNewDemands, 1L);
        checkDemandExist(allNewDemands, 2L);
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

