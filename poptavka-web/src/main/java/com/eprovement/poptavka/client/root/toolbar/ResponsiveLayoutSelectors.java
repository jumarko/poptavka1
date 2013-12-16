/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.root.toolbar;

import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Selector;
import com.google.gwt.query.client.Selectors;

/**
 * Selectors for responsive layout sliding panels.
 *
 * @author Martin Slavkovsky
 */
public interface ResponsiveLayoutSelectors extends Selectors {

    @Selector("#gwt-debug-leftSlidingPanel")
    GQuery getLeftSlidingPanel();

    @Selector("#gwt-debug-rightSlidingPanel")
    GQuery getRightSlidingPanel();
}
