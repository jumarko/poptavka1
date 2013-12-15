/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.messages;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;

/**
 * Manages history for Messages module.
 *
 * @author Martin Slavkovsky
 */
@History(type = HistoryConverterType.DEFAULT, name = "messages")
public class MessagesHistoryConverter implements HistoryConverter<MessagesEventBus> {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private static final String MESSAGES_INBOX_TEXT = "messagesInbox";

    /**************************************************************************/
    /* ConvertToToken events                                                  */
    /**************************************************************************/
    /**
     * Creates history token for Messages module.
     * @return history token string
     */
    public String onCreateTokenForHistory() {
        return MESSAGES_INBOX_TEXT;
    }

    /**************************************************************************/
    /* ConvertFromToken events                                                */
    /**************************************************************************/
    /**
     * Convert history token into action
     * Called either when browser action <b>back</b> or <b>forward</b> is evocated,
     * or by clicking on <b>hyperlink</b> with set token.
     *
     * @param methodName - name of the called method
     * @param param - string behind '?' in url (module/method?param).
     *                URL creates onGoToMessagesModule method in MessagesModuleHistoryConverter class.
     * @param eventBus - MessagesModuleEventBus
     */
    @Override
    public void convertFromToken(String historyName, String param, MessagesEventBus eventBus) {
        //If application is called by URL, log in user and forward him to overview (goToClientDemandModule.Welcome)
        if (Storage.isAppCalledByURL() != null && Storage.isAppCalledByURL()) {
            Storage.setAppCalledByURL(false);
            eventBus.setHistoryStoredForNextOne(false);
            eventBus.loginFromSession(getCurrentViewConstant(param));
            return;
        }
        eventBus.goToMessagesModule(null, Constants.NONE);
    }

    /**************************************************************************/
    /* Other events                                                           */
    /**************************************************************************/
    @Override
    public boolean isCrawlable() {
        return false;
    }

    /**************************************************************************/
    /* Helper methods.                                                        */
    /**************************************************************************/
    /**
     * Converts history token to current view constant.
     * @param token
     * @return
     */
    private int getCurrentViewConstant(String token) {
        return Constants.MESSAGES_INBOX;
    }
}
