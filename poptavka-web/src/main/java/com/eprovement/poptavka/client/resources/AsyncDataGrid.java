package com.eprovement.poptavka.client.resources;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.user.cellview.client.DataGrid;

public interface AsyncDataGrid extends DataGrid.Resources {

    @Source("datagrid/dataGridHeader.gif")
    ImageResource dataGridHeader();

    //@Source("datagrid/dataGridSortableHeader.gif")
    //ImageResource dataGridSortableHeader();

    @Source("datagrid/dataGridSortedHeaderAscending.png")
    @ImageOptions(flipRtl = true)
    ImageResource dataGridSortAscending();

    @Source("datagrid/dataGridSortedHeaderDescending.png")
    @ImageOptions(flipRtl = true)
    ImageResource dataGridSortDescending();

    @Source({DataGrid.Style.DEFAULT_CSS, "AsyncDataGrid.css" })
    CustomStyle dataGridStyle();

    interface CustomStyle extends DataGrid.Style {

    }
}