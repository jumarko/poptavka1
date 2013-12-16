/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.admin.tab;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.smallPopups.SimpleConfirmPopup;
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
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.view.client.ListDataProvider;
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
    /**************************************************************************/
    /* Attributes                                                               */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField TextArea descriptionBox;
    @UiField TextBox companyName, firstName, lastName, email, phone, overalRating, identifNumber, supplierID;
    @UiField CheckBox certified;
    @UiField ListBox businessType, verification;
    @UiField(provided = true) CellList categoryList, localityList;
    // Supplier detail button fields
    @UiField Button editCatBtn, editLocBtn, createButton, updateButton;
    /** Class attributes. **/
    private ListDataProvider categoryProvider;
    private ListDataProvider localityProvider;
    private SimpleConfirmPopup selectorWidgetPopup;

    /**************************************************************************/
    /* INITIALIZATON                                                          */
    /**************************************************************************/
    public AdminSupplierInfoView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* setters                                                               */
    /**************************************************************************/
    public void updateSupplierDetail(FullSupplierDetail supplierToUpdate) {
        supplierToUpdate.getUserData().setCompanyName(companyName.getText());
        supplierToUpdate.getUserData().setDescription(descriptionBox.getText());
        supplierToUpdate.getUserData().setPersonFirstName(firstName.getText());
        supplierToUpdate.getUserData().setPersonLastName(lastName.getText());
        supplierToUpdate.getUserData().setEmail(email.getText());
        supplierToUpdate.getUserData().setPhone(phone.getText());
        supplierToUpdate.getUserData().setVerification(verification.getItemText(verification.getSelectedIndex()));
        supplierToUpdate.getUserData().setIdentificationNumber(identifNumber.getText());
        supplierToUpdate.getUserData().setBusinessType(businessType.getItemText(businessType.getSelectedIndex()));
        supplierToUpdate.setCertified(certified.getValue());
        supplierToUpdate.setCategories(categoryProvider.getList());
        supplierToUpdate.setLocalities(localityProvider.getList());
        supplierToUpdate.setOveralRating(Integer.valueOf(overalRating.getText()));
    }

    public void setSupplierDetail(FullSupplierDetail supplier) {
        if (supplier != null) {
            this.categoryProvider.setList(supplier.getCategories());
            this.localityProvider.setList(supplier.getLocalities());

            updateButton.setEnabled(supplier != null);
            if (supplier != null) {
                //Company
                companyName.setText(supplier.getUserData().getCompanyName());
                if (supplier.getOveralRating() == null) {
                    overalRating.setText(Storage.MSGS.commonNotRanked());
                } else {
                    overalRating.setText(supplier.getOveralRating().toString());
                }
                descriptionBox.setText(supplier.getUserData().getDescription());
                //Contact
                firstName.setText(supplier.getUserData().getPersonFirstName());
                lastName.setText(supplier.getUserData().getPersonLastName());
                email.setText(supplier.getUserData().getEmail());
                phone.setText(supplier.getUserData().getPhone());

                //Busines data
                identifNumber.setText(supplier.getUserData().getIdentificationNumber());
                supplierID.setText(Long.toString(supplier.getSupplierId()));
                // BusinessType settings
                // Add the types to the status box.
                int i = 0;
                int j = 0;
                businessType.clear();
                for (BusinessType type : BusinessType.values()) {
                    businessType.addItem(type.getValue());
                    if (supplier.getUserData().getBusinessType() != null
                            && supplier.getUserData().getBusinessType().equals(type)) {
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
                    if (supplier.getUserData().getVerification() != null
                            && supplier.getUserData().getVerification().equals(type)) {
                        j = i;
                    }
                    i++;
                }
                verification.setSelectedIndex(j);
            }
        }
    }

    /**************************************************************************/
    /* Getters                                                               */
    /**************************************************************************/
    public SimpleConfirmPopup getSelectorWidgetPopup() {
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

    public List<ICatLocDetail> getCategories() {
        return categoryProvider.getList();
    }

    public List<ICatLocDetail> getLocalities() {
        return localityProvider.getList();
    }
}
