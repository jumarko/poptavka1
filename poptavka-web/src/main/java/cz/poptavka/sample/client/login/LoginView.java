package cz.poptavka.sample.client.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class LoginView extends Composite implements LoginPresenter.LoginInterface {

    private static LoginViewUiBinder uiBinder = GWT.create(LoginViewUiBinder.class);
    interface LoginViewUiBinder extends UiBinder<Widget, LoginView> {   }

    @UiField
    Anchor loginButton;

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Dummy loging method.
     */
    public void login() {
        Window.alert("Login funcionality not implemented yet.");
    }

    @Override
    public HasClickHandlers getLoginButton() {
        return loginButton;
    }

    @Override
    public Widget getViewWidget() {
        return this;
    }



}
