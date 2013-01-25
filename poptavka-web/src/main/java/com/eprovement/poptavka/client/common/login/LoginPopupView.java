package com.eprovement.poptavka.client.common.login;

import com.eprovement.poptavka.client.common.SimpleIconLabel;
import com.eprovement.poptavka.client.common.login.LoginPopupPresenter.LoginPopupInterface;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail.BusinessRole;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.FlowPanel;

import com.mvp4g.client.view.ReverseViewInterface;
import java.util.HashMap;

//public class LoginPopupView extends PopupPanel implements LoginPopupInterface {
public class LoginPopupView extends PopupPanel
        implements ReverseViewInterface<LoginPopupPresenter>, LoginPopupInterface {

    /************** DEVEL ONLY *********/
    private static HashMap<BusinessRole, PrivateUser> privateUsers = new HashMap<BusinessRole, PrivateUser>();

    class PrivateUser {

        private String name;
        private String pass;

        public PrivateUser(BusinessRole role) {
            if (role == BusinessRole.CLIENT) {
                name = "webgres@gmail.com";
                pass = "123123";
            }
            if (role == BusinessRole.SUPPLIER) {
                name = "pras3xer@gmail.com";
                pass = "123123";
            }
            if (role == BusinessRole.ADMIN) {
                name = "admin@admin.cz";
                pass = "svnsvn";
            }
        }

        public String getName() {
            return name;
        }

        public String getPass() {
            return pass;
        }
    }

    {
        privateUsers.put(BusinessRole.CLIENT, new PrivateUser(BusinessRole.CLIENT));
        privateUsers.put(BusinessRole.SUPPLIER, new PrivateUser(BusinessRole.SUPPLIER));
        privateUsers.put(BusinessRole.ADMIN, new PrivateUser(BusinessRole.ADMIN));
    }
    /************** DEVEL ONLY *********/
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private SimpleIconLabel statusLabel = null;
    private LoginPopupPresenter presenter;
    private Button closeButton;
    private Button submitButton;
    private TextBox emailTextBox;
    private PasswordTextBox passwordTextBox;
    private Label emailLabel;
    private Label passwordLabel;
    private VerticalPanel vp;

    @Override
    public void createView() {
        FlowPanel panel = new FlowPanel();
        initFastLoginForDevel(panel);
        initRealLoginForm(panel);
        setWidget(panel);
        this.setModal(true);
        this.setGlassEnabled(true);
        this.center();
        this.show();
        emailTextBox.setFocus(true);
    }

    // TODO prod - delete and user real form
    private void initFastLoginForDevel(FlowPanel panel) {
        final ListBox list = new ListBox();
        list.setWidth("100%");
        list.setVisibleItemCount(privateUsers.size());
        list.insertItem("CLIENT", "CLIENT", 0);
        list.insertItem("SUPPLIER", "SUPPLIER", 1);
        list.insertItem("ADMIN", "ADMIN", 2);
        list.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                String roleString = list.getValue(list.getSelectedIndex());
                BusinessRole role = BusinessRole.values()[BusinessRole.valueOf(roleString).ordinal()];
                setLogin(role);
            }
        });
        panel.add(list);
    }

    private void setLogin(BusinessRole role) {
        emailTextBox.setValue(privateUsers.get(role).getName());
        passwordTextBox.setValue(privateUsers.get(role).getPass());
    }

    /** binding real form to create login popup. **/
    private void initRealLoginForm(FlowPanel panel) {
        emailLabel = new Label(MSGS.loginPopupEmail());
        passwordLabel = new Label(MSGS.loginPopupPass());

        emailTextBox = new TextBox();
        passwordTextBox = new PasswordTextBox();

        closeButton = new Button(MSGS.commonBtnClose());
        closeButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                    // TODO Martin - check this is correct
                if (History.getToken().contains("atHome")) {
                    History.forward();
                }
                if (History.getToken().contains("atAccount")) {
                    History.back();
                }
                hidePopup();
            }
        });
        closeButton.setEnabled(true);
        submitButton = new Button(MSGS.commonBtnSubmit());
        submitButton.setEnabled(true);
        submitButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                presenter.doLogin();
            }
        });

        HorizontalPanel hpe = new HorizontalPanel();
        hpe.add(emailLabel);
        hpe.add(emailTextBox);

        HorizontalPanel hpp = new HorizontalPanel();
        hpp.add(passwordLabel);
        hpp.add(passwordTextBox);

        HorizontalPanel hpb = new HorizontalPanel();
        hpb.add(submitButton);
        hpb.add(closeButton);

        vp = new VerticalPanel();
        vp.setSpacing(10);
        vp.add(hpe);
        vp.add(hpp);
        vp.add(hpb);

        panel.add(vp);
    }

    /** Called to hide popup. **/
    public void hidePopup() {
        this.hide();
    }

    /** Getters values from HTML form. */
    @Override
    public TextBox getPassword() {
//        return ((InputElement) Document.get().getElementById(PASSWORD_ID)).getValue();
        return passwordTextBox;
    }

    @Override
    public TextBox getLogin() {
//        return ((InputElement) Document.get().getElementById(LOGIN_ID)).getValue();
        return emailTextBox;
    }

    @Override
    public boolean isValid() {
        // TODO ivlcek - maybe we should check the lenght of email and password to avoid 1 - 4 character long values
        // TODO ivlcek - are values somehow checked against some sort sql attacks?
        if ((getLogin().getText().length() == 0) || (getPassword().getText().length() == 0)) {
            clearStatusLabel();
            statusLabel = new SimpleIconLabel(MSGS.commonEmptyCredentials(), true);
            statusLabel.setImageResource(StyleResource.INSTANCE.images().errorIcon24());
            statusLabel.setPopupEnabled(false);
            vp.add(statusLabel);
            return false;
        }
        return true;
    }

    /**
     * Method displays the current status in the login process.
     * Status consists of status icon and status message.
     *
     * @param localizableMessage - message to be displayed in LoginPopupView
     */
    @Override
    public void setLoadingStatus(String localizableMessage) {
        clearStatusLabel();
        statusLabel = new SimpleIconLabel(localizableMessage, true);
        statusLabel.setImageResource(StyleResource.INSTANCE.images().loadIcon24());
        statusLabel.setPopupEnabled(false);
        vp.add(statusLabel);
        closeButton.setEnabled(false);
        submitButton.setEnabled(false);
    }

    /**
     * This method displays error status when some problem occurs in application.
     * We ask users to try again later.
     */
    @Override
    public void setUnknownError() {
        clearStatusLabel();
        statusLabel = new SimpleIconLabel(MSGS.loginUnknownError(), true);
        statusLabel.setImageResource(StyleResource.INSTANCE.images().errorIcon24());
        statusLabel.setPopupEnabled(false);
        vp.add(statusLabel);
        closeButton.setEnabled(true);
        submitButton.setEnabled(true);
    }

    /**
     * This method displays error status when username or password was not found
     * in our database.
     */
    @Override
    public void setLoginError() {
        clearStatusLabel();
        statusLabel = new SimpleIconLabel(MSGS.wrongLoginMessage(), true);
        statusLabel.setImageResource(StyleResource.INSTANCE.images().errorIcon24());
        statusLabel.setPopupEnabled(false);
        vp.add(statusLabel);
        closeButton.setEnabled(true);
        submitButton.setEnabled(true);
    }

    private void clearStatusLabel() {
        if (statusLabel != null) {
            statusLabel.removeFromParent();
        }
    }

    @Override
    public void setPresenter(LoginPopupPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public LoginPopupPresenter getPresenter() {
        return presenter;
    }
}
