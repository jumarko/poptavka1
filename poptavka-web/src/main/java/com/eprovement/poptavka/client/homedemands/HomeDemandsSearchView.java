package com.eprovement.poptavka.client.homedemands;

import com.eprovement.poptavka.client.common.BigDecimalBox;
import com.eprovement.poptavka.client.common.MyDateBox;
import com.eprovement.poptavka.client.common.ValidationMonitor;
import com.eprovement.poptavka.client.common.myListBox.MyListBox;
import com.eprovement.poptavka.client.common.search.SearchModulePresenter;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.SearchGroup;
import com.eprovement.poptavka.domain.enums.DemandTypeType;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail.DemandField;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeDemandsSearchView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, HomeDemandsSearchView> {
    }
    /** UiBinder attributes. **/
    @UiField(provided = true) MyListBox creationDate, demandTypes;
    @UiField(provided = true) ValidationMonitor priceMonitorFrom, priceMonitorTo;
    @UiField TextBox demandTitle;
    @UiField MyDateBox endDateFrom, endDateTo;
    @UiField Button clearBtn;
    /** Additional Search Fields. **/
    private static final String DEMAND_TYPE_DESCRIPTION_FIELD = ".description";

    public HomeDemandsSearchView() {
        creationDate = MyListBox.createListBox(getCreationDateData(), Storage.MSGS.creationDateNoLimits());
        demandTypes = MyListBox.createListBox(getDemandTypeData(), Storage.MSGS.commonListDefault());
        initValidationMonitors();
        initWidget(uiBinder.createAndBindUi(this));

        StyleResource.INSTANCE.common().ensureInjected();
    }

    private void initValidationMonitors() {
        priceMonitorFrom = new ValidationMonitor<FullDemandDetail>(
                FullDemandDetail.class, SearchGroup.class, FullDemandDetail.DemandField.PRICE.getValue());
        priceMonitorTo = new ValidationMonitor<FullDemandDetail>(
                FullDemandDetail.class, SearchGroup.class, FullDemandDetail.DemandField.PRICE.getValue());
    }

    private List<String> getDemandTypeData() {
        List<String> data = new ArrayList<String>();
        data.add(Storage.MSGS.commonListDefault());
        data.addAll(DemandTypeType.getStringValues());
        return data;
    }

    private List<String> getCreationDateData() {
        List<String> data = new ArrayList<String>();
        data.add(Storage.MSGS.creationDateToday());
        data.add(Storage.MSGS.creationDateYesterday());
        data.add(Storage.MSGS.creationDateLastWeek());
        data.add(Storage.MSGS.creationDateLastMonth());
        data.add(Storage.MSGS.creationDateNoLimits());
        return data;
    }

    @Override
    public ArrayList<FilterItem> getFilter() {
        ArrayList<FilterItem> filters = new ArrayList<FilterItem>();
        if (!demandTitle.getText().isEmpty()) {
            filters.add(new FilterItem(
                    DemandField.TITLE.getValue(),
                    FilterItem.OPERATION_LIKE, demandTitle.getText()));
        }
        if (priceMonitorFrom.getValue() != null) {
            filters.add(new FilterItem(
                    DemandField.PRICE.getValue(),
                    FilterItem.OPERATION_FROM, priceMonitorFrom.getValue()));
        }
        if (priceMonitorTo.getValue() != null) {
            filters.add(new FilterItem(
                    DemandField.PRICE.getValue(),
                    FilterItem.OPERATION_TO, priceMonitorTo.getValue()));
        }
        if (demandTypes.isSelected()) {
            filters.add(new FilterItem(
                    DemandField.DEMAND_TYPE.getValue().concat(DEMAND_TYPE_DESCRIPTION_FIELD),
                    FilterItem.OPERATION_EQUALS, demandTypes.getSelected()));
        }
        if (creationDate.isSelected()) {
            filters.add(new FilterItem(
                    DemandField.CREATED.getValue(),
                    FilterItem.OPERATION_FROM, getCreatedDate()));
        }
        if (endDateFrom.getValue() != null) {
            filters.add(new FilterItem(
                    DemandField.END_DATE.getValue(),
                    FilterItem.OPERATION_FROM, endDateFrom.getValue()));
        }
        if (endDateTo.getValue() != null) {
            filters.add(new FilterItem(
                    DemandField.END_DATE.getValue(),
                    FilterItem.OPERATION_TO, endDateTo.getValue()));
        }
        return filters;
    }

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

    @UiHandler("clearBtn")
    void clearBtnAction(ClickEvent event) {
        clear();
    }

    @Override
    public void clear() {
        demandTitle.setText("");
        ((BigDecimalBox) priceMonitorFrom.getWidget()).setText("");
        priceMonitorFrom.reset();
        ((BigDecimalBox) priceMonitorTo.getWidget()).setText("");
        priceMonitorTo.reset();
        demandTypes.setSelected(Storage.MSGS.commonListDefault());
        creationDate.setSelected(Storage.MSGS.creationDateNoLimits());
        endDateFrom.getTextBox().setText("");
        endDateTo.getTextBox().setText("");
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}