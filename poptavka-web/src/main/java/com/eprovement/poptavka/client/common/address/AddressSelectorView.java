package com.eprovement.poptavka.client.common.address;

import com.eprovement.poptavka.client.common.address.AddressSelectorPresenter.AddressSelectorInterface;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.i18n.client.ValidationMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class AddressSelectorView extends Composite
        implements ReverseViewInterface<AddressSelectorPresenter>, AddressSelectorInterface,
        ProvidesValidate {

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
    /** CONSTANTS. **/
    private final static String NORMAL_STYLE = StyleResource.INSTANCE.common().emptyStyle();
    private final static String ERROR_STYLE = StyleResource.INSTANCE.common().errorField();
    private final static ValidationMessages MSGSV = GWT.create(ValidationMessages.class);
    //
    private static final int ZIP_SIZE = 4;
    //
    public final static int COUNTRY = 0;
    public final static int REGION = 1;
    public final static int DISTRICT = 2;
    public final static int CITY = 3;
    public final static int ZIP_CODE = 4;
    public final static int STREET = 5;
    /** UiBinder Attributes. **/
    @UiField(provided = true)
    SuggestBox cityBox;
    @UiField
    TextBox street, zipCode;
    @UiField
    Label cityErrorLabel, streetErrorLabel, zipErrorLabel;
    /** Class attributes. **/
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

        initWidget(uiBinder.createAndBindUi(this));

        StyleResource.INSTANCE.layout().ensureInjected();
    }

    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/
    @UiHandler("street")
    public void validateStreet(BlurEvent e) {
        isStreetValid();
    }

    @UiHandler("zipCode")
    public void validateZipCode(BlurEvent e) {
        isZipCodeValid();
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
    public void eraseAddressBoxes() {
        zipCode.setText("");
        street.setText("");
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    @Override
    public AddressDetail createAddress() {
        AddressDetail address = new AddressDetail();
        address.setCountry("United States");
        address.setRegion(region);
        address.setCity(city);
        address.setCityId(cityId);
        address.setDistrict("");
        address.setStreet(street.getText());
        address.setZipCode(zipCode.getText());

        return address;
    }

    //SuggestBoxes
    //--------------------------------------------------------------------------
    @Override
    public SuggestBox getCitySuggestBox() {
        return cityBox;
    }

    @Override
    public TextBox getStreetTextBox() {
        return street;
    }

    @Override
    public TextBox getZipCodeTextBox() {
        return zipCode;
    }

    //Labels
    //--------------------------------------------------------------------------
    @Override
    public Label getCityErrorLabel() {
        return cityErrorLabel;
    }

    //Others
    //--------------------------------------------------------------------------
    @Override
    public boolean isValid() {
        boolean valid = isCityValid();
        valid = isStreetValid() && valid;
        valid = isZipCodeValid() && valid;
        return valid;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    /**************************************************************************/
    /* Helper Methods                                                         */
    /**************************************************************************/
    public void setError(int item, String style, String errorMessage) {
        switch (item) {
            case CITY:
                this.cityBox.setStyleName(style);
                this.cityErrorLabel.setText(errorMessage);
                break;
            case ZIP_CODE:
                this.zipCode.setStyleName(style);
                this.zipErrorLabel.setText(errorMessage);
                break;
            case STREET:
                this.street.setStyleName(style);
                this.streetErrorLabel.setText(errorMessage);
                break;
            default:
                break;
        }
    }

    private boolean isCityValid() {
        if (cityBox.getTextBox().getText().isEmpty()) {
            setError(CITY, ERROR_STYLE, MSGSV.addressNotBlankCity());
            return false;
        }
        setError(CITY, NORMAL_STYLE, "");
        return true;
    }

    private boolean isStreetValid() {
        if (street.getText().isEmpty()) {
            setError(STREET, ERROR_STYLE, MSGSV.addressNotBlankStreet());
            return false;
        }
        setError(STREET, NORMAL_STYLE, "");
        return true;
    }

    private boolean isZipCodeValid() {
        if (zipCode.getText().isEmpty()) {
            setError(ZIP_CODE, ERROR_STYLE, MSGSV.addressNotBlankZipCode());
            return false;
        }
        if (!zipCode.getText().matches("[0-9]+")) {
            setError(ZIP_CODE, ERROR_STYLE, MSGSV.addressPatternZipCode());
            return false;
        }
        if (zipCode.getText().length() < ZIP_SIZE) {
            setError(ZIP_CODE, ERROR_STYLE, MSGSV.addressSizeZipCode());
            return false;
        }
        setError(ZIP_CODE, NORMAL_STYLE, "");
        return true;
    }
}
