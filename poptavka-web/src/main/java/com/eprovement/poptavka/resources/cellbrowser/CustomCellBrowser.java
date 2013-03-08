package com.eprovement.poptavka.resources.cellbrowser;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.user.cellview.client.CellBrowser;

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
    @Source("cellTreeSelectedBackground.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal, flipRtl = true)
    ImageResource cellBrowserOpenBackground();

    /**
     * The background used for selected items.
     */
    @Override
    @Source("cellTreeSelectedBackground.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal, flipRtl = true)
    ImageResource cellBrowserSelectedBackground();

    /**
     * The styles used in this widget.
     */
    @Override
    @Source("customCellBrowser.css")
    CellBrowser.Style cellBrowserStyle();
}
