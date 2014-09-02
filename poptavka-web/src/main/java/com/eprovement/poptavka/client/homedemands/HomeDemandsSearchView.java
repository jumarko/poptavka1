/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.homedemands;

import com.eprovement.poptavka.client.common.ui.WSDateBox;
import com.eprovement.poptavka.client.common.monitors.ValidationMonitor;
import com.eprovement.poptavka.client.common.ui.WSListBox;
import com.eprovement.poptavka.client.common.ui.WSListBoxData;
import com.eprovement.poptavka.client.search.SearchModulePresenter;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.ui.WSPriceBox;
import com.eprovement.poptavka.client.common.validation.SearchGroup;
import com.eprovement.poptavka.client.homedemands.HomeDemandsSearchView.SearchModulViewUiBinder;
import com.eprovement.poptavka.domain.enums.DemandTypeType;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.DemandField;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.shared.search.FilterItem.Operation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import java.util.ArrayList;
import java.util.Date;

/**
 * Home demands search presenter.
 *
 * @author Martin Slavkovsky
 */
public class HomeDemandsSearchView extends Composite implements
    SearchModulePresenter.SearchModulesViewInterface {

    /**************************************************************************/
    /*  UiBinder                                                              */
    /**************************************************************************/
    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, HomeDemandsSearchView> {
    }
    /**************************************************************************/
    /*  Attributes                                                            */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) WSListBox creationDate, demandTypes;
    @UiField(provided = true) ValidationMonitor titleMonitor, priceMonitorFrom, priceMonitorTo;
    @UiField WSDateBox endDateFrom, endDateTo;

    /**************************************************************************/
    /*  Initialization                                                        */
    /**************************************************************************/
    public HomeDemandsSearchView() {
        createCreationDateListBox();
        createDemandTypeListBox();

        initValidationMonitors();
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    /**
     * Clear view components.
     */
    @Override
    public void reset() {
        titleMonitor.setValue("");
        ((WSPriceBox) priceMonitorFrom.getWidget()).setText("");
        priceMonitorFrom.reset();
        ((WSPriceBox) priceMonitorTo.getWidget()).setText("");
        priceMonitorTo.reset();
        demandTypes.setSelected(Storage.MSGS.columnType());
        creationDate.setSelected(Storage.MSGS.creationDateNoLimits());
        endDateFrom.getTextBox().setText("");
        endDateTo.getTextBox().setText("");
    }

    /**************************************************************************/
    /*  Getters                                                               */
    /**************************************************************************/
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        boolean valid = true;
        valid = titleMonitor.isValid() && valid;
        valid = priceMonitorFrom.isValid() && valid;
        valid = priceMonitorTo.isValid() && valid;
        return valid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<FilterItem> getFilter() {
        ArrayList<FilterItem> filters = new ArrayList<FilterItem>();
        int group = 0;
        if (!((String) titleMonitor.getValue()).isEmpty()) {
            filters.add(new FilterItem(
                DemandField.TITLE,
                Operation.OPERATION_LIKE, ((String) titleMonitor.getValue()), group++));
        }
        if (!((WSPriceBox) priceMonitorFrom.getWidget()).getText().isEmpty()) {
            filters.add(new FilterItem(
                DemandField.PRICE,
                Operation.OPERATION_FROM, priceMonitorFrom.getValue(), group++));
        }
        if (!((WSPriceBox) priceMonitorTo.getWidget()).getText().isEmpty()) {
            filters.add(new FilterItem(
                DemandField.PRICE,
                Operation.OPERATION_TO, priceMonitorTo.getValue(), group++));
        }
        if (demandTypes.isSelected()) {
            filters.add(new FilterItem(
                DemandField.DEMAND_TYPE,
                Operation.OPERATION_EQUALS, demandTypes.getSelected(), group++));
        }
        if (creationDate.isSelected()) {
            filters.add(new FilterItem(
                DemandField.CREATED,
                Operation.OPERATION_FROM, getCreatedDate(), group++));
        }
        if (endDateFrom.getValue() != null) {
            filters.add(new FilterItem(
                DemandField.END_DATE,
                Operation.OPERATION_FROM, endDateFrom.getValue(), group++));
        }
        if (endDateTo.getValue() != null) {
            filters.add(new FilterItem(
                DemandField.END_DATE,
                Operation.OPERATION_TO, endDateTo.getValue(), group++));
        }
        return filters;
    }

    /**************************************************************************/
    /*  Helper methods                                                        */
    /**************************************************************************/
    /**
     * Inits validation monitors.
     */
    private void initValidationMonitors() {
        Class<?>[] groups = {SearchGroup.class};
        titleMonitor = createValidationMonitor(DemandField.TITLE, groups);
        priceMonitorFrom = createValidationMonitor(DemandField.PRICE, groups);
        priceMonitorTo = createValidationMonitor(DemandField.PRICE, groups);
    }

    /**
     * Creates validation monitors
     * @param field - validation field
     * @param groups - validation groups
     * @return created validation monitor
     */
    private ValidationMonitor createValidationMonitor(DemandField field, Class<?>[] groups) {
        return new ValidationMonitor<FullDemandDetail>(FullDemandDetail.class, groups, field.getValue());
    }

    /**
     * Creates demand type WSListBox.
     */
    private void createDemandTypeListBox() {
        WSListBoxData demandTypeData = new WSListBoxData();
        demandTypeData.insertItem(Storage.MSGS.columnType(), 0);
        demandTypeData.insertItem(DemandTypeType.NORMAL.getValue(), 1);
        demandTypeData.insertItem(DemandTypeType.ATTRACTIVE.getValue(), 2);
        demandTypes = WSListBox.createListBox(demandTypeData, 0);
    }

    /**
     * Creates creation date WSListBox.
     */
    private void createCreationDateListBox() {
        WSListBoxData creationDateData = new WSListBoxData();
        creationDateData.insertItem(Storage.MSGS.creationDateToday(), 0);
        creationDateData.insertItem(Storage.MSGS.creationDateYesterday(), 1);
        creationDateData.insertItem(Storage.MSGS.creationDateLastWeek(), 2);
        creationDateData.insertItem(Storage.MSGS.creationDateLastMonth(), 3);
        creationDateData.insertItem(Storage.MSGS.creationDateNoLimits(), 4);
        creationDate = WSListBox.createListBox(creationDateData, 4);
    }

    /**
     * Gets created date from created string.
     * @return return parsed date
     */
    private Date getCreatedDate() {
        Date date = new Date(); //today
        if (creationDate.getSelected().equals(Storage.MSGS.creationDateYesterday())) {
            CalendarUtil.addDaysToDate(date, -1);   //yesterday
        } else if (creationDate.getSelected().equals(Storage.MSGS.creationDateLastWeek())) {
            CalendarUtil.addDaysToDate(date, -7);   //last week
        } else if (creationDate.getSelected().equals(Storage.MSGS.creationDateLastMonth())) {
            CalendarUtil.addMonthsToDate(date, -1); //last month
        }
        return date;
    }
}