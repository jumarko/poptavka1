package com.eprovement.poptavka.client.common.services;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalGrid;
import com.eprovement.poptavka.client.user.widget.grid.cell.RadioCell;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;

public class ServicesSelectorView extends Composite implements ServicesSelectorPresenter.SupplierServiceInterface {

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
    @UiField(provided = true)
    UniversalGrid table;
    /** Class attributes. **/
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
        table = new UniversalGrid(ServiceDetail.KEY_PROVIDER);

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
        //Price column
        table.addColumn(new Column<ServiceDetail, Number>(new NumberCell()) {
            @Override
            public Number getValue(ServiceDetail object) {
                return object.getPrice();
            }
        }, Storage.MSGS.columnPrice());
        //Duration column
        table.addColumn(new Column<ServiceDetail, Number>(new NumberCell()) {
            @Override
            public Number getValue(ServiceDetail object) {
                return object.getPrepaidMonths();
            }
        }, Storage.MSGS.columnDuration());

        //Set table and columns sizes
        table.setSize("400px", "200px");
        table.setColumnWidth(0, "35px");
        table.setColumnWidth(1, "155px");
        table.setColumnWidth(2, "55px");
        table.setColumnWidth(3, "55px");
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    @Override
    public void setServices(ArrayList<ServiceDetail> services) {
        table.getDataProvider().setList(services);
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
    public Widget getWidgetView() {
        return this;
    }
}
