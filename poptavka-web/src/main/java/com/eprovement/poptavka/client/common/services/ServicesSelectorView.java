package com.eprovement.poptavka.client.common.services;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.user.widget.grid.UniversalGrid;
import com.eprovement.poptavka.client.user.widget.grid.cell.RadioCell;
import com.eprovement.poptavka.resources.datagrid.AsyncDataGrid;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;

public class ServicesSelectorView extends Composite
        implements ServicesSelectorPresenter.SupplierServiceInterface, ProvidesValidate, HasChangeHandlers {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static SupplierServiceUiBinder uiBinder = GWT
            .create(SupplierServiceUiBinder.class);

    interface SupplierServiceUiBinder extends UiBinder<Widget, ServicesSelectorView> {
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) UniversalGrid table;
    /** Class attributes. **/
    private ServiceDetail originalSelected;
    private ServiceDetail selected;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        initTable();

        initWidget(uiBinder.createAndBindUi(this));
    }

    private void initTable() {
        /** Table initialization. **/
        DataGrid.Resources resource = GWT.create(AsyncDataGrid.class);
        table = new UniversalGrid<ServiceDetail>(10, resource, ServiceDetail.KEY_PROVIDER);

        /** Column initialization. **/
        //Radio column
        Column<ServiceDetail, Boolean> radioButton = new Column<ServiceDetail, Boolean>(new RadioCell()) {
            @Override
            public Boolean getValue(ServiceDetail object) {
                return false;
            }
        };
        radioButton.setFieldUpdater(new FieldUpdater<ServiceDetail, Boolean>() {
            @Override
            public void update(int index, ServiceDetail object, Boolean value) {
                if (value) {
                    selected = object;
                    DomEvent.fireNativeEvent(Document.get().createChangeEvent(), getWidget());
                }
            }
        });
        table.addColumn(radioButton);
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
        table.setSize("500px", "300px");
        table.setColumnWidth(0, "35px");
        table.setColumnWidth(1, "55px");
        table.setColumnWidth(2, "210px");
//        table.setColumnWidth(3, "55px");
    }

    /**************************************************************************/
    /* HasChangeHandlers                                                      */
    /**************************************************************************/
    @Override
    public HandlerRegistration addChangeHandler(ChangeHandler handler) {
        return addDomHandler(handler, ChangeEvent.getType());
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    @Override
    public void setServices(ArrayList<ServiceDetail> services) {
        table.getDataProvider().setList(services);
    }

    public void setService(ServiceDetail service) {
        originalSelected = service;
        selected = service;
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public UniversalGrid getTable() {
        return table;
    }

    @Override
    public ServiceDetail getSelectedService() {
        return selected;
    }

    @Override
    public boolean isValid() {
        return table != null;
    }

    public boolean isChanged() {
        if (originalSelected == null) {
            return true;
        }
        return !originalSelected.equals(selected);
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
