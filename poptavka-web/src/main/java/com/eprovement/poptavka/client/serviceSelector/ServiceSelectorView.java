/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.serviceSelector;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.cell.RadioCell;
import com.eprovement.poptavka.resources.datagrid.DataGridResources;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * View consists of Table.
 *
 * @author Martin Slavkovsky
 */
public class ServiceSelectorView extends Composite implements ServiceSelectorPresenter.SupplierServiceInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static SupplierServiceUiBinder uiBinder = GWT.create(SupplierServiceUiBinder.class);

    interface SupplierServiceUiBinder extends UiBinder<Widget, ServiceSelectorView> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) DataGrid table;
    /** Class attributes. **/
    private ListDataProvider<ServiceDetail> dataProvider;
    private SingleSelectionModel<ServiceDetail> selectionModel;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates ServiceSelector view's compontents.
     */
    @Override
    public void createView() {
        initTable();

        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Creates table.
     */
    private void initTable() {
        /** Table initialization. **/
        DataGrid.Resources resource = GWT.create(DataGridResources.class);
        table = new DataGrid<ServiceDetail>(10, resource, ServiceDetail.KEY_PROVIDER);
        dataProvider = new ListDataProvider<ServiceDetail>();
        dataProvider.addDataDisplay(table);
        selectionModel = new SingleSelectionModel<ServiceDetail>();
        table.setSelectionModel(selectionModel);

        initTableColumns();
    }

    /**
     * Creates table columns.
     * @param radioBtnColumn - radio button column.
     */
    private void initTableColumns() {
        /** Column initialization. **/
        //Radio column
        table.addColumn(new Column<ServiceDetail, Boolean>(new RadioCell()) {
            @Override
            public Boolean getValue(ServiceDetail object) {
                return selectionModel.isSelected(object);
            }
        });
        //Service title column
        table.addColumn(new Column<ServiceDetail, String>(new TextCell()) {
            @Override
            public String getValue(ServiceDetail object) {
                return object.getTitle();
            }
        }, Storage.MSGS.columnService());
        //Service description column
        table.addColumn(new Column<ServiceDetail, String>(new TextCell()) {
            @Override
            public String getValue(ServiceDetail object) {
                return object.getDescription();
            }
        }, Storage.MSGS.columDescription());
        //Price column
        // TODO LATER ivlcek - commented for BETA version
//        table.addColumn(new Column<ServiceDetail, Number>(new NumberCell()) {
//            @Override
//            public Number getValue(ServiceDetail object) {
//                return object.getPrice();
//            }
//        }, Storage.MSGS.columnPrice());
        //Duration column -
//        table.addColumn(new Column<ServiceDetail, Number>(new NumberCell()) {
//            @Override
//            public Number getValue(ServiceDetail object) {
//                return object.getPrepaidMonths();
//            }
//        }, Storage.MSGS.columnDuration());

        //Set table and columns sizes
        table.setSize("100%", "100px");
        table.setColumnWidth(0, "35px");
        table.setColumnWidth(1, "55px");
        table.setColumnWidth(2, "210px");
//        table.setColumnWidth(3, "55px");
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    /**
     * {@inheritDoc}
     */
    public DataGrid getTable() {
        return table;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SingleSelectionModel<ServiceDetail> getSelectionModel() {
        return selectionModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ListDataProvider<ServiceDetail> getDataProvider() {
        return dataProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        selectionModel.clear();
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * Validates view's components.
     * @return true if valid, false otherwise
     */
    @Override
    public boolean isValid() {
        return selectionModel.getSelectedObject() != null;
    }
}
