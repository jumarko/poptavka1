package com.eprovement.poptavka.client.addressSelector;

import com.eprovement.poptavka.client.addressSelector.others.AddressSelectorSuggestDisplay;
import com.eprovement.poptavka.client.addressSelector.others.AddressSelectorSuggestOracle;
import com.eprovement.poptavka.client.addressSelector.AddressSelectorPresenter.AddressSelectorInterface;
import com.eprovement.poptavka.client.common.monitors.ValidationMonitor;
import com.eprovement.poptavka.client.common.session.CssInjector;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.common.ReverseCompositeView;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.AddressDetail.AddressField;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AddressSelectorView extends ReverseCompositeView<AddressSelectorPresenter>
        implements AddressSelectorInterface, ProvidesValidate {

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
    @Override
    public void createView() {
        cityBox = new SuggestBox(
                new AddressSelectorSuggestOracle(presenter),
                new TextBox(),
                new AddressSelectorSuggestDisplay());
        initValidationMonitors();

        initWidget(uiBinder.createAndBindUi(this));
    }

    private void initValidationMonitors() {
        cityMonitor = createValidationMonitor(AddressField.CITY);
        zipcodeMonitor = createValidationMonitor(AddressField.ZIP_CODE);
        streetMonitor = createValidationMonitor(AddressField.STREET);
    }

    private ValidationMonitor createValidationMonitor(AddressField field) {
        return new ValidationMonitor<AddressDetail>(AddressDetail.class, field.getValue());
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
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
    @Override
    public SuggestBox getCitySuggestBox() {
        return cityBox;
    }

    @Override
    public ValidationMonitor getCityMonitor() {
        return cityMonitor;
    }

    @Override
    public ValidationMonitor getStreetMonitor() {
        return streetMonitor;
    }

    @Override
    public ValidationMonitor getZipcodeMonitor() {
        return zipcodeMonitor;
    }

    @Override
    public TextBox getStreetMonitorBox() {
        return streetMonitorBox;
    }

    @Override
    public TextBox getZipcodeMonitorBox() {
        return zipcodeMonitorBox;
    }

    //Others
    //--------------------------------------------------------------------------
    @Override
    public boolean isValid() {
        boolean valid = true;
        valid = cityMonitor.isValid() && valid;
        valid = zipcodeMonitor.isValid() && valid;
        valid = streetMonitor.isValid() && valid;
        return valid;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
