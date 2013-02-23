package com.eprovement.poptavka.shared.domain.demandsModule;

import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

import java.math.BigDecimal;
import java.util.Date;

import com.eprovement.poptavka.shared.domain.type.MessageType;

public class ClientOfferDetail implements IsSerializable {

    private boolean read;
    private boolean starred;
    private OfferStateType offerStateType;
    private String sender;
    private int clientRating;
    private BigDecimal offerPrice;
    private Date received;
    private Date accepted;
    private int messageCount = 0;
    private int unreadSubmessages;
    //
    private long userMessageId;
    private long messageId;
    private long demandId;
    private long offerId;
    private long supplierId;
    public static final ProvidesKey<ClientOfferDetail> KEY_PROVIDER =
            new ProvidesKey<ClientOfferDetail>() {

                @Override
                public Object getKey(ClientOfferDetail item) {
                    return item == null ? null : item.getOfferId();
                }
            };

    public long getOfferId() {
        return offerId;
    }

    public void setOfferId(long offerId) {
        this.offerId = offerId;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public long getDemandId() {
        return demandId;
    }

    public void setDemandId(long demandId) {
        this.demandId = demandId;
    }

    public long getUserMessageId() {
        return userMessageId;
    }

    public void setUserMessageId(long userMessageId) {
        this.userMessageId = userMessageId;
    }

    public MessageType getMessageType() {
        return MessageType.OFFER;
    }

    public long getMessageId() {
        return messageId;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public String getPrice() {
        return offerPrice.toString();
    }

    public void setPrice(BigDecimal price) {
        this.offerPrice = price;
    }

    public OfferStateType getOfferState() {
        return offerStateType;
    }

    public void setOfferState(OfferStateType offerState) {
        this.offerStateType = offerState;
    }

    public String getFormattedMessageCount() {
        return "(" + getMessageCount() + "/" + getUnreadSubmessages() + ")";
    }

    public int getUnreadSubmessages() {
        return unreadSubmessages;
    }

    public void setUnreadSubmessages(int unreadSubmessages) {
        this.unreadSubmessages = unreadSubmessages;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String name) {
        sender = name;
    }

    public Date getReceivedDate() {
        return received;
    }

    public void setReceivedDate(Date receivedDate) {
        this.received = receivedDate;
    }

    public Date getAcceptedDate() {
        return accepted;
    }

    public void setAcceptedDate(Date acceptedDate) {
        this.accepted = acceptedDate;
    }

    public int getRating() {
        return clientRating;
    }

    public void setRating(int rating) {
        clientRating = rating;
    }
}
