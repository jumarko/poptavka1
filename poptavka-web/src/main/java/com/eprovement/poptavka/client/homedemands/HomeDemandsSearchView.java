package com.eprovement.poptavka.client.homedemands;

import com.eprovement.poptavka.client.common.MyDateBox;
import com.eprovement.poptavka.client.common.myListBox.MyListBox;
import com.eprovement.poptavka.client.common.search.SearchModulePresenter;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.domain.enums.DemandTypeType;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.IntegerBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
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
    @UiField TextBox demandTitle;
    @UiField IntegerBox priceFrom, priceTo;
    @UiField ListBox demandTypes;
    @UiField(provided = true) MyListBox creationDate;
    @UiField MyDateBox finnishDateFrom, finnishDateTo;
    @UiField Button clearBtn;
    /** Search Fields. **/
    private static final String FIELD_TITLE = "title";
    private static final String FIELD_DEMAND_TYPE = "type.description";
    private static final String FIELD_PRICE = "price";
    private static final String FIELD_CREATED_DATE = "createdDate";
    private static final String FIELD_END_DATE = "endDate";

    public HomeDemandsSearchView() {
        creationDate = MyListBox.createListBox(getCreaionDateData());
        creationDate.addStyleName(Storage.RSCS.common().myListBox());
        initWidget(uiBinder.createAndBindUi(this));

        demandTypes.addItem(Storage.MSGS.commonListDefault());
        for (DemandTypeType type : DemandTypeType.values()) {
            demandTypes.addItem(type.name());
        }

        StyleResource.INSTANCE.common().ensureInjected();
    }

    private List<String> getCreaionDateData() {
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
            filters.add(new FilterItem(FIELD_TITLE, FilterItem.OPERATION_LIKE, demandTitle.getText()));
        }
        if (demandTypes.getSelectedIndex() != 0) {
            filters.add(new FilterItem(FIELD_DEMAND_TYPE,
                    FilterItem.OPERATION_EQUALS,
                    demandTypes.getItemText(demandTypes.getSelectedIndex())));
        }
        if (!priceFrom.getText().isEmpty()) {
            filters.add(new FilterItem(FIELD_PRICE, FilterItem.OPERATION_FROM, priceFrom.getValue()));
        }
        if (!priceTo.getText().isEmpty()) {
            filters.add(new FilterItem(FIELD_PRICE, FilterItem.OPERATION_TO, priceTo.getValue()));
        }
        if (!creationDate.getSelected().equals(Storage.MSGS.creationDateNoLimits())) {
            filters.add(new FilterItem(FIELD_CREATED_DATE, FilterItem.OPERATION_FROM, getCreatedDate()));
        }
        if (finnishDateFrom.getValue() != null) {
            filters.add(new FilterItem(FIELD_END_DATE, FilterItem.OPERATION_FROM, finnishDateFrom.getValue()));
        }
        if (finnishDateTo.getValue() != null) {
            filters.add(new FilterItem(FIELD_END_DATE, FilterItem.OPERATION_TO, finnishDateTo.getValue()));
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
        demandTypes.setSelectedIndex(0);
        priceFrom.setText("");
        priceTo.setText("");
        creationDate.setSelected(Storage.MSGS.creationDateNoLimits());
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}