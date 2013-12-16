/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.widget.grid;

/**
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

    int getUnreadMessagesCount();
    void setUnreadMessagesCount(int messageCount);

}
