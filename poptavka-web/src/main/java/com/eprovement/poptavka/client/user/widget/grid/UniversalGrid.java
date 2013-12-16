/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid;

import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import com.eprovement.poptavka.client.common.session.Storage;
import com.google.gwt.user.client.ui.Label;

/**
 * Contains common functionality for grids like list data provider and list sort handler.
 *
 * @author Martin Slavkovsky
 * @param <T>
 */
public class UniversalGrid<T> extends DataGrid<T> {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private ListDataProvider<T> dataProvider = new ListDataProvider<T>();
    private ListHandler<T> sortHandler = new ListHandler<T>(dataProvider.getList());

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Constructs a table with a default page size of 50.
     */
    public UniversalGrid() {
        super();
        initGridDefaults();
    }

    /**
     * Constructs a table with a default page size of 50, and the given key provider.
     *
     * @param keyProvider - onstructs a table with a default page size of 50, and the given key provider.
     */
    public UniversalGrid(ProvidesKey<T> keyProvider) {
        super(keyProvider);
        initGridDefaults();
    }

    /**
     * Constructs a table with the given page size, the specified Resources, and the given key provider.
     *
     * @param pageSize - the page size
     * @param resources - the resources to use for this widget
     * @param keyProvider - an instance of ProvidesKey , or null if the record object should act as its own key
     */
    public UniversalGrid(int pageSize, Resources resources, ProvidesKey<T> keyProvider) {
        super(pageSize, resources, keyProvider);
        initGridDefaults();
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    /**
     * Sets diffrent data provider.
     * @param dataProvider - different data provider
     */
    public void setDataProvider(ListDataProvider<T> dataProvider) {
        this.dataProvider = dataProvider;
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * Get data provider.
     * @return the ListDataProvider
     */
    public ListDataProvider<T> getDataProvider() {
        return dataProvider;
    }

    /**
     * Get sort handler.
     * @return the ListHandler
     */
    public ListHandler<T> getSortHandler() {
        return sortHandler;
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Inits common functionality.
     */
    private void initGridDefaults() {
        this.addColumnSortHandler(getSortHandler());
        dataProvider.addDataDisplay(this);
        setEmptyTableWidget(new Label(Storage.MSGS.commonNoData()));
    }
}
