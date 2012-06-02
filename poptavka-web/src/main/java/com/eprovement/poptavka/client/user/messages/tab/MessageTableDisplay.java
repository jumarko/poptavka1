package com.eprovement.poptavka.client.user.messages.tab;

import com.eprovement.poptavka.shared.domain.message.MessageDetail;

public interface MessageTableDisplay {

    boolean isRead();

    void setRead(boolean value);

    boolean isStarred();

    void setStarred(boolean value);

    MessageDetail getMessageDetail();

    String getSenderEmail();

    int getMessageCount();

    void setMessageCount(int messageCount);

    int getUnreadMessageCount();

    void setUnreadMessageCount(int unreadMessageCount);

    String getFormattedMessageCount();
}
