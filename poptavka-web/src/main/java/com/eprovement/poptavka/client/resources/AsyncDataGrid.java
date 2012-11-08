package com.eprovement.poptavka.client.resources;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.DataGrid;

public interface AsyncDataGrid extends DataGrid.Resources {
    @Source("images/dataGridHeader.gif")
    ImageResource dataGridHeader();

    @Source("images/dataGridHeaderBorder.gif")
    ImageResource dataGridHeaderBorder();

    @Source("AsyncDataGrid.css")
    DataGrid.Style dataGridStyle();
}