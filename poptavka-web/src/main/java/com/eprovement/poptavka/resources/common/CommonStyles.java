package com.eprovement.poptavka.resources.common;

import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.ClassName;

public interface CommonStyles extends CssResource {

    /** Action box styles. **/
    @ClassName("action-box")
    String actionBox();

    /** Pager styles. **/
    @ClassName("universal-pager")
    String pager();

    @ClassName("small-loader")
    String smallLoader(); //not used

    @ClassName("elem-visible")
    String elemHiddenOff(); //not used

    @ClassName("elem-hidden")
    String elemHiddenOn(); //not used

    @ClassName("error-field")
    String errorField(); //ListChangeMonitor, FormDemandAdvView, SupplierCreationView

    @ClassName("info-message")
    String infoMessage(); //not used

    @ClassName("error-message")
    String errorMessage(); //not used

    @ClassName("disabled-text")
    String disabledText(); //not used

    @ClassName("loading-popup")
    String loadingPopup(); //not used

    @ClassName("hyperlink")
    String hyperlinkInline(); //not used

    @ClassName("user_content")
    String userContent(); //Admin views

    @ClassName("user")
    String user(); //AdminPresenter

    @ClassName("empty_style")
    String emptyStyle(); //SupplierCreationPresenter, AdminDemandsPresenter

    @ClassName("textBoxAsLabel")
    String textBoxAsLabel();

    @ClassName("button-empty")
    String buttonEmpty(); //ManagerView

    @ClassName("button-right-small")
    String buttonRightSmall(); //DemandCreation, SupplierCreation, ErrorVIew

    @ClassName("button-right-medium")
    String buttonRightMedium(); //DemandCreation

    @ClassName("button-right-large")
    String buttonRightLarge(); //DemandCreation, SupplierCreation

    @ClassName("button-left-small")
    String buttonLeftSmall(); //not used

    @ClassName("button-left-medium")
    String buttonLeftMedium(); //DemandCreation

    @ClassName("button-left-large")
    String buttonLeftLarge(); //DemandCreation, SupplierCreation

    @ClassName("button-grey")
    String buttonGrey(); //Login, Activation, HomeDemands, HomeSuppliers, EmailDialogPopup, SettingsView, FooterView

    @ClassName("button-long-grey")
    String buttonLongGrey(); //Activation

    @ClassName("button-green")
    String buttonGreen(); //Login, Activation, UserSettings, FeedbackPopup

    @ClassName("button-ok-green")
    String buttonOkGreen(); //EmailDialog

    @ClassName("selector-button-brown")
    String selectorButtonBrown(); //ManagerView

    @ClassName("selected-items-widget")
    String selectedItemsWidget(); //CellBrowserView

    @ClassName("switch-left")
    String switchLeft(); //UserRegistration, FormDemandAdvView

    @ClassName("switch-right")
    String switchRight(); //UserRegistration, FormDemandAdvView

//    @ClassName("changeMonitorStyle")
//    String changeMonitorStyle(); //CHangeMOnitor

    @ClassName("form-fields")
    String formFields(); //many views

    @ClassName("well-container")
    String wellContainer(); //many views

    @ClassName("validation-container")
    String validationContainer(); //ValidationMonitor

    /** How it works **/
    @ClassName("how-it-works-container")
    String howItWorksContainer(); //HowItWorks, ErrorVIew, AboutUs, FAQ, PrivacyPolicy

    @ClassName("myListBox")
    String myListBox(); //MyListBox, HomeDemandsSearch, EmailDialogPopup, NotificationItem

    /** Alert Box styles **/
    @ClassName("alert-container")
    String alertContainer(); //Login, ActionCodePopup

    @ClassName("catLocManager")
    String catLocManager();
}
