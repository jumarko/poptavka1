/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.resources.datagrid;

import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.user.cellview.client.DataGrid;

/**
 * Defines DataGrid resources for UniversalAsyncGrid.
 *
 * @author Jaro
 */
public interface DataGridResources extends DataGrid.Resources {

    @Source("dataGridHeader.gif")
    ImageResource dataGridHeader();

    @Source("dataGridSortableHeader.gif")
    @ImageOptions(flipRtl = true)
    ImageResource dataGridSortableHeader();

    @Source("dataGridLastSortableHeader.gif")
    @ImageOptions(flipRtl = true)
    ImageResource dataGridLastSortableHeader();

    @Override
    @Source("dataGridSortedHeaderAscending.gif")
    @ImageOptions(flipRtl = true)
    ImageResource dataGridSortAscending();

    @Override
    @Source("dataGridSortedHeaderDescending.gif")
    @ImageOptions(flipRtl = true)
    ImageResource dataGridSortDescending();

    @Override
    @NotStrict
    @Source("DataGridStylesLarge.css")
    DataGridStyles dataGridStyle();

    @NotStrict
    @Source("DataGridStylesMiddle.css")
    DataGridStyles dataGridStyleMiddle();

    @NotStrict
    @Source("DataGridStylesSmall.css")
    DataGridStyles dataGridStyleSmall();

    @NotStrict
    @Source("DataGridStylesTiny.css")
    DataGridStyles dataGridStyleTiny();
}