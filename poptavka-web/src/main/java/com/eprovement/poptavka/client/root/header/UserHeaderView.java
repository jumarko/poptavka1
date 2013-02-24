package com.eprovement.poptavka.client.root.header;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.root.ReverseCompositeView;
import com.eprovement.poptavka.client.root.interfaces.IUserHeaderView;
import com.eprovement.poptavka.client.root.interfaces.IUserHeaderView.IUserHeaderPresenter;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PushButton;

public class UserHeaderView extends ReverseCompositeView<IUserHeaderPresenter>
implements IUserHeaderView {

    private static UserHeaderViewUiBinder uiBinder = GWT.create(UserHeaderViewUiBinder.class);

    interface UserHeaderViewUiBinder extends UiBinder<Widget, UserHeaderView> {
    }

    public UserHeaderView() {
        initWidget(uiBinder.createAndBindUi(this));
    }
    /** login area **/
    @UiField
    HTMLPanel headerHolder;
    @UiField
    PushButton pushButton;
    @UiField
    Label newMessagesCount;
    @UiField
    MenuItem username, menuLogOut, menuMyProfile, menuHelp, menuCustomerService;

    public UserHeaderView(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public MenuItem getUsername() {
        return username;
    }

    @Override
    public MenuItem getMenuLogOut() {
        return menuLogOut;
    }

    @Override
    public MenuItem getMenuMyProfile() {
        return menuMyProfile;
    }

    @Override
    public MenuItem getMenuHelp() {
        return menuHelp;
    }

    @Override
    public MenuItem getMenuCustomerService() {
        return menuCustomerService;
    }

    /**
     * @return the newMessagesCount
     */
    @Override
    public Label getNewMessagesCount() {
        return newMessagesCount;
    }

    @UiHandler("pushButton")
    void handleClick(ClickEvent e) {
        Window.alert("Uzivatel bude presmerovany na modul Messages, kde uvidi neprecitane spravy");
    }
}
