/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Abstract converter class. Contains common functionality to all converters.
 *
 * @author Juraj Martinka
 * @param <Domain> class
 * @param <Detail> class
 */
public abstract class AbstractConverter<Domain, Detail> implements Converter<Domain, Detail> {

    /**
     * @{inheritDoc}
     */
    @Override
    public ArrayList<Detail> convertToTargetList(Collection<Domain> domainObjects) {
        final ArrayList<Detail> detailObjects = new ArrayList<Detail>();
        for (Domain domainObject : domainObjects) {
            detailObjects.add(convertToTarget(domainObject));
        }
        return detailObjects;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public ArrayList<Domain> convertToSourceList(Collection<Detail> detailObjects) {
        final ArrayList<Domain> domainObjects = new ArrayList<Domain>();
        for (Detail detailObject : detailObjects) {
            domainObjects.add(convertToSource(detailObject));
        }
        return domainObjects;
    }

    /**
     * TODO Martin refactor - it is still needed?
     * Why convert java.util.Date to java.util.Date ?
     */
    public Date convertDate(Date date) {
        if (date != null) {
            return new Date(date.getTime());
        }

        return null;
    }
}
