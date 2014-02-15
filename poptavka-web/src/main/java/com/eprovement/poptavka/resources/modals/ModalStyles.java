/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.resources.modals;

import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.ClassName;

/**
 * Defines styles for modals.
 *
 * @author Jaro
 */
public interface ModalStyles extends CssResource {

    /* Common Modal Styles */
    @ClassName("common-modal-style")
    String commonModalStyle();

    /* Tooltip */
    @ClassName("tooltip")
    String tooltip();

    /** Login Modal Styles **/
    @ClassName("login-modal")
    String loginModal();

    /** Advance Search Modal styles **/
    @ClassName("advanced-search-popup")
    String advancedSearchPopup();

    @ClassName("advanced-search-tabPanel")
    String advancedSearchTabPanel();

    /* Loader Modal Styles */
    @ClassName("small-loader-modal")
    String smallLoaderModal();

    /* Suggest Modal Styles*/
    @ClassName("suggest-modal")
    String suggestModal();

    /* Loader Modal Styles */
    @ClassName("feedback-modal")
    String feedbackModal();

    /* Contact Us Modal Styles */
    @ClassName("contactUs-modal")
    String contactUsModal();

    /* Category and Locality Modal Styles */
    @ClassName("cell-browser-selection-modal")
    String cellBrowserSelectionModal();

    /* Activation Code Modal Styles */
    @ClassName("activation-code")
    String activationCodePopupStyle();

    /* Thank You Modal Styles*/
    @ClassName("thankyou")
    String thankYouPopupStyle();

    /* Thank You Modal Styles*/
    @ClassName("terms-and-conditions")
    String termsAndConditions();
}
