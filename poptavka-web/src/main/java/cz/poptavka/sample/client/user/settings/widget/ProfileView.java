package cz.poptavka.sample.client.user.settings.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ProfileView extends Composite {

    private static ProfileViewUiBinder uiBinder = GWT
            .create(ProfileViewUiBinder.class);

    interface ProfileViewUiBinder extends UiBinder<Widget, ProfileView> {
    }

    public ProfileView() {
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
    @UiField
    TextArea servicesBox;
    @UiField
    CheckBox newMessageButton;
    @UiField
    ListBox newMessageOptions = new ListBox();
    @UiField
    CheckBox newDemandButton;
    @UiField
    ListBox newDemandOptions = new ListBox();
    @UiField
    CheckBox newSystemMessageButton;
    @UiField
    ListBox newSystemMessageOptions = new ListBox();
    @UiField
    CheckBox newOperatorMessageButton;
    @UiField
    ListBox newOperatorMessageOptions = new ListBox();
    @UiField
    CheckBox demandStateChangeButton;
    @UiField
    ListBox demandStateChangeOptions = new ListBox();

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

    public TextArea getServicesBox() {
        return servicesBox;
    }

    public CheckBox getNewMessageButton() {
        return newMessageButton;
    }

    public ListBox getNewMessageOptions() {
        return newMessageOptions;
    }

    public CheckBox getNewDemandButton() {
        return newDemandButton;
    }

    public ListBox getNewDemandOptions() {
        return newDemandOptions;
    }

    public CheckBox getNewSystemMessageButton() {
        return newSystemMessageButton;
    }

    public ListBox getNewSystemMessageOptions() {
        return newSystemMessageOptions;
    }

    public CheckBox getNewOperatorMessageButton() {
        return newOperatorMessageButton;
    }

    public ListBox getNewOperatorMessageOptions() {
        return newOperatorMessageOptions;
    }

    public CheckBox getDemandStateChangeButton() {
        return demandStateChangeButton;
    }

    public ListBox getDemandStateChangeOptions() {
        return demandStateChangeOptions;
    }

}
