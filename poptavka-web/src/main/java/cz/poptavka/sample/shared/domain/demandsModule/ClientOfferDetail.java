package cz.poptavka.sample.shared.domain.demandsModule;

import com.google.gwt.view.client.ProvidesKey;
import cz.poptavka.sample.domain.demand.DemandStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.shared.domain.message.TableDisplay;
import cz.poptavka.sample.shared.domain.type.MessageType;

public class ClientOfferDetail implements Serializable, TableDisplay {

    private static final long serialVersionUID = 2349565802701324033L;
    //
    private String sender;
    private BigDecimal offerPrice;
    private Date received;
    private Date accepted;
    private int clientRating;
    private boolean read;
    private boolean starred;
    private int messageCount = 0;
    private int unreadSubmessages;
    //
    private long userMessageId;
    private long messageId;
    private long demandId;
    private long offerId;
    private long supplierId;

    public static ClientOfferDetail createMessageDetail(UserMessage message) {
        ClientOfferDetail detail = new ClientOfferDetail();
        //TODO Martin
        return detail;
    }

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
    public static final ProvidesKey<ClientOfferDetail> KEY_PROVIDER = new ProvidesKey<ClientOfferDetail>() {

        @Override
        public Object getKey(ClientOfferDetail item) {
            return item == null ? null : item.getOfferId();
        }
    };

    @Override
    public long getMessageId() {
        return messageId;
    }

    @Override
    public boolean isRead() {
        return read;
    }

    @Override
    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public boolean isStarred() {
        return starred;
    }

    @Override
    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    @Override
    public String getPrice() {
        return offerPrice.toString();
    }

    public void setPrice(BigDecimal price) {
        this.offerPrice = price;
    }

    @Override
    public String getFormattedMessageCount() {
        return "(" + getMessageCount() + "/" + getUnreadSubmessages() + ")";
    }

    public int getUnreadSubmessages() {
        return unreadSubmessages;
    }

    public void setUnreadSubmessages(int unreadSubmessages) {
        this.unreadSubmessages = unreadSubmessages;
    }

    @Override
    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    @Override
    public String getSender() {
        return sender;
    }

    public void setSender(String name) {
        sender = name;
    }

    @Override
    public Date getReceivedDate() {
        return received;
    }

    public void setReceivedDate(Date receivedDate) {
        this.received = receivedDate;
    }

    @Override
    public Date getAcceptedDate() {
        return accepted;
    }

    public void setAcceptedDate(Date acceptedDate) {
        this.accepted = acceptedDate;
    }

    @Override
    public int getRating() {
        return clientRating;
    }

    public void setRating(int rating) {
        clientRating = rating;
    }

    @Override
    public Date getValidToDate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getTitle() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Date getCreated() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Date getEndDate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DemandStatus getDemandStatus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Date getExpireDate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
