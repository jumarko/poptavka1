package com.eprovement.poptavka.client.common.address;

import com.eprovement.poptavka.client.common.ChangeMonitor;
import com.eprovement.poptavka.client.common.address.AddressSelectorPresenter.AddressSelectorInterface;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.ChangeDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;
import java.util.Arrays;
import java.util.List;

public class AddressSelectorView extends Composite
        implements ReverseViewInterface<AddressSelectorPresenter>, AddressSelectorInterface,
        ProvidesValidate, HasChangeHandlers {

    /**************************************************************************/
    /* UIBINDER                                                               */
    /**************************************************************************/
    private static AddressSelectorUiBinder uiBinder = GWT.create(AddressSelectorUiBinder.class);

    interface AddressSelectorUiBinder extends UiBinder<Widget, AddressSelectorView> {
    }

    /**************************************************************************/
    /* PRESENTER                                                              */
    /**************************************************************************/
    @Override
    public void setPresenter(AddressSelectorPresenter presenter) {
        this.addressSelectorPresenter = presenter;
    }

    @Override
    public AddressSelectorPresenter getPresenter() {
        return addressSelectorPresenter;
    }
    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** UiBinder Attributes. **/
    @UiField(provided = true) SuggestBox cityBox;
    @UiField(provided = true) ChangeMonitor cityMonitor, streetMonitor, zipcodeMonitor;
    /** Class attributes. **/
    private List<ChangeMonitor> monitors;
    //Presenter - needed to create CitySuggestOracle
    private AddressSelectorPresenter addressSelectorPresenter;
    //Address attributes - needed to remember suggestBox suggestion which is parsed to these attributes
    private String region;
    private String city;
    private Long cityId;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        cityBox = new SuggestBox(
                new CitySuggestOracle(addressSelectorPresenter),
                new TextBox(),
                new MySuggestDisplay());
        initValidationMonitors();

        initWidget(uiBinder.createAndBindUi(this));

        StyleResource.INSTANCE.layout().ensureInjected();
    }

    private void initValidationMonitors() {
        cityMonitor = new ChangeMonitor<AddressDetail>(
                AddressDetail.class, new ChangeDetail(AddressDetail.AddressField.CITY.getValue()));
        zipcodeMonitor = new ChangeMonitor<AddressDetail>(
                AddressDetail.class, new ChangeDetail(AddressDetail.AddressField.ZIP_CODE.getValue()));
        streetMonitor = new ChangeMonitor<AddressDetail>(
                AddressDetail.class, new ChangeDetail(AddressDetail.AddressField.STREET.getValue()));
        monitors = Arrays.asList(cityMonitor, zipcodeMonitor, streetMonitor);
    }

    /**************************************************************************/
    /* HAS CHANGE HANDLERS                                                    */
    /**************************************************************************/
    @Override
    public HandlerRegistration addChangeHandler(ChangeHandler handler) {
        cityMonitor.addChangeHandler(handler);
        zipcodeMonitor.addChangeHandler(handler);
        return streetMonitor.addChangeHandler(handler);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        // nothing by default
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    @Override
    public void setState(String state) {
        this.region = state;
    }

    @Override
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    @Override
    public void setChangeHandler(ChangeHandler handler) {
        for (ChangeMonitor monitor : monitors) {
            monitor.addChangeHandler(handler);
        }
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    @Override
    public AddressDetail createAddress() {
        AddressDetail address = new AddressDetail();
        address.setCountry(Constants.COUNTRY);
        address.setRegion(region);
        address.setCity(city);
        address.setCityId(cityId);
        address.setDistrict(Constants.DISTRICT);
        address.setStreet((String) streetMonitor.getValue());
        address.setZipCode((String) zipcodeMonitor.getValue());

        return address;
    }

    //SuggestBoxes
    //--------------------------------------------------------------------------
    @Override
    public SuggestBox getCitySuggestBox() {
        return cityBox;
    }

    @Override
    public ChangeMonitor getCityMonitor() {
        return cityMonitor;
    }

    @Override
    public ChangeMonitor getStreetMonitor() {
        return streetMonitor;
    }

    @Override
    public ChangeMonitor getZipcodeMonitor() {
        return zipcodeMonitor;
    }

    //Others
    //--------------------------------------------------------------------------
    @Override
    public boolean isValid() {
        return cityMonitor.isValid() && zipcodeMonitor.isValid() && streetMonitor.isValid();
    }

    @Override
    public boolean isAddressChanged() {
        return cityMonitor.isModified()
                || zipcodeMonitor.isModified()
                || streetMonitor.isModified();
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
