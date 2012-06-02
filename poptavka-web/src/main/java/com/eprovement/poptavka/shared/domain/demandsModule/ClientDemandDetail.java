package com.eprovement.poptavka.shared.domain.demandsModule;

import com.google.gwt.view.client.ProvidesKey;

import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.demand.DemandStatus;

import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.offer.OfferState;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Represents full detail of demand. Serves for creating new demand or for call of detail, that supports editing.
 *
 * @author Beho
 */
public class ClientDemandDetail implements Serializable { //, TableDisplay {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -530982467233195456L;
    private long demandId;
    private long messageId;
    private long userMessageId;
    private DemandStatus demandStatus;
    private String demandTitle; //title
    private BigDecimal price = null;
    private Date endDate;
    private Date validToDate;
    private boolean read = false;
    private boolean starred = false;
    private int messageCount = -1;
    private int unreadSubmessages = -1;

    public static final ProvidesKey<ClientDemandDetail> KEY_PROVIDER =
            new ProvidesKey<ClientDemandDetail>() {

                @Override
                public Object getKey(ClientDemandDetail item) {
                    return item == null ? null : item.getDemandId();
                }
            };

    /**
     * Method created FullDemandDetail from provided Demand domain object.
     *
     * @param demand
     * @return ClientDemandDetail
     */
    public static ClientDemandDetail createDemandDetail(UserMessage userMessage) {
        ClientDemandDetail detail = new ClientDemandDetail();
        detail.setUserMessageId(userMessage.getId());
        if (userMessage.getMessage() != null && userMessage.getMessage().getDemand() != null) {
            detail.setDemandId(userMessage.getMessage().getDemand().getId());
            detail.setDemandStatus(userMessage.getMessage().getDemand().getStatus());
            detail.setTitle(userMessage.getMessage().getDemand().getTitle());
            detail.setPrice(userMessage.getMessage().getDemand().getPrice());
            detail.setEndDate(userMessage.getMessage().getDemand().getEndDate());
            detail.setValidToDate(userMessage.getMessage().getDemand().getValidTo());
        }
        detail.setRead(userMessage.isRead());
        detail.setStarred(userMessage.isStarred());
        return detail;
    }

    public static ClientDemandDetail createDemandDetail(Demand demand) {
        ClientDemandDetail detail = new ClientDemandDetail();
        detail.setUserMessageId(-1);
        detail.setDemandId(demand.getId());
        detail.setDemandStatus(demand.getStatus());
        detail.setTitle(demand.getTitle());
        detail.setPrice(demand.getPrice());
        detail.setEndDate(demand.getEndDate());
        detail.setValidToDate(demand.getValidTo());
        detail.setRead(false);
        detail.setStarred(false);
        return detail;
    }

    //---------------------------- GETTERS AND SETTERS --------------------
    public long getDemandId() {
        return demandId;
    }

    public void setDemandId(long demandId) {
        this.demandId = demandId;
    }

    public String getDemandTitle() {
        return demandTitle;
    }

    public void setDemandTitle(String demandTitle) {
        this.demandTitle = demandTitle;
    }

    public long getUserMessageId() {
        return userMessageId;
    }

    public void setUserMessageId(long userMessageId) {
        this.userMessageId = userMessageId;
    }

    public DemandStatus getDemandStatus() {
        return demandStatus;
    }

    public void setDemandStatus(DemandStatus demandStatus) {
        this.demandStatus = demandStatus;
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

    public long getMessageId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

//    @Override
//    public String getTitle() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
    public Date getCreated() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getPrice() {
        return price == null ? "N/A" : price.toString();
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public int getUnreadSubmessages() {
        return unreadSubmessages;
    }

    public void setUnreadSubmessages(int unreadSubmessages) {
        this.unreadSubmessages = unreadSubmessages;
    }

    public String getFormattedMessageCount() {
        return "(" + getMessageCount() + "/" + getUnreadSubmessages() + ")";
    }

    public String getSender() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getRating() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Date getExpireDate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Date getReceivedDate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Date getAcceptedDate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getTitle() {
        return demandTitle;
    }

    public void setTitle(String title) {
        demandTitle = title;
    }

    public OfferState.Type getOfferState() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
