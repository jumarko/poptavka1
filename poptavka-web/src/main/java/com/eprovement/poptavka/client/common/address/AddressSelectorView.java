package com.eprovement.poptavka.client.common.address;

import com.eprovement.poptavka.client.common.address.AddressSelectorPresenter.AddressSelectorInterface;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.validation.client.Validation;
import com.mvp4g.client.view.ReverseViewInterface;
import java.util.HashSet;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

public class AddressSelectorView extends Composite
        implements ReverseViewInterface<AddressSelectorPresenter>, AddressSelectorInterface,
        ProvidesValidate, Editor<AddressDetail> {

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
    /** UiBinder Attributes. **/
    @UiField(provided = true)
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
    Label cityErrorLabel, streetErrorLabel, zipErrorLabel;
    /** Class attributes. **/
    //Presenter
    private AddressSelectorPresenter addressSelectorPresenter;
    //Address
    private String region;
    private String cityTmp;

    interface Driver extends SimpleBeanEditorDriver<AddressDetail, AddressSelectorView> {
    }
    private AddressSelectorView.Driver driver = GWT.create(AddressSelectorView.Driver.class);
    private Validator validator = null;
    private AddressDetail addressDetail = new AddressDetail();
    private Set<String> valid = new HashSet<String>();
    //
    public final static String COUNTRY = "country";
    public final static String REGION = "region";
    public final static String CITY = "city";
    public final static String DISTRICT = "district";
    public final static String STREET = "street";
    public final static String ZIP_CODE = "zipCode";

    @Override
    public void createView() {
        city = new SuggestBox(
                new CitySuggestOracle(addressSelectorPresenter),
                new TextBox(),
                new MySuggestDisplay());

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        initWidget(uiBinder.createAndBindUi(this));
        this.driver.initialize(this);
        this.driver.edit(addressDetail);

        StyleResource.INSTANCE.layout().ensureInjected();
    }

    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/
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

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    @Override
    public void setState(String state) {
        this.region = state;
    }

    @Override
    public void setCity(String city) {
        this.cityTmp = city;
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    @Override
    public AddressDetail createAddress() {
        AddressDetail address = new AddressDetail();
        address.setCountry(getCountry());
        address.setRegion(getRegion());
        address.setCity(getCity());
        address.setDistrict(getDistrict());
        address.setStreet(getStreet());
        address.setZipCode(getZipCode());

        return address;
    }

    //SuggestBoxes
    //--------------------------------------------------------------------------
    @Override
    @Ignore
    public SuggestBox getCitySuggestBox() {
        return city;
    }

    //Labels
    //--------------------------------------------------------------------------
    @Override
    @Ignore
    public Label getCityErrorLabel() {
        return cityErrorLabel;
    }

    //Strings for validation
    //--------------------------------------------------------------------------
    public String getCountry() {
        return "United States";
    }

    public String getRegion() {
        return region;
    }

    public String getCity() {
        return cityTmp;
    }

    public String getDistrict() {
        return "";
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
        validateZipCode(null);
        validateStreet(null);
        return valid.isEmpty();
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    /**************************************************************************/
    /* Helper Methods                                                         */
    /**************************************************************************/
    //Validation
    //--------------------------------------------------------------------------
    private void displayPropertyError(String item, Set<ConstraintViolation<AddressDetail>> violations) {
        for (ConstraintViolation<AddressDetail> violation : violations) {
            setError(item, ERROR_STYLE, violation.getMessage());
            valid.add(item);
        }
        setError(item, NORMAL_STYLE, "");
        valid.remove(item);
    }

    public void setError(String itemPath, String style, String errorMessage) {
        if (itemPath.equals(CITY)) {
            this.city.setStyleName(style);
            this.cityErrorLabel.setText(errorMessage);
        } else if (itemPath.equals(STREET)) {
            this.street.setStyleName(style);
            this.streetErrorLabel.setText(errorMessage);
        } else if (itemPath.equals(ZIP_CODE)) {
            this.zipCode.setStyleName(style);
            this.zipErrorLabel.setText(errorMessage);
        }
    }

    @Override
    public void eraseAddressBoxes() {
        zipCode.setText("");
        street.setText("");
    }
}
