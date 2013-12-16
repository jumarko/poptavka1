package com.eprovement.poptavka.client.root.interfaces;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ResizeLayoutPanel;

public interface IRootView extends IsWidget {

    public interface IRootPresenter {
    }

    void setHeader(IsWidget header);

    void setToolbar(IsWidget toolbar);

    void setBody(IsWidget body);

    ResizeLayoutPanel getBody();
}
