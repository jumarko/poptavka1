package cz.poptavka.sample.client.homeWelcome;

/*
 * GWT Validation Framework - A JSR-303 validation framework for GWT
 *
 * (c) 2008 gwt-validation contributors
 * (http://code.google.com/p/gwt-validation/)  *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.resources.StyleResource;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class AddressEditor extends Composite implements Editor<Address> {

    interface AddressEditorUiBinder extends UiBinder<Widget, AddressEditor> {
    }
    private static AddressEditorUiBinder uiBinder = GWT.create(AddressEditorUiBinder.class);

    interface Driver extends SimpleBeanEditorDriver<Address, AddressEditor> {}
    Driver driver = GWT.create(Driver.class);
    private ValidatorFactory factory = null;
    @UiField
    TextBox street;
    @UiField
    TextBox state;
    @UiField
    TextBox city;
    @UiField
    TextBox zip;
    @UiField
    @Path("owner.firstName")
    TextBox firstName;
    @UiField
    @Path("owner.lastName")
    TextBox lastName;
    @UiField
    Button submit;
    @UiField
    Button clear;
    @UiField
    @Ignore
    HTML errors;
    private Address baseLineAddress = null;

    public AddressEditor() {
        this.factory = Validation.byDefaultProvider().configure().buildValidatorFactory();
        this.initWidget(uiBinder.createAndBindUi(this));
        this.driver.initialize(this);
    }

    public void edit(Address address) {
        this.baseLineAddress = address;
        this.driver.edit(this.baseLineAddress);
    }

    @UiHandler("submit")
    void submit(ClickEvent e) {
        clearStyles();
        Address address = driver.flush();

        Validator validator = this.factory.getValidator();

        StringBuilder builder = new StringBuilder();

        Set<ConstraintViolation<Address>> violations = validator.validate(address);

        for (ConstraintViolation<Address> violation : violations) {
            builder.append(violation.getMessage());
            builder.append(" : <i>(");
            builder.append(violation.getPropertyPath().toString());
            setErrorStyles(violation.getPropertyPath().toString());
            builder.append(" = ");
            builder.append("" + violation.getInvalidValue());
            builder.append(")</i>");
            builder.append("<br/>");
        }

        this.errors.setHTML(builder.toString());

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
        this.firstName.setText("");
        this.lastName.setText("");
        this.errors.setHTML("");
        
        clearStyles();
    }
    
    public void clearStyles() {
        this.city.removeStyleName(StyleResource.INSTANCE.common().errorField());
        this.state.removeStyleName(StyleResource.INSTANCE.common().errorField());
        this.street.removeStyleName(StyleResource.INSTANCE.common().errorField());
        this.zip.removeStyleName(StyleResource.INSTANCE.common().errorField());
        this.firstName.removeStyleName(StyleResource.INSTANCE.common().errorField());
        this.lastName.removeStyleName(StyleResource.INSTANCE.common().errorField());
    }
    
    private void setErrorStyles(String item) {
        if (item.equals("city")) {
            this.city.setStyleName(StyleResource.INSTANCE.common().errorField());
        } else if (item.equals("state")) {
            this.state.setStyleName(StyleResource.INSTANCE.common().errorField());
        } else if (item.equals("street")) {
            this.street.setStyleName(StyleResource.INSTANCE.common().errorField());
        } else if (item.equals("zip")) {
            this.zip.setStyleName(StyleResource.INSTANCE.common().errorField());
        } else if (item.equals("firstName")) {
            this.firstName.setStyleName(StyleResource.INSTANCE.common().errorField());
        } else if (item.equals("lastName")) {
            this.lastName.setStyleName(StyleResource.INSTANCE.common().errorField());
        }
        
    }
}
