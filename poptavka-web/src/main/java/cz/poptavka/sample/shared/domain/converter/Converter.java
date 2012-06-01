/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.shared.domain.converter;

import java.util.List;

public interface Converter<Source, Target> {

    Target convertToTarget(Source source);

    Source converToSource(Target target);

    List<Target> convertToTargetList(List<Source> sourceObjects);

    List<Source> convertToSourceList(List<Target> targetObjects);
}
