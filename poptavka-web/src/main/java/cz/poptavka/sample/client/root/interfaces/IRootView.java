package cz.poptavka.sample.client.root.interfaces;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public interface IRootView extends IsWidget {

    public interface IRootPresenter {
    }

    void setHeader(IsWidget header);

    void setMenu(IsWidget menu);

    void setSearchBar(IsWidget searchBar);

    void setBody(IsWidget body);

    void setFooter(IsWidget footer);

    // TODO praso - tieto metody by sa mali odstranit pozri na mvp4g layout tutorial part1
    IsWidget getHeader();

    SimplePanel getSearchBar();

    Widget getBody();

    Widget getMenu();

    void setWaitVisible(boolean visible);
}
