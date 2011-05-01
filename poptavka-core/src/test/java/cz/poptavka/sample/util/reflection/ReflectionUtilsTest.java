package cz.poptavka.sample.util.reflection;

import cz.poptavka.sample.domain.demand.Demand;
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
