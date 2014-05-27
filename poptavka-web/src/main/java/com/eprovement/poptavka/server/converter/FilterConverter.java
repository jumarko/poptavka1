/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.server.DomainObjectsMaping;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.googlecode.genericdao.search.Filter;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Converts Filter to FilterItem
 * @author Juraj Martinka
 */
public final class FilterConverter {

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates FilterConverter.
     */
    private FilterConverter() {
        // Spring instantiates converters - see converters.xml
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
//    public FilterItem convertToTarget(Filter source) {
//        throw new UnsupportedOperationException("Convertion Filter to FilterItem failed!");
//    }

    /**
     * @{inheritDoc}
     */
    public Filter convertToSource(Class<?> searchClass, FilterItem filterItem) {
        String path = DomainObjectsMaping.getInstance().getPath(searchClass, filterItem.getFieldClass());
        Filter filter;
        switch (filterItem.getOperation()) {
            case OPERATION_EQUALS:
                filter = new Filter(
                    path.concat(filterItem.getItem()), filterItem.getValue(), Filter.OP_EQUAL);
                break;
            case OPERATION_LIKE:
                filter = new Filter(
                    path.concat(filterItem.getItem()),
                    "%".concat((String) filterItem.getValue()).concat("%"),
                    Filter.OP_LIKE);
                break;
            case OPERATION_IN:
                filter = new Filter(
                    path.concat(filterItem.getItem()), filterItem.getValue(), Filter.OP_IN);
                break;
            case OPERATION_FROM:
                filter = new Filter(
                    path.concat(filterItem.getItem()), filterItem.getValue(), Filter.OP_GREATER_OR_EQUAL);
                break;
            case OPERATION_TO:
                filter = new Filter(
                    path.concat(filterItem.getItem()), filterItem.getValue(), Filter.OP_LESS_OR_EQUAL);
                break;
            default:
                filter = new Filter();
                break;
        }
        return filter;
    }

    /**
     * @{inheritDoc}
     */
//    public ArrayList<FilterItem> convertToTargetList(Collection<Filter> sourceObjects) {
//        final ArrayList<FilterItem> detailObjects = new ArrayList<FilterItem>();
//        for (Filter domainObject : sourceObjects) {
//            detailObjects.add(convertToTarget(domainObject));
//        }
//        return detailObjects;
//    }
    /**
     * @{inheritDoc}
     */
    public ArrayList<Filter> convertToSourceList(Class<?> searchClass, Collection<FilterItem> targetObjects) {
        ArrayList<Filter> filtersAnd = new ArrayList<Filter>();
        ArrayList<Filter> filtersOr = new ArrayList<Filter>();
        int group = -1;
        for (FilterItem item : targetObjects) {
            if (group == -1 || group == item.getGroup()) {
                filtersOr.add(convertToSource(searchClass, item));
            } else {
                filtersAnd.add(Filter.or(filtersOr.toArray(new Filter[filtersOr.size()])));
                filtersOr = new ArrayList<Filter>();
                filtersOr.add(convertToSource(searchClass, item));
            }
            group = item.getGroup();
        }
        //add last iteration
        filtersAnd.add(Filter.or(filtersOr.toArray(new Filter[filtersOr.size()])));
        return filtersAnd;
    }
}
