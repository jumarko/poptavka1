package cz.poptavka.sample.client.root.interfaces;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public interface IRootView extends IsWidget {

    public interface IRootPresenter {

    }

    void setMenu(IsWidget menu);

    void setSearchBar(IsWidget searchBar);

    void setBody(IsWidget body);

    void setFooter(IsWidget footer);

    void setHeader(IsWidget header);

    IsWidget getHeader();

    SimplePanel getSearchBar();

    Widget getBody();

    Widget getMenu();

}
