package com.eprovement.poptavka.base;

import com.gdevelop.gwt.syncrpc.SyncProxy;
import com.google.common.base.Preconditions;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * Base class which can be extended by all tests for Rpc services.
 * <p>This kind of tests is ignored by maven-surefire plugin - see pom.xml</p>
 *
 * <p>
 *     The name of concrete test class must end with prefix "RpcServiceTest" (case insensitive).
 *     Otherwise {@link IllegalStateException} is thrown. See constructor of this class

 * <p>
 *     Two preconditions must be satisfied to be able to run this kind of tests:
 *     <ul>
 *       <li>*gwt.rpc files must be on test classpath. These files are generated by GWT compiler and they can be found
 *            at "poptavka-web/target/poptavka/poptavka/" directory after successful compilation.</li>
 *       <li>running server must be available at location configured by this base test class. See
 *       {@link #getModuleBaseUrl()}</li>
 *     </ul>
 * <p>
 *     For more information see <a href="http://code.google.com/p/gwt-syncproxy/">syncproxy project home page</a>
 *     and following post:
 *     <a href="http://www.gdevelop.com/blog/2010/01/testing-gwt-rpc-services/">Testing GWT Rpc services</a>
 *
 *
 *
 * @author Juraj Martinka
 *         Date: 15.5.11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext-test.xml" })
public abstract class RpcServiceBaseTest {

    private static final String POPTAVKA_MODULE_BASE_URL = "http://46.137.95.172:8080/poptavka/";
    private static final String RPC_SERVICE_TEST_CLASS_SUFFIX = "RpcServiceTest";


    protected RpcServiceBaseTest() {
        // check name convention
        Preconditions.checkState(getClass().getName().endsWith(RPC_SERVICE_TEST_CLASS_SUFFIX),
                "Name of Gwt Test class must end with '" + RPC_SERVICE_TEST_CLASS_SUFFIX + "' suffix.");
    }

    /**
     * Determines the relative path for service that is being tested by concrete implementation of
     * this {@link RpcServiceBaseTest}.
     *
     * @return
     */
    protected abstract String getRemoteServiceRelativePath();


    /**
     * Determines the base url where tested web application is running.
     * @return
     */
    String getModuleBaseUrl() {
        return POPTAVKA_MODULE_BASE_URL;
    }

    /**
     * Each descendant of this base class should call this method when creating new instance of rpc service
     *     instead of calling {@link SyncProxy#newProxyInstance} methods directly.
     *
     * @param rpcServiceClass class of rpc service which instance should be created
     * @param <T> generic type equal to the <code>rpcServiceClass</code> type
     * @return new instance of rpc service of type <code>T</code>
     */
    protected <T> T createRpcServiceInstance(Class<T> rpcServiceClass) {
        return (T) SyncProxy.newProxyInstance(rpcServiceClass,
                getModuleBaseUrl(),
                getRemoteServiceRelativePath());
    }

}
