package com.eprovement.poptavka.base;

import com.google.common.base.Preconditions;
import com.google.gwt.junit.client.GWTTestCase;

/**
 * Base class for tests of Gwt related stuff, i.e. mainly client side code.
 * <p>Each test class name must end with suffix "GwtTest".
 * <p>This kind of tests is ignored by maven-surefire plugin - see pom.xml
 *
 * <p>
 *     At this time, GwtBaseTest does not function correctly. Maybe because of incorrect integration
 *     of {@link GWTTestCase} with JUnit 4.
 *
 * @author Juraj Martinka
 *         Date: 15.5.11
 */
public abstract class GwtBaseTest extends GWTTestCase {

    private static final String GWT_TEST_CLASS_SUFFIX = "GwtTest";

    protected GwtBaseTest() {
        // check name convention
        Preconditions.checkState(getClass().getName().endsWith(GWT_TEST_CLASS_SUFFIX),
                "Name of Gwt Test class must end with '" + GWT_TEST_CLASS_SUFFIX + "' suffix.");
    }


}
