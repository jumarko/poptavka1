/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.shared.search.SortPair;
import com.googlecode.genericdao.search.Sort;

public final class SortConverter extends AbstractConverter<Sort, SortPair> {

    private SortConverter() {
        // Spring instantiates converters - see converters.xml
    }

    @Override
    public SortPair convertToTarget(Sort source) {
        throw new UnsupportedOperationException("Convertion ResultCriteria to SearchDefinition failed!");
    }

    @Override
    public Sort convertToSource(SortPair target) {
        return new Sort(
                checkPath(target.getPathToAttributes()).concat(target.getColumnName()),
                target.getColumnOrderType() == OrderType.DESC,
                true);
    }

    private String checkPath(String pathToAttribute) {
        if (pathToAttribute == null) {
            return "";
        } else {
            if (pathToAttribute.isEmpty() || pathToAttribute.endsWith(".")) {
                return pathToAttribute;
            } else {
                return pathToAttribute.concat(".");
            }
        }
    }
}
