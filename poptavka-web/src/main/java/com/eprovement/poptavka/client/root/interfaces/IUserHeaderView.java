package com.eprovement.poptavka.client.root.interfaces;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuItem;

public interface IUserHeaderView extends IsWidget {

    public interface IUserHeaderPresenter {
    }

    Label getNewMessagesCount();

    MenuItem getUsername();

    MenuItem getMenuLogOut();

    MenuItem getMenuMyProfile();

    MenuItem getMenuHelp();

    MenuItem getMenuCustomerService();
}
