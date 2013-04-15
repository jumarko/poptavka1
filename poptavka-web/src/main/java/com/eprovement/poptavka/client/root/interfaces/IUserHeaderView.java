package com.eprovement.poptavka.client.root.interfaces;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PushButton;

public interface IUserHeaderView extends IsWidget {

    public interface IUserHeaderPresenter {
    }

    Label getNewMessagesCount();

    Label getNewSystemMessagesCount();

    MenuItem getUsername();

    MenuItem getMenuLogOut();

    MenuItem getMenuMyProfile();

    PushButton getPushButton();

    PushButton getPushSystemButton();

}
