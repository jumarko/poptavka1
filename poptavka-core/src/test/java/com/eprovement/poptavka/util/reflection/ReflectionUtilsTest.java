package com.eprovement.poptavka.util.reflection;

import com.eprovement.poptavka.domain.demand.Demand;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Juraj Martinka
 *         Date: 1.5.11
 */
public class ReflectionUtilsTest {

    @Test
    public void testGetValue() throws Exception {
        final Demand demand = new Demand();
        final String demandTitle = "Test demand";
        demand.setTitle(demandTitle);

        Assert.assertEquals(demandTitle, ReflectionUtils.getValue(demand, "title"));

    }
}
