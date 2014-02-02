/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid;

/**
 * Defines getters & setters for a detail object to be displayable in corresponding table.
 *
 * @author Martin Slavkovsky
 */
public interface TableDisplayUserMessage {

    long getUserMessageId();
    void setUserMessageId(long userMessageId);

    boolean isStarred();
    void setStarred(boolean isStarred);

    boolean isRead();
    void setRead(boolean isStarred);

    int getMessagesCount();
    void setMessagesCount(int messageCount);

}
