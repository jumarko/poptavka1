package com.eprovement.poptavka.client.home.createSupplier.widget;

import com.eprovement.poptavka.client.common.table.RadioTable;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;

public class SupplierServiceView extends Composite implements SupplierServicePresenter.SupplierServiceInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static SupplierServiceUiBinder uiBinder = GWT
            .create(SupplierServiceUiBinder.class);

    interface SupplierServiceUiBinder extends UiBinder<Widget, SupplierServiceView> {
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** CONSTANTS. **/
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private static final String HEADER_TITLE = MSGS.title();
    private static final String HEADER_DESCRIPTION = MSGS.description();
    private static final String HEADER_PRICE = MSGS.price();
    private static final String HEADER_MONTHS = MSGS.duration();
    private static final int FEW_MONTHS = 4;
    /** Attributes. **/
    private ArrayList<ServiceDetail> serviceList;
    /** UiFields. **/
    @UiField(provided = true)
    RadioTable table;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(HEADER_TITLE);
        list.add(HEADER_PRICE);
        list.add(HEADER_MONTHS);
        table = new RadioTable(list, false, 0);
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/
    @UiHandler("table")
    public void tableClickHandler(ClickEvent event) {
        table.getClickedRow(event);
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    @Override
    public void setServices(ArrayList<ServiceDetail> services) {
        this.serviceList = services;
        int i = 0;
        for (ServiceDetail sv : services) {
            ArrayList<String> data = new ArrayList<String>();
            data.add(sv.getId() + "");
            data.add("checkbox");
            data.add(sv.getTitle());
            data.add(sv.getPrice() + "");
            //set different count for different amount of months
            int months = sv.getPrepaidMonths();
            if (months < 2) {
                data.add(months + MSGS.month());
            } else {
                if (months > FEW_MONTHS) {
                    data.add(months + MSGS.months());
                } else {
                    data.add(months + MSGS.fewMonths());
                }
            }
            table.setRow(data, i);
            i++;
        }
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public int getSelectedService() {
        return table.getSelectedValue();
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
