/*
 * Copyright (C) 2013, eProvement s.r.o. All rights reserved.
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

    @Key("companyName.size")
    String companyNameSize();

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

    @Key("email.size")
    String emailSize();

    @Key("emailDialog.message.notBlank")
    String emailDialogMessageNotBlank();

    @Key("emailDialog.message.size")
    String emailDialogMessageSize();

    @Key("endDate.notNull")
    String endDateNotNull();

    @Key("endDate.future")
    String endDateFuture();

    /*******************************************************************  F **/
    @Key("firstName.notBlank")
    String firstNameNotBlank();

    @Key("firstName.size")
    String firstNameSize();

    @Key("finishDate.notNull")
    String finishDateNotNull();

    @Key("finishDate.dateEqualOrGreater")
    String finishDateEqualOrGreater();

    /*******************************************************************  I **/
    @Key("identifNumber.size")
    String identifNumberSize();

    @Key("itin.size")
    String itinSize();

    /*******************************************************************  L **/
    @Key("lastName.notBlank")
    String lastNameNotBlank();

    @Key("lastName.size")
    String lastNameSize();

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
    @Key("pattern.no.special.chars")
    String patternNoSpecChars();

    @Key("pattern.email")
    String patternEmail();

    @Key("pattern.company")
    String patternCompany();

    @Key("pattern.itin")
    String patternItin();

    @Key("pattern.no.chars.no.number")
    String patternNoSpecCharsNoNumbers();

    @Key("pattern.nonstring")
    String patternNonString();

    @Key("pattern.website")
    String patternWebsite();

    @Key("pattern.phone")
    String patternPhone();

    @Key("password.notBlank")
    String passwordNotBlank();

    @Key("password.size")
    String passwordSize();

    @Key("passwordConfirm.notBlank")
    String passwordConfirmNotBlank();

    @Key("phone.notBlank")
    String phoneNotBlank();

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
