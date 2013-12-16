package com.eprovement.poptavka.resources.pager;

import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.Style;
import com.google.gwt.resources.client.ImageResource;

public interface UniversalPagerResources extends SimplePager.Resources {
    @Override
    @Source("first.png")
    ImageResource simplePagerFirstPage();

    @Override
    @Source("first.png")
    ImageResource simplePagerFirstPageDisabled();

    @Override
    @Source("last.png")
    ImageResource simplePagerLastPage();

    @Override
    @Source("last.png")
    ImageResource simplePagerLastPageDisabled();

    @Override
    @Source("next.png")
    ImageResource simplePagerNextPage();

    @Override
    @Source("next.png")
    ImageResource simplePagerNextPageDisabled();

    @Override
    @Source("previous.png")
    ImageResource simplePagerPreviousPage();

    @Override
    @Source("previous.png")
    ImageResource simplePagerPreviousPageDisabled();

    /**
     * The styles used in this widget.
     */
    @Override
    @Source("UniversalPagerStyles.css")
    Style simplePagerStyle();

    @Source("UniversalPagerStylesTiny.css")
    Style simplePagerStyleTiny();
}