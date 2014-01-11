package com.eprovement.poptavka.client.root.interfaces;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;
import com.google.gwt.user.client.ui.SimplePanel;

public interface IRootView extends IsWidget {

    public interface IRootPresenter {
    }

    SimplePanel getHeader();

    SimplePanel getToolbar();

    SimplePanel getBody();

    ResizeLayoutPanel getPage();
}
