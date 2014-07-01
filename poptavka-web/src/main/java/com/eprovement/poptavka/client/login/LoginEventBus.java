/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.login;

import com.eprovement.poptavka.client.home.createDemand.interfaces.IDemandCreationModule;
import com.eprovement.poptavka.client.home.createSupplier.interfaces.ISupplierCreationModule;
import com.eprovement.poptavka.client.login.activation.ActivationCodePopupHandler;
import com.eprovement.poptavka.client.login.activation.ActivationCodePopupPresenter;
import com.eprovement.poptavka.client.root.gateways.InfoWidgetsGateway;
import com.eprovement.poptavka.client.user.admin.interfaces.IAdminModule;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.root.UserActivationResult;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBusWithLookup;

/**
 * LoginEventBus serves all events for module LoginModule.
 * Uses for login user.
 *
 * @author Martin Slavkovsky
 */
@Events(startPresenter = LoginPopupPresenter.class, module = LoginModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface LoginEventBus extends EventBusWithLookup, InfoWidgetsGateway,
    IAdminModule.Gateway,
    ISupplierCreationModule.Gateway,
    IDemandCreationModule.Gateway {

    /**
     * Start event is called only when module is instantiated first time.
     * We can use it for history initialization.
     */
    @Start
    @Event(handlers = LoginPopupPresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. It there is nothing to carry out
     * in this method we should remove forward event to save the number of method invocations.
     * We can use forward event to switch css style for selected menu button.
     */
    @Forward
    @Event(handlers = LoginPopupPresenter.class, navigationEvent = true)
    void forward();

    /**************************************************************************/
    /* Parent's navigation events                                             */
    /**************************************************************************/
    @Event(forwardToParent = true)
    void goToHomeWelcomeModule();

    @Event(forwardToParent = true)
    void goToHomeDemandsModule(SearchModuleDataHolder filter);

    @Event(forwardToParent = true)
    void goToHomeSuppliersModule(SearchModuleDataHolder filter);

    @Event(forwardToParent = true)
    void goToSearchModule();

    @Event(forwardToParent = true)
    void goToClientDemandsModule(SearchModuleDataHolder filter, int loadWidget);

    @Event(forwardToParent = true)
    void goToSupplierDemandsModule(SearchModuleDataHolder filter, int loadWidget);

    @Event(forwardToParent = true)
    void goToMessagesModule(SearchModuleDataHolder filter, int loadWidget);

    @Event(forwardToParent = true)
    void goToSettingsModule();

    /**************************************************************************/
    /* Parent's other events                                                  */
    /**************************************************************************/
    @Event(forwardToParent = true)
    void atHome();

    @Event(forwardToParent = true)
    void atAccount();

    /**************************************************************************/
    /* Business events handled by LoginPresenter                              */
    /**************************************************************************/
    /**
     * Login is handled by HeaderPresenter so that event handler can be set up before calling LoginPopupPresenter.
     */
    @Event(handlers = LoginPopupPresenter.class)
    void login(int widgetToLoad);

    @Event(handlers = LoginPopupPresenter.class)
    void autoLogin(String email, String password, int widgetToLoad);

    /**
     * This method populates Storage i.e. our custom GWT session object with UserDetail.
     * A secured RPC service is invoked so this method can be called only if user is logged in and he opened our
     * website in new browser tab, which obviously starts the whole app from the begining. If user is not logged in
     * the RPC service will cause the initiation of loginPopupView via SecuredAsyncCallback.
     */
    @Event(handlers = LoginHandler.class)
    void logout(int widgetToLoad);

    @Event(handlers = LoginPopupPresenter.class)
    void setLoadingProgress(int percentage, String message);

    @Event(handlers = LoginPopupPresenter.class)
    void setErrorMessage(String message);

    @Event(handlers = LoginPopupPresenter.class)
    void hideView();

    /**************************************************************************/
    /* Business events handled by LoginHanlder                                */
    /**************************************************************************/
    @Event(handlers = LoginHandler.class)
    void verifyUser(String user, String password, int widgetToLoad);

    @Event(handlers = LoginHandler.class)
    void loginFromSession(int widgetToLoad);

    @Event(handlers = LoginHandler.class)
    void resetPassword(String email);

    @Event(handlers = LoginHandler.class)
    void showThankYouPopupAfterLogin(boolean displayThankYouPopup);
    /**************************************************************************/
    /* User Activation                                                        */
    /**************************************************************************/
    @Event(handlers = ActivationCodePopupPresenter.class)
    void initActivationCodePopup(BusinessUserDetail user, int widgetToLoad);

    @Event(handlers = ActivationCodePopupHandler.class)
    void activateUser(BusinessUserDetail user, String activationCode);

    @Event(handlers = ActivationCodePopupHandler.class)
    void sendActivationCodeAgain(BusinessUserDetail client);

    @Event(handlers = ActivationCodePopupHandler.class)
    void checkActivationEmail(BusinessUserDetail user);

    @Event(handlers = ActivationCodePopupPresenter.class)
    void responseActivateUser(UserActivationResult activationResult);

    @Event(handlers = ActivationCodePopupPresenter.class)
    void responseSendActivationCodeAgain(boolean sent);
}
