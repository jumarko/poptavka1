/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common;

import com.eprovement.poptavka.client.root.gateways.InfoWidgetsGateway;
import com.eprovement.poptavka.client.root.gateways.SearchModuleGateway;
import com.eprovement.poptavka.client.root.gateways.LoginGateway;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Event;

/**
 * This interface contains common methods from Child Modules that are always forwarded to parent Module.
 */
public interface BaseChildEventBus extends SearchModuleGateway, LoginGateway, InfoWidgetsGateway {

    @Event(forwardToParent = true)
    void setBody(IsWidget widget);

    @Event(forwardToParent = true)
    void setToolbarContent(String title, Widget toolbarContent);

    @Event(forwardToParent = true)
    void toolbarRefresh();

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

    @Event(forwardToParent = true)
    void openDetail();

    @Event(forwardToParent = true)
    void closeSubMenu();
}
