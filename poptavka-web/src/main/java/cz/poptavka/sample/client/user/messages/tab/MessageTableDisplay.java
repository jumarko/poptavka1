package cz.poptavka.sample.client.user.messages.tab;

import cz.poptavka.sample.shared.domain.message.MessageDetail;

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
