package cz.poptavka.sample.client.homeWelcome;

/*
 * Martin - skopyrovany java doc
 *
 * GWT Validation Framework - A JSR-303 validation framework for GWT
 *
 * (c) 2008 gwt-validation contributors
 * (http://code.google.com/p/gwt-validation/) * Licensed to the Apache Software
 * Foundation (ASF) under one or more contributor license agreements. See the
 * NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import cz.poptavka.sample.client.resources.StyleResource;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

public class AddressEditor extends Composite implements Editor<Address> {

    interface AddressEditorUiBinder extends UiBinder<Widget, AddressEditor> {
    }
    private static AddressEditorUiBinder uiBinder = GWT.create(AddressEditorUiBinder.class);

    interface Driver extends SimpleBeanEditorDriver<Address, AddressEditor> {
    }
    private Driver driver = GWT.create(Driver.class);
    private Validator validator = null;
    @UiField
    TextBox street;
    @UiField
    TextBox state;
    @UiField
    TextBox city;
    @UiField
    TextBox zip;
    @UiField
    Button submit;
    @UiField
    Button clear;
    @UiField
    @Ignore
    Label errorLabelState, errorLabelCity, errorLabelStreet, errorLabelZip;
    private Address baseLineAddress = null;
    //Constants
    private final static String NORMAL_STYLE = StyleResource.INSTANCE.common().emptyStyle();
    private final static String ERROR_STYLE = StyleResource.INSTANCE.common().errorField();
    private final static int CITY = 0;
    private final static int STATE = 1;
    private final static int STREET = 2;
    private final static int ZIP = 3;

    public AddressEditor() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.initWidget(uiBinder.createAndBindUi(this));
        this.driver.initialize(this);
    }

    public void edit(Address address) {
        this.baseLineAddress = address;
        this.driver.edit(this.baseLineAddress);
    }

    @UiHandler("state")
    public void validateState(BlurEvent e) {
        Address address = driver.flush();
        Set<ConstraintViolation<Address>> violations = validator.validateValue(
                Address.class, "state", address.getState(), Default.class);
        this.displayErrors(STATE, violations);
    }

    @UiHandler("city")
    public void validateCity(BlurEvent e) {
        Address address = driver.flush();
        Set<ConstraintViolation<Address>> violations = validator.validateValue(
                Address.class, "city", address.getCity(), Default.class);
        this.displayErrors(CITY, violations);
    }

    @UiHandler("street")
    public void validateStreet(BlurEvent e) {
        Address address = driver.flush();
        Set<ConstraintViolation<Address>> violations = validator.validateValue(
                Address.class, "street", address.getStreet(), Default.class);
        this.displayErrors(STREET, violations);
    }

    @UiHandler("zip")
    public  void validateZip(BlurEvent e) {
        Address address = driver.flush();
        Set<ConstraintViolation<Address>> violations = validator.validateValue(
                Address.class, "zip", address.getZip(), Default.class);
        this.displayErrors(ZIP, violations);
    }

    @UiHandler("submit")
    public void validateAll(ClickEvent e) {
        validateCity(null);
        validateState(null);
        validateStreet(null);
        validateZip(null);
    }

    @UiHandler("clear")
    public void clear(ClickEvent e) {
        this.city.setText("");
        this.state.setText("");
        this.street.setText("");
        this.zip.setText("");

        setError(CITY, NORMAL_STYLE, "");
        setError(STATE, NORMAL_STYLE, "");
        setError(STREET, NORMAL_STYLE, "");
        setError(ZIP, NORMAL_STYLE, "");
    }

    private void displayErrors(int item, Set<ConstraintViolation<Address>> violations) {
        for (ConstraintViolation<Address> violation : violations) {
            setError(item, ERROR_STYLE, violation.getMessage());
            return;
        }
        setError(item, NORMAL_STYLE, "");
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
            case CITY:
                this.city.setStyleName(style);
                this.errorLabelCity.setText(errorMessage);
                break;
            case STATE:
                this.state.setStyleName(style);
                this.errorLabelState.setText(errorMessage);
                break;
            case STREET:
                this.street.setStyleName(style);
                this.errorLabelStreet.setText(errorMessage);
                break;
            case ZIP:
                this.zip.setStyleName(style);
                this.errorLabelZip.setText(errorMessage);
                break;
            default:
                break;
        }
    }
}