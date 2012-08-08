package com.eprovement.poptavka.client.user.widget.detail;

import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class SupplierDetailView extends Composite {

    private static SupplierDetailViewUiBinder uiBinder = GWT
            .create(SupplierDetailViewUiBinder.class);

    interface SupplierDetailViewUiBinder extends
            UiBinder<Widget, SupplierDetailView> {
    }
    @UiField
    Label overallRating, description, localities, categories, email, companyName,
    taxId, identificationNumber, firstName, lastName, phone, website, street,
    city, zipCode, websiteContactPerson, certified;
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

    public void setSupplierDetail(BusinessUserDetail userDetail) {
        detail.setVisible(true);

        description.setText(userDetail.getSupplier().getDescription());
//    verification = userDetail.get
        StringBuilder localitiesBuilder = new StringBuilder();
        for (LocalityDetail s : userDetail.getSupplier().getLocalities()) {
            localitiesBuilder.append(s.getName());
            localitiesBuilder.append("\n");
        }
        localities.setText(localitiesBuilder.toString());
        StringBuilder categoriesBuilder = new StringBuilder();
        for (CategoryDetail s : userDetail.getSupplier().getCategories()) {
            categoriesBuilder.append(s.getName());
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
        city.setText(userDetail.getAddresses().get(0).getCity());
        zipCode.setText(userDetail.getAddresses().get(0).getZipCode());
        websiteContactPerson.setText(userDetail.getWebsite());
        taxId.setText(userDetail.getTaxId());

    }

    public void setSupplierDetail(FullSupplierDetail detail) {
//        detail.setVisible(true);
        if (detail.getOverallRating() == -1) {
            overallRating.setText("");
        } else {
            overallRating.setText(Integer.toString(detail.getOverallRating()));
        }
        certified.setText(Boolean.toString(detail.isCertified()));
        description.setText(detail.getDescription());
//    verification = userDetail.get
        StringBuilder localitiesBuilder = new StringBuilder();
        for (LocalityDetail loc : detail.getLocalities()) {
            localitiesBuilder.append(loc.getName());
            localitiesBuilder.append("\n");
        }
        localities.setText(localitiesBuilder.toString());
        StringBuilder categoriesBuilder = new StringBuilder();
        for (CategoryDetail cat : detail.getCategories()) {
            categoriesBuilder.append(cat.getName());
            categoriesBuilder.append("\n");
        }
        categories.setText(categoriesBuilder.toString());
//    services = userDetail.getSupplier().
//    bsuRoles = userDetail.getSupplier().

//    businessType = userDetail.get
        email.setText(detail.getEmail());
        companyName.setText(detail.getCompanyName());
        identificationNumber.setText(detail.getIdentificationNumber());
        firstName.setText(detail.getFirstName());
        lastName.setText(detail.getLastName());
        phone.setText(detail.getPhone());
        website.setText(detail.getWebsite());
        //TODO Martin - ako zobrazit tie adresy ked ich je viac?
        street.setText(detail.getAddresses().get(0).getStreet());
        city.setText(detail.getAddresses().get(0).getCity());
        zipCode.setText(detail.getAddresses().get(0).getZipCode());
//        websiteContactPerson.setText(detail.getWebsite());
        taxId.setText(detail.getTaxId());

    }

    public void toggleVisible() {
        if (detail.isVisible()) {
            detail.getElement().getStyle().setDisplay(Style.Display.NONE);
        } else {
            detail.getElement().getStyle().setDisplay(Style.Display.BLOCK);
        }
    }
}