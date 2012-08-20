package com.eprovement.poptavka.application.scripting;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import java.io.IOException;
import javax.script.ScriptException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dummy test of script evaluation.
 * <p>To make this test run it is neccessary to have "JsScript.js" file in current directory.
 * Make sure that this file has also been copied into the target/... directory.
 *
 * <strong>this is incomplete test, but at least it checks if no exception is thrown within script code</strong>
 * that uses Spring application context to retrieve bean for getting some data from DB!
 *
 * @author Juraj Martinka
 *         Date: 24.1.11
 */
// Does not work correctly with JAVA 7 - maybe ScriptExecutor should be completely removed!
@Ignore
@DataSet(path = "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml",
        dtd = "classpath:test.dtd")
public class ScriptExecutorTest extends DBUnitIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptExecutorTest.class);


    @Test
    public void testScriptEvaluation() {
        try {
            ScriptExecutor.getInstance().execute(new Script(null, Script.SCRIPT_ENGINE.JAVA_SCRIPT,
                // script body
                "importPackage(Packages.com.eprovement.poptavka.application)\n"
                + "var allClients = "
                + "ApplicationContextHolder.getApplicationContext().getBean(\"clientService\").getAll();\n"
                + "print(\"Number of clients:\" + allClients.size() + \"\\n\");"
                + "for (var i = 0; i < allClients.size(); i++) {\n"
                + "   print(allClients.get(i) + \"||\\n\")\n"
                + "}\n"));
        } catch (ScriptException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            Assert.fail();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            Assert.fail();
        }
    }

}
