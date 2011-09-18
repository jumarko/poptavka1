package com.google.gwt.i18n.client;

import com.google.gwt.safehtml.shared.SafeHtml;


public interface LocalizableMessages extends Messages {

    String logIn();
    String logOut();
    String endDate();
    String expireDate();
    String emptyField();
    String footerDisplay();
    String footerHide();
    String status();

    //offers view - buttons
    String answer();
    String refuse();
    String accept();
    //offer table
    String price();
    String demand();
    //demand creation
    String mailAvailable();
    String malformedEmail();
    String shortPassword();
    String strongPassword();
    String semiStrongPassword();
    String passwordsMatch();
    String passwordsUnmatch();

    //supplier registration
    //service definitions
    String serviceOne();
    String serviceOneDescription();
    String serviceTwo();
    String serviceTwoDescription();
    String serviceThree();
    String serviceThreeDescription();

    //status messages, description
    String basicMessage();
    String basicDescription();
    String locMessage();
    String locDescription();
    String catMessage();
    String catDescription();
    String advMessage();
    String advDescription();
    String loginMessage();
    String loginDescription();
    String regMessage();
    String regDescription();

    //global loading popup
    String progressRegisterClient();
    String progressGettingDemandData();
    String progressCreatingDemand();
    String progressLogingUser();
    String wrongLoginDescription();
    String wrongLoginMessage();
    String close();

    //common
    String title();
    String description();
    String duration();

    String month();
    String fewMonths();
    String months();
    String submit();
    SafeHtml rememberMe();
    String pass();
    String email();
    String loading();
    String progressGetUserDetail();
    String progressCreatingUserInterface();
    String progressDemandsLayoutInit();
    String notEntered();

    //used in displying demands
    String date();
    String validTo();
    String type();
    String category();
    String locality();
    String offers();
    String attachment();
    String createdDate();
    String createdTime();
    String urgency();
    String commonInfo();
    String minRating();
    String maxOffers();
    String excludedSuppliers();
}
