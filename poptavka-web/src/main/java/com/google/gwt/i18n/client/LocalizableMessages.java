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
    //home - searchPanel
    String allCategories();
    String allLocalities();
    String search();
    String advancedSearch();
    String searchContent();
    String searchInDemands();
    String searchInSuppliers();
    String today();
    String yesterday();
    String lastWeek();
    String lastMonth();
    String noLimits();
    String searchIn();
    String priceFrom();
    String priceTo();
    String ratingFrom();
    String ratingTo();
    String demandType();
    String finnishDate();
    String creationDate();
    String currency();
    String supplierDescription();
    String demandText();
    String supplierName();
    String select();

    //offers view - buttons
    String answer();
    String refuse();
    String accept();
    //offer table
    String price();
    String demand();
    //demand creation
    String mailAvailable();
    String mailNotAvailable();
    String malformedEmail();
    String shortPassword();
    String strongPassword();
    String semiStrongPassword();
    String passwordsMatch();
    String passwordsUnmatch();

    //supplier registration
    String agreementMessage();
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
    String id();
    String cid();
    String rid();
    String did();
    String pid();
    String sid();
    String oid();
    String code();
    String name();
    String title();
    String permissions();
    String company();
    String firstName();
    String lastName();
    String expiration();
    String activationLink();
    String timeout();
    String invoiceNumber();
    String varSymb();
    String totalPrice();
    String payMethod();
    String subject();
    String state();
    String key();
    String value();
    String text();
    String companyName();
    String businessType();
    String certified();
    String verified();
    String description();
    String duration();
    String from();
    String to();
    String noData();
    String rating();
    String address();
    String action();
    String read();
    String unread();
    String star();
    String unstar();
    String notDefined();
    String deliveryDate();

    String month();
    String fewMonths();
    String months();
    String submit();
    SafeHtml rememberMe();
    String pass();
    String email();
    String loading();
    String loadingRootCategories();
    String loadingCategories();
    String loadingLocalities();
    String progressGetUserDetail();
    String progressCreatingUserInterface();
    String progressDemandsLayoutInit();
    String progressMessagesLayoutInit();
    String progressAdminLayoutInit();
    String notEntered();
    String supplierPotentialDemandsTableTitle();
    String supplierContestsTableTitle();
    String supplierAssignedDemandsTableTitle();

    //used in displying demands
    String date();
    String validTo();
    String type();
    String category();
    String locality();
    String offers();
    String attachment();
    String createdDate();
    String sentDate();
    String createdTime();
    String urgency();
    String commonInfo();
    String minRating();
    String maxOffers();
    String excludedSuppliers();

    String client();
    String emptyTable();

    //Admin module
    String getDemandsData();
    String getClientsData();
    String getOffersData();
    String getSuppliersData();
    String getAccessRoleData();
    String getEmailActivationData();
    String getInvoiceData();
    String getMessageData();
    String getPaymentMethodData();
    String getPermissionData();
    String getPreferenceData();
    String getProblemData();
    String commit();

    //Demand module
    String received();
    String accepted();

    //General
    String currencyFormat();

}
