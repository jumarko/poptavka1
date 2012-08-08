/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public abstract class AbstractConverter<Domain, Detail> implements Converter<Domain, Detail> {

    @Override
    public ArrayList<Detail> convertToTargetList(Collection<Domain> domainObjects) {
        final ArrayList<Detail> detailObjects = new ArrayList<Detail>();
        for (Domain domainObject : domainObjects) {
            detailObjects.add(convertToTarget(domainObject));
        }
        return detailObjects;
    }

    @Override
    public ArrayList<Domain> convertToSourceList(Collection<Detail> detailObjects) {
        final ArrayList<Domain> domainObjects = new ArrayList<Domain>();
        for (Detail detailObject : detailObjects) {
            domainObjects.add(converToSource(detailObject));
        }
        return domainObjects;
    }

    public Date convertDate(Date date) {
        if (date != null) {
            return new Date(date.getTime());
        }

        return null;
    }
}
