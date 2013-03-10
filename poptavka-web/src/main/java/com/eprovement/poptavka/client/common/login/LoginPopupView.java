package com.eprovement.poptavka.client.common.login;

import com.eprovement.poptavka.client.common.login.LoginPopupPresenter.LoginPopupInterface;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail.BusinessRole;
import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.github.gwtbootstrap.client.ui.ProgressBar;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

import com.mvp4g.client.view.ReverseViewInterface;
import java.util.HashMap;

//public class LoginPopupView extends PopupPanel implements LoginPopupInterface {
public class LoginPopupView extends Composite
        implements ReverseViewInterface<LoginPopupPresenter>, LoginPopupInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private LoginPopupViewUiBinder uiBinder = GWT.create(LoginPopupViewUiBinder.class);

    interface LoginPopupViewUiBinder extends UiBinder<Widget, LoginPopupView> {
    }
    /**************************************************************************/
    /* Attribute                                                              */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField
    Modal modal;
    @UiField
    ListBox list;
    @UiField
    TextBox emailTextBox;
    @UiField
    PasswordTextBox passwordTextBox;
    @UiField
    Alert status;
    @UiField
    ProgressBar progressBar;
    @UiField
    Icon icon;
    @UiField
    Label infoLabel;
    @UiField
    Button submitBtn;
    @UiField
    Button cancelBtn;
    @UiField
    ControlGroup emailControlGroup;
    @UiField
    ControlGroup passwordControlGroup;
    /** Class attributes. **/
    private LoginPopupPresenter presenter;
    /**************************************************************************/
    /* DEVEL ONLY                                                             */
    /**************************************************************************/
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
                name = "jumarko@gmail.com";
                pass = "123123";
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

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        initFastLoginForDevel();
        progressBar.setVisible(false);
        icon.setVisible(true);
        emailTextBox.setFocus(true);
        modal.show();
    }

    // TODO prod - delete and user real form
    private void initFastLoginForDevel() {
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
    }

    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/
    @UiHandler("cancelBtn")
    public void cancelBtnHandler(ClickEvent e) {
        modal.hide();
    }

    @UiHandler("submitBtn")
    public void submitBtnHandler(ClickEvent e) {
        progressBar.setVisible(true);
        presenter.doLogin();
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /** Getters values from HTML form. */
    @Override
    public TextBox getPassword() {
        return passwordTextBox;
    }

    @Override
    public TextBox getLogin() {
        return emailTextBox;
    }

    @Override
    public boolean isValid() {
        // TODO ivlcek - maybe we should check the lenght of email and password to avoid 1 - 4 character long values
        // TODO ivlcek - are values somehow checked against some sort sql attacks?
        if ((getLogin().getText().length() == 0) || (getPassword().getText().length() == 0)) {
            setErrorMessage(Storage.MSGS.commonEmptyCredentials());
            return false;
        }
        return true;
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    private void setLogin(BusinessRole role) {
        emailTextBox.setValue(privateUsers.get(role).getName());
        passwordTextBox.setValue(privateUsers.get(role).getPass());
    }

    /** Called to hide popup. **/
    public void hidePopup() {
        modal.hide();
    }

    /**
     * Method displays the current status in the login process.
     * Status consists of status icon and status message.
     *
     * @param message - message to be displayed in LoginPopupView
     */
    @Override
    public void setLoadingStatus(String message) {
        emailControlGroup.setType(ControlGroupType.NONE);
        passwordControlGroup.setType(ControlGroupType.NONE);
        status.setType(AlertType.SUCCESS);
        progressBar.setVisible(true);
        icon.setVisible(false);
        infoLabel.setText(message);
        cancelBtn.setEnabled(false);
        submitBtn.setEnabled(false);
    }

    @Override
    public void setLoadingProgress(Integer newPercentage, String newMessage) {
        if (newPercentage != null) {
            progressBar.setPercent(newPercentage);
        }
        if (newMessage != null) {
            infoLabel.setText(newMessage);
        }
    }

    /**
     * This method displays error status when some problem occurs in application.
     * We ask users to try again later.
     */
    @Override
    public void setErrorMessage(String message) {
        emailControlGroup.setType(ControlGroupType.ERROR);
        passwordControlGroup.setType(ControlGroupType.ERROR);
        status.setType(AlertType.ERROR);
        progressBar.setVisible(false);
        icon.setVisible(true);
        icon.setType(IconType.REMOVE_SIGN);
        infoLabel.setText(message);
        cancelBtn.setEnabled(true);
        submitBtn.setEnabled(true);
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
