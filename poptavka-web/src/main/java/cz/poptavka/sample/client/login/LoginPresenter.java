package cz.poptavka.sample.client.login;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

@Presenter(view = LoginView.class)
public class LoginPresenter extends LazyPresenter<LoginPresenter.LoginInterface, LoginEventBus> {

    public interface LoginInterface extends LazyView {

        Anchor getLoginButton();

        Widget getViewWidget();

      //dummy method
        void login();
    }

    private boolean loggedIn = false;

    public void bindView() {
        view.getLoginButton().addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent arg0) {
                if (loggedIn) {
                    eventBus.atHome();
                    view.getLoginButton().setText("Log In");
                } else {
                    eventBus.initUser();
                    view.getLoginButton().setText("Log Out");
                }
                loggedIn = !loggedIn;
            }
        });
    }

    public void onInitLogin() {
        //TODO check permanent login

        //init view
        eventBus.setLoginWidget(view.getViewWidget());
    }
}
