/*
 * This interface contains common methods from Child Modules that are always forwarded to parent Module.
 */
package com.eprovement.poptavka.client.root;

import com.eprovement.poptavka.client.root.gateways.InfoWidgetsGateway;
import com.eprovement.poptavka.client.root.gateways.SearchModuleGateway;
import com.eprovement.poptavka.client.root.gateways.LoginGateway;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Event;

public interface BaseChildEventBus extends SearchModuleGateway, LoginGateway, InfoWidgetsGateway {

    @Event(forwardToParent = true)
    void setBody(IsWidget widget);

    @Event(forwardToParent = true)
    void setToolbarContent(String title, Widget toolbarContent, boolean hasAnimationLayout);

    @Event(forwardToParent = true)
    void setFooter(SimplePanel footerPanel);

    @Event(forwardToParent = true)
    void loadingShow(String loadingMessage);

    @Event(forwardToParent = true)
    void loadingHide();

    @Event(forwardToParent = true)
    void menuStyleChange(int loadedModule);

    @Event(forwardToParent = true)
    void sendStatusMessage(String statusMessageBody);
}
