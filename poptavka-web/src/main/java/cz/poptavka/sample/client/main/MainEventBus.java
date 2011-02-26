package cz.poptavka.sample.client.main;


import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.InitHistory;
import com.mvp4g.client.annotation.module.ChildModule;
import com.mvp4g.client.annotation.module.ChildModules;
import com.mvp4g.client.event.EventBus;

import cz.poptavka.sample.client.home.HomeModule;
import cz.poptavka.sample.client.login.LoginModule;

@Events(startView = MainView.class, historyOnStart = true)
@ChildModules({
    @ChildModule(moduleClass = LoginModule.class, async = true, autoDisplay = false),
    @ChildModule(moduleClass = HomeModule.class, async = true, autoDisplay = false)
    })
public interface MainEventBus extends EventBus {

    /**
     * Init method for layout and history initialization. Layout initialization
     * contains menu injection.
     * To inject more widgets, update onStart() method of MainPresenter
     */
    @InitHistory
    @Event(handlers = MainPresenter.class)
    void start();

    /**
     * Init login module
     */
    @Event(modulesToLoad = LoginModule.class)
    void initLogin();

    /**
     * Init home module (unlogged user)
     */
    @Event(modulesToLoad = HomeModule.class)
    void initHome();

//    /**
//     * Init user module (Logged user)
//     */
//    @Event(modulesToLoad = UserModule.class)
//    void initUser();

    /**
     * Sets widget to View's body section. Body section can hold one widget only.
     *
     * @param body
     */
    @Event(handlers = MainPresenter.class)
    void setBodyHolderWidget(Widget body);

    /**
     * Sets widget to login-area section of header widget. Designed for Login Wigdet.
     *
     * @param login login widget
     */
    @Event (handlers = MainPresenter.class)
    void setLoginWidget(Widget login);
}
