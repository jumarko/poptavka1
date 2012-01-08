package cz.poptavka.sample.client.main.common.search.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;

public class AdminInvoicesViewView extends Composite implements
        AdminInvoicesViewPresenter.AdminInvoicesViewInterface {

    private static SearchModulViewUiBinder uiBinder = GWT.create(SearchModulViewUiBinder.class);

    interface SearchModulViewUiBinder extends UiBinder<Widget, AdminInvoicesViewView> {
    }
    @UiField
    TextBox idFrom, idTo, variableSymbol, totalPriceFrom, totalPriceTo,
    invoiceNumberFrom, invoiceNumberTo;
    @UiField
    ListBox paymentMethod;

//    @Override
//    public void createView() {
    public AdminInvoicesViewView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public SearchModuleDataHolder getFilter() {
        SearchModuleDataHolder data = new SearchModuleDataHolder();
        data.initAdminInvoices();
        if (!idFrom.getText().equals("")) {
            data.getAdminInvoice().setIdFrom(Long.valueOf(idFrom.getText()));
        }
        if (!idTo.getText().equals("")) {
            data.getAdminInvoice().setIdTo(Long.valueOf(idTo.getText()));
        }
        if (!invoiceNumberFrom.getText().equals("")) {
            data.getAdminInvoice().setInvoiceNumberFrom(Integer.valueOf(invoiceNumberFrom.getText()));
        }
        if (!invoiceNumberTo.getText().equals("")) {
            data.getAdminInvoice().setInvoiceNumberTo(Integer.valueOf(invoiceNumberTo.getText()));
        }
        if (!totalPriceFrom.getText().equals("")) {
            data.getAdminInvoice().setTotalPriceFrom(Integer.valueOf(totalPriceFrom.getText()));
        }
        if (!totalPriceTo.getText().equals("")) {
            data.getAdminInvoice().setTotalPriceTo(Integer.valueOf(totalPriceTo.getText()));
        }
        if (!variableSymbol.getText().equals("")) {
            data.getAdminInvoice().setVariableSymbol(variableSymbol.getText());
        }
        if (paymentMethod.getSelectedIndex() != 0) {
            data.getAdminInvoice().setPaymentMethodId(
                    Long.valueOf(paymentMethod.getValue(paymentMethod.getSelectedIndex())));
        }
        return data;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public ListBox getPaymentMethodList() {
        return paymentMethod;
    }

    @Override
    public void displayAdvSearchDataInfo(SearchModuleDataHolder data, TextBox infoHolder) {
        StringBuilder infoText = new StringBuilder();
        if (data.getAdminInvoice().getIdFrom() != null) {
            infoText.append("idFrom:");
            infoText.append(data.getAdminInvoice().getIdFrom());
        }
        if (data.getAdminInvoice().getIdTo() != null) {
            infoText.append("idTo:");
            infoText.append(data.getAdminInvoice().getIdTo());
        }
        if (data.getAdminInvoice().getInvoiceNumberFrom() != null) {
            infoText.append("invoiceNumFrom:");
            infoText.append(data.getAdminInvoice().getInvoiceNumberFrom());
        }
        if (data.getAdminInvoice().getInvoiceNumberTo() != null) {
            infoText.append("invoiceNumTo:");
            infoText.append(data.getAdminInvoice().getInvoiceNumberTo());
        }
        if (data.getAdminInvoice().getTotalPriceFrom() != null) {
            infoText.append("priceFrom:");
            infoText.append(data.getAdminInvoice().getTotalPriceFrom());
        }
        if (data.getAdminInvoice().getTotalPriceTo() != null) {
            infoText.append("priceTO:");
            infoText.append(data.getAdminInvoice().getTotalPriceTo());
        }
        if (data.getAdminInvoice().getVariableSymbol() != null) {
            infoText.append("varSymb:");
            infoText.append(data.getAdminInvoice().getVariableSymbol());
        }
        if (data.getAdminInvoice().getPaymentMethodId() != null) {
            infoText.append("methodId:");
            infoText.append(data.getAdminInvoice().getPaymentMethodId());
        }
        infoHolder.setText(infoText.toString());
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
}