package com.google.gwt.i18n.client;

import com.google.gwt.safehtml.shared.SafeHtml;
import java.util.Date;

public interface LocalizableMessages extends Messages {

    /**************************************************************************/
    /*  0 - Root module view                                                  */
    /*  1 - Home Welcome module view                                          */
    /*  2 - Home Demands module view                                          */
    /*  3 - Home Suppliers module view                                        */
    /*  4 - Create Demand module view                                         */
    /*  6 - Search module view                                                */
    /*  7 - Settings module view                                              */
    /*  8 - Client Demands module view                                        */
    /*  9 - Supplier Demands module view                                      */
    /*  10 - Admin module view                                                */
    /*                                                                        */
    /*** 0 - Root module view ******************************************* 0 ***/
    /*** ATTRIBUTES ***********************************************************/
    /** Formats i18n. **/
    String formatDate();
    String formatCurrency();
    String formatCurrencySign();

    /** Common button names. **/
    String commonBtnBack();
    String commonBtnCancel();
    String commonBtnClear();
    String commonBtnClose();
    String commonBtnDelete();
    String commonBtnEdit();
    String commonBtnLogin();
    String commonBtnNext();
    String commonBtnRate();
    String commonBtnRefresh();
    String commonBtnReport();
    String commonBtnRevert();
    String commonBtnSend();
    String commonBtnSubmit();

    /** Common labels. **/
    String commonCategoriesLabel();
    String commonClient();
    String commonEmptyCredentials();
    String commonEmptyTable();
    String commonLocalitiesLabel();
    String commonNoData();
    String commonNotDefined();
    String commonNotRanked();
    String commonOK();
    String commonSelected();
    String commonServicesLabel();
    String commonSupplier();

    /** Messages actions list box. **/
    String commonListDefault();
    String commonListRead();
    String commonListUnread();
    String commonListStarred();
    String commonListUnstarred();

    /** Form section info labels . **/
    String formAccountInfo();
    String formAddressInfo();
    String formBankInfo();
    String formClientInfo();
    String formCommonInfo();
    String formCompanyInfo();
    String formDatesInfo();
    String formDemandInfo();
    String formDetailInfo();
    String formInvoiceInfo();
    String formPriceInfo();
    String formSupplierInfo();

    /** Form labels. **/
    String formAdddress();
    String formAttachment();
    String formBankAccNum();
    String formBankCode();
    String formBusinessType();
    String formCategories();
    String formCertified();
    String formCity();
    String formClientRating();
    String formCompanyName();
    String formConstSymbol();
    String formCreationDate();
    String formDemandStatus();
    String formDemandTitle();
    String formDemandType();
    String formDescription();
    String formEmail();
    String formExcludedSuppliers();
    String formExpirationDate();
    String formFinnishDate();
    String formFirstName();
    String formFrom();
    String formFromTo(int from, int to);
    String formFromTo2(int from1, int to1, int from2, int to2);
    String formFromToDate(Date from, Date to);
    String formFromToPrice(int from1, int to1, int from2, int to2);
    String formFromToPercentage(int from, int to);
    String formIdentificationNumber();
    String formInvoiceNumber();
    String formIssueDate();
    String formLastName();
    String formLocalities();
    String formMaxOffers();
    String formMaxSuppliers();
    String formMinSupplierRating();
    String formPassword();
    String formPasswordConfirm();
    String formPaymentMethod();
    String formPhone();
    String formPrice();
    String formRating();
    String formRateComment();
    String formRequiredFields();
    String formServices();
    String formShipmentDate();
    String formStreet();
    String formTaxBasis();
    String formTaxNumber();
    String formTitle();
    String formTo();
    String formTotalPrice();
    String formValidTo();
    String formVarSymbol();
    String formVat();
    String formVatRate();
    String formVerified();
    String formWebsite();
    String formZipCode();

    /** Horizontal Menu. **/
    String menuHome();
    String menuClient();
    String menuSupplier();
    String menuDemands();
    String menuSuppliers();
    String menuCreateDemand();
    String menuCreateSupplier();
    String menuMessages();
    String menuAdministration();

    /** Vertical Menu. **/
    String menuClientDemands();
    String menuClientOffers();
    String menuClientAssignedDemands();
    String menuClientClosedDemands();
    String menuClientRatings();
    String menuSupplierDemands();
    String menuSupplierOffers();
    String menuSupplierAssignedDemands();
    String menuSupplierClosedDemands();
    String menuSupplierRatings();
    String menuSettingsUser();
    String menuSettingsClient();
    String menuSettingsSupplier();
    String menuSettingsSystem();

    /*** ATTRIBUTES ***********************************************************/
    /** HeaderView.ui. **/
    String headerLogIn();

    /** UserHeaderView.ui. **/
    String userHeaderCustomerService();
    String userHeaderHelp();
    String userHeaderLogOut();
    String userHeaderMyProfile();

    /** FooterView.ui. **/
    String footerAboutUs();
    String footerCompanyName();
    String footerContactUs();
    String footerTermsConditions();

    /** EmailDialogPopupView.ui. **/
    String emailDialogCustomerSupport();
    String emailDialogEnterYourEmail();
    String emailDialogMaximumChars();
    String emailDialogQuestionOrConcern();
    String emailDialogReEnterYourEmail();
    String emailDialogSendUsEmail();
    String emailDialogSubject();
    String emailDialogSubjectGeneralQuestion();
    String emailDialogSubjectHelp();
    String emailDialogSubjectPartnership();
    String emailDialogSubjectReportIssue();
    String emailDialogSubjectReportUser();
    String emailDialogThanksForMessage();
    String emailDialogTo();

    /** ActivationCodePopupView.ui. **/
    String activationBtnActivate();
    String activationBtnSendAgain();
    String activationCodeSent();
    String activationEnterActivationCode();
    String activationFailedExpiredActivationCode();
    String activationFailedIncorrectActivationCode();
    String activationFailedSentNewCode();
    String activationFailedUnknownError();
    String activationFailedUnknownUser();
    String activationNewCodeSent();
    String activationPassed();

    /** DetailsWrapperView.ui. **/
    String detailsWrapperTabClientDetail();
    String detailsWrapperTabDemandDetail();
    String detailsWrapperTabSupplierDetail();
    String detailsWrapperTabConversationDetail();
    /** OfferQuestionWindow.ui. **/
    String offerResponse();
    String questionResponse();

    /** List creationDate. **/
    String creationDateToday();
    String creationDateYesterday();
    String creationDateLastWeek();
    String creationDateLastMonth();
    String creationDateNoLimits();

    /** Service definitions. **/
    String serviceOne();
    String serviceOneDescription();
    String serviceTwo();
    String serviceTwoDescription();
    String serviceThree();
    String serviceThreeDescription();

    /** Global loading popup. **/
    String progressCommiting();
    String progressCreatingDemand();
    String progressGettingDemandData();
    String progressLogingUser();
    String progressRegisterClient();
    String progressRegisterSupplier();
    String progressUpdatingProfile();

    String wrongLoginDescription();

    /** Table columns. **/
    String columBusinessType();
    String columCertified();
    String columCompanyName();
    String columDescription();
    String columnActivationCode();
    String columnAddress();
    String columnCID();
    String columnClient();
    String columnCode();
    String columnCompany();
    String columnCreatedDate();
    String columnDate();
    String columnDeliveryDate();
    String columnDemandTitle();
    String columnDID();
    String columnDuration();
    String columnEndDate();
    String columnExpiration();
    String columnFinnishDate();
    String columnFirstName();
    String columnID();
    String columnInvoiceNumber();
    String columnKey();
    String columnLastName();
    String columnLocality();
    String columnLogo();
    String columnName();
    String columnOfferID();
    String columnPayMethod();
    String columnPermissions();
    String columnPID();
    String columnPrice();
    String columnRating();
    String columnRatingClient();
    String columnRatingSupplier();
    String columnReceived();
    String columnSenderID();
    String columnService();
    String columnState();
    String columnStatus();
    String columnSubject();
    String columnSupplierName();
    String columnText();
    String columnTimeout();
    String columnTitle();
    String columnTotalPrice();
    String columnType();
    String columnValidTo();
    String columnValue();
    String columnVarSymb();
    String columVerified();

    String month();
    String fewMonths();
    String months();

    SafeHtml rememberMe();

    /** Login. **/
    String loginPopupPass();
    String loginPopupEmail();

    /** Loading. **/
    String loading();
    String loadingRootCategories();
    String loadingCategories();
    String loadingLocalities();

    /** Progress. **/
    String progressAdminLayoutInit();
    String progressCreatingUserInterface();
    String progressDemandsLayoutInit();
    String progressGetUserDetail();
    String progressMessagesLayoutInit();

    /** UrgentImageCell.java. **/
    String urgencyDesc();
    String urgencyHighDesc();
    String urgencyHigherDesc();
    String urgencyNormalDesc();
    String urgencyTooltip();

    /** Explanation texts for DemandStatus icon column. **/
    String demandStatusActive();
    String demandStatusAssigned();
    String demandStatusCanceled();
    String demandStatusClosed();
    String demandStatusCrawled();
    String demandStatusFinnished();
    String demandStatusInactive();
    String demandStatusInvalid();
    String demandStatusNew();
    String demandStatusToBeChecked();

    /** Explanation texts for Offer state icon column. **/
    String offerStateAccepted();
    String offerStatePending();
    String offerStateDeclined();

    // Messages for LoginPopupPresenter & LoginPopupView
    String loggingForward();
    String loggingIn();
    String loggingLoadProfile();
    String loggingVerifyAccount();
    String wrongLoginMessage();
    String loginUnknownError();

    /** ErrorView.java. Error messages and their descriptions **/
    String errorMsgAccessDenied();
    String errorMsgAccessDeniedDesc();
    String errorMsgAlert();
    String errorMsgBadRequest();
    String errorMsgBadRequestDesc();
    String errorMsgCodelistsNotDisplaying();
    String errorMsgInternalError();
    String errorMsgInternalErrorDesc();
    String errorMsgNotAuthorized();
    String errorMsgNotAuthorizedDesc();
    String errorMsgPageNotFound();
    String errorMsgPageNotFoundDesc();
    String errorMsgSecurityError();
    String errorMsgServerError();
    String errorMsgServerErrorDesc();
    String errorMsgServiceUnavailable();
    String errorMsgServiceUnavailableDesc();

    /** Error tips for users. **/
    String errorTipCheckAccount();
    String errorTipCheckWebAddress();
    String errorTipPleaseTryFollowing();
    String errorTipReportIssue();
    String errorTipTryFromHome();
    String errorTipTryOtherBrowser();
    String errorTipTryRegistration();
    String errorTipTrySearchBox();
    String errorTipTryWaiting();

    /** AddressSelectorView.ui. **/
    String addressSelectCityFromSuggestedList();
    String addressCity();
    String addressCountry();
    String addressDistrict();
    String addressLoadingInfoLabel(int minCharsCity);
    String addressMyCityIsLessInfoLabel(int minCharsCity);
    String addressNoCityFound();
    String addressRegion();
    String addressState();
    String addressStreet();
    String addressZipCode();

    /*** 1 - Home Welcome module view *********************************** 1 ***/

    /*** 2 - Home Demands module view *********************************** 2 ***/
    /** HomeDemandsView.ui. **/
    String homeDemandsToolbarLabel();
    String homeDemandsOfferBtn();

    /*** 3 - Home Suppliers module view ********************************* 3 ***/
    /** HomeSuppliersView.ui. **/
    String homeSuppliersToolbarLabel();
    String homeSuppliersContactBtn();

    /*** 4 - Create Demand module view ********************************** 4 ***/
    /** DemandCreationModuleView.ui. **/
    String demandCreationTab1();
    String demandCreationTab2();
    String demandCreationTab3();
    String demandCreationTab4();
    String demandCreationTab5();
    String demandCreationInfoLabel1();
    String demandCreationInfoLabel12();
    String demandCreationInfoLabel2();
    String demandCreationInfoLabel3();
    String demandCreationInfoLabel4();
    String demandCreationInfoLabel5();
    String demandCreationLoginOrRegister();
    String demandCreationCreateDemandBtn();
    String demandCreationRegisterBtn();
    String demandCreationSuccessfullyCreated();

    /** FormDemandAdvView.ui. **/
    String formDemandAdvDemandAttractive();
    String formDemandAdvDemandNormal();
    String formDemandAdvExcludeBtn();
    String formDemandAdvExcludedList();
    String formDemandAdvMinRating();

    /*** 5 - Create Supplier module view ******************************** 5 ***/
    /** SupplierCreationModuleView.ui. **/
    String supplierCreationTab1();
    String supplierCreationTab2();
    String supplierCreationTab3();
    String supplierCreationTab4();
    String supplierCreationInfoLabel1();
    String supplierCreationInfoLabel2();
    String supplierCreationInfoLabel3();
    String supplierCreationInfoLabel4();
    String supplierCreationAgreementConditions();
    String supplierCreationAgreementMessage();
    String supplierCreationRegisterBtn();

    /** FormUserRegistrationView.ui. **/
    String formUserRegMailAvailable();
    String formUserRegMailNotAvailable();
    String formUserRegMalformedEmail();
    String formUserRegPasswordsMatch();
    String formUserRegPasswordsUnmatch();
    String formUserRegPrivatePerson();
    String formUserRegSemiStrongPassword();
    String formUserRegShortPassword();
    String formUserRegStrongPassword();

    /*** 6 - Search module view ***************************************** 6 ***/
    /** SearchModuleView.ui. **/
    String searchBtnAdvance();
    String searchBtnSearch();
    String searchContent();
    String searchMenuDemand();
    String searchMenuSupplier();
    String searchNoSearchingCriteria();

    /** Search in possible labels. **/
    String searchInClientAssignedDemands();
    String searchInClientDemands();
    String searchInClientDemandsDiscussions();
    String searchInClientOfferedDemandOffers();
    String searchInClientOfferedDemands();
    String searchInCurrentView();
    String searchInDemands();
    String searchInSuppliers();
    String searchInSuppliersAssignedDemands();
    String searchInSuppliersOffers();
    String searchInSuppliersPotentialDemands();

    /** Search popup possible tab names. **/
    String searchClientAssignedDemandsTab();
    String searchClientDemandsDiscussionsTab();
    String searchClientDemandsTab();
    String searchClientOfferedDemandOffersTab();
    String searchClientOfferedDemandsTab();
    String searchCurrentViewTab();
    String searchDemandsTab();
    String searchSuppliersAssignedDemandsTab();
    String searchSuppliersOffersTab();
    String searchSuppliersPotentialDemandDiscussionsTab();
    String searchSuppliersPotentialDemandsTab();
    String searchSuppliersTab();

    /** AdvanceSearchContentView.ui. **/
    String advSearchCategoriesTab();
    String advSearchCurrentViewTab();
    String advSearchDemandsTab();
    String advSearchLocalitiesTab();
    String advSearchSuppliersTab();

    /*** 7 - Settings module view *************************************** 7 ***/
    /** SettingsView.ui. **/
    String settingsNothingToUpdate();
    String settingsNotificationLeavingPage();
    String settingsToolbarLabel();
    String settingsUpdateButton();
    String settingsUpdatedOK();
    String settingsUpdatedNotOK();
    String settingsUserSettings();
    String settingsClientSettings();
    String settingsSupplierSettings();
    String settingsSystemSettings();

    /** Notifications. **/
    String notifyImmediately();
    String notifyDaily();
    String notifyWeekly();

    /** UserSettingsView.ui. **/
    String userSettingsBillingDetails();
    String userSettingsNotifications();

    /** ClientSettingsView.ui. **/

    /** SupplierSettingsView.ui. **/
    String supplierSettingsCategories();
    String supplierSettingsLocalities();
    String supplierServices();


    /*** 8 - Client Demands module view ********************************* 8 ***/
    String clientDemandsToolbarLabel();
    /** ClientDemandsView.ui. **/
    String clientDemandsTableTitle();
    String clientDemandsBackBtn();

    /** ClientOffersView.ui. **/
    String clientOffersTableTitle();
    String clientOffersBackBtn();
    String clientOffersAcceptBtn();

    /** ClientAssignedDemandsView.ui. **/
    String clientAssignedDemandsTitle();
    String clientAssignedDemandsCloseBtn();

    /*** 9 - Supplier Demands module view ******************************* 9 ***/
    String supplierDemandsToolbarLabel();
    /** SupplierDemandsView.ui. **/
    String supplierPotentialDemandsTableTitle();

    /** SupplierOffersView.ui. **/
    String supplierOffersTableTitle();

    /** SupplierAssignedDemandsView.ui. **/
    String supplierAssignedDemandsFinnishBtn();
    String supplierAssignedDemandsTableTitle();

    /*** 10 - Admin module view ***************************************** 10 ***/
    /** AdminView.ui. **/
    String adminTableAccessRole();
    String adminTableDemand();
    String adminTableEmailActivation();
    String adminTableInvoice();
    String adminTableMessage();
    String adminTableOffer();
    String adminTableOurPaymentDetail();
    String adminTablePaymentMethod();
    String adminTablePermission();
    String adminTablePreference();
    String adminTableProblem();
    String adminTableSupplier();
    String adminTableUser();

    /** Admin commons. **/
    String adminCommonChangesLabel();
    String adminCommonBtnCommit();
    String adminCommonBtnCreate();
    String adminCommonBtnRefresh();
    String adminCommonBtnRollback();
    String adminCommonBtnUpdate();

    /** Admin form labels. **/
    String adminFormActivationLink();
    String adminFormBody();
    String adminFormCID();
    String adminFormCode();
    String adminFormDID();
    String adminFormId();
    String adminFormKey();
    String adminFormMID();
    String adminFormName();
    String adminFormPermissions();
    String adminFormPID();
    String adminFormSenderID();
    String adminFormState();
    String adminFormSubject();
    String adminFormSupplierID();
    String adminFormTimeout();
    String adminFormType();
    String adminFormValue();

    /* Feedback popup info */
    String feedbackToClientMessage();
    String feedbackAdditionalCommentPlaceholder();
    String feedbackComment1();
    String feedbackComment2();
    String feedbackComment3();
    String feedbackComment4();
    String feedbackComment5();

    /* Generated Messages */
    String acceptedOfferMessage();
    String finishedOfferMessage();
    String closeDemandMessage();

}
