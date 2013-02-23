package com.eprovement.poptavka.resources.datagrid;

import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.user.cellview.client.DataGrid;

public interface AsyncDataGrid extends DataGrid.Resources {

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
    @Source({ DataGrid.Style.DEFAULT_CSS, "AsyncDataGrid.css" })
    CustomStyle dataGridStyle();

    interface CustomStyle extends DataGrid.Style {

    }
}