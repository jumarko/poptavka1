package com.eprovement.poptavka.client.user.admin.searchViews;

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
import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.client.main.common.search.SearchModulePresenter;
import com.eprovement.poptavka.client.main.common.search.dataHolders.FilterItem;
import com.eprovement.poptavka.domain.enums.OfferState;
import com.eprovement.poptavka.domain.enums.OfferState.Type;
import java.util.ArrayList;

public class AdminOffersViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminOffersViewView> {
    }
    @UiField
    TextBox offerIdFrom, offerIdTo, demandIdFrom, demandIdTo, supplierIdFrom;
    @UiField
    TextBox supplierIdTo, priceFrom, priceTo;
    @UiField
    DateBox createdFrom, createdTo, finnishFrom, finnishTo;
    @UiField
    ListBox state;
    @UiField
    Button clearBtn;

    public AdminOffersViewView() {
        initWidget(uiBinder.createAndBindUi(this));
        state.addItem(Storage.MSGS.select());
        for (Type type : OfferState.Type.values()) {
            state.addItem(type.name());
        }
    }

    @Override
    public ArrayList<FilterItem> getFilter() {
        ArrayList<FilterItem> filters = new ArrayList<FilterItem>();
        if (!offerIdFrom.getText().equals("")) {
            filters.add(new FilterItem("id", FilterItem.OPERATION_FROM, offerIdFrom.getText()));
        }
        if (!offerIdTo.getText().equals("")) {
            filters.add(new FilterItem("id", FilterItem.OPERATION_TO, offerIdTo.getText()));
        }
        if (!demandIdFrom.getText().equals("")) {
            filters.add(new FilterItem("demand.id", FilterItem.OPERATION_FROM, demandIdFrom.getText()));
        }
        if (!demandIdTo.getText().equals("")) {
            filters.add(new FilterItem("demand.id", FilterItem.OPERATION_TO, demandIdTo.getText()));
        }
        if (!supplierIdFrom.getText().equals("")) {
            filters.add(new FilterItem("supplier.id", FilterItem.OPERATION_FROM, supplierIdFrom.getText()));
        }
        if (!supplierIdTo.getText().equals("")) {
            filters.add(new FilterItem("supplier.id", FilterItem.OPERATION_TO, supplierIdTo.getText()));
        }
        if (!priceFrom.getText().equals("")) {
            filters.add(new FilterItem("price", FilterItem.OPERATION_FROM, priceFrom.getText()));
        }
        if (!priceTo.getText().equals("")) {
            filters.add(new FilterItem("price", FilterItem.OPERATION_TO, priceTo.getText()));
        }
        if (createdFrom.getValue() != null) {
            filters.add(new FilterItem("created", FilterItem.OPERATION_FROM, createdFrom.getValue()));
        }
        if (createdTo.getValue() != null) {
            filters.add(new FilterItem("created", FilterItem.OPERATION_FROM, createdTo.getValue()));
        }
        if (finnishFrom.getValue() != null) {
            filters.add(new FilterItem("finishDate", FilterItem.OPERATION_FROM, finnishFrom.getValue()));
        }
        if (finnishTo.getValue() != null) {
            filters.add(new FilterItem("finishDate", FilterItem.OPERATION_FROM, finnishTo.getValue()));
        }
        if (state.getSelectedIndex() != 0) {
            filters.add(new FilterItem("state", FilterItem.OPERATION_EQUALS,
                    state.getItemText(state.getSelectedIndex())));
        }
        return filters;
    }

    @UiHandler("offerIdFrom")
    void validateOfferIdFrom(ChangeEvent event) {
        if (!offerIdFrom.getText().matches("[0-9]+")) {
            offerIdFrom.setText("");
        }
    }

    @UiHandler("offerIdTo")
    void validateOfferIdTo(ChangeEvent event) {
        if (!offerIdTo.getText().matches("[0-9]+")) {
            offerIdTo.setText("");
        }
    }

    @UiHandler("demandIdFrom")
    void validateDemandIdFrom(ChangeEvent event) {
        if (!demandIdFrom.getText().matches("[0-9]+")) {
            demandIdFrom.setText("");
        }
    }

    @UiHandler("demandIdTo")
    void validateDemandIdTo(ChangeEvent event) {
        if (!demandIdTo.getText().matches("[0-9]+")) {
            demandIdTo.setText("");
        }
    }

    @UiHandler("supplierIdFrom")
    void validateSupplierIdFrom(ChangeEvent event) {
        if (!supplierIdFrom.getText().matches("[0-9]+")) {
            supplierIdFrom.setText("");
        }
    }

    @UiHandler("supplierIdTo")
    void validateSupplierIdTo(ChangeEvent event) {
        if (!supplierIdTo.getText().matches("[0-9]+")) {
            supplierIdTo.setText("");
        }
    }

    @UiHandler("priceFrom")
    void validatePriceFrom(ChangeEvent event) {
        if (!priceFrom.getText().matches("[0-9]+")) {
            priceFrom.setText("");
        }
    }

    @UiHandler("priceTo")
    void validatePriceTo(ChangeEvent event) {
        if (!priceTo.getText().matches("[0-9]+")) {
            priceTo.setText("");
        }
    }

    @UiHandler("clearBtn")
    void clearBtnAction(ClickEvent event) {
        offerIdFrom.setText("");
        offerIdTo.setText("");
        demandIdFrom.setText("");
        demandIdTo.setText("");
        supplierIdFrom.setText("");
        supplierIdTo.setText("");
        priceFrom.setText("");
        priceTo.setText("");
        createdFrom.setValue(null);
        createdTo.setValue(null);
        finnishFrom.setValue(null);
        finnishTo.setValue(null);
        state.setSelectedIndex(0);
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}