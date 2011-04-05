package cz.poptavka.sample.client.login;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBus;

@Events(startView = LoginView.class, module = LoginModule.class)
public interface LoginEventBus extends EventBus {

    /**
     * Init Login presenter.
     */
    @Event(handlers = LoginPresenter.class)
    void initLogin();

    @Event(forwardToParent = true)
    void atHome();

    @Event(forwardToParent = true)
    void initUser();

    /**
     * Send widget to parent to display it.
     *
     * @param body
     */
    @Event(forwardToParent = true)
    void setLoginWidget(Widget body);
}
