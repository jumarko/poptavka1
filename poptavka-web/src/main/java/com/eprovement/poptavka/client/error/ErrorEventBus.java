/*
 * Error module will display all erroneous states to user.
 */
package com.eprovement.poptavka.client.error;

import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.Forward;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.event.EventBusWithLookup;

/**
 *
 * @author ivlcek
 */
@Events(startPresenter = ErrorPresenter.class, module = ErrorModule.class)
@Debug(logLevel = Debug.LogLevel.DETAILED)
public interface ErrorEventBus extends EventBusWithLookup {

    /**
     * First event to be handled.
     */
    @Start
    @Event(handlers = ErrorPresenter.class)
    void start();

    /**
     * Forward event is called only if it is configured here. If there is nothing to carry out
     * in this method we should remove forward event to save the number of method invocations.
     */
    @Forward
    @Event(handlers = ErrorPresenter.class)
    void forward();

    /**************************************************************************/
    /* Navigation events.                                                     */
    /**************************************************************************/
    /**
     * The only entry point to this module due to code-spliting feature.
     */
    @Event(handlers = ErrorPresenter.class)
    void displayError(int errorResponseCode, String errorId);

    @Event(forwardToParent = true)
    void sendUsEmail(int subject, String errorId);
}
