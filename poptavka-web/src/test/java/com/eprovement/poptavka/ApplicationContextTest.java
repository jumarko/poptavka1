package com.eprovement.poptavka;

import com.eprovement.poptavka.base.BasicIntegrationTest;
import javax.servlet.ServletException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * @author Juraj Martinka
 *         Date: 30.1.11
 */
public class ApplicationContextTest extends BasicIntegrationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testGetApplicationContext() throws ServletException {
        Assert.assertNotNull(this.applicationContext);
    }
}
