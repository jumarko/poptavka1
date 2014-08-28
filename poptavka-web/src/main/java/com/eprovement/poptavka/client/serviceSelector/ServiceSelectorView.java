/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.serviceSelector;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.serviceSelector.interfaces.IServiceSelectorModule;
import com.eprovement.poptavka.client.user.widget.grid.cell.RadioCell;
import com.eprovement.poptavka.resources.datagrid.DataGridResources;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.eprovement.poptavka.shared.domain.UserServiceDetail;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.FormElement;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * View consists of Table.
 *
 * @author Martin Slavkovsky
 */
public class ServiceSelectorView extends Composite implements IServiceSelectorModule.View {

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
    @UiField Label infoLabel;
    @UiField FormElement form;
    @UiField InputElement formReturnUrl, formItemName, formItemNumber, formItemId, formAmount;
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
        table.addColumn(new Column<ServiceDetail, String>(new TextCell()) {
            @Override
            public String getValue(ServiceDetail object) {
                return Storage.CURRENCY_FORMAT.format(object.getPrice());
            }
        }, Storage.MSGS.columnPrice());
        //Credits column
        table.addColumn(new Column<ServiceDetail, Number>(new NumberCell()) {
            @Override
            public Number getValue(ServiceDetail object) {
                return object.getCredits();
            }
        }, Storage.MSGS.columnCredits());

        //Set table and columns sizes
        table.setSize("100%", "100px");
        table.setColumnWidth(0, "35px");
        table.setColumnWidth(1, "30%");
        table.setColumnWidth(2, "70%");
        table.setColumnWidth(3, "70px");
        table.setColumnWidth(4, "70px");
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    public void setPaymentDetails(String returnRul, UserServiceDetail userServiceDetail) {
        formReturnUrl.setValue(returnRul);
        formItemName.setValue("Buy now");
        formItemNumber.setValue(userServiceDetail.getService().getTitle());
        formItemId.setValue(Long.toOctalString(userServiceDetail.getService().getId()));
        formAmount.setValue(userServiceDetail.getService().getPrice().toString());
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
    public FormElement getPaymentForm() {
        return form;
    }

    /**
     * Validates view's components.
     * @return true if valid, false otherwise
     */
    @Override
    public boolean isValid() {
        return selectionModel.getSelectedObject() != null;
    }
}
