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
import com.eprovement.poptavka.client.root.interfaces.IRoot;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;

/**
 * Holds business logic for root module.
 * <b><i>Note:</i></b>
 * Presenters holds business logic for particular module. They have access to
 * eventBuses (and through them to other application parts),
 * and to views that can be swithed easily because everything is defined through interfaces.
 * @author Mato
 */
@Presenter(view = RootView.class)
public class RootPresenter extends BasePresenter<IRoot.View, RootEventBus>
    implements IRoot.Presenter {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private IRootSelectors animation = GWT.create(IRootSelectors.class);
    private static final String SLIDE_PX = "90px";
    private static final int SLIDE_DURATION = 500;
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
        //call rezize event to set correct body height
        eventBus.resize(Document.get().getClientWidth());
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
        //TODO 500 as constants
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
        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                //broadcast resize event to all listeners
                eventBus.resize(event.getWidth());
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
     * Removes animation styles on resize and recalculate scrollpanel height.
     * When animation is used it overrides application styles with its own.
     * It is not a problem unless application uses responsive design.
     * When resizing, responsive design should take care to redesign application,
     * but it is not if animation took place and overrided them.
     * Therefore remove them on resize.
     */
    public void onResize(int actualWidth) {
        //set shorter dates on small screens
        if (actualWidth > 1200) {
            Storage.get().initDateTimeFormat(true);
            //reset animation style (menu related)
            animation.getToolbarContainer().removeAttr("style");
            animation.getMenuPanel().removeAttr("style");
        }
        isMenuPanelVisible = false;
        //recalculate body height for scrollbar to behave correctly
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

            @Override
            public void execute() {
                //not working properly, trying to use Document.getHeight instead of Page.getHeight
//                int bodyHeight = view.getPage().getOffsetHeight();
                int bodyHeight = Document.get().getClientHeight();
                bodyHeight -= view.getToolbar().getOffsetHeight();
                bodyHeight -= view.getHeader().getOffsetHeight();
                view.getBody().setHeight(bodyHeight + "px");
            }
        });
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
}