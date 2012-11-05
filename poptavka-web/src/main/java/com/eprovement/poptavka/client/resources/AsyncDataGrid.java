package com.eprovement.poptavka.client.resources;

import com.google.gwt.user.cellview.client.DataGrid;

public interface AsyncDataGrid extends DataGrid.Resources {

    @Source("AsyncDataGrid.css")
    DataGrid.Style dataGridStyle();
}