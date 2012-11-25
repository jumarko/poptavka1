/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.i18n.client;

/**
 * Custom validation messages. Interface to represent the constants contained in
 * resource bundle:
 * 'com/google/gwt/sample/validation/client/ValidationMessages.properties'
 * TODO(nchalko) move this to the root package so client and server can share
 * the same properties files.
 */
public interface ValidationMessages extends ConstantsWithLookup {

    /**
     * Translated "Name must be at least {size} characters long.".
     *
     * @return translated "Name must be at least {min} characters long."
     */
    /**************************************************************************/
    /** Create Demand Module - error messages.                                */
    /**************************************************************************/
    @Key("country.noMatch")
    String countryNotMatch();

    @Key("region.notMatch")
    String regionNotMatch();

    @Key("city.notMatch")
    String cityNotMatch();

    @Key("address.notBlank.country")
    String addressNotBlankCountry();

    @Key("address.notBlank.region")
    String addressNotBlankRegion();

    @Key("address.notBlank.city")
    String addressNotBlankCity();

    @Key("address.notBlank.district")
    String addressNotBlankDistrict();

    @Key("address.notBlank.street")
    String addressNotBlankStreet();

    @Key("address.notBlank.zipCode")
    String addressNotBlankZipCode();

    @Key("address.size.zipCode")
    String addressSizeZipCode();

    @Key("address.pattern.zipCode")
    String addressPatternZipCode();

    /**************************************************************************/
    /** Create Demand Module - error messages.                                */
    /**************************************************************************/
    //Preco musi byt ten key?
    @Key("demand.notBlank.title")
    String demandNotBlankTitle();

    @Key("demand.size.title")
    String demandSizeTitle();

    @Key("demand.notBlank.price")
    String demandNotBlankPrice();

    @Key("demand.pattern.price")
    String demandPatternPrice();

    @Key("demand.notNull.endDate")
    String demandNotNullEndDate();

    @Key("demand.future.endDate")
    String demandFutureEndDate();

    @Key("demand.notNull.validToDate")
    String demandNotNullValidToDate();

    @Key("demand.future.validToDate")
    String demandFutureValidToDate();

    @Key("demand.notBlank.description")
    String demandNotBlankDescription();

    @Key("demand.size.description")
    String demandSizeDescription();

    @Key("demand.pattern")
    String demandPattern();

    /**************************************************************************/
    /** Create Supplier Module - error messages.                              */
    /**************************************************************************/
    @Key("supplier.notBlank.email")
    String supplierNotBlankEmail();

    @Key("supplier.email")
    String supplierEmail();

    @Key("supplier.notBlank.companyName")
    String supplierNotBlankCompanyName();

    @Key("supplier.notBlank.identifNumber")
    String supplierNotBlankIdentifNumber();

    @Key("supplier.notBlank.taxNumber")
    String supplierNotBlankTaxNumber();

    @Key("supplier.notBlank.firstName")
    String supplierNotBlankFirstName();

    @Key("supplier.notBlank.lastName")
    String supplierNotBlankLastName();

    @Key("supplier.notBlank.phone")
    String supplierNotBlankPhone();

    @Key("supplier.pattern.phone")
    String supplierPatternPhone();

    @Key("supplier.notBlank.description")
    String supplierNotBlankDescription();

    @Key("supplier.notBlank.password")
    String supplierNotBlankPassword();

    @Key("supplier.min.password")
    String supplierMinPassword();

    @Key("supplier.notBlank.passwordConfirm")
    String supplierNotBlankPassrowdConfirm();

    @Key("supplier.pattern.website")
    String supplierPatternWebsite();

    /**************************************************************************/
    /** Email Dialog Popup - error messages.                                  */
    /**************************************************************************/
    @Key("email.dialog.notBlank.email")
    String emailDialogNotBlankEmail();

    @Key("email.dialog.email")
    String emailDialogEmail();

    @Key("reEmail.dialog.notBlank.email")
    String reRmailDialogNotBlankEmail();

    @Key("reEmail.dialog.email")
    String reEmailDialogEmail();

    @Key("email.dialog.notNull.message")
    String emailDialogNotNullMessage();

    @Key("email.dialog.size.message")
    String emailDialogSizeMessage();
}
