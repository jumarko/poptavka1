package cz.poptavka.sample.application;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Juraj Martinka
 *         Date: 25.1.11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public class ApplicationContextHolderTest {

    @Test
    public void testGetApplicationContext() {
        final ApplicationContext applicationContext = ApplicationContextHolder.getApplicationContext();
        Assert.assertNotNull(applicationContext);
    }
}
