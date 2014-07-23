/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.addressSelector;

import com.eprovement.poptavka.client.addressSelector.others.AddressSelectorSuggestDisplay;
import com.eprovement.poptavka.client.addressSelector.others.AddressSelectorSuggestOracle;
import com.eprovement.poptavka.client.addressSelector.AddressSelectorPresenter.AddressSelectorInterface;
import com.eprovement.poptavka.client.common.monitors.ValidationMonitor;
import com.eprovement.poptavka.client.common.session.CssInjector;
import com.eprovement.poptavka.client.common.ReverseCompositeView;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.AddressDetail.AddressField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * Address selector view consists of city suggest box, street and zipcode textboxes.
 *
 * @author Martin Slavkovsky
 */
public class AddressSelectorView extends ReverseCompositeView<AddressSelectorPresenter>
        implements AddressSelectorInterface {

    /**************************************************************************/
    /* UIBINDER                                                               */
    /**************************************************************************/
    private static AddressSelectorUiBinder uiBinder = GWT.create(AddressSelectorUiBinder.class);

    interface AddressSelectorUiBinder extends UiBinder<Widget, AddressSelectorView> {
    }

    /**************************************************************************/
    /* CSS                                                                    */
    /**************************************************************************/
    static {
        CssInjector.INSTANCE.ensureCommonStylesInjected();
    }

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** UiBinder Attributes. **/
    @UiField(provided = true) SuggestBox cityBox;
    @UiField(provided = true) ValidationMonitor cityMonitor, streetMonitor, zipcodeMonitor;
    @UiField TextBox streetMonitorBox, zipcodeMonitorBox;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    /**
     * Creates AddressSelector view's compontents.
     */
    @Override
    public void createView() {
        cityBox = new SuggestBox(
                new AddressSelectorSuggestOracle(presenter),
                new TextBox(),
                new AddressSelectorSuggestDisplay());
        initValidationMonitors();

        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Inits validation monitors.
     */
    private void initValidationMonitors() {
        cityMonitor = createValidationMonitor(AddressField.CITY);
        zipcodeMonitor = createValidationMonitor(AddressField.ZIP_CODE);
        streetMonitor = createValidationMonitor(AddressField.STREET);
    }

    /**
     * Creates validation monitors
     * @param field - validation field
     * @param groups - validation groups
     * @return created validation monitor
     */
    private ValidationMonitor createValidationMonitor(AddressField field) {
        return new ValidationMonitor<AddressDetail>(AddressDetail.class, field.getValue());
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    /**
     * Sets address data for editing.
     * @param address data to be eddited
     */
    @Override
    public void setAddressForEditing(AddressDetail address) {
        this.cityMonitor.setValue(address.getCity() + ", " + address.getRegion());
        this.zipcodeMonitor.setValue(address.getZipCode());
        this.streetMonitor.setValue(address.getStreet());
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    //SuggestBoxes
    //--------------------------------------------------------------------------
    /**
     * @return the city suggest box
     */
    @Override
    public SuggestBox getCitySuggestBox() {
        return cityBox;
    }

    /**
     * @return the city validation monitor
     */
    @Override
    public ValidationMonitor getCityMonitor() {
        return cityMonitor;
    }

    /**
     * @return the street validation monitor
     */
    @Override
    public ValidationMonitor getStreetMonitor() {
        return streetMonitor;
    }

    /**
     * @return the zipcode validation monitor
     */
    @Override
    public ValidationMonitor getZipcodeMonitor() {
        return zipcodeMonitor;
    }

    /**
     * @return the street textbox
     */
    @Override
    public TextBox getStreetMonitorBox() {
        return streetMonitorBox;
    }

    /**
     * @return the zipcode textbox
     */
    @Override
    public TextBox getZipcodeMonitorBox() {
        return zipcodeMonitorBox;
    }

    //Others
    //--------------------------------------------------------------------------
    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        cityMonitor.reset();
        streetMonitor.reset();
        zipcodeMonitor.reset();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        boolean valid = true;
        valid = cityMonitor.isValid() && valid;
        valid = zipcodeMonitor.isValid() && valid;
        valid = streetMonitor.isValid() && valid;
        return valid;
    }
}
