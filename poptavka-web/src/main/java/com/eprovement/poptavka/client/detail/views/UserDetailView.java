/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.detail.views;

import com.eprovement.poptavka.client.catLocSelector.others.CatLogSimpleCell;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.FullClientDetail;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.FluidRow;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * User detail view for displaying data from FullClient or FullSupplier details.
 * @author Martin Slavkovsky
 */
public class UserDetailView extends Composite {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static UserDetailViewUiBinder uiBinder = GWT.create(UserDetailViewUiBinder.class);

    interface UserDetailViewUiBinder extends UiBinder<Widget, UserDetailView> {
    }

    /**************************************************************************/
    /* CSS                                                                    */
    /**************************************************************************/
    static {
        StyleResource.INSTANCE.details().ensureInjected();
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) CellList categories, localities;
    @UiField HTMLPanel categoryPanel, localityPanel;
    @UiField FluidRow invoiceRow, addressBlock;
    @UiField Column businessTypeColumn, certifiedColumn, phoneColumn, emailColumn;
    @UiField Label overalRating, description, email, companyName, taxId, identificationNumber,
    firstName, lastName, phone, website, street, city, zipCode, certified, businessType;
    /** Constants. **/
    private static final String EMPTY = "";

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates user detail view's components.
     */
    public UserDetailView() {
        categories = new CellList<ICatLocDetail>(new CatLogSimpleCell());
        localities = new CellList<ICatLocDetail>(new CatLogSimpleCell());
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    /**
     * Sets client detail data.
     * @param detail the FullClientDetail object
     */
    public void setClientDetail(FullClientDetail detail) {
        if (detail.getOveralRating() == 0 || detail.getOveralRating() == null) {
            overalRating.setText(Storage.MSGS.commonNotRanked());
        } else {
            overalRating.setText(detail.getOveralRating().toString());
        }
        if (detail.getUserData().getBusinessType() != null) {
            businessType.setText(detail.getUserData().getBusinessType().getValue());
        }
        categoryPanel.setVisible(false);
        localityPanel.setVisible(false);
        description.setText(detail.getUserData().getDescription());
        email.setText(detail.getUserData().getEmail());
        //Address
        street.setText(detail.getUserData().getAddresses().get(0).getStreet());
        city.setText(detail.getUserData().getAddresses().get(0).getCity());
        zipCode.setText(detail.getUserData().getAddresses().get(0).getZipCode());
        //BusinessUserData
        companyName.setText(detail.getUserData().getCompanyName());
        identificationNumber.setText(detail.getUserData().getIdentificationNumber());
        firstName.setText(detail.getUserData().getPersonFirstName());
        lastName.setText(detail.getUserData().getPersonLastName());
        phone.setText(detail.getUserData().getPhone());
        website.setText(detail.getUserData().getWebsite());
        taxId.setText(detail.getUserData().getTaxId());
    }

    /**
     * Sets supplier detail data.
     * @param detail the FullSupplierDetail object
     */
    public void setSupplierDetail(FullSupplierDetail detail) {
        if (detail.getOveralRating() == 0 || detail.getOveralRating() == null) {
            overalRating.setText(Storage.MSGS.commonNotRanked());
        } else {
            overalRating.setText(detail.getOveralRating().toString());
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
        firstName.setText(detail.getUserData().getPersonFirstName());
        lastName.setText(detail.getUserData().getPersonLastName());
        phone.setText(detail.getUserData().getPhone());
        website.setText(detail.getUserData().getWebsite());
        taxId.setText(detail.getUserData().getTaxId());
    }

    /**
     * Sets advanced items visibility according to given boolean value.
     * Advanced items: Address Info, Contact Info, Invoice Info, ...
     * In general, adnvaced items are meant for admin.
     *
     * @param advancedView true to display advanced items, false otherwise.
     */
    public void setAdvancedVisibility(boolean advancedView) {
        addressBlock.setVisible(advancedView);
        businessTypeColumn.setVisible(advancedView);;
        certifiedColumn.setVisible(advancedView);
        phoneColumn.setVisible(advancedView);
        emailColumn.setVisible(advancedView);
        invoiceRow.setVisible(advancedView);
    }

    /**
     * Clears view's components data.
     */
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