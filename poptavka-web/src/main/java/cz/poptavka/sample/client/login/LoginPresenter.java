package cz.poptavka.sample.client.login;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

@Presenter(view = LoginView.class)
public class LoginPresenter extends LazyPresenter<LoginPresenter.LoginInterface, LoginEventBus> {

    public interface LoginInterface extends LazyView {

        HasClickHandlers getLoginButton();

        Widget getViewWidget();

      //dummy method
        void login();
    }

    public void bindView() {
        view.getLoginButton().addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent arg0) {
                view.login();
            }
        });
    }

    public void onInitLogin() {
        eventBus.setLoginWidget(view.getViewWidget());
    }
}
