package cz.poptavka.sample.client.main.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusWidget;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.main.common.SimpleIconLabel;
import cz.poptavka.sample.client.main.login.LoginPopupPresenter.LoginPopupInterface;
import cz.poptavka.sample.client.resources.StyleResource;

public class LoginPopupView extends PopupPanel implements  LoginPopupInterface {

    private static LoginPopupUiBinder uiBinder = GWT.create(LoginPopupUiBinder.class);
    interface LoginPopupUiBinder extends UiBinder<Widget, LoginPopupView> {
    }

    @UiField TextBox mailBox;
    @UiField PasswordTextBox passBox;
    @UiField Button loginBtn, cancelBtn;

    SimpleIconLabel statusLabel = null;

    @Override
    public void createView() {
        setWidget(uiBinder.createAndBindUi(this));
        this.setModal(true);
        this.setGlassEnabled(true);
        center();
        mailBox.setFocus(true);
    }

    @Override
    public Button getLoginButton() {
        return loginBtn;
    }

    @Override
    public boolean isValid() {
        if ((mailBox.getText().length() == 0) || (passBox.getText().length() == 0)) {
            Element elem = (Element) Document.get().getElementById("loginStatus");
            clearStatusLabel(elem);
            statusLabel = new SimpleIconLabel("Bad input", true);
            statusLabel.setImageResource(StyleResource.INSTANCE.images().errorIcon24());
            elem.appendChild(statusLabel.getElement());
            return false;
        }

        return true;
    }

    @Override
    public String getLogin() {
        return mailBox.getText();
    }

    @Override
    public String getPassword() {
        return passBox.getText();
    }

    @Override
    public void setLoadingStatus() {
        Element elem = (Element) Document.get().getElementById("loginStatus");
        clearStatusLabel(elem);
        statusLabel = new SimpleIconLabel("Loading", true);
        statusLabel.setImageResource(StyleResource.INSTANCE.images().loadIcon24());
        elem.appendChild(statusLabel.getElement());
    }

    @Override
    public void setUnknownError() {
        // TODO Auto-generated method stub

    }

    @Override
    public FocusWidget getCancelButton() {
        return cancelBtn;
    }

    @Override
    public void setLoginError() {
        Element elem = (Element) Document.get().getElementById("loginStatus");
        clearStatusLabel(elem);
        statusLabel = new SimpleIconLabel("Wrong name", true);
        statusLabel.setImageResource(StyleResource.INSTANCE.images().errorIcon24());
        elem.appendChild(statusLabel.getElement());
    }

    private void clearStatusLabel(Element parent) {
        NodeList<Node> list = parent.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.getItem(i);
            parent.removeChild(node);
        }
    }
}
