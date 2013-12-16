/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.root.toolbar;

import com.google.gwt.user.client.ui.Widget;

/**
 * Implements this interface in a <b>view</b> and call
 * <i>eventBus.setFooter(view.getFooterContainer())</i> in a <b>presenter</b>
 * in order to set some custom content to toolbar.
 *
 * @author Martin Slavkovsky
 */
public interface ProvidesToolbar {

    /**
     * Provides custom content for toolbar.
     * @return widget
     */
    Widget getToolbarContent();
}