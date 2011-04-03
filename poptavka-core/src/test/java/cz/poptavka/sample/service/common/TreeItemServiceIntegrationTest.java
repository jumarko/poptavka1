package cz.poptavka.sample.service.common;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import org.junit.Ignore;

/**
 * @author Juraj Martinka
 *         Date: 12.2.11
 */
@Ignore
@DataSet(path = {
        "classpath:cz/poptavka/sample/domain/address/LocalityDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/CategoryDataSet.xml",
        "classpath:cz/poptavka/sample/domain/demand/DemandDataSet.xml" },
        dtd = "classpath:test.dtd")
public class TreeItemServiceIntegrationTest extends DBUnitBaseTest {
}
