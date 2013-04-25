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
 */
public interface ValidationMessages extends ConstantsWithLookup {

    /*******************************************************************  B  **/
    @Key("body.notBlank")
    String bodyNotBlank();

    @Key("body.size")
    String bodySize();

    /*******************************************************************  C  **/
    @Key("city.notBlank")
    String cityNotBlank();

    @Key("city.notMatch")
    String cityNotMatch();

    @Key("companyName.notBlank")
    String companyNameNotBlank();

    @Key("country.notBlank")
    String countryNotBlank();

    @Key("country.notMatch")
    String countryNotMatch();

    /*******************************************************************  D **/
    @Key("description.notBlank")
    String descriptionNotBlank();

    @Key("description.size")
    String descriptionSize();

    @Key("district.notBlank")
    String districtNotBlank();

    /*******************************************************************  E **/
    @Key("email.notBlank")
    String emailNotBlank();

    @Key("email.email")
    String emailEmail();

    @Key("endDate.notNull")
    String endDateNotNull();

    @Key("endDate.future")
    String endDateFuture();

    /*******************************************************************  F **/
    @Key("firstName.notBlank")
    String firstNameNotBlank();

    @Key("finishDate.notNull")
    String finishDateNotNull();

    @Key("finishDate.dateEqualOrGreater")
    String finishDateEqualOrGreater();

    /*******************************************************************  I **/
    @Key("identifNumber.notBlank")
    String identifNumberNotBlankIdentif();

    /*******************************************************************  L **/
    @Key("lastName.notBlank")
    String lastNameNotBlank();

    /*******************************************************************  M **/
    @Key("maxSuppliers.min")
    String maxSuppliersMin();

    @Key("message.notNull")
    String messageNotNull();

    @Key("message.size")
    String messageSize();

    /*******************************************************************  N **/
    @Key("number.format")
    String numberFormat();

    /*******************************************************************  P **/
    @Key("pattern.string")
    String patternString();

    @Key("pattern.website")
    String patternWebsite();

    @Key("password.notBlank")
    String passwordNotBlank();

    @Key("password.length")
    String passwordLength();

    @Key("passwordConfirm.notBlank")
    String passwordConfirmNotBlank();

    @Key("phone.notBlank")
    String phoneNotBlank();

    @Key("phone.pattern")
    String phonePattern();

    @Key("price.notNull")
    String priceNotNull();

    @Key("price.min")
    String priceMin();

    @Key("price.digits")
    String priceDigits();

    /*******************************************************************  R **/
    @Key("rating.min")
    String ratingMin();

    @Key("rating.max")
    String ratingMax();

    @Key("reEmail.notBlank")
    String reRmailNotBlank();

    @Key("reEmail.email")
    String reEmailEmail();

    @Key("region.notBlank")
    String regionNotBlank();

    @Key("region.notMatch")
    String regionNotMatch();

    /*******************************************************************  S **/
    @Key("street.notBlank")
    String streetNotBlank();

    /*******************************************************************  T **/
    @Key("taxNumber.notBlank")
    String taxNumberNotBlank();

    @Key("title.notBlank")
    String titleNotBlank();

    @Key("title.size")
    String titleSize();

    /*******************************************************************  V **/
    @Key("validTo.notNull")
    String validToNotNull();

    @Key("validTo.future")
    String validToFuture();

    /*******************************************************************  Z **/
    @Key("zipCode.notBlank")
    String zipCodeNotBlank();

    @Key("zipCode.size")
    String zipCodeSize();

    @Key("zipCode.pattern")
    String zipCodePattern();
}
