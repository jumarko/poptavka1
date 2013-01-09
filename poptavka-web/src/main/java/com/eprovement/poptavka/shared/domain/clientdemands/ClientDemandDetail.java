package com.eprovement.poptavka.shared.domain.clientdemands;

import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.eprovement.poptavka.shared.domain.message.TableDisplay;
import com.google.gwt.view.client.ProvidesKey;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Represents full detail of demand. Serves for creating new demand or for call of detail, that supports editing.
 *
 * @author Beho
 */
public class ClientDemandDetail implements Serializable, TableDisplay {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -530982467233195456L;
    private long demandId;
    private long messageId;
    private long userMessageId;
    private long threadRootId;
    private DemandStatus demandStatus;
    private String demandTitle; //title
    private BigDecimal price = null;
    private Date endDate;
    private Date validToDate;
    private boolean read = false;
    private boolean starred = false;
    private int messageCount = -1;
    private int unreadMessageCount = -1;
    public static final ProvidesKey<ClientDemandDetail> KEY_PROVIDER =
            new ProvidesKey<ClientDemandDetail>() {
                @Override
                public Object getKey(ClientDemandDetail item) {
                    return item == null ? null : item.getDemandId();
                }
            };

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

    public int getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(int unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    public String getFormattedMessageCount() {
        return "(" + getUnreadMessageCount() + "/" + getMessageCount() + ")";
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

    public OfferStateType getOfferState() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static String displayTitleHtml(ClientDemandDetail clientDemandDetail) {
        if (clientDemandDetail.getUnreadMessageCount() > 0) {
            StringBuilder str = new StringBuilder();
            str.append("<strong>");
            str.append(clientDemandDetail.getDemandTitle());
            str.append(" (");
            str.append(clientDemandDetail.getUnreadMessageCount());
            str.append(")");
            str.append("<strong>");
            return str.toString();
        } else {
            return clientDemandDetail.getDemandTitle();
        }
    }

    /**
     * @return the threadRootId
     */
    public long getThreadRootId() {
        return threadRootId;
    }

    /**
     * @param threadRootId the threadRootId to set
     */
    public void setThreadRootId(long threadRootId) {
        this.threadRootId = threadRootId;
    }
}
