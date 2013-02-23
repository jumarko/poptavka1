package com.eprovement.poptavka.shared.domain.clientdemands;

import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.eprovement.poptavka.shared.domain.message.TableDisplay;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;


import java.math.BigDecimal;
import java.util.Date;

/**
 * Represents full detail of demand. Serves for creating new demand or for call of detail, that supports editing.
 *
 * @author Beho
 */
public class ClientDemandDetail implements IsSerializable, TableDisplay {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private long demandId;
    private long messageId;
    private long userMessageId;
    private long threadRootId;
    private DemandStatus demandStatus;
    private String demandTitle; //title
    private BigDecimal price = null;
    private Date endDate;
    private Date validToDate;
    private boolean isRead = false;
    private boolean isStarred = false;
    private int unreadSubmessagesCount;
    public static final ProvidesKey<ClientDemandDetail> KEY_PROVIDER =
            new ProvidesKey<ClientDemandDetail>() {
                @Override
                public Object getKey(ClientDemandDetail item) {
                    return item == null ? null : item.getDemandId();
                }
            };

    /**************************************************************************/
    /* Constructors                                                           */
    /**************************************************************************/
    public ClientDemandDetail() {
        //for serialization
    }

    /**************************************************************************/
    /* Getters & Setters                                                      */
    /**************************************************************************/
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

    @Override
    public boolean isStarred() {
        return isStarred;
    }

    @Override
    public void setIsStarred(boolean isStarred) {
        this.isStarred = isStarred;
    }

    public long getMessageId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Date getCreated() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getPrice() {
        return price == null ? "N/A" : price.toString();
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    /**
     * @return the isRead
     */
    public boolean isIsRead() {
        return isRead;
    }

    /**
     * @param isRead the isRead to set
     */
    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    /**
     * @return the unreadSubmessagesCount
     */
    @Override
    public int getUnreadSubmessagesCount() {
        return unreadSubmessagesCount;
    }

    /**
     * @param unreadSubmessagesCount the unreadSubmessagesCount to set
     */
    public void setUnreadSubmessagesCount(int unreadSubmessagesCount) {
        this.unreadSubmessagesCount = unreadSubmessagesCount;
    }
}
