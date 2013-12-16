/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.server.DomainObjectsMaping;
import com.eprovement.poptavka.shared.search.SortPair;
import com.googlecode.genericdao.search.Sort;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Converts SortPairs to Sort object
 * @author Juraj Martinka
 */
public final class SortConverter {

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates SortConverter.
     */
    private SortConverter() {
        // Spring instantiates converters - see converters.xml
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * Converts SortPair to Sort using DomainObjectMaping.
     * @param searchClass
     * @param sortPair
     * @return the Sort object
     */
    public Sort convertToSource(Class<?> searchClass, SortPair sortPair) {
        String path = DomainObjectsMaping.getInstance().getPath(searchClass, sortPair.getSearchClass());
        return new Sort(
                path.concat(sortPair.getColumnName()),
                sortPair.getColumnOrderType() == OrderType.DESC,
                true);
    }

    /**
     * Converts list of SortPairs to Sort array.
     * @param searchClass
     * @param detailObjects
     * @return the Sort array
     */
    public Sort[] convertToSourceList(Class<?> searchClass, Collection<SortPair> detailObjects) {
        final ArrayList<Sort> domainObjects = new ArrayList<Sort>();
        if (detailObjects != null) {
            for (SortPair detailObject : detailObjects) {
                domainObjects.add(convertToSource(searchClass, detailObject));
            }
        }
        return domainObjects.toArray(new Sort[domainObjects.size()]);
    }
}
