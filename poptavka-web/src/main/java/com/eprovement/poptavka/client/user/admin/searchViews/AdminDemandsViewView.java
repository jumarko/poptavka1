package com.eprovement.poptavka.client.user.admin.searchViews;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.search.SearchModulePresenter;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.DemandTypeType;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.shared.search.FilterItem.Operation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import java.util.ArrayList;

public class AdminDemandsViewView extends Composite implements SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminDemandsViewView> {
    }

    @UiField TextBox demandIdFrom, demandIdTo, clientIdFrom, clientIdTo, demandTitle;
    @UiField DateBox expirationDateFrom, expirationDateTo, endDateFrom, endDateTo;
    @UiField ListBox demandType, demandStatus;

    public AdminDemandsViewView() {
        initWidget(uiBinder.createAndBindUi(this));
        demandType.addItem(Storage.MSGS.commonListDefault());
        for (DemandTypeType type : DemandTypeType.values()) {
            demandType.addItem(type.name());
        }
        demandStatus.addItem(Storage.MSGS.commonListDefault());
        for (DemandStatus status : DemandStatus.values()) {
            demandStatus.addItem(status.name());
        }
    }

    @Override
    public ArrayList<FilterItem> getFilter() {
        ArrayList<FilterItem> filters = new ArrayList<FilterItem>();
        int group = 0;
        if (!demandIdFrom.getText().equals("")) {
            filters.add(new FilterItem("id", Operation.OPERATION_FROM, demandIdFrom.getText(), group++));
        }
        if (!demandIdTo.getText().equals("")) {
            filters.add(new FilterItem("id", Operation.OPERATION_TO, demandIdTo.getText(), group++));
        }
        if (!clientIdFrom.getText().equals("")) {
            filters.add(new FilterItem("client.id", Operation.OPERATION_FROM, clientIdFrom.getText(), group++));
        }
        if (!clientIdTo.getText().equals("")) {
            filters.add(new FilterItem("client.id", Operation.OPERATION_TO, clientIdTo.getText(), group++));
        }
        if (!demandTitle.getText().equals("")) {
            filters.add(new FilterItem("title", Operation.OPERATION_LIKE, demandTitle.getText(), group++));
        }
        if (expirationDateFrom.getValue() != null) {
            filters.add(new FilterItem("validTo", Operation.OPERATION_FROM, expirationDateFrom.getValue(), group++));
        }
        if (expirationDateTo.getValue() != null) {
            filters.add(new FilterItem("validTo", Operation.OPERATION_TO, expirationDateTo.getValue(), group++));
        }
        if (endDateFrom.getValue() != null) {
            filters.add(new FilterItem("endDate", Operation.OPERATION_FROM, endDateFrom.getValue(), group++));
        }
        if (endDateTo.getValue() != null) {
            filters.add(new FilterItem("endDate", Operation.OPERATION_TO, endDateTo.getValue(), group++));
        }
        if (demandType.getSelectedIndex() != 0) {
            filters.add(new FilterItem("type", Operation.OPERATION_EQUALS,
                    demandType.getItemText(demandType.getSelectedIndex()), group++));
        }
        if (demandStatus.getSelectedIndex() != 0) {
            filters.add(new FilterItem("status", Operation.OPERATION_EQUALS,
                    demandStatus.getItemText(demandStatus.getSelectedIndex()), group++));
        }
        return filters;
    }

    @UiHandler("demandIdFrom")
    void validatePriceFrom(ChangeEvent event) {
        if (!demandIdFrom.getText().matches("[0-9]+")) {
            demandIdFrom.setText("");
        }
    }

    @UiHandler("demandIdTo")
    void validatePriceTo(ChangeEvent event) {
        if (!demandIdTo.getText().matches("[0-9]+")) {
            demandIdTo.setText("");
        }
    }

    @UiHandler("clientIdFrom")
    void validateRatingFrom(ChangeEvent event) {
        if (!clientIdFrom.getText().matches("[0-9]+")) {
            clientIdFrom.setText("");
        }
    }

    @UiHandler("clientIdTo")
    void validateratingTo(ChangeEvent event) {
        if (!clientIdTo.getText().matches("[0-9]+")) {
            clientIdTo.setText("");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        demandIdFrom.setText("");
        demandIdTo.setText("");
        clientIdFrom.setText("");
        clientIdTo.setText("");
        demandTitle.setText("");
        expirationDateFrom.setValue(null);
        expirationDateTo.setValue(null);
        endDateFrom.setValue(null);
        endDateTo.setValue(null);
        demandType.setSelectedIndex(0);
        demandStatus.setSelectedIndex(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        //no validation monitors
        return true;
    }
}