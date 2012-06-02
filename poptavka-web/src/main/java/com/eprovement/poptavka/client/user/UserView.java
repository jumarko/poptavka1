package com.eprovement.poptavka.client.user;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.eprovement.poptavka.client.root.ReverseCompositeView;
import com.eprovement.poptavka.client.user.IUserMenuView.IUserMenuPresenter;

public class UserView extends ReverseCompositeView<IUserMenuPresenter> implements IUserMenuView {
//        Composite implements IUserMenuView {

    private static UserViewUiBinder uiBinder = GWT.create(UserViewUiBinder.class);

    interface UserViewUiBinder extends UiBinder<Widget, UserView> {
    }
//    @UiField
//    HTMLPanel container;
    //menu section
    @UiField
    UListElement menuList;
//    @UiField
//    Hyperlink linkHome;
    @UiField
    Button demands, messages, settings, contacts, administration;
//    @UiField
//    SimplePanel contentHolder, searchPanel;

    public UserView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public HasClickHandlers getDemandsButton() {
        return demands;
    }

    @Override
    public HasClickHandlers getMessagesButton() {
        return messages;
    }

    @Override
    public HasClickHandlers getSettingsButton() {
        return settings;
    }

    @Override
    public HasClickHandlers getContactsButton() {
        return contacts;
    }

    @Override
    public HasClickHandlers getAdministrationButton() {
        return administration;
    }
}
