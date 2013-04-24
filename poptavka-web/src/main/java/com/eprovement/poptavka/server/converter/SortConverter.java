/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.server.DomainObjectsMaping;
import com.eprovement.poptavka.shared.search.SortPair;
import com.googlecode.genericdao.search.Sort;
import java.util.ArrayList;
import java.util.Collection;

public final class SortConverter {

    private SortConverter() {
        // Spring instantiates converters - see converters.xml
    }

    public Sort convertToSource(Class<?> searchClass, SortPair target) {
        String path = DomainObjectsMaping.getInstance().getPath(searchClass, target.getSearchClass());
        return new Sort(
                path.concat(target.getColumnName()),
                target.getColumnOrderType() == OrderType.DESC,
                true);
    }

    public Sort[] convertToSourceList(Class<?> searchClass, Collection<SortPair> detailObjects) {
        final ArrayList<Sort> domainObjects = new ArrayList<Sort>();
        for (SortPair detailObject : detailObjects) {
            domainObjects.add(convertToSource(searchClass, detailObject));
        }
        return domainObjects.toArray(new Sort[domainObjects.size()]);
    }
}
