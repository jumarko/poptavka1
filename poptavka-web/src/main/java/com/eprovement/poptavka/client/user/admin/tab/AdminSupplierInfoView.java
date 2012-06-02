/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.admin.tab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.domain.user.BusinessType;
import com.eprovement.poptavka.domain.user.Verification;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;

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
    TextBox companyName, firstName, lastName, email, phone, overalRating, identifNumber, supplierID;
    @UiField
    CheckBox certified;
    @UiField
    ListBox editCatList, editLocList, categoryList, localityList, businessType, verification;
    // Supplier detail button fields
    @UiField
    Button editCatBtn, editLocBtn, createButton, updateButton,
    finnishCatBtn, finnishLocBtn, backCatBtn, backLocBtn,
    rootCatBtn, rootLocBtn;
    @UiField
    VerticalPanel editCatPanel, editLocPanel;
    @UiField
    Label catPath, locPath;
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
    public Button getEditCatBtn() {
        return editCatBtn;
    }

    @Override
    public Button getEditLocBtn() {
        return editLocBtn;
    }

    @Override
    public Button getFinnishCatBtn() {
        return finnishCatBtn;
    }

    @Override
    public Button getFinnishLocBtn() {
        return finnishLocBtn;
    }

    @Override
    public ListBox getEditCatList() {
        return editCatList;
    }

    @Override
    public ListBox getEditLocList() {
        return editLocList;
    }

    @Override
    public Button getBackCatBtn() {
        return backCatBtn;
    }

    @Override
    public Button getBackLocBtn() {
        return backLocBtn;
    }

    @Override
    public Button getRootCatBtn() {
        return rootCatBtn;
    }

    @Override
    public Button getRootLocBtn() {
        return rootLocBtn;
    }

    @Override
    public VerticalPanel getEditCatPanel() {
        return editCatPanel;
    }

    @Override
    public VerticalPanel getEditLocPanel() {
        return editLocPanel;
    }

    @Override
    public ListBox getCategoryList() {
        return categoryList;
    }

    @Override
    public ListBox getLocalityList() {
        return localityList;
    }

    @Override
    public Label getCatPath() {
        return catPath;
    }

    @Override
    public Label getLocPath() {
        return locPath;
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
        supplierInfo.setVerification(verification.getItemText(verification.getSelectedIndex()));
        supplierInfo.setOverallRating(Integer.valueOf(overalRating.getText()));
        supplierInfo.setIdentificationNumber(identifNumber.getText());
        supplierInfo.setBusinessType(businessType.getItemText(businessType.getSelectedIndex()));
        supplierInfo.setCertified(certified.getValue());

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

            //Busines data
            identifNumber.setText(supplier.getIdentificationNumber());
            supplierID.setText(Long.toString(supplier.getSupplierId()));
            // BusinessType settings
            // Add the types to the status box.
            int i = 0;
            int j = 0;
            businessType.clear();
            for (BusinessType type : BusinessType.values()) {
                businessType.addItem(type.getValue());
                if (supplier.getBusinessType() != null
                        && supplier.getBusinessType().equalsIgnoreCase(type.getValue())) {
                    j = i;
                }
                i++;
            }
            businessType.setSelectedIndex(j);

            certified.setValue(supplier.isCertified());
            // Verification.
            i = 0;
            j = 0;
            verification.clear();
            for (Verification type : Verification.values()) {
                verification.addItem(type.name());
                if (supplier.getVerification() != null
                        && supplier.getVerification().equalsIgnoreCase(type.name())) {
                    j = i;
                }
                i++;
            }
            verification.setSelectedIndex(j);

            //Category
            categoryList.clear();
            if (supplier.getCategories() != null) {
                for (String cat : supplier.getCategories().values()) {
                    categoryList.addItem(cat);
                }

            }
            //Locality
            localityList.clear();
            if (supplier.getLocalities() != null) {
                for (String loc : supplier.getLocalities().values()) {
                    localityList.addItem(loc);
                }
            }
        }
    }
}
