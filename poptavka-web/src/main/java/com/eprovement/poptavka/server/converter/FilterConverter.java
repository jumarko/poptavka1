/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.shared.search.FilterItem;
import com.googlecode.genericdao.search.Filter;
import java.util.ArrayList;
import java.util.Collection;

public final class FilterConverter implements Converter<Filter, FilterItem> {

    private FilterConverter() {
        // Spring instantiates converters - see converters.xml
    }

    @Override
    public FilterItem convertToTarget(Filter source) {
        throw new UnsupportedOperationException("Convertion Filter to FilterItem failed!");
    }

    @Override
    public Filter converToSource(FilterItem filterItem) {
        Filter filter;
        switch (filterItem.getOperation()) {
            case FilterItem.OPERATION_EQUALS:
                filter = new Filter(filterItem.getItem(), filterItem.getValue(), Filter.OP_EQUAL);
                break;
            case FilterItem.OPERATION_LIKE:
                filter = new Filter(filterItem.getItem(), filterItem.getValue(), Filter.OP_LIKE);
                break;
            case FilterItem.OPERATION_IN:
                filter = new Filter(filterItem.getItem(), filterItem.getValue(), Filter.OP_IN);
                break;
            case FilterItem.OPERATION_FROM:
                filter = new Filter(filterItem.getItem(), filterItem.getValue(), Filter.OP_GREATER_OR_EQUAL);
                break;
            case FilterItem.OPERATION_TO:
                filter = new Filter(filterItem.getItem(), filterItem.getValue(), Filter.OP_LESS_OR_EQUAL);
                break;
            default:
                filter = new Filter();
                break;
        }
        return filter;
    }

    @Override
    public ArrayList<FilterItem> convertToTargetList(Collection<Filter> sourceObjects) {
        final ArrayList<FilterItem> detailObjects = new ArrayList<FilterItem>();
        for (Filter domainObject : sourceObjects) {
            detailObjects.add(convertToTarget(domainObject));
        }
        return detailObjects;
    }

    @Override
    public ArrayList<Filter> convertToSourceList(Collection<FilterItem> targetObjects) {
        final ArrayList<Filter> domainObjects = new ArrayList<Filter>();
        for (FilterItem detailObject : targetObjects) {
            domainObjects.add(converToSource(detailObject));
        }
        return domainObjects;
    }
}
