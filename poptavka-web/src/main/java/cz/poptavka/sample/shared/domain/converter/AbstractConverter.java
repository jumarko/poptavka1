/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.shared.domain.converter;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractConverter<Domain, Detail> implements Converter<Domain, Detail> {

    @Override
    public List<Detail> convertToTargetList(List<Domain> domainObjects) {
        final List<Detail> detailObjects = new ArrayList<Detail>();
        for (Domain domainObject : domainObjects) {
            detailObjects.add(convertToTarget(domainObject));
        }
        return detailObjects;
    }

    @Override
    public List<Domain> convertToSourceList(List<Detail> detailObjects) {
        final List<Domain> domainObjects = new ArrayList<Domain>();
        for (Detail detailObject : detailObjects) {
            domainObjects.add(converToSource(detailObject));
        }
        return domainObjects;
    }
}
