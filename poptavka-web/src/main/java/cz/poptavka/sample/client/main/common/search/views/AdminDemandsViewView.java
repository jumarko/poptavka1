package cz.poptavka.sample.client.main.common.search.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.main.common.search.SearchModulePresenter;
import cz.poptavka.sample.client.main.common.search.dataHolders.FilterItem;
import cz.poptavka.sample.domain.demand.DemandStatus;
import cz.poptavka.sample.domain.demand.DemandType;
import cz.poptavka.sample.domain.demand.DemandType.Type;
import java.util.ArrayList;

public class AdminDemandsViewView extends Composite implements SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminDemandsViewView> {
    }
    @UiField
    TextBox demandIdFrom, demandIdTo, clientIdFrom, clientIdTo, demandTitle;
    @UiField
    DateBox expirationDateFrom, expirationDateTo, endDateFrom, endDateTo;
    @UiField
    ListBox demandType, demandStatus;
    @UiField
    Button clearBtn;

    public AdminDemandsViewView() {
        initWidget(uiBinder.createAndBindUi(this));
        demandType.addItem(Storage.MSGS.select());
        for (Type type : DemandType.Type.values()) {
            demandType.addItem(type.name());
        }
        demandStatus.addItem(Storage.MSGS.select());
        for (DemandStatus status : DemandStatus.values()) {
            demandStatus.addItem(status.name());
        }
    }

    @Override
    public ArrayList<FilterItem> getFilter() {
        ArrayList<FilterItem> filters = new ArrayList<FilterItem>();
        if (!demandIdFrom.getText().equals("")) {
            filters.add(new FilterItem("id", FilterItem.OPERATION_FROM, demandIdFrom.getText()));
        }
        if (!demandIdTo.getText().equals("")) {
            filters.add(new FilterItem("id", FilterItem.OPERATION_TO, demandIdTo.getText()));
        }
        if (!clientIdFrom.getText().equals("")) {
            filters.add(new FilterItem("client.id", FilterItem.OPERATION_FROM, clientIdFrom.getText()));
        }
        if (!clientIdTo.getText().equals("")) {
            filters.add(new FilterItem("client.id", FilterItem.OPERATION_TO, clientIdTo.getText()));
        }
        if (!demandTitle.getText().equals("")) {
            filters.add(new FilterItem("title", FilterItem.OPERATION_LIKE, demandTitle.getText()));
        }
        if (expirationDateFrom.getValue() != null) {
            filters.add(new FilterItem("validTo", FilterItem.OPERATION_FROM, expirationDateFrom.getValue()));
        }
        if (expirationDateTo.getValue() != null) {
            filters.add(new FilterItem("validTo", FilterItem.OPERATION_TO, expirationDateTo.getValue()));
        }
        if (endDateFrom.getValue() != null) {
            filters.add(new FilterItem("endDate", FilterItem.OPERATION_FROM, endDateFrom.getValue()));
        }
        if (endDateTo.getValue() != null) {
            filters.add(new FilterItem("endDate", FilterItem.OPERATION_TO, endDateTo.getValue()));
        }
        if (demandType.getSelectedIndex() != 0) {
            filters.add(new FilterItem("type", FilterItem.OPERATION_EQUALS,
                    demandType.getItemText(demandType.getSelectedIndex())));
        }
        if (demandStatus.getSelectedIndex() != 0) {
            filters.add(new FilterItem("status", FilterItem.OPERATION_EQUALS,
                    demandStatus.getItemText(demandStatus.getSelectedIndex())));
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

    @UiHandler("clearBtn")
    void clearBtnAction(ClickEvent event) {
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

    @Override
    public Widget getWidgetView() {
        return this;
    }
}