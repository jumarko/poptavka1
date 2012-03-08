/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import cz.poptavka.sample.shared.domain.adminModule.InvoiceDetail;
import java.math.BigDecimal;

/**
 *
 * @author Martin Slavkovsky
 */
public class AdminInvoiceInfoView extends Composite implements
        AdminInvoicesPresenter.AdminInvoiceInfoInterface {

    private static AdminInvoiceInfoViewUiBinder uiBinder = GWT.create(AdminInvoiceInfoViewUiBinder.class);

    interface AdminInvoiceInfoViewUiBinder extends
            UiBinder<Widget, AdminInvoiceInfoView> {
    }
    // demand detail input fields
    @UiField
    TextBox bankAccountNumber, bankCode, constSymbol, invoiceNumber,
    taxBasis, totalPrice, variableSymbol, vat, vatRate;
    @UiField
    DateBox dueDate, issueDate, shipmentDate;
    @UiField
    ListBox paymentMethod, userServices;
    @UiField
    Button updateButton;
    private InvoiceDetail invoiceInfo;

    public AdminInvoiceInfoView() {
        initWidget(uiBinder.createAndBindUi(this));
        initInvoiceInfoForm();
    }

    private void initInvoiceInfoForm() {
        // initWidget(uiBinder.createAndBindUi(this));
        DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_LONG);
        dueDate.setFormat(new DateBox.DefaultFormat(dateFormat));
        issueDate.setFormat(new DateBox.DefaultFormat(dateFormat));
        shipmentDate.setFormat(new DateBox.DefaultFormat(dateFormat));

        // Initialize the contact to null.
        setInvoiceDetail(null);
    }

    @Override
    public InvoiceDetail getUpdatedInvoiceDetail() {
        if (invoiceInfo == null) {
            return null;
        }
        boolean t = totalPrice.getText() == null;
        if (t) {
            GWT.log("d" + t + "max offer");
        }
        GWT.log("d" + t + "max offer");
        GWT.log("d" + totalPrice.getText().equals("") + "price ");

        // Update the invoice.
        invoiceInfo.setBankAccountNumber(bankAccountNumber.getText());
        invoiceInfo.setBankCode(bankCode.getText());
        invoiceInfo.setConstSymbol(constSymbol.getText());
        invoiceInfo.setDueDate(dueDate.getValue());
        invoiceInfo.setInvoiceNumber(invoiceNumber.getText());
        invoiceInfo.setIssueDate(issueDate.getValue());
//        invoiceInfo.setPaymentMethod(paymentMethod.getItemText(paymentMethod.getSelectedIndex()));
        invoiceInfo.setShipmentDate(shipmentDate.getValue());
        invoiceInfo.setTaxBasis(BigDecimal.valueOf(Long.valueOf(taxBasis.getText())));
        invoiceInfo.setTotalPrice(BigDecimal.valueOf(Long.valueOf(totalPrice.getText())));
//        invoiceInfo.setUserServices(userServices.get());
        invoiceInfo.setVariableSymbol(variableSymbol.getText());
        invoiceInfo.setVat(BigDecimal.valueOf(Long.valueOf(vat.getText())));
        invoiceInfo.setVatRate(Integer.valueOf(vatRate.getText()));

        return invoiceInfo;
    }

    @Override
    public void setInvoiceDetail(InvoiceDetail invoice) {
        this.invoiceInfo = invoice;
        updateButton.setEnabled(invoice != null);
        if (invoice != null) {
            bankAccountNumber.setText(invoice.getBankAccountNumber());
            bankCode.setText(invoice.getBankCode());
            constSymbol.setText(invoice.getConstSymbol());
            dueDate.setValue(invoice.getDueDate());
            invoiceNumber.setText(invoice.getInvoiceNumber());
            issueDate.setValue(invoice.getIssueDate());
//            paymentMethod.setText(PaymentMethodDetail.createPaymentMethodDetail(invoice.getPaymentMethod()));
            shipmentDate.setValue(invoice.getShipmentDate());
            taxBasis.setText(Long.toString(invoice.getTaxBasis().longValue()));
            totalPrice.setText(Long.toString(invoice.getTotalPrice().longValue()));
//            userServices.setText(UserServices);
            variableSymbol.setText(invoice.getVariableSymbol());
            vat.setText(Long.toString(invoice.getVat().longValue()));
            vatRate.setText(Integer.toString(invoice.getVatRate()));
        }
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public Button getUpdateBtn() {
        return updateButton;
    }
}