package cz.poptavka.sample.service.common;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import org.junit.Ignore;
import org.springframework.test.annotation.DirtiesContext;

/**
 * @author Juraj Martinka
 *         Date: 12.2.11
 */
@Ignore
@DataSet(path = "classpath:cz/poptavka/sample/base/BaseDataSet.xml", dtd = "classpath:test.dtd")
@DirtiesContext
public class TreeItemServiceIntegrationTest extends DBUnitBaseTest {
}
