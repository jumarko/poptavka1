/*
 * This interface contains common methods from Child Modules that are always forwarded to parent Module.
 */
package com.eprovement.poptavka.client.root;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Event;

public interface BaseChildEventBus {

    @Event(forwardToParent = true)
    void setBody(IsWidget widget);

    @Event(forwardToParent = true)
    void setFooter(SimplePanel footerHolder);

    @Event(forwardToParent = true)
    void setUpSearchBar(IsWidget searchView);

    @Event(forwardToParent = true)
    void loadingShow(String loadingMessage);

    @Event(forwardToParent = true)
    void loadingHide();

    @Event(forwardToParent = true)
    void login(int widgetToLoad);

    @Event(forwardToParent = true)
    void loginFromSession(int widgetToLoad);

    @Event(forwardToParent = true)
    void menuStyleChange(int loadedModule);

    @Event(forwardToParent = true)
    void userMenuStyleChange(int loadedModule);

    @Event(forwardToParent = true)
    void displayError(int errorResponseCode, String errorId);

    @Event(forwardToParent = true)
    void sendUsEmail(int subject, String errorId);

    @Event(forwardToParent = true)
    void sendStatusMessage(String statusMessageBody);
}
