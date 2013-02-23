package com.eprovement.poptavka.resources;

import com.google.gwt.user.cellview.client.DataGrid;

/**
 * Custom stylesheet for our cellTables/Datagrids.
 *
 * @author beho
 */
public interface GridResources extends DataGrid.Resources {

    @Source("MyDataGrid.css")
    DataGrid.Style cellTreeStyle();
}
