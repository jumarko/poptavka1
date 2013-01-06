package com.eprovement.poptavka.client.user.widget.detail;

import com.eprovement.poptavka.client.common.category.CategoryCell;
import com.eprovement.poptavka.client.common.locality.LocalityCell;
import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class SupplierDetailView extends Composite {

    private static SupplierDetailViewUiBinder uiBinder = GWT
            .create(SupplierDetailViewUiBinder.class);

    interface SupplierDetailViewUiBinder extends
            UiBinder<Widget, SupplierDetailView> {
    }
    @UiField(provided = true)
    CellList categories, localities;
    @UiField
    Label overallRating, description, email, companyName, taxId, identificationNumber,
    firstName, lastName, phone, website, street, city, zipCode, websiteContactPerson, certified;

    public SupplierDetailView() {
        categories = new CellList<CategoryDetail>(new CategoryCell(CategoryCell.DISPLAY_COUNT_DISABLED));
        localities = new CellList<LocalityDetail>(new LocalityCell(LocalityCell.DISPLAY_COUNT_DISABLED));
        initWidget(uiBinder.createAndBindUi(this));
        StyleResource.INSTANCE.detailViews().ensureInjected();
    }

    public SupplierDetailView(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
        StyleResource.INSTANCE.detailViews().ensureInjected();
    }

    public void setSupplierDetail(BusinessUserDetail userDetail) {
        description.setText(userDetail.getSupplier().getDescription());
        localities.setRowData(userDetail.getSupplier().getLocalities());
        categories.setRowData(userDetail.getSupplier().getCategories());
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
        if (detail.getOverallRating() == -1) {
            overallRating.setText("");
        } else {
            overallRating.setText(Integer.toString(detail.getOverallRating()));
        }
        certified.setText(Boolean.toString(detail.isCertified()));
        description.setText(detail.getDescription());
        localities.setRowData(detail.getLocalities());
        categories.setRowData(detail.getCategories());
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
        websiteContactPerson.setText(detail.getWebsite());
        taxId.setText(detail.getTaxId());

    }
}