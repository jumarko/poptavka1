package cz.poptavka.sample.shared.domain.message;

import java.util.Date;

import cz.poptavka.sample.shared.domain.type.MessageType;

public interface MessageDetail {

    void setMessageId(long messageId);
    long getMessageId();

    void setThreadRootId(long threadRootId);
    long getThreadRootId();

    void setParentId(long parentId);
    long getParentId();

    void setDemandId(long demandId);
    long getDemandId();

    void setSubject(String subject);
    String getSubject();

    void setBody(String body);
    String getBody();

    void setMessageState(String messageState);
    String getMessageState();

    void setCreated(Date created);
    Date getCreated();

    void setSent(Date sent);
    Date getSent();

    void setSenderId(long senderId);
    long getSenderId();

    void setReceiverId(long receiverId);
    long getReceiverId();

    String toString();

    MessageType getMessageType();
}
