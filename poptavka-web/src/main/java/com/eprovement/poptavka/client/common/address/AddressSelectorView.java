package com.eprovement.poptavka.client.common.address;

import java.util.HashSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.i18n.client.ValidationMessages;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.validation.client.Validation;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

public class AddressSelectorView extends Composite
        implements AddressSelectorPresenter.AddressSelectorInterface, ProvidesValidate, Editor<AddressDetail> {

    private final static String NORMAL_STYLE = StyleResource.INSTANCE.common().emptyStyle();
    private final static String ERROR_STYLE = StyleResource.INSTANCE.common().errorField();
    private static AddressSelectorUiBinder uiBinder = GWT.create(AddressSelectorUiBinder.class);

    interface AddressSelectorUiBinder extends UiBinder<Widget, AddressSelectorView> {
    }
    @UiField ListBox country, region, district, city;
    //TODO Martin -- ZipCode by sa mohol doplnat sam, ked uz som vybral mesto z ciselnika???
    @UiField TextBox street, zipCode;
    @UiField @Ignore Label countryErrorLabel, regionErrorLabel, districtErrorLabel,
    cityErrorLabel, streetErrorLabel, zipErrorLabel;

    //Validation
    interface Driver extends SimpleBeanEditorDriver<AddressDetail, AddressSelectorView> {
    }
    private AddressSelectorView.Driver driver = GWT.create(AddressSelectorView.Driver.class);
    private Validator validator = null;
    private AddressDetail addressDetail = new AddressDetail();
    private Set<Integer> valid = new HashSet<Integer>();
    //
    private final static int COUNTRY = 0;
    private final static int REGION = 1;
    private final static int CITY = 2;
    private final static int DISTRICT = 3;
    private final static int STREET = 4;
    private final static int ZIP_CODE = 5;

    @Override
    public void createView() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        initWidget(uiBinder.createAndBindUi(this));
        this.driver.initialize(this);
        this.driver.edit(addressDetail);

        StyleResource.INSTANCE.layout().ensureInjected();
    }
    private ValidationMessages messages = GWT.create(ValidationMessages.class);

    @UiHandler("country")
    public void validateCountry(BlurEvent e) {
        displayListBoxError(country, COUNTRY, messages.addressNotBlankCountry());
    }

    @UiHandler("region")
    public void validateRegion(BlurEvent e) {
        displayListBoxError(region, REGION, messages.addressNotBlankRegion());
    }

    @UiHandler("city")
    public void validateCity(BlurEvent e) {
        displayListBoxError(city, CITY, messages.addressNotBlankZipCode());
    }

    @UiHandler("district")
    public void validateDistrict(BlurEvent e) {
        displayListBoxError(district, DISTRICT, messages.addressNotBlankDistrict());
    }

    @UiHandler("street")
    public void validateStreet(BlurEvent e) {
        AddressDetail detail = driver.flush();
        Set<ConstraintViolation<AddressDetail>> violations = validator.validateValue(
                AddressDetail.class, "street", detail.getStreet(), Default.class);
        this.displayErrors(STREET, violations);
    }

    @UiHandler("zipCode")
    public void validateZipCode(BlurEvent e) {
        AddressDetail detail = driver.flush();
        Set<ConstraintViolation<AddressDetail>> violations = validator.validateValue(
                AddressDetail.class, "zipCode", detail.getZipCode(), Default.class);
        this.displayErrors(ZIP_CODE, violations);
    }

    private void displayErrors(int item, Set<ConstraintViolation<AddressDetail>> violations) {
        for (ConstraintViolation<AddressDetail> violation : violations) {
            setError(item, ERROR_STYLE, violation.getMessage());
            valid.add(item);
            return;
        }
        setError(item, NORMAL_STYLE, "");
        valid.remove(item);
    }

    private void displayListBoxError(ListBox listBox, int item, String message) {
        if (listBox.getSelectedIndex() == 0) {
            setError(item, ERROR_STYLE, message);
            valid.add(item);
        } else {
            setError(item, NORMAL_STYLE, "");
            valid.add(item);
        }
    }

    /**
     * Set style and error message to given item.
     *
     * @param item - use class constant CITY, STATE, STREET, ZIP
     * @param style - user class constant NORMAL_STYLE, ERROR_STYLE
     * @param errorMessage - message of item's ErrorLabel
     */
    private void setError(int item, String style, String errorMessage) {
        switch (item) {
            case COUNTRY:
                this.country.setStyleName(style);
                this.countryErrorLabel.setText(errorMessage);
                break;
            case REGION:
                this.region.setStyleName(style);
                this.regionErrorLabel.setText(errorMessage);
                break;
            case CITY:
                this.city.setStyleName(style);
                this.cityErrorLabel.setText(errorMessage);
                break;
            case DISTRICT:
                this.district.setStyleName(style);
                this.districtErrorLabel.setText(errorMessage);
                break;
            case STREET:
                this.street.setStyleName(style);
                this.streetErrorLabel.setText(errorMessage);
                break;
            case ZIP_CODE:
                this.zipCode.setStyleName(style);
                this.zipErrorLabel.setText(errorMessage);
                break;
            default:
                break;
        }
    }

    @Override
    public AddressDetail createAddress() {
        AddressDetail address = new AddressDetail();
        address.setCountry(country.getItemText(country.getSelectedIndex()));
        address.setRegion(region.getItemText(region.getSelectedIndex()));
        address.setCity(city.getItemText(city.getSelectedIndex()));
        address.setDistrict(district.getItemText(district.getSelectedIndex()));
        address.setStreet(street.getText());
        address.setZipCode(zipCode.getText());

        return address;
    }

    // GETTERS
    @Override
    public ListBox getCountry() {
        return country;
    }

    @Override
    public ListBox getRegion() {
        return region;
    }

    @Override
    public ListBox getCity() {
        return city;
    }

    @Override
    public ListBox getDistrict() {
        return district;
    }

    public String getRegionText() {
        return region.getItemText(region.getSelectedIndex());
    }

    public String getCityText() {
        return city.getItemText(region.getSelectedIndex());
    }

    public String getDistrictText() {
        return district.getItemText(region.getSelectedIndex());
    }

    public String getZipCodeText() {
        return zipCode.getText();
    }

    public String getStreetText() {
        return street.getText();
    }

    @Override
    public boolean isValid() {
        validateCity(null);
        validateCountry(null);
        validateDistrict(null);
        validateRegion(null);
        validateStreet(null);
        validateZipCode(null);
        return valid.isEmpty();
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
