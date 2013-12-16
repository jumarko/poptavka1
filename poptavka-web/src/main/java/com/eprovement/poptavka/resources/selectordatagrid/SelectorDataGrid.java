package com.eprovement.poptavka.resources.selectordatagrid;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.user.cellview.client.DataGrid;

public interface SelectorDataGrid extends DataGrid.Resources {

    @Override
    @NotStrict
    @ClientBundle.Source({ DataGrid.Style.DEFAULT_CSS, "selectorDataGrid.css" })
    SelectorDataGrid.CustomStyle dataGridStyle();

    interface CustomStyle extends DataGrid.Style {

    }
}