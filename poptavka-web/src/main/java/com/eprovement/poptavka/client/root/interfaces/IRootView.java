package com.eprovement.poptavka.client.root.interfaces;

import com.google.gwt.user.client.ui.IsWidget;

public interface IRootView extends IsWidget {

    public interface IRootPresenter {
    }

    void setHeader(IsWidget header);

    void setMenu(IsWidget menu);

    void setSearchBar(IsWidget searchBar);

    void setUpSearchBar(IsWidget searchView, boolean cat, boolean loc, boolean advBtn);

    void setBody(IsWidget body);

    void setFooter(IsWidget footer);

    void setWaitVisible(boolean visible);

}
