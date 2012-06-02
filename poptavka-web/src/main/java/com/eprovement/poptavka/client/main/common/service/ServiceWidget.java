package com.eprovement.poptavka.client.main.common.service;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.Composite;

import com.eprovement.poptavka.client.main.common.table.RadioTable;
import com.eprovement.poptavka.shared.domain.ServiceDetail;

public class ServiceWidget extends Composite {

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private static final String HEADER_TITLE = MSGS.title();
    private static final String HEADER_DESCRIPTION = MSGS.description();
    private static final String HEADER_PRICE = MSGS.price();
    private static final String HEADER_MONTHS = MSGS.duration();
    private static final int FEW_MONTHS = 4;

    private ArrayList<ServiceDetail> serviceList;

    private RadioTable table;

    public ServiceWidget() {
        ArrayList<String> list = new ArrayList<String>();
        list.add(HEADER_TITLE);
        list.add(HEADER_PRICE);
        list.add(HEADER_MONTHS);
        table = new RadioTable(list, false, 0);
        initHandlers();
        initWidget(table);
    }

    private void initHandlers() {
        table.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                table.getClickedRow(event);
            }
        });

    }

    public void setData(ArrayList<ServiceDetail> services) {
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

    public int getSelectedService() {
        return table.getSelectedValue();
    }

}
