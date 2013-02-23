package com.eprovement.poptavka.application;

import com.eprovement.poptavka.base.integration.BasicIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author Juraj Martinka
 *         Date: 25.1.11
 */
public class ApplicationContextHolderTest extends BasicIntegrationTest {

    @Test
    public void testGetApplicationContext() {
        final ApplicationContext applicationContext = ApplicationContextHolder.getApplicationContext();
        Assert.assertNotNull(applicationContext);
    }
}
