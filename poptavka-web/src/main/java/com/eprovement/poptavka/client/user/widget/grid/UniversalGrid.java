package com.eprovement.poptavka.client.user.widget.grid;

import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import com.eprovement.poptavka.client.common.session.Storage;
import com.google.gwt.user.client.ui.Label;

public class UniversalGrid<T> extends DataGrid<T> {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private ListDataProvider<T> dataProvider = new ListDataProvider<T>();
    private ListHandler<T> sortHandler = new ListHandler<T>(dataProvider.getList());

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public UniversalGrid() {
        super();
        initGridDefaults();
    }

    public UniversalGrid(ProvidesKey<T> keyProvider) {
        super(keyProvider);
        initGridDefaults();
    }

    public UniversalGrid(int pageSize, Resources resources, ProvidesKey<T> keyProvider) {
        super(pageSize, resources, keyProvider);
        initGridDefaults();
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    public void setDataProvider(ListDataProvider<T> dataProvider) {
        this.dataProvider = dataProvider;
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    public ListDataProvider<T> getDataProvider() {
        return dataProvider;
    }

    public ListHandler<T> getSortHandler() {
        return sortHandler;
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    private void initGridDefaults() {
        this.addColumnSortHandler(getSortHandler());
        dataProvider.addDataDisplay(this);
        setEmptyTableWidget(new Label(Storage.MSGS.commonNoData()));
    }
}
