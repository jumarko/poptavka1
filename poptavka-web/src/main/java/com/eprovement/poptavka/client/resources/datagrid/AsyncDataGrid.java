package com.eprovement.poptavka.client.resources.datagrid;

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

    @Source("dataGridSortedHeaderAscending.png")
    @ImageOptions(flipRtl = true)
    ImageResource dataGridSortAscending();

    @Source("dataGridSortedHeaderDescending.png")
    @ImageOptions(flipRtl = true)
    ImageResource dataGridSortDescending();

    @Source({ DataGrid.Style.DEFAULT_CSS, "AsyncDataGrid.css" })
    CustomStyle dataGridStyle();

    interface CustomStyle extends DataGrid.Style {

    }
}