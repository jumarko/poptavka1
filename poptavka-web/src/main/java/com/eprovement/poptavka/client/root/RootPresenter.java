/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.root;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.root.interfaces.IRootSelectors;
import com.eprovement.poptavka.client.root.interfaces.IRootView;
import com.eprovement.poptavka.client.root.interfaces.IRootView.IRootPresenter;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;

/**
 * Holds business logic for root module.
 * <b><i>Note:</i></b>
 * Presenters holds business logic for particular module. They have access to
 * eventBuses (and through them to other application parts),
 * and to views that can be swithed easily because everything is defined through interfaces.
 * @author Mato
 */
@Presenter(view = RootView.class)
public class RootPresenter extends BasePresenter<IRootView, RootEventBus>
    implements IRootPresenter {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private IRootSelectors animation = GWT.create(IRootSelectors.class);
    private static final String SLIDE_PX = "90px";
    private static final int SLIDE_DURATION = 500;
    private boolean runResize = false;
    private boolean isMenuPanelVisible = false;

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    /**
     * Invokes actions at module start up.
     * <b><i>Note:</i></b>
     * When your module starts, you may want to automatically fire an event
     * so that actions needed at first can occur.
     * Fires only once at module first run before any other methods.
     * Must be defined in eventBus by <b>Start</b> anotation
     */
    public void onStart() {
        GWT.log("Root presenter loaded");
        eventBus.atHome();
        eventBus.goToSearchModule();
        //If application is started because of URL, the token is full when starting
        if (History.getToken().isEmpty()) {
            GWT.log("++++++++++++++++++++++++++++NORMAL START OF APP");
            // normal start of app
            eventBus.goToHomeWelcomeModule();
            Storage.setAppCalledByURL(false);
        } else {
            // start of app by entering URL token
            GWT.log("++++++++++++++++++++++++++++START BY URL OF APP");
            Storage.setAppCalledByURL(true);
        }
    }

    /**
     * This event will be called in case an error occurs while loading the
     * ChildModule code.
     *
     * @param reason - An object may be fired for the event used in case of
     * error but the type of this object must be compatible with
     * java.lang.Throwable. In this case, the error returned by the
     * RunAsync object is passed to the event.
     */
    public void onErrorOnLoad(Throwable reason) {
        eventBus.displayError(500, null); // HTTP 500 - internal server error.
    }

    /**
     * This event will be called before starting to load the ChildModule code.
     * You can for example decide to display a wait popup.
     */
    public void onBeforeLoad() {
        eventBus.loadingShow(Storage.MSGS.loading());
    }

    /**
     * This event will be called after the code is done loading.
     * You can for example decide to hide a wait popup.
     */
    public void onAfterLoad() {
        eventBus.loadingHide();
    }

    /**************************************************************************/
    /* Bind events                                                            */
    /**************************************************************************/
    @Override
    public void bind() {
        /* Advantage - can reset animation styles on resize
         * Disadvantage - calles each time browser is resized.
         *              - for changing layout to landscape and vice versa it's called only once, which is good. */
        //With this we can change view which is more flexible instead of using responsive styles???
        view.getPage().addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                //Don't run resize on browser startup
                if (runResize) {
                    eventBus.resetAnimation(event.getWidth());
                }
                runResize = true;
                //set shorter dates on small screens
                Storage.get().initDateTimeFormat(event.getWidth() > 1200);

                //Calculate height each time browser resizes for scroll panels to behave correctly-->
                int bodyHeight = view.getPage().getOffsetHeight();
                bodyHeight -= view.getToolbar().getOffsetHeight();
                bodyHeight -= view.getHeader().getOffsetHeight();
                view.getBody().setHeight(bodyHeight + "px");
            }
        });
    }

    /**************************************************************************/
    /* Layout events                                                          */
    /**************************************************************************/
    /**
     * Invokes action when user loggs in.
     */
    public void onAtAccount() {
        GWT.log("User has logged in and his user data are about to be retrieved");
        // TODO LATER ivlcek: comment for production
        //showDevelUserInfoPopupThatShouldBedeletedAfter();
    }

    /**
     * Invokes action when user loggs out.
     */
    public void onAtHome() {
        // nothing by default
    }

    /**************************************************************************/
    /* Business events.                                                       */
    /**************************************************************************/
    /**
     * Sets widget to header container.
     * @param header widget
     */
    public void onSetHeader(IsWidget header) {
        GWT.log("Header widget set");
        view.getHeader().setWidget(header);
    }

    /**
     * Sets widget to toolbar container.
     * @param toolbar widget
     */
    public void onSetToolbar(IsWidget toolbar) {
        GWT.log("Toolbar widget set");
        view.getToolbar().setWidget(toolbar);
    }

    /**
     * Sets widget to body container.
     * @param body widget
     */
    public void onSetBody(IsWidget body) {
        GWT.log("Body widget set");
        view.getBody().setWidget(body);
    }

    /**
     * Removes animation styles on resize.
     * When animation is used it overrides application styles with its own.
     * It is not a problem unless application uses responsive design.
     * When resizing, responsive design should take care to redesign application,
     * but it is not if animation took place and overrided them.
     * Therefore remove them on resize.
     */
    public void onResetAnimation(int actualWidth) {
        animation.getToolbarContainer().removeAttr("style");
        animation.getBodyContainer().removeAttr("style");
        isMenuPanelVisible = false;
    }

    /**************************************************************************/
    /* Business events handled by presenter                                   */
    /**************************************************************************/
    public void onNotFound() {
        eventBus.start();
        eventBus.setBody(new Label("Page not found"));
    }

    /**
     * Hides menu.
     */
    public void onCloseMenu() {
        if (isMenuPanelVisible) {
            isMenuPanelVisible = false;
            animation.getBodyContainer().animate("top: -=" + SLIDE_PX, SLIDE_DURATION);
            animation.getToolbarContainer().animate("top: -=" + SLIDE_PX, SLIDE_DURATION);
        }
    }
    /**
     * Opens menu.
     */
    public void onOpenMenu() {
        if (!isMenuPanelVisible) {
            isMenuPanelVisible = true;
            animation.getToolbarContainer().animate("top: +=" + SLIDE_PX, SLIDE_DURATION);
            animation.getBodyContainer().animate("top: +=" + SLIDE_PX, SLIDE_DURATION);
        }
    }
    // Inject widgets for user registration
    //--------------------------------------------------------------------------
    //Martin - for devel purposes
//    private void showDevelUserInfoPopupThatShouldBedeletedAfter() {
//        final DialogBox userInfoPanel = new DialogBox(false, false);
//        userInfoPanel.setText("User Info Box");
//        userInfoPanel.setWidth("200px");
//        String br = "<br />";
//        StringBuilder sb = new StringBuilder("<b>User Info:</b>" + br);
//        UserDetail userDetail = Storage.getUser();
//        BusinessUserDetail user = Storage.getBusinessUserDetail();
//        sb.append("ID: " + user.getUserId() + br);
//
//        sb.append("<i>-- Business user roles --</i>" + br);
//        if (user.getBusinessRoles().contains(BusinessRole.CLIENT)) {
//            sb.append("<b><i>BusinessRole: CLIENT</i></b>" + br);
//            sb.append("ClientID: " + user.getClientId() + br);
//            sb.append("Demands Messages: " + "n/a" + " / " + "n/a" + br);
//            sb.append("Demands Offers: " + "n/a" + " / " + "n/a" + br);
//            sb.append("<i>-- -- -- --</i>" + br);
//        }
//        if (user.getBusinessRoles().contains(BusinessRole.SUPPLIER)) {
//            sb.append("<b><i>BusinessRole: SUPPLIER</i></b>" + br);
//            sb.append("SupplierID: " + user.getSupplierId() + br);
//            sb.append("Potentional Demands: " + "n/a" + " / " + "n/a" + br);
//            sb.append("<i>-- -- -- --</i>" + br);
//        }
//        if (user.getBusinessRoles().contains(BusinessRole.PARTNER)) {
//            sb.append("<b><i>BusinessRole: PARTNER</i></b>" + br);
//            sb.append("<i>-- -- -- --</i>" + br);
//        }
////        if (user.getBusinessRoles().contains(BusinessRole.OPERATOR)) {
////            sb.append("<b><i>OPERATOR</i></b>" + br);
////            sb.append("<i>-- -- -- --</i>" + br);
////        }
//        sb.append("<i>-- User access roles --</i>" + br);
//        if (userDetail.getAccessRoles().contains(CommonAccessRoles.ADMIN)) {
//            sb.append("<b><i>ADMIN</i></b>" + br);
//        }
//        if (userDetail.getAccessRoles().contains(CommonAccessRoles.CLIENT)) {
//            sb.append("<b><i>CLIENT</i></b>" + br);
//        }
//        if (userDetail.getAccessRoles().contains(CommonAccessRoles.SUPPLIER)) {
//            sb.append("<b><i>SUPPLIER</i></b>" + br);
//        }
//        sb.append("<i>-- -- -- --</i>" + br);
//        sb.append("Messages: " + "n/a" + " / " + "n/a" + br);
//
//        HTML content = new HTML(sb.toString());
//        Button closeButton = new Button("Close");
//        closeButton.addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                userInfoPanel.hide();
//            }
//        });
//        FlowPanel m = new FlowPanel();
//        m.add(content);
//        m.add(closeButton);
//        userInfoPanel.add(m);
//        userInfoPanel.setPopupPosition(Window.getClientWidth() - 200, 20);
//        userInfoPanel.show();
//    }
}