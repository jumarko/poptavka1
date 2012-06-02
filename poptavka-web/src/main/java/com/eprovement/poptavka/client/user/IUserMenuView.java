package com.eprovement.poptavka.client.user;


import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.IsWidget;

public interface IUserMenuView extends IsWidget {

    public interface IUserMenuPresenter {
    }

    HasClickHandlers getDemandsButton();

    HasClickHandlers getMessagesButton();

    HasClickHandlers getSettingsButton();

    HasClickHandlers getContactsButton();

    HasClickHandlers getAdministrationButton();
}
