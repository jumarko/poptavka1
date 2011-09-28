package cz.poptavka.sample.application.scripting;

import cz.poptavka.sample.base.integration.DBUnitBaseTest;
import cz.poptavka.sample.base.integration.DataSet;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptException;
import java.io.IOException;

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
@DataSet(path = "classpath:cz/poptavka/sample/domain/user/UsersDataSet.xml",
        dtd = "classpath:test.dtd")
public class ScriptExecutorTest extends DBUnitBaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScriptExecutorTest.class);


    @Test
    public void testScriptEvaluation() {
        try {
            ScriptExecutor.getInstance().execute(new Script(null, Script.SCRIPT_ENGINE.JAVA_SCRIPT,
                // script body
                "importPackage(Packages.cz.poptavka.sample.application)\n"
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
