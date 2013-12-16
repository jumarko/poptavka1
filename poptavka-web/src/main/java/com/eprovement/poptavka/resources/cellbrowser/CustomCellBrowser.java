/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.resources.cellbrowser;

import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.user.cellview.client.CellBrowser;

/**
 * Defines CellBroser styles for CatLocSelector module - CellBrowser widget.
 *
 * @author Jaro
 */
public interface CustomCellBrowser extends CellBrowser.Resources {
    /**
     * An image indicating a closed branch.
     */
    @Source("cellBrowserClosed.png")
    @ImageOptions(flipRtl = true)
    ImageResource cellBrowserClosed();

    /**
     * An image indicating an open branch.
     */
    @Source("cellBrowserOpen.png")
    @ImageOptions(flipRtl = true)
    ImageResource cellBrowserOpen();

    /**
     * The background used for open items.
     */
    @Source("cellBrowserSelectedBackground.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal, flipRtl = true)
    ImageResource cellBrowserOpenBackground();

    /**
     * The background used for selected items.
     */
    @Override
    @Source("cellBrowserSelectedBackground.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal, flipRtl = true)
    ImageResource cellBrowserSelectedBackground();

    /**
     * The styles used in this widget.
     */
    @Override
    @NotStrict
    @Source("customCellBrowser.css")
    CellBrowser.Style cellBrowserStyle();
}
