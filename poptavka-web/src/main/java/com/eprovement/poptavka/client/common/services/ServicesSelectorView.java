package com.eprovement.poptavka.client.common.services;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalGrid;
import com.eprovement.poptavka.client.user.widget.grid.cell.RadioCell;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
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
    /** CONSTANTS. **/
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private static final String HEADER_TITLE = MSGS.columnTitle();
    private static final String HEADER_DESCRIPTION = MSGS.columDescription();
    private static final String HEADER_PRICE = MSGS.columnPrice();
    private static final String HEADER_MONTHS = MSGS.columnDuration();
    private static final int FEW_MONTHS = 4;
    /** Attributes. **/
    private ArrayList<ServiceDetail> serviceList;
    /** UiFields. **/
//    @UiField(provided = true) RadioTable table;
    @UiField(provided = true)
    UniversalGrid table;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
//        ArrayList<String> list = new ArrayList<String>();
//        list.add(HEADER_TITLE);
//        list.add(HEADER_PRICE);
//        list.add(HEADER_MONTHS);
//        table = new RadioTable(list, false, 0);
        initTable();

        initWidget(uiBinder.createAndBindUi(this));
    }

    private void initTable() {
        /** Table initialization. **/
        table = new UniversalGrid(ServiceDetail.KEY_PROVIDER);
        
        /** Column initialization. **/
        //Radio column
        table.addColumn(new Column<ServiceDetail, Boolean>(new RadioCell()) {
            @Override
            public Boolean getValue(ServiceDetail object) {
                return false;
            }
        }, "  ", "  ");
        //Service title column
        table.addColumn(new Column<ServiceDetail, String>(new TextCell()) {
            @Override
            public String getValue(ServiceDetail object) {
                return object.getTitle();
            }
        }, Storage.MSGS.columnService(), "  ");
        //Price column
        table.addColumn(new Column<ServiceDetail, Number>(new NumberCell()) {
            @Override
            public Number getValue(ServiceDetail object) {
                return object.getPrice();
            }
        }, Storage.MSGS.columnPrice(), "  ");
        //Duration column
        table.addColumn(new Column<ServiceDetail, Number>(new NumberCell()) {
            @Override
            public Number getValue(ServiceDetail object) {
                return object.getPrepaidMonths();
            }
        }, Storage.MSGS.columnDuration(), "  ");

        table.setSize("400px", "200px");
        table.setColumnWidth(0, "35px");
        table.setColumnWidth(1, "155px");
        table.setColumnWidth(2, "55px");
        table.setColumnWidth(3, "55px");
    }

    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/
//    @UiHandler("table")
//    public void tableClickHandler(ClickEvent event) {
//        table.getClickedRow(event);
//    }
    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    @Override
    public void setServices(ArrayList<ServiceDetail> services) {
        table.getDataProvider().setList(services);
//        this.serviceList = services;
//        int i = 0;
//        for (ServiceDetail sv : services) {
//            ArrayList<String> data = new ArrayList<String>();
//            data.add(sv.getId() + "");
//            data.add("checkbox");
//            data.add(sv.getTitle());
//            data.add(sv.getPrice() + "");
//            //set different count for different amount of months
//            int months = sv.getPrepaidMonths();
//            if (months < 2) {
//                data.add(months + MSGS.month());
//            } else {
//                if (months > FEW_MONTHS) {
//                    data.add(months + MSGS.months());
//                } else {
//                    data.add(months + MSGS.fewMonths());
//                }
//            }
//            table.setRow(data, i);
//            i++;
//        }
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public UniversalGrid getTable() {
        return table;
    }

//    @Override
//    public int getSelectedService() {
//        return table.getSelectedValue();
//    }
    @Override
    public Widget getWidgetView() {
        return this;
    }
}
