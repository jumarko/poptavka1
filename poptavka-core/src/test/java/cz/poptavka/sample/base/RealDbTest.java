package cz.poptavka.sample.base;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Handy ancestor for all Tests that want to communicate with real database.
 * <p>
 * Classic integration tests ({@link DbUnitBaseTest} should be prefered.
 * Db tests serve only as a secondary testing tool to test behavior of an application  against real db.
 * <p>
 *     However, all data that are modified with these tests should be rolled back unless custom transaction handling
 *     or some obscure operations are used.
 *     If you really want to changes being commited into the DB you have to use @Rollback(value = false) annotation
 *     on test method.
 * <p>
 *     Each test method that can change the state of DB should be marked as being ignored by following annotation>
 *     <pre> @Ignore // do not use live DB if it is not neccessary </pre>
 * <p>
 *     Default transaction propagation for this kind of tests is <i>readOnly</i>.
 *     If you want to perform CUD operations then you want to use following annotation:
 *     <pre>@Transactional(propagation = Propagation.REQUIRED)</pre>
 *
 *
 *
 * @author Juraj Martinka
 *         Date: 24.4.11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
@Transactional(readOnly = true)
public abstract class RealDbTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(RealDbTest.class);

    @Before
    public void printWarning() {
        LOGGER.warn("This test [" + this.getClass() + "] communicates with live DB!");
    }
}
