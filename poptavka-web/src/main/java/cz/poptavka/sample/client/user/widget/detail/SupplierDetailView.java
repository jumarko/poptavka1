package cz.poptavka.sample.client.user.widget.detail;

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
    Label overallRating, description, localities,
    categories, email, companyName, taxId, identificationNumber, firstName,
    lastName, phone, website, street, city, zipCode, websiteContactPerson;
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

    public Label getDescription() {
        return description;
    }

    public Label getLocalities() {
        return localities;
    }

    public Label getCategories() {
        return categories;
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


    public Label getWebsite() {
        return website;
    }


    public static SupplierDetailViewUiBinder getUiBinder() {
        return uiBinder;
    }


    public Label getStreet() {
        return street;
    }


    public Label getCity() {
        return city;
    }


    public Label getZipCode() {
        return zipCode;
    }


    public Label getWebsiteContactPerson() {
        return websiteContactPerson;
    }


    public Label getTaxId() {
        return taxId;
    }


    public void displaySuppliersDetail(UserDetail userDetail) {
        detail.setVisible(true);

        description.setText(userDetail.getSupplier().getDescription());
//    verification = userDetail.get
        StringBuilder localitiesBuilder = new StringBuilder();
        for (String s : userDetail.getSupplier().getLocalities()) {
            localitiesBuilder.append(s);
            localitiesBuilder.append("\n");
        }
        localities.setText(localitiesBuilder.toString());
        StringBuilder categoriesBuilder = new StringBuilder();
        for (String s : userDetail.getSupplier().getCategories()) {
            categoriesBuilder.append(s);
            categoriesBuilder.append("\n");
        }
        categories.setText(categoriesBuilder.toString());
//    services = userDetail.getSupplier().
//    bsuRoles = userDetail.getSupplier().

//    businessType = userDetail.get
        email.setText(userDetail.getEmail());
        companyName.setText(userDetail.getCompanyName());
        identificationNumber.setText(userDetail.getIdentificationNumber());
        firstName.setText(userDetail.getFirstName());
        lastName.setText(userDetail.getLastName());
        phone.setText(userDetail.getPhone());
        website.setText(userDetail.getWebsite());
        street.setText(userDetail.getAddresses().get(0).getStreet());
        city.setText(userDetail.getAddresses().get(0).getCityName());
        zipCode.setText(userDetail.getAddresses().get(0).getZipCode());
        websiteContactPerson.setText(userDetail.getWebsite());
        taxId.setText(userDetail.getTaxId());

    }
}