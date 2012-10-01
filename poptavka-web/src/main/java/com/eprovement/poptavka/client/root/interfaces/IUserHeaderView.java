package com.eprovement.poptavka.client.root.interfaces;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.IsWidget;

public interface IUserHeaderView extends IsWidget {

    public interface IUserHeaderPresenter {
    }

    Anchor getLogoutLink();

}
