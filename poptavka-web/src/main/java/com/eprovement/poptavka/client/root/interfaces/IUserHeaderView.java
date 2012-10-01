package com.eprovement.poptavka.client.root.interfaces;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;

public interface IUserHeaderView extends IsWidget {

    public interface IUserHeaderPresenter {
    }

    Anchor getLogoutLink();
    Label getUsername();
    Label getNewMessagesCount();

}
