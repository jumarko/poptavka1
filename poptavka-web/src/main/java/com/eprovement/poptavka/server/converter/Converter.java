/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import java.util.ArrayList;
import java.util.Collection;

public interface Converter<Source, Target> {

    Target convertToTarget(Source source);

    Source converToSource(Target target);

    ArrayList<Target> convertToTargetList(Collection<Source> sourceObjects);

    ArrayList<Source> convertToSourceList(Collection<Target> targetObjects);
}
