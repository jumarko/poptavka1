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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

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
    void validateState(BlurEvent e) {
        this.state.removeStyleName(StyleResource.INSTANCE.common().errorField());
        this.errorLabelState.setText("");
        Address address = driver.flush();
        Set<ConstraintViolation<Address>> violations = validator.validateValue(
                Address.class, "state", address.getState(), Default.class);
        this.displayErrors(violations);
    }

    @UiHandler("city")
    void validateCity(BlurEvent e) {
        this.city.removeStyleName(StyleResource.INSTANCE.common().errorField());
        this.errorLabelCity.setText("");
        Address address = driver.flush();
        Set<ConstraintViolation<Address>> violations = validator.validateValue(
                Address.class, "city", address.getCity(), Default.class);
        this.displayErrors(violations);
    }

    @UiHandler("street")
    void validateStreet(BlurEvent e) {
        this.street.removeStyleName(StyleResource.INSTANCE.common().errorField());
        this.errorLabelStreet.setText("");
        Address address = driver.flush();
        Set<ConstraintViolation<Address>> violations = validator.validateValue(
                Address.class, "street", address.getStreet(), Default.class);
        this.displayErrors(violations);
    }

    @UiHandler("zip")
    void validateZip(BlurEvent e) {
        this.zip.removeStyleName(StyleResource.INSTANCE.common().errorField());
        this.errorLabelZip.setText("");
        Address address = driver.flush();
        Set<ConstraintViolation<Address>> violations = validator.validateValue(
                Address.class, "zip", address.getZip(), Default.class);
        this.displayErrors(violations);
    }

    @UiHandler("submit")
    void validateAll(ClickEvent e) {
        validateCity(null);
        validateState(null);
        validateStreet(null);
        validateZip(null);
    }

    private void displayErrors(Set<ConstraintViolation<Address>> violations) {
        StringBuilder builder = new StringBuilder();
        for (ConstraintViolation<Address> violation : violations) {
            builder.append(violation.getMessage());
            builder.append(" : <i>(");
            builder.append(violation.getPropertyPath().toString());
            setErrors(violation.getPropertyPath().toString(), violation.getMessage());
            builder.append(" = ");
            builder.append("" + violation.getInvalidValue());
            builder.append(")</i>");
            builder.append("<br/>");
        }

        //Martin: Toto este neviem naco je
        List<ConstraintViolation<?>> adaptedViolations = new ArrayList<ConstraintViolation<?>>();
        for (ConstraintViolation<Address> violation : violations) {
            adaptedViolations.add(violation);
        }

        driver.setConstraintViolations((List<ConstraintViolation<?>>) adaptedViolations);
    }

    @UiHandler("clear")
    public void clear(ClickEvent e) {
        this.city.setText("");
        this.state.setText("");
        this.street.setText("");
        this.zip.setText("");

        clearStyles();
    }

    public void clearStyles() {
        this.city.removeStyleName(StyleResource.INSTANCE.common().errorField());
        this.errorLabelCity.setText("");
        this.state.removeStyleName(StyleResource.INSTANCE.common().errorField());
        this.errorLabelState.setText("");
        this.street.removeStyleName(StyleResource.INSTANCE.common().errorField());
        this.errorLabelStreet.setText("");
        this.zip.removeStyleName(StyleResource.INSTANCE.common().errorField());
        this.errorLabelZip.setText("");
    }

    private void setErrors(String item, String errorMessage) {
        if (item.equals("city")) {
            this.city.setStyleName(StyleResource.INSTANCE.common().errorField());
            this.errorLabelCity.setText(errorMessage);
        } else if (item.equals("state")) {
            this.state.setStyleName(StyleResource.INSTANCE.common().errorField());
            this.errorLabelState.setText(errorMessage);
        } else if (item.equals("street")) {
            this.street.setStyleName(StyleResource.INSTANCE.common().errorField());
            this.errorLabelStreet.setText(errorMessage);
        } else if (item.equals("zip")) {
            this.zip.setStyleName(StyleResource.INSTANCE.common().errorField());
            this.errorLabelZip.setText(errorMessage);
        }
    }
}
