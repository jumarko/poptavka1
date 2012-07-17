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
import com.eprovement.poptavka.client.common.search.SearchModulePresenter;
import com.eprovement.poptavka.shared.search.FilterItem;
import java.util.ArrayList;

public class AdminInvoicesViewView extends Composite implements
        SearchModulePresenter.SearchModulesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminInvoicesViewView> {
    }
    @UiField
    TextBox idFrom, idTo, variableSymbol, totalPriceFrom, totalPriceTo;
    @UiField
    TextBox invoiceNumberFrom, invoiceNumberTo;
    @UiField
    ListBox paymentMethod;
    @UiField
    Button clearBtn;

    public AdminInvoicesViewView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public ArrayList<FilterItem> getFilter() {
        ArrayList<FilterItem> filters = new ArrayList<FilterItem>();
        if (!idFrom.getText().equals("")) {
            filters.add(new FilterItem("id", FilterItem.OPERATION_FROM, idFrom.getText()));
        }
        if (!idTo.getText().equals("")) {
            filters.add(new FilterItem("id", FilterItem.OPERATION_TO, idTo.getText()));
        }
        if (!invoiceNumberFrom.getText().equals("")) {
            filters.add(new FilterItem("invoiceNumber",
                    FilterItem.OPERATION_FROM, invoiceNumberFrom.getText()));
        }
        if (!invoiceNumberTo.getText().equals("")) {
            filters.add(new FilterItem("invoiceNumber", FilterItem.OPERATION_TO, invoiceNumberTo.getText()));
        }
        if (!totalPriceFrom.getText().equals("")) {
            filters.add(new FilterItem("totalPrice", FilterItem.OPERATION_FROM, totalPriceFrom.getText()));
        }
        if (!totalPriceTo.getText().equals("")) {
            filters.add(new FilterItem("totalPrice", FilterItem.OPERATION_TO, totalPriceTo.getText()));
        }
        if (!variableSymbol.getText().equals("")) {
            filters.add(new FilterItem("variableSymbol",
                    FilterItem.OPERATION_FROM, variableSymbol.getText()));
        }
        if (paymentMethod.getSelectedIndex() != 0) {
            filters.add(new FilterItem("paymentMethod", FilterItem.OPERATION_TO,
                    paymentMethod.getValue(paymentMethod.getSelectedIndex())));
        }
        return filters;
    }

    public ListBox getPaymentMethodList() {
        return paymentMethod;
    }

    @UiHandler("idFrom")
    void validateIdFrom(ChangeEvent event) {
        if (!idFrom.getText().matches("[0-9]+")) {
            idFrom.setText("");
        }
    }

    @UiHandler("idTo")
    void validateIdTo(ChangeEvent event) {
        if (!idTo.getText().matches("[0-9]+")) {
            idTo.setText("");
        }
    }

    @UiHandler("invoiceNumberFrom")
    void validateInvoiceNumberFrom(ChangeEvent event) {
        if (!invoiceNumberFrom.getText().matches("[0-9]+")) {
            invoiceNumberFrom.setText("");
        }
    }

    @UiHandler("invoiceNumberTo")
    void validateInvoiceNumberTo(ChangeEvent event) {
        if (!invoiceNumberTo.getText().matches("[0-9]+")) {
            invoiceNumberTo.setText("");
        }
    }

    @UiHandler("totalPriceFrom")
    void validateTotalPriceFrom(ChangeEvent event) {
        if (!totalPriceFrom.getText().matches("[0-9]+")) {
            totalPriceFrom.setText("");
        }
    }

    @UiHandler("totalPriceTo")
    void validateTotalPriceTo(ChangeEvent event) {
        if (!totalPriceTo.getText().matches("[0-9]+")) {
            totalPriceTo.setText("");
        }
    }

    @UiHandler("clearBtn")
    void clearBtnAction(ClickEvent event) {
        idFrom.setText("");
        idTo.setText("");
        invoiceNumberFrom.setText("");
        invoiceNumberTo.setText("");
        totalPriceFrom.setText("");
        totalPriceTo.setText("");
        variableSymbol.setText("");
        paymentMethod.setSelectedIndex(0);
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}