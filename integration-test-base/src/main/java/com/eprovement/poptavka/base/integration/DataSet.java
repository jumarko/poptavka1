package com.eprovement.poptavka.base.integration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that enables simple specification of location XML file used for populating the database
 * as well as specifying the DTD location for XML file.
 * <p>
 *
 *
 * @see DBUnitBaseTest
 * @author Juraj Martinka
 *         Date: 9.1.11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface DataSet {

    /**
     * Path to XML file for population of database. Either relative or in arbitrary place on classpath
     * (use "classpath:" prefix in that case).
     */
    String[] path();

    /**
     * Path to DTD file for XML. Either relative or in arbitrary place on classpath (use "classpath:" prefix
     * in that case).
     */
    String dtd() default "";

    /**
     * Indicates wheter foreign key checks should be disabled or not. Can be useful in some (corner) cases,
     * e.g. when circular dependencies on foreign key occurs.
     *
     * @see MessageDataSet.xml, MessageDataIntegregionTest
     *
     */
    boolean disableForeignKeyChecks() default false;
}
