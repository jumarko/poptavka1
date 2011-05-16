package cz.poptavka.sample.base.integration;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Base class for integration tests that do not require any testing data to be filled into the in memory database.
 *
 * If testing data are required then use {@link DBUnitBaseTest}.
 *
 * @author Juraj Martinka
 *         Date: 16.5.11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext-test.xml" })
public abstract class BasicIntegrationTest {
}
