package cz.poptavka.sample.client.home.supplier.widget;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.main.common.RichTextToolbarWidget;
import cz.poptavka.sample.client.main.common.StatusIconLabel;
import cz.poptavka.sample.client.main.common.creation.ProvidesValidate;
import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.shared.domain.AddressDetail;
import cz.poptavka.sample.shared.domain.SupplierDetail;

public class SupplierInfoView extends Composite
    implements SupplierInfoPresenter.SupplierInfoInterface, ProvidesValidate {

    private static FormUserRegistrationUiBinder uiBinder = GWT.create(FormUserRegistrationUiBinder.class);

    interface FormUserRegistrationUiBinder extends UiBinder<Widget, SupplierInfoView> {
    }
    private ArrayList<TextBox> widgets = new ArrayList<TextBox>();

    private static final int MIN_SIZE = 10;

    @UiField TextBox companyNameBox;
    @UiField TextBox companyIdBox;
    @UiField TextBox companyTaxBox;
    @UiField TextBox websiteBox;
    @UiField TextBox streetBox;
    @UiField TextBox cityBox;
    @UiField TextBox zipBox;
    @UiField TextBox nameBox;
    @UiField TextBox surnameBox;
    @UiField TextBox phoneBox;
    @UiField TextBox mailBox;
    @UiField PasswordTextBox passBox, passConfirmBox;
    @UiField StatusIconLabel mailStatus;
    @UiField StatusIconLabel pwdStatus;
    @UiField StatusIconLabel pwdCheckStatus;
    @UiField RichTextToolbarWidget richText;

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        widgets.add(companyNameBox);
        widgets.add(companyIdBox);
        widgets.add(companyTaxBox);
        widgets.add(streetBox);
        widgets.add(cityBox);
        widgets.add(zipBox);
        widgets.add(nameBox);
        widgets.add(surnameBox);
        widgets.add(phoneBox);
        widgets.add(mailBox);
        widgets.add(passBox);
    }

    public boolean isValid() {
        int errorCount = 0;
        for (TextBox item : widgets) {
            ((Widget) item).removeStyleName(StyleResource.INSTANCE.common().errorField());
            if (item.getText().length() == 0) {
                errorCount++;
                ((Widget) item).setStyleName(StyleResource.INSTANCE.common().errorField());
            }
        }
        ((Widget) richText).removeStyleName(StyleResource.INSTANCE.common().errorField());
        if (richText.getValue().length() < MIN_SIZE) {
            ((Widget) richText).setStyleName(StyleResource.INSTANCE.common().errorField());
            errorCount++;
        }
        return errorCount == 0;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public SupplierDetail getBaseSupplier() {
        SupplierDetail supplier = new SupplierDetail(mailBox.getText(), passBox.getText());

        supplier.setCompanyName(companyNameBox.getText());
        supplier.setIdentifiacationNumber(companyIdBox.getText());
        supplier.setTaxId(companyTaxBox.getText());

        supplier.setFirstName(nameBox.getText());
        supplier.setLastName(surnameBox.getText());
        supplier.setPhone(phoneBox.getText());
        supplier.setWebsite(websiteBox.getText());

        AddressDetail address = new AddressDetail();
        address.setCityName(cityBox.getText());
        address.setStreet(streetBox.getText());
        address.setZipCode(zipBox.getText());

        supplier.setAddress(address);

        return supplier;
    }

    @Override
    public HasValueChangeHandlers<String> getEmailBox() {
        return mailBox;
    }

    @Override
    public StatusIconLabel getMailStatus() {
        return mailStatus;
    }

    @Override
    public StatusIconLabel getPwdStatus() {
        return pwdStatus;
    }

    @Override
    public StatusIconLabel getPwdConfirmStatus() {
        return pwdCheckStatus;
    }

    @Override
    public PasswordTextBox getPwdBox() {
        return passBox;
    }

    @Override
    public PasswordTextBox getPwdConfirmBox() {
        return passConfirmBox;
    }

    @Override
    public SupplierDetail createSupplier() {
        SupplierDetail sup = new SupplierDetail(mailBox.getText(), passBox.getText());

        sup.setCompanyName(companyNameBox.getText());
        sup.setIdentifiacationNumber(companyIdBox.getText());
        sup.setTaxId(companyTaxBox.getText());
        sup.setDescription(richText.getValue());
        sup.setFirstName(nameBox.getText());
        sup.setLastName(surnameBox.getText());
        AddressDetail address = new AddressDetail();
        address.setCityName(cityBox.getText());
        address.setStreet(streetBox.getText());
        address.setZipCode(zipBox.getText());
        sup.setAddress(address);

        return sup;
    }

}
