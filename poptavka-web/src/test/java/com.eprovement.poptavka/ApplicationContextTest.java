package com.eprovement.poptavka;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.ServletException;

/**
 * @author Juraj Martinka
 *         Date: 30.1.11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext.xml" })
public class ApplicationContextTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testGetApplicationContext() throws ServletException {
        Assert.assertNotNull(this.applicationContext);
    }
}
