package cz.poptavka.sample.shared.domain.message;

import java.math.BigDecimal;
import java.util.Date;

import cz.poptavka.sample.shared.domain.type.MessageType;


public interface OfferDemandMessage extends PotentialDemandMessage {

    void setOfferCount(int offerCount);
    int getOfferCount();

    void setMaxOfferCount(int maxOfferCount);
    int getMaxOfferCount();

    void setDemandTitle(String demandTitle);
    String getDemandTitle();

    void setPrice(BigDecimal price);
    BigDecimal getPrice();

    void setRead(boolean read);
    boolean isRead();

    void setStarred(boolean starred);
    boolean isStarred();

    void setEndDate(Date endDate);
    Date getEndDate();

    void setValidToDate(Date validTo);
    Date getValidToDate();

    void setUserMessageId(Long userMessageId);
    Long getUserMessageId();

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
