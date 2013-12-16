/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Defines converter methods. Each converter must implements this interface.
 *
 * @author Juraj Martinka.
 * @param <Source> class
 * @param <Target> class
 */
public interface Converter<Source, Target> {

    /**
     * Converts domain object (<Source>) to detail object (<Target>)).
     * @param source object
     * @return target object (detail)
     */
    Target convertToTarget(Source source);

    /**
     * Converts detail object (<Target>)) to domain object (<Source>).
     * @param target object
     * @return source object (domain)
     */
    Source convertToSource(Target target);

    /**
     * Converts collection of domain object (<Source>) to list of detail objects (<Target>)).
     * @param sourceObjects collection
     * @return list of target objects (detail)
     */
    ArrayList<Target> convertToTargetList(Collection<Source> sourceObjects);

    /**
     * Converts collection of detail object (<Target>)) to list of domain objects (<Source>).
     * @param targetObjects collection
     * @return list of source objects (domain)
     */
    ArrayList<Source> convertToSourceList(Collection<Target> targetObjects);
}
