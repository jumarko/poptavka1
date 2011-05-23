package cz.poptavka.sample.client.main.login;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.main.MainEventBus;
import cz.poptavka.sample.client.service.demand.UserRPCServiceAsync;
import cz.poptavka.sample.shared.domain.UserDetail;

@Presenter(view = LoginPopupView.class, multiple = true)
public class LoginPopupPresenter extends LazyPresenter<LoginPopupPresenter.LoginPopupInterface, MainEventBus> {

    public interface LoginPopupInterface extends LazyView {

        Button getLoginButton();

        void show();

        boolean isValid();

        String getLogin();

        String getPassword();

        void hide();

        void setLoadingStatus();

        void setUnknownError();

        FocusWidget getCancelButton();

        void setLoginError();

    }

    @Inject
    private UserRPCServiceAsync userService;

    public void bindView() {
        view.getLoginButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                boolean isValid = view.isValid();
                if (isValid) {
                    registerUser(view.getLogin(), view.getPassword());
                }
            }
        });
        view.getCancelButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                if (!historyBack()) {
                    eventBus.atHome();
                }
                view.hide();
            }
        });
    }

    public void onLogin() {
        view.show();
    }

    private void registerUser(String login, String password) {
        view.setLoadingStatus();
        userService.loginUser(new UserDetail(login, password), new AsyncCallback<UserDetail>() {

            @Override
            public void onFailure(Throwable arg0) {
                view.setUnknownError();
            }

            @Override
            public void onSuccess(UserDetail user) {
                if (user != null) {
                    eventBus.atAccount(user);
                } else {
                    view.setLoginError();
                }
            }
        });
    }

    private static native boolean historyBack()
    /*-{
        var length = window.history.length;
        if (length > 0) {
            parent.history.back();
            return true;
        } else {
            return false;
        }
    }-*/;

}
