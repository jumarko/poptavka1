package com.eprovement.poptavka.shared.domain.clientdemands;

import com.eprovement.poptavka.client.user.widget.grid.IUniversalDetail;
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
public class ClientDemandConversationDetail implements IsSerializable, TableDisplay, IUniversalDetail {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private long demandId;
    private long messageId;
    private long threadMessageId;
    private long userMessageId;
    private long supplierId;
    private long supplierUserId;
    private Date messageSent;
    private String supplierName;
    private String messageBody;
    private String demandTitle;
    private boolean isRead;
    private boolean isStarred;
    private int messageCount;
    private int supplierRating;
    // TODO LATER ivlcek: remove unreadSubmessages and optimize usage of this detail object
    private int unreadSubmessagesCount;
    public static final ProvidesKey<IUniversalDetail> KEY_PROVIDER =
            new ProvidesKey<IUniversalDetail>() {
                @Override
                public Object getKey(IUniversalDetail item) {
                    return item == null ? null : item.getSupplierId();
                }
            };

    /**************************************************************************/
    /* Constructors                                                           */
    /**************************************************************************/
    public ClientDemandConversationDetail() {
        //for serialization
    }

    /**************************************************************************/
    /* Getters & Setters                                                      */
    /**************************************************************************/
    @Override
    public long getDemandId() {
        return demandId;
    }

    public void setDemandId(long demandId) {
        this.demandId = demandId;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    @Override
    public int getMessageCount() {
        return messageCount;
    }

    @Override
    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public boolean isStarred() {
        return isStarred;
    }

    public void setIsStarred(boolean isStarred) {
        this.isStarred = isStarred;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public long getUserMessageId() {
        return userMessageId;
    }

    public void setUserMessageId(long userMessageId) {
        this.userMessageId = userMessageId;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
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
    public OfferStateType getOfferState() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @param threadMessageId the threadMessageId to set
     */
    public void setThreadRootId(long threadMessageId) {
        this.threadMessageId = threadMessageId;
    }

    @Override
    public long getClientId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getClientName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getRating() {
        return this.supplierRating;
    }

    @Override
    public long getThreadRootId() {
        return threadMessageId;
    }

    @Override
    public long getSenderId() {
        return supplierUserId;
    }

    public void setSenderId(long supplierUserId) {
        this.supplierUserId = supplierUserId;
    }

    @Override
    public Date getMessageSent() {
        return messageSent;
    }

    public void setMessageSent(Date messageSent) {
        this.messageSent = messageSent;
    }

    @Override
    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    @Override
    public Date getDeliveryDate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Date getValidTo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Date getReceivedDate() {
        return messageSent;
    }

    public void setTitle(String title) {
        demandTitle = title;
    }

    @Override
    public String getTitle() {
        return demandTitle;
    }

    @Override
    public BigDecimal getPrice() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getOfferId() {
        throw new UnsupportedOperationException("Not supported yet.");
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

    /**
     * @param supplierRating the supplierRating to set
     */
    public void setRating(int supplierRating) {
        this.supplierRating = supplierRating;
    }
}
