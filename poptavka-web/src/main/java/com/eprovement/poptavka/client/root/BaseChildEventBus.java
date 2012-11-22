/*
 * This interface contains common methods from Child Modules that are always forwarded to parent Module.
 */
package com.eprovement.poptavka.client.root;

import com.mvp4g.client.annotation.Event;

public interface BaseChildEventBus {

    @Event(forwardToParent = true)
    void login(int widgetToLoad);

    @Event(forwardToParent = true)
    void displayError(int errorResponseCode, String errorId);

    @Event(forwardToParent = true)
    void sendUsEmail(int subject, String errorId);
}
