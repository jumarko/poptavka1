/*
 * Copyright (C) 2012, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.shared.domain.demandsModule;

import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.shared.domain.demand.DemandTypeDetail;
import java.util.Date;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

import java.math.BigDecimal;

/**
 * Potential demand detail.
 * TODO Martin remove
 *
 * @author Ivan Vlcek
 * @author Martin Slavkovsky
 */
public class PotentialDemandDetail implements IsSerializable {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
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
    /** Key provider. **/
    public static final ProvidesKey<PotentialDemandDetail> KEY_PROVIDER = new ProvidesKey<PotentialDemandDetail>() {
        @Override
        public Object getKey(PotentialDemandDetail item) {
            return item == null ? null : item.getDemandId();
        }
    };

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates PotentialDemandDetail.
     */
    public PotentialDemandDetail() {
        //For serialization.
    }

    /**************************************************************************/
    /* Getter & Setter pairs                                                  */
    /**************************************************************************/
    /*
     * Sender pair
     */
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    /*
     * Rating pair.
     */
    public int getRating() {
        return (clientRating == null ? 0 : clientRating.intValue());
    }

    public void setRating(Integer clientRating) {
        this.clientRating = clientRating;
    }

    /*
     * Created date pair.
     */
    public Date getCreated() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /*
     * Demand id pair.
     */
    public long getDemandId() {
        return demandId;
    }

    public void setDemandId(long demandId) {
        this.demandId = demandId;
    }

    /*
     * Demand status pair
     */
    public DemandStatus getDemandStatus() {
        return demandStatus;
    }

    public void setDemandStatus(DemandStatus demandStatus) {
        this.demandStatus = demandStatus;
    }

    /*
     * Femand title pair
     */
    public String getTitle() {
        return demandTitle;
    }

    public void setTitle(String demandTitle) {
        this.demandTitle = demandTitle;
    }

    /*
     * Demand type pair.
     */
    public DemandTypeDetail getDemandType() {
        return demandType;
    }

    public void setDemandType(DemandTypeDetail demandType) {
        this.demandType = demandType;
    }

    /*
     * Message id pair.
     */
    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    /*
     * Demand price pair.
     */
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

    /*
     * Starred pair
     */
    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    /*
     * User message id pair
     */
    public long getUserMessageId() {
        return userMessageId;
    }

    public void setUserMessageId(long userMessageId) {
        this.userMessageId = userMessageId;
    }

    /*
     * End date pair.
     */
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

    /*
     * Message count pair
     */
    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int count) {
        this.messageCount = count;
    }

    /*
     * unread sub messages pair.
     */
    public int getUnreadSubmessages() {
        return unreadSubmessages;
    }

    public void setUnreadSubmessages(int unreadSubmessages) {
        this.unreadSubmessages = unreadSubmessages;
    }

    /*
     * Valid to date pair.
     */
    public Date getValidToDate() {
        return validToDate;
    }

    public void setValidToDate(Date date) {
        this.validToDate = date;
    }
}
