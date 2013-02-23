package com.eprovement.poptavka.client.root.interfaces;

import com.google.gwt.user.client.ui.IsWidget;

public interface IRootView extends IsWidget {

    public interface IRootPresenter {
    }

    void setLogoStyle(String style);

    void setHeader(IsWidget header);

    void setMenu(IsWidget menu);

    void setSearchBar(IsWidget searchBar);

    void setUpSearchBar(IsWidget advanceSearchWidget);

    void setBody(IsWidget body);

    void setFooter(IsWidget footer);

}
