/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import java.util.Collection;
import java.util.List;

public interface Converter<Source, Target> {

    Target convertToTarget(Source source);

    Source converToSource(Target target);

    List<Target> convertToTargetList(Collection<Source> sourceObjects);

    List<Source> convertToSourceList(List<Target> targetObjects);
}
