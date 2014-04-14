package com.eprovement.poptavka.client.root.interfaces;

import com.eprovement.poptavka.client.root.header.ui.LoginElement;
import com.eprovement.poptavka.client.root.header.ui.LogoutElement;
import com.eprovement.poptavka.client.root.header.ui.NotificationsElement;
import com.eprovement.poptavka.client.root.header.ui.SearchElement;
import com.github.gwtbootstrap.client.ui.base.IconAnchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;

public interface IHeaderView extends IsWidget {

    public interface IHeaderPresenter {
    }

    Button getLogo();

    SimplePanel getMenu();

    SearchElement getSearch();

    NotificationsElement getNotifications();

    LoginElement getLogin();

    LogoutElement getLogout();

    IconAnchor getSettingsAnchor();
}
