/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common.actionBox;

import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Debug;
import com.mvp4g.client.annotation.Debug.LogLevel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBusWithLookup;
import java.util.List;

/**
 * Small module but exists due to code spliting feature.
 * Used in every table with multiple selection model, where star and read status can be updated.
 *
 * @author Martin Slavkovsky
 */
@Debug(logLevel = LogLevel.DETAILED)
@Events(startPresenter = ActionBoxPresenter.class, module = ActionBoxModule.class)
public interface ActionBoxEventBus extends EventBusWithLookup {

    /**************************************************************************/
    /* Parent events                                                          */
    /**************************************************************************/
    /**
     * Send status message i.e. when user clicks on action buttons like Accept Offer, Finish Offer, Close Demand etc.
     * @param statusMessageBody representing status message text
     */
    @Event(forwardToParent = true)
    void sendStatusMessage(String statusMessageBody);

    @Event(forwardToParent = true)
    void displayError(int errorResponseCode, String errorId);

    /**************************************************************************/
    /* Business event                                                         */
    /**************************************************************************/
    @Event(handlers = ActionBoxPresenter.class)
    void initActionBox(SimplePanel holderWidget, UniversalAsyncGrid grid);

    /**************************************************************************/
    /* Business event pairs                                                   */
    /**************************************************************************/
    /**
     * Send/Response method pair
     * Update message read status.
     * @param messageToSend
     */
    @Event(handlers = ActionBoxHandler.class)
    void requestReadStatusUpdate(List<Long> userMessageIds, boolean isRead);

    @Event(handlers = ActionBoxPresenter.class)
    void responseReadStatusUpdate();

    /**
     * Send/Response method pair
     * Update message star status.
     * @param messageToSend
     */
    @Event(handlers = ActionBoxHandler.class)
    void requestStarStatusUpdate(List<Long> userMessageIdList, boolean newStatus);

    @Event(handlers = ActionBoxPresenter.class)
    void responseStarStatusUpdate();
}
