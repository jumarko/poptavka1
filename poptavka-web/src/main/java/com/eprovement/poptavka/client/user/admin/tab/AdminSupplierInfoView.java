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
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.domain.enums.BusinessType;
import com.eprovement.poptavka.domain.enums.Verification;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.google.gwt.user.client.ui.PopupPanel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ivan.vlcek, jarko, Martin Slavkovsky
 */
public class AdminSupplierInfoView extends Composite {

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
    ListBox categoryList, localityList, businessType, verification;
    // Supplier detail button fields
    @UiField
    Button editCatBtn, editLocBtn, createButton, updateButton;
    private FullSupplierDetail supplierInfo;
    private ArrayList<CategoryDetail> categories;
    private ArrayList<LocalityDetail> localities;
    private PopupPanel selectorWidgetPopup;

    public PopupPanel getSelectorWidgetPopup() {
        return selectorWidgetPopup;
    }

    public Widget getWidgetView() {
        return this;
    }

    public Button getUpdateBtn() {
        return updateButton;
    }

    public Button getEditCatBtn() {
        return editCatBtn;
    }

    public Button getEditLocBtn() {
        return editLocBtn;
    }

    public ListBox getCategoryList() {
        return categoryList;
    }

    public ListBox getLocalityList() {
        return localityList;
    }

    public AdminSupplierInfoView() {
        initWidget(uiBinder.createAndBindUi(this));
        createSelectorWidgetPopup();
    }

    private void createSelectorWidgetPopup() {
        selectorWidgetPopup = new PopupPanel(true);
        selectorWidgetPopup.setSize("300px", "300px");
        selectorWidgetPopup.setGlassEnabled(true);
        selectorWidgetPopup.hide();
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
        supplierInfo.setCategories(categories);
        supplierInfo.setLocalities(localities);

        return supplierInfo;
    }

    public ArrayList<CategoryDetail> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDetail> categories) {
        this.categories = new ArrayList<CategoryDetail>(categories);
        setCategoryBox(categories);
    }

    public void setCategoryBox(List<CategoryDetail> categoriesList) {
        categoryList.clear();
        if (categoriesList != null) {
            for (CategoryDetail cat : categoriesList) {
                categoryList.addItem(cat.getName());
            }

        }
    }

    public ArrayList<LocalityDetail> getLocalities() {
        return localities;
    }

    public void setLocalities(List<LocalityDetail> localities) {
        this.localities = new ArrayList<LocalityDetail>(localities);
        setLocalityBox(localities);
    }

    public void setLocalityBox(List<LocalityDetail> localitiesList) {
        localityList.clear();
        if (localitiesList != null) {
            for (LocalityDetail loc : localitiesList) {
                localityList.addItem(loc.getName());
            }
        }
    }

    public FullSupplierDetail getSupplierDetail() {
        return supplierInfo;
    }

    public void setSupplierDetail(FullSupplierDetail supplier) {
        if (supplier != null) {
            this.supplierInfo = supplier;
            this.categories = supplier.getCategories();
            this.localities = supplier.getLocalities();

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

                setCategoryBox(supplier.getCategories());
                setLocalityBox(supplier.getLocalities());
            }
        }
    }
}
