package com.eprovement.poptavka.client.common.address;

import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.i18n.client.ValidationMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;

import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.validation.client.Validation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

public class AddressSelectorView extends Composite
        implements AddressSelectorPresenter.AddressSelectorInterface, ProvidesValidate, Editor<AddressDetail> {

    /**************************************************************************/
    /* UIBINDER                                                               */
    /**************************************************************************/
    private static AddressSelectorUiBinder uiBinder = GWT.create(AddressSelectorUiBinder.class);

    interface AddressSelectorUiBinder extends UiBinder<Widget, AddressSelectorView> {
    }
    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** CONSTANTS. **/
    private final static String NORMAL_STYLE = StyleResource.INSTANCE.common().emptyStyle();
    private final static String ERROR_STYLE = StyleResource.INSTANCE.common().errorField();
    /** UiBinder Attributes. **/
//    @UiField ListBox district;
    @UiField(provided = true)
    SuggestBox country, region;
    @UiField
    SuggestBox city;
    //TODO Martin -- ZipCode by sa mohol doplnat sam, ked uz som vybral mesto z ciselnika???
    @UiField
    TextBox street, zipCode;
    /*
     * Ak attribut nie je definovany v interface, staci @Ignore na atribut. Ak ale je
     * definovany v interface, a nechceme ho, musi byt @Ignore v interface aj u get methody
     */
    @UiField
    @Ignore
    Label countryErrorLabel, regionErrorLabel, //districtErrorLabel,
    cityErrorLabel, streetErrorLabel, zipErrorLabel;
    /** Class attributes. **/
    //Validation
    private ValidationMessages messages = GWT.create(ValidationMessages.class);

    interface Driver extends SimpleBeanEditorDriver<AddressDetail, AddressSelectorView> {
    }
    private AddressSelectorView.Driver driver = GWT.create(AddressSelectorView.Driver.class);
    private Validator validator = null;
    private AddressDetail addressDetail = new AddressDetail();
    private Set<String> valid = new HashSet<String>();
    //
    private final static String COUNTRY = "country";
    private final static String REGION = "region";
    private final static String CITY = "city";
    private final static String DISTRICT = "district";
    private final static String STREET = "street";
    private final static String ZIP_CODE = "zipCode";

    @Override
    public void createView() {
        region = new SuggestBox(new LocalityDetailSuggestOracle());
        country = new SuggestBox(new LocalityDetailSuggestOracle());

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        initWidget(uiBinder.createAndBindUi(this));
        this.driver.initialize(this);
        this.driver.edit(addressDetail);

        StyleResource.INSTANCE.layout().ensureInjected();
    }

    @UiHandler("street")
    public void validateStreet(BlurEvent e) {
        AddressDetail detail = driver.flush();
        Set<ConstraintViolation<AddressDetail>> violations = validator.validateValue(
                AddressDetail.class, STREET, detail.getStreet(), Default.class);
        this.displayPropertyError(STREET, violations);
    }

    @UiHandler("zipCode")
    public void validateZipCode(BlurEvent e) {
        AddressDetail detail = driver.flush();
        Set<ConstraintViolation<AddressDetail>> violations = validator.validateValue(
                AddressDetail.class, ZIP_CODE, detail.getZipCode(), Default.class);
        this.displayPropertyError(ZIP_CODE, violations);
    }

    public void validateAddress() {
        AddressDetail detail = driver.flush();
        Set<ConstraintViolation<AddressDetail>> violations = validator.validate(detail, Default.class);
        this.displayErrors(violations);
    }

    @Override
    public AddressDetail createAddress() {
        AddressDetail address = new AddressDetail();
        address.setCountry(country.getText());
        address.setRegion(region.getText());
        address.setCity(city.getText());
//        address.setDistrict(district.getItemText(district.getSelectedIndex()));
        address.setStreet(street.getText());
        address.setZipCode(zipCode.getText());

        return address;
    }

    //SETTERS
    @Override
    public void setSuggestBoxOracleData(LocalityType localityType, List<LocalityDetail> data) {
        switch (localityType) {
            case COUNTRY:
                setOracleData(country, data);
                break;
            case REGION:
                setOracleData(region, data);
                break;
            case CITY:
//                setOracleData(city, data);
                MultiWordSuggestOracle oracle = (MultiWordSuggestOracle) city.getSuggestOracle();
                for (LocalityDetail loc : data) {
                    oracle.add(loc.getName());
                }
                break;
            default:
                break;
        }
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    //SuggestBoxes
    //--------------------------------------------------------------------------
    @Override
    @Ignore
    public SuggestBox getCountrySuggestBox() {
        return country;
    }

    @Override
    @Ignore
    public SuggestBox getRegionSuggestBox() {
        return region;
    }

    @Override
    @Ignore
    public SuggestBox getCitySuggestBox() {
        return city;
    }

//    @Override
//    public ListBox getDistrict() {
//        return null;
//    }
    //Labels
    //--------------------------------------------------------------------------
    @Override
    @Ignore
    public Label getCountryErrorLabel() {
        return countryErrorLabel;
    }

    @Override
    @Ignore
    public Label getRegionErrorLabel() {
        return regionErrorLabel;
    }

    @Override
    @Ignore
    public Label getCityErrorLabel() {
        return cityErrorLabel;
    }

    //Strings for validation
    //--------------------------------------------------------------------------
    public String getCountry() {
        return country.getText();
    }

    public String getRegion() {
        return region.getText();
    }

    public String getCity() {
        return city.getText();
    }

    public String getDistrict() {
        return ""; //district.getItemText(district.getSelectedIndex());
    }

    public String getZipCode() {
        return zipCode.getText();
    }

    public String getStreet() {
        return street.getText();
    }

    //Others
    //--------------------------------------------------------------------------
    @Override
    public boolean isValid() {
        validateAddress();
        //for devel purposes
        valid.remove(DISTRICT);
        return valid.isEmpty();
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    /**************************************************************************/
    /* Helper Methods                                                         */
    /**************************************************************************/
    //SuggestBox
    //--------------------------------------------------------------------------
    private void setOracleData(SuggestBox suggestBox, List<LocalityDetail> data) {
        MultiWordSuggestOracle o;
        LocalityDetailSuggestOracle oracle = (LocalityDetailSuggestOracle) suggestBox.getSuggestOracle();
        for (LocalityDetail loc : data) {
            oracle.add(new LocalityDetailMultiWordSuggestion(loc));
        }
    }

    //Validation
    //--------------------------------------------------------------------------
    private void displayErrors(Set<ConstraintViolation<AddressDetail>> violations) {
        setError2(COUNTRY, NORMAL_STYLE, "");
        setError2(REGION, NORMAL_STYLE, "");
        setError2(CITY, NORMAL_STYLE, "");
        setError2(STREET, NORMAL_STYLE, "");
        setError2(ZIP_CODE, NORMAL_STYLE, "");
        valid.clear();

        for (ConstraintViolation<AddressDetail> violation : violations) {
            setError2(violation.getPropertyPath().toString(), ERROR_STYLE, violation.getMessage());
            valid.add(violation.getPropertyPath().toString());
        }
    }

    private void displayPropertyError(String item, Set<ConstraintViolation<AddressDetail>> violations) {
        for (ConstraintViolation<AddressDetail> violation : violations) {
            setError2(item, ERROR_STYLE, violation.getMessage());
            valid.add(item);
        }
        setError2(item, NORMAL_STYLE, "");
        valid.remove(item);
    }

    public void setError2(String itemPath, String style, String errorMessage) {
        if (itemPath.equals(COUNTRY)) {
            this.country.getTextBox().setStyleName(style);
            this.countryErrorLabel.setText(errorMessage);
        } else if (itemPath.equals(REGION)) {
            this.region.setStyleName(style);
            this.regionErrorLabel.setText(errorMessage);
        } else if (itemPath.equals(CITY)) {
            this.city.setStyleName(style);
            this.cityErrorLabel.setText(errorMessage);
        } else if (itemPath.equals(DISTRICT)) {
//                this.district.setStyleName(style);
//                this.districtErrorLabel.setText(errorMessage);
        } else if (itemPath.equals(STREET)) {
            this.street.setStyleName(style);
            this.streetErrorLabel.setText(errorMessage);
        } else if (itemPath.equals(ZIP_CODE)) {
            this.zipCode.setStyleName(style);
            this.zipErrorLabel.setText(errorMessage);
        }
    }
}
