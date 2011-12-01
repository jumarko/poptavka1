package cz.poptavka.sample.client.homesettings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class HomeSettingsView extends Composite implements
        HomeSettingsPresenter.HomeSettingsViewInterface {

    private static HomeSettingsViewUiBinder uiBinder = GWT
            .create(HomeSettingsViewUiBinder.class);

    interface HomeSettingsViewUiBinder extends
            UiBinder<Widget, HomeSettingsView> {
    }

    public HomeSettingsView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiField
    TextBox companyName;
    @UiField
    TextBox clientRating;
    @UiField
    TextBox supplierRating;
    @UiField
    TextBox street;
    @UiField
    TextBox city;
    @UiField
    TextBox zipCode;
    @UiField
    TextArea categoriesBox;
    @UiField
    TextArea localitiesBox;
    @UiField
    TextBox web;
    @UiField
    TextBox email;
    @UiField
    TextBox phone;
    @UiField
    TextBox firstName;
    @UiField
    TextBox lastName;
    @UiField
    TextBox identificationNumber;
    @UiField
    TextBox taxNumber;
    @UiField
    TextArea descriptionBox;

    @Override
    public Widget getWidgetView() {
        return this;
    }

    public TextBox getCompanyName() {
        return companyName;
    }

    public TextBox getClientRating() {
        return clientRating;
    }

    public TextBox getSupplierRating() {
        return supplierRating;
    }

    public TextBox getStreet() {
        return street;
    }

    public TextBox getCity() {
        return city;
    }

    public TextBox getZipCode() {
        return zipCode;
    }

    public TextArea getCategoriesBox() {
        return categoriesBox;
    }

    public TextArea getLocalitiesBox() {
        return localitiesBox;
    }

    public TextBox getWeb() {
        return web;
    }

    public TextBox getEmail() {
        return email;
    }

    public TextBox getPhone() {
        return phone;
    }

    public TextBox getFirstName() {
        return firstName;
    }

    public TextBox getLastName() {
        return lastName;
    }

    public TextBox getIdentificationNumber() {
        return identificationNumber;
    }

    public TextBox getTaxNumber() {
        return taxNumber;
    }

    public TextArea getDescriptionBox() {
        return descriptionBox;
    }

}
