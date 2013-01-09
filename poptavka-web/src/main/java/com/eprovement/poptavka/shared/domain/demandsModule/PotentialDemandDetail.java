package com.eprovement.poptavka.shared.domain.demandsModule;

import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.shared.domain.demand.DemandTypeDetail;
import java.io.Serializable;
import java.util.Date;

import com.google.gwt.view.client.ProvidesKey;

import java.math.BigDecimal;
public class PotentialDemandDetail implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6105359783491407143L;
    private long demandId;
    private long messageId;
    private long userMessageId;
    private boolean read;
    private boolean starred;
    private int messageCount;
    private int unreadSubmessages;
    private String sender;
    private String demandTitle;
    private Integer clientRating;
    private BigDecimal price;
    private Date createdDate;
    private Date endDate;
    private Date validToDate;
    private DemandTypeDetail demandType;
    private DemandStatus demandStatus;


    public static final ProvidesKey<PotentialDemandDetail> KEY_PROVIDER = new ProvidesKey<PotentialDemandDetail>() {

        @Override
        public Object getKey(PotentialDemandDetail item) {
            return item == null ? null : item.getDemandId();
        }
    };

    /**
     * Set Client Sender.
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Set Client Rating.
     */
    public void setRating(Integer clientRating) {
        this.clientRating = clientRating;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public long getDemandId() {
        return demandId;
    }

    public void setDemandId(long demandId) {
        this.demandId = demandId;
    }

    public DemandStatus getDemandStatus() {
        return demandStatus;
    }

    public void setDemandStatus(DemandStatus demandStatus) {
        this.demandStatus = demandStatus;
    }

    public void setTitle(String demandTitle) {
        this.demandTitle = demandTitle;
    }

    public DemandTypeDetail getDemandType() {
        return demandType;
    }

    public void setDemandType(DemandTypeDetail demandType) {
        this.demandType = demandType;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getPrice() {
        return price.toString();
    }

    public void setPrice(BigDecimal price) {
        if (price == null) {
            this.price = BigDecimal.ZERO;
        } else {
            this.price = price;
        }
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

    public long getUserMessageId() {
        return userMessageId;
    }

    public void setUserMessageId(long userMessageId) {
        this.userMessageId = userMessageId;
    }

    public String getTitle() {
        return demandTitle;
    }

    public String getSender() {
        return sender;
    }

    public Date getCreated() {
        return createdDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date date) {
        this.endDate = date;
    }

    public String getFormattedMessageCount() {
        return "(" + getMessageCount() + "/"
                + getUnreadSubmessages() + ")";
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int count) {
        this.messageCount = count;
    }

    public int getUnreadSubmessages() {
        return unreadSubmessages;
    }

    public void setUnreadSubmessages(int unreadSubmessages) {
        this.unreadSubmessages = unreadSubmessages;
    }

    public int getRating() {
        return (clientRating == null ? 0 : clientRating.intValue());
    }

    public Date getValidToDate() {
        return validToDate;
    }

    public void setValidToDate(Date date) {
        this.validToDate = date;
    }
}
