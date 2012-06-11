package com.eprovement.poptavka.shared.domain.demandsModule;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.view.client.ProvidesKey;

import com.eprovement.poptavka.domain.demand.DemandStatus;
import com.eprovement.poptavka.domain.demand.DemandType;
import com.eprovement.poptavka.domain.message.UserMessage;
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
    private DemandType demandType;
    private DemandStatus demandStatus;

    public static PotentialDemandDetail createMessageDetail(UserMessage userMessage) {
        PotentialDemandDetail detail = new PotentialDemandDetail();
        detail.setUserMessageId(userMessage.getId());
        detail.setRead(userMessage.isRead());
        detail.setStarred(userMessage.isStarred());
        if (userMessage.getMessage() != null) {
            detail.setMessageId(userMessage.getMessage().getId());
            if (userMessage.getMessage().getDemand() != null) {
                detail.setDemandId(userMessage.getMessage().getDemand().getId());
                detail.setTitle(userMessage.getMessage().getDemand().getTitle());
                detail.setPrice(userMessage.getMessage().getDemand().getPrice());
                detail.setDemandType(userMessage.getMessage().getDemand().getType());
                detail.setDemandStatus(userMessage.getMessage().getDemand().getStatus());
                // TODO move to the own converter and use convertDate method for dates!!!
                detail.setCreatedDate(userMessage.getMessage().getCreated());
                detail.setEndDate(userMessage.getMessage().getDemand().getEndDate());
                detail.setValidToDate(userMessage.getMessage().getDemand().getValidTo());
                if (userMessage.getMessage().getDemand().getClient() != null) {
                    detail.setRating(userMessage.getMessage().getDemand().getClient().getOveralRating());
                    if (userMessage.getMessage().getDemand().getClient().getBusinessUser() != null
                            && userMessage.getMessage().getDemand().getClient()
                                .getBusinessUser().getBusinessUserData() != null) {
                        detail.setSender(userMessage.getMessage().getDemand().getClient()
                                    .getBusinessUser().getBusinessUserData().getDisplayName());
                    }
                }
            }
        }

        return detail;
    }
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

    public DemandType getDemandType() {
        return demandType;
    }

    public void setDemandType(DemandType demandType) {
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
