/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
//import cz.poptavka.sample.shared.domain.type.ClientSupplierType;
//import cz.poptavka.sample.shared.domain.type.SupplierStatusType;

/**
 *
 * @author ivan.vlcek, jarko
 */
public class AdminSupplierInfoView extends Composite implements
        AdminSupplierInfoPresenter.AdminSupplierInfoInterface {

    private static AdminsupplierInfoViewUiBinder uiBinder = GWT.create(AdminsupplierInfoViewUiBinder.class);

    interface AdminsupplierInfoViewUiBinder extends
            UiBinder<Widget, AdminSupplierInfoView> {
    }
    // Supplier detail input fields
    @UiField
    TextArea descriptionBox;
    @UiField
    TextBox companyName, firstName, lastName, email, phone, verification,
    overalRating, identifNumber, supplierID, businessType, certified;
    @UiField
    ListBox categories, localities;
    // Supplier detail button fields
    @UiField
    Button categoryButton;
    @UiField
    Button localityButton;
    @UiField
    Button createButton;
    @UiField
    Button updateButton;
    private FullSupplierDetail supplierInfo;

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public Button getUpdateBtn() {
        return updateButton;
    }

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        initsupplierInfoForm();
    }

    public FullSupplierDetail getUpdatedSupplierDetail() {
        if (supplierInfo == null) {
            return null;
        }
        // Update the detail object.
        supplierInfo.setCompanyName(companyName.getText());
        supplierInfo.setDescription(descriptionBox.getText());
        supplierInfo.setFirstName(firstName.getText());
        supplierInfo.setLastName(lastName.getText());
        supplierInfo.setEmail(email.getText());
        supplierInfo.setPhone(phone.getText());
        supplierInfo.setVerification(verification.getText());
        supplierInfo.setOverallRating(Integer.valueOf(overalRating.getText()));
        supplierInfo.setIdentificationNumber(identifNumber.getText());
        supplierInfo.setBusinessType(businessType.getText());
        supplierInfo.setCertified(Boolean.valueOf(businessType.getText()));

        return supplierInfo;
    }

    private void initsupplierInfoForm() {
        // initWidget(uiBinder.createAndBindUi(this));

        // Initialize the contact to null.
        setSupplierDetail(null);
    }

    public void setSupplierDetail(FullSupplierDetail supplier) {
        this.supplierInfo = supplier;
        updateButton.setEnabled(supplier != null);
        if (supplier != null) {
            //Company
            companyName.setText(supplier.getCompanyName());
            overalRating.setText(Integer.toString(supplier.getOverallRating()));
            descriptionBox.setText(supplier.getDescription());
            //Contact
            firstName.setText(supplier.getFirstName());
            lastName.setText(supplier.getLastName());
            email.setText(supplier.getEmail());
            phone.setText(supplier.getPhone());
            //Location
            for (String category : supplier.getCategories().values()) {
                categories.addItem(category);
            }
            for (String locality : supplier.getLocalities().values()) {
                localities.addItem(locality);
            }
            //Busines data
            identifNumber.setText(supplier.getIdentificationNumber());
            supplierID.setText(Long.toString(supplier.getSupplierId()));
            businessType.setText(supplier.getBusinessType());
            certified.setText(Boolean.toString(supplier.isCertified()));
            verification.setText(supplier.getVerification());
        }
    }
}
