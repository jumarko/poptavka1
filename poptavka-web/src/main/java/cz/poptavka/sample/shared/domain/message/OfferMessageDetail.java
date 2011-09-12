package cz.poptavka.sample.shared.domain.message;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.shared.domain.type.MessageType;
import cz.poptavka.sample.shared.domain.type.OfferStateType;

public class OfferMessageDetail implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6942849643003699484L;
    private long supplierId;
    private long offerId;
    private String supplierName;
    private OfferStateType offerState;
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

    public static PotentialDemandMessage fillMessageDetail(PotentialDemandMessage detail, UserMessage message) {
        detail.setMessageId(message.getId());
        detail.setBody(message.getMessage().getBody());
        detail.setCreated(message.getMessage().getCreated());
//        m.setFirstBornId(serialVersionUID);
        detail.setMessageState(message.getMessage().getMessageState().name());
//        m.setNexSiblingId(serialVersionUID);
        detail.setParentId(message.getMessage().getParent() == null ? detail.getThreadRootId()
                : message.getMessage().getParent().getId());
//        m.setReceiverId();
        detail.setSenderId(message.getMessage().getSender().getId());
        detail.setSent(message.getMessage().getSent());
        detail.setSubject(message.getMessage().getSubject());
        detail.setThreadRootId(message.getMessage().getThreadRoot().getId());
        detail.setUserMessageId(message.getId());
        detail.setDemandId(message.getMessage().getDemand().getId());
        detail.setPrice(message.getMessage().getDemand().getPrice());
        detail.setRead(message.isIsRead());
        detail.setStarred(message.isIsStarred());
        detail.setEndDate(message.getMessage().getDemand().getEndDate());
        detail.setValidToDate(message.getMessage().getDemand().getValidTo());
        return detail;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setOfferId(long offerId) {
        this.offerId = offerId;
    }

    public long getOfferId() {
        return offerId;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setOfferState(OfferStateType offerState) {
        this.offerState = offerState;
    }

    public String getOfferState() {
        return offerState.getValue();
    }

    public MessageType getMessageType() {
        return MessageType.OFFER;
    }

    public Long getUserMessageId() {
        return userMessageId;
    }

    public void setUserMessageId(long userMessageId) {
        this.userMessageId = userMessageId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getValidToDate() {
        return validToDate;
    }

    public void setValidToDate(Date validToDate) {
        this.validToDate = validToDate;
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

    public void setUserMessageId(Long userMessageId) {
        this.userMessageId = userMessageId;
    }
}
