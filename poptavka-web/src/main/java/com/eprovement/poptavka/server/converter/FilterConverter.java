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
    public Filter convertToSource(FilterItem filterItem) {
        Filter filter;
        switch (filterItem.getOperation()) {
            case OPERATION_EQUALS:
                filter = new Filter(filterItem.getItem(), filterItem.getValue(), Filter.OP_EQUAL);
                break;
            case OPERATION_LIKE:
                filter = new Filter(
                        filterItem.getItem(),
                        "%".concat((String) filterItem.getValue()).concat("%"),
                        Filter.OP_LIKE);
                break;
            case OPERATION_IN:
                filter = new Filter(filterItem.getItem(), filterItem.getValue(), Filter.OP_IN);
                break;
            case OPERATION_FROM:
                filter = new Filter(filterItem.getItem(), filterItem.getValue(), Filter.OP_GREATER_OR_EQUAL);
                break;
            case OPERATION_TO:
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
        ArrayList<Filter> filtersAnd = new ArrayList<Filter>();
        ArrayList<Filter> filtersOr = new ArrayList<Filter>();
        int group = -1;
        for (FilterItem item : targetObjects) {
            if (group == -1 || group == item.getGroup()) {
                filtersOr.add(convertToSource(item));
            } else {
                filtersAnd.add(Filter.or(filtersOr.toArray(new Filter[filtersOr.size()])));
                filtersOr = new ArrayList<Filter>();
                filtersOr.add(convertToSource(item));
            }
            group = item.getGroup();
        }
        //add last iteration
        filtersAnd.add(Filter.or(filtersOr.toArray(new Filter[filtersOr.size()])));
        return filtersAnd;
    }
}
