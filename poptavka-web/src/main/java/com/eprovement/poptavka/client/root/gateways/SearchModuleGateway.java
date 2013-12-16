/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.root.gateways;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Event;

/**
 * Gateway interface for Search module.
 * Defines which methods are accessible to the rest of application.
 *
 * @author Martin Slavkovsky
 */
public interface SearchModuleGateway {

    @Event(forwardToParent = true)
    void resetSearchBar(Widget newAttributeSearchWidget);
}
