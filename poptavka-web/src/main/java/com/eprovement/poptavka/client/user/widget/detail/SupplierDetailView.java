package com.eprovement.poptavka.client.user.widget.detail;

import com.eprovement.poptavka.client.common.category.CategoryCell;
import com.eprovement.poptavka.client.common.locality.LocalityCell;
import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.FullClientDetail;
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
    private static final String EMPTY = "";
    @UiField(provided = true)
    CellList categories, localities;
    @UiField
    Label overallRating, description, email, companyName, taxId, identificationNumber,
    firstName, lastName, phone, website, street, city, zipCode, certified, businessType;

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

    public void setSupplierDetail(FullClientDetail detail) {
        if (detail.getUserData().getOverallRating() == -1) {
            overallRating.setText("Not ranked");
        } else {
            overallRating.setText(Integer.toString(detail.getUserData().getOverallRating()));
        }
        if (detail.getUserData().getBusinessType() != null) {
            businessType.setText(detail.getUserData().getBusinessType().getValue());
        }
        description.setText(detail.getUserData().getDescription());
        email.setText(detail.getUserData().getEmail());
        //Address
        street.setText(detail.getUserData().getAddresses().get(0).getStreet());
        city.setText(detail.getUserData().getAddresses().get(0).getCity());
        zipCode.setText(detail.getUserData().getAddresses().get(0).getZipCode());
        //BusinessUserData
        companyName.setText(detail.getUserData().getCompanyName());
        identificationNumber.setText(detail.getUserData().getIdentificationNumber());
        firstName.setText(detail.getUserData().getFirstName());
        lastName.setText(detail.getUserData().getLastName());
        phone.setText(detail.getUserData().getPhone());
        website.setText(detail.getUserData().getWebsite());
        taxId.setText(detail.getUserData().getTaxId());
    }

    public void setSupplierDetail(FullSupplierDetail detail) {
        if (detail.getUserData().getOverallRating() == -1) {
            //TODO i18n
            overallRating.setText("Not ranked");
        } else {
            overallRating.setText(Integer.toString(detail.getUserData().getOverallRating()));
        }
        if (detail.getUserData().getBusinessType() != null) {
            businessType.setText(detail.getUserData().getBusinessType().getValue());
        }
        certified.setText(Boolean.toString(detail.isCertified()));
        description.setText(detail.getUserData().getDescription());
        localities.setRowData(detail.getLocalities());
        categories.setRowData(detail.getCategories());
        email.setText(detail.getUserData().getEmail());
        //Address
        street.setText(detail.getUserData().getAddresses().get(0).getStreet());
        city.setText(detail.getUserData().getAddresses().get(0).getCity());
        zipCode.setText(detail.getUserData().getAddresses().get(0).getZipCode());
        //BusinessUserData
        companyName.setText(detail.getUserData().getCompanyName());
        identificationNumber.setText(detail.getUserData().getIdentificationNumber());
        firstName.setText(detail.getUserData().getFirstName());
        lastName.setText(detail.getUserData().getLastName());
        phone.setText(detail.getUserData().getPhone());
        website.setText(detail.getUserData().getWebsite());
        taxId.setText(detail.getUserData().getTaxId());
    }

    public void clear() {
        description.setText(EMPTY);
        localities.setRowCount(0);
        categories.setRowCount(0);
        email.setText(EMPTY);
        companyName.setText(EMPTY);
        identificationNumber.setText(EMPTY);
        firstName.setText(EMPTY);
        lastName.setText(EMPTY);
        phone.setText(EMPTY);
        website.setText(EMPTY);
        street.setText(EMPTY);
        city.setText(EMPTY);
        zipCode.setText(EMPTY);
        taxId.setText(EMPTY);
    }
}