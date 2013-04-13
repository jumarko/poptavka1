package com.eprovement.poptavka.client.root.interfaces;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;

public interface IRootView extends IsWidget {

    public interface IRootPresenter {
    }

    Button getLogo();

    void setHeader(IsWidget header);

    void setMenu(IsWidget menu);

    void setSearchBar(IsWidget searchBar);

    void setUpSearchBar(IsWidget advanceSearchWidget);

    void setBody(IsWidget body);

}
