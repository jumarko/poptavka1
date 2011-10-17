package cz.poptavka.sample.shared.domain.message;

import com.google.gwt.view.client.ProvidesKey;
import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.shared.domain.type.MessageType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PotentialDemandMessage implements Serializable, TableDisplay {

    /**
     *
     */
    private static final long serialVersionUID = -6105359783491407143L;
    private long userMessageId;
    private BigDecimal price;
    private boolean read;
    private boolean starred;
    private Date endDate;
    private Date validToDate;
    private long messageId;
    private long threadRootId;
    private long parentId;
//    private long firstBornId;
//    private long nexSiblingId;
    private long demandId;
    private String subject;
    private String body;
    private String messageState;
    private Date created;
    private Date sent;
    private long senderId;
    private long receiverId;

    public static PotentialDemandMessage createMessageDetail(UserMessage message) {
        return fillMessageDetail(new PotentialDemandMessage(), message);
    }

    public static PotentialDemandMessage fillMessageDetail(PotentialDemandMessage detail,
            UserMessage userMessage) {
        detail.setMessageId(userMessage.getMessage().getId());
        detail.setBody(userMessage.getMessage().getBody());
        detail.setCreated(userMessage.getMessage().getCreated());
//        m.setFirstBornId(serialVersionUID);
        detail.setMessageState(userMessage.getMessage().getMessageState().name());
//        m.setNexSiblingId(serialVersionUID);
        detail.setParentId(userMessage.getMessage().getParent() == null ? detail.getThreadRootId()
                : userMessage.getMessage().getParent().getId());
//        m.setReceiverId();
        detail.setSenderId(userMessage.getMessage().getSender().getId());
        detail.setSent(userMessage.getMessage().getSent());
        detail.setSubject(userMessage.getMessage().getSubject());
        detail.setThreadRootId(userMessage.getMessage().getThreadRoot().getId());
        detail.setUserMessageId(userMessage.getId());
        detail.setDemandId(userMessage.getMessage().getDemand().getId());
        detail.setPrice(userMessage.getMessage().getDemand().getPrice());
        detail.setRead(userMessage.isIsRead());
        detail.setStarred(userMessage.isIsStarred());
        detail.setEndDate(userMessage.getMessage().getDemand().getEndDate());
        detail.setValidToDate(userMessage.getMessage().getDemand().getValidTo());
        return detail;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public boolean isRead() {
        return read;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setValidToDate(Date validTo) {
        this.validToDate = validTo;
    }

    public Date getValidToDate() {
        return validToDate;
    }

    public void setPrice(BigDecimal price) {
        if (price == null) {
            this.price = BigDecimal.ZERO;
        } else {
            this.price = price;
        }
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setUserMessageId(Long userMessageId) {
        this.userMessageId = userMessageId;
    }

    public Long getUserMessageId() {
        return userMessageId;
    }

    public MessageType getMessageType() {
        return MessageType.POTENTIAL_DEMAND;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public long getThreadRootId() {
        return threadRootId;
    }

    public void setThreadRootId(long threadRootId) {
        this.threadRootId = threadRootId;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getDemandId() {
        return demandId;
    }

    public void setDemandId(long demandId) {
        this.demandId = demandId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMessageState() {
        return messageState;
    }

    public void setMessageState(String messageState) {
        this.messageState = messageState;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public void setUserMessageId(long userMessageId) {
        this.userMessageId = userMessageId;
    }

    public static final ProvidesKey<PotentialDemandMessage> KEY_PROVIDER = new ProvidesKey<PotentialDemandMessage>() {
        @Override
        public Object getKey(PotentialDemandMessage item) {
            return item == null ? null : item.getDemandId();
        }
    };

    @Override
    public String getTitle() {
        return subject;
    }

}
