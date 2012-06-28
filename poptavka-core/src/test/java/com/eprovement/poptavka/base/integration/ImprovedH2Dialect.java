/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.base.integration;

import org.hibernate.dialect.H2Dialect;
//import org.hibernate.dialect.HSQLDialect;

/**
 * This class is used as a workaround for bug: https://hibernate.onjira.com/browse/HHH-7002
 * @deprecated Right now this is only known working solution. Once fixed in hibernate or elsewhere,
 * please remove this class!
 */
@Deprecated
public class ImprovedH2Dialect extends H2Dialect {
    @Override
    public String getDropSequenceString(String sequenceName) {
        // Adding the "if exists" clause to avoid warnings
        return "drop sequence if exists " + sequenceName;
    }

    @Override
    public boolean dropConstraints() {
        // We don't need to drop constraints before dropping tables, that just leads to error
        // messages about missing tables when we don't have a schema in the database
        return false;
    }
}
