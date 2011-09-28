package cz.poptavka.sample.client.user.demands.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.shared.domain.UserDetail;

public class SupplierDetailView extends Composite {

    private static SupplierDetailViewUiBinder uiBinder = GWT
            .create(SupplierDetailViewUiBinder.class);

    interface SupplierDetailViewUiBinder extends
            UiBinder<Widget, SupplierDetailView> {
    }

    @UiField
    Label overallRating, certified, description, verification, localities,
    categories, services, bsuRoles, addresses, businessType, email, companyName,
    identificationNumber, firstName, lastName, phone;
    @UiField
    HTMLPanel detail;

    public SupplierDetailView() {
        initWidget(uiBinder.createAndBindUi(this));
    }


    public SupplierDetailView(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    public Label getOverallRating() {
        return overallRating;
    }

    public Label getCertified() {
        return certified;
    }

    public Label getDescription() {
        return description;
    }

    public Label getVerification() {
        return verification;
    }

    public Label getLocalities() {
        return localities;
    }

    public Label getCategories() {
        return categories;
    }

    public Label getServices() {
        return services;
    }

    public Label getBsuRoles() {
        return bsuRoles;
    }

    public Label getAddresses() {
        return addresses;
    }

    public Label getBusinessType() {
        return businessType;
    }

    public Label getEmail() {
        return email;
    }

    public Label getCompanyName() {
        return companyName;
    }

    public Label getIdentificationNumber() {
        return identificationNumber;
    }

    public Label getFirstName() {
        return firstName;
    }

    public Label getLastName() {
        return lastName;
    }

    public Label getPhone() {
        return phone;
    }

    public HTMLPanel getDetail() {
        return detail;
    }


    public void displaySuppliersDetail(UserDetail userDetail) {
        detail.setVisible(true);

        overallRating.setText(Integer.toString(userDetail.getSupplier().getOverallRating()));
        certified.setText(Boolean.toString(userDetail.getSupplier().isCertified()));
        description.setText(userDetail.getSupplier().getDescription());
//    verification = userDetail.get
        localities.setText(userDetail.getSupplier().getLocalities().toString());
        categories.setText(userDetail.getSupplier().getCategories().toString());
//    services = userDetail.getSupplier().
//    bsuRoles = userDetail.getSupplier().
        addresses.setText(userDetail.getAddress().toString());
//    businessType = userDetail.get
        email.setText(userDetail.getEmail());
        companyName.setText(userDetail.getCompanyName());
        identificationNumber.setText(userDetail.getIdentifiacationNumber());
        firstName.setText(userDetail.getFirstName());
        lastName.setText(userDetail.getLastName());
        phone.setText(userDetail.getPhone());

    }

}
