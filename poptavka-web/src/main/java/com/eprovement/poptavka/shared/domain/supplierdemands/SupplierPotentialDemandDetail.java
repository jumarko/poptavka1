/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.domain.supplierdemands;

import com.eprovement.poptavka.client.user.widget.grid.IUniversalDetail;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.eprovement.poptavka.shared.domain.message.TableDisplay;
import com.google.gwt.view.client.ProvidesKey;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author Ivan
 */
public class SupplierPotentialDemandDetail implements Serializable, TableDisplay, IUniversalDetail {

    private static final long serialVersionUID = -6019479783491937543L;
    // Client part
    private long clientId;
    private String clientName; // column 2
    // Message part
    private long messageId;
    private long threadRootId;
    private long senderId;
    private Date messageSent; // column 5
    // UserMessage part
    private long userMessageId;
    private boolean isStarred; // column 1
    private int messageCount; // all messages between Supplier and Client regarding this potential demand
    // TODO ivlcek - we can remove unreadMessageCount if isRead works. All RPC must be changes to retrieve latest
    // UserMessage object from conversation between CLient and supplier
    private int unreadMessageCount; // number of Supplier's unread messages regarding this potential demand
    private boolean isRead;
    // Demand part
    private long demandId;
    private Date validTo;
    private Date endDate; // column 4 - I believe this field is used to make urgency icon in our table
    private String title; // column 3
    private String price; // column ? - maybe we will not display this in table

    // Keyprovider
    //--------------------------------------------------------------------------
    public static final ProvidesKey<IUniversalDetail> KEY_PROVIDER =
            new ProvidesKey<IUniversalDetail>() {
                @Override
                public Object getKey(IUniversalDetail item) {
                    return item == null ? null : item.getDemandId();
                }
            };

    // Client part
    //--------------------------------------------------------------------------
    /**
     * @return the clientId
     */
    @Override
    public long getClientId() {
        return clientId;
    }

    /**
     * @param clientId the clientId to set
     */
    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    /**
     * @return the clientName
     */
    @Override
    public String getClientName() {
        return clientName;
    }

    /**
     * @param clientName the clientName to set
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    // Supplier part
    //--------------------------------------------------------------------------
    @Override
    public long getSupplierId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSupplierName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getRating() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // Message part
    //--------------------------------------------------------------------------
    /**
     * @return the messageId
     */
    @Override
    public long getMessageId() {
        return messageId;
    }

    /**
     * @param messageId the messageId to set
     */
    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    /**
     * @return the threadRootId
     */
    @Override
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
     * @return the senderId
     */
    @Override
    public long getSenderId() {
        return senderId;
    }

    /**
     * @param senderId the senderId to set
     */
    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    /**
     * @return the messageSent
     */
    @Override
    public Date getMessageSent() {
        return messageSent;
    }

    /**
     * @param messageSent the messageSent to set
     */
    public void setMessageSent(Date messageSent) {
        this.messageSent = messageSent;
    }

    // UserMessage part
    //--------------------------------------------------------------------------
    /**
     * @return the userMessageId
     */
    @Override
    public long getUserMessageId() {
        return userMessageId;
    }

    /**
     * @param userMessageId the userMessageId to set
     */
    public void setUserMessageId(long userMessageId) {
        this.userMessageId = userMessageId;
    }

    /**
     * @return the isStarred
     */
    @Override
    public boolean isStarred() {
        return isStarred;
    }

    /**
     * @param isStarred the isStarred to set
     */
    @Override
    public void setIsStarred(boolean isStarred) {
        this.isStarred = isStarred;
    }

    /**
     * @return the messageCount
     */
    @Override
    public int getMessageCount() {
        return messageCount;
    }

    /**
     * @param messageCount the messageCount to set
     */
    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    /**
     * @return the unreadMessageCount
     */
    @Override
    public int getUnreadMessageCount() {
        return unreadMessageCount;
    }

    /**
     * @param unreadMessageCount the unreadMessageCount to set
     */
    public void setUnreadMessageCount(int unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    @Override
    public Date getDeliveryDate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    // Demand part
    //--------------------------------------------------------------------------
    /**
     * @return the demandId
     */
    @Override
    public long getDemandId() {
        return demandId;
    }

    /**
     * @param demandId the demandId to set
     */
    public void setDemandId(long demandId) {
        this.demandId = demandId;
    }

    /**
     * @return the validTo
     */
    @Override
    public Date getValidTo() {
        return validTo;
    }

    /**
     * @param validTo the validTo to set
     */
    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    /**
     * @return the endDate
     */
    @Override
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public Date getReceivedDate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the title
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the price
     */
    @Override
    public String getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public DemandStatus getDemandStatus() {
        return DemandStatus.ACTIVE;
    }


    // Offer part
    //--------------------------------------------------------------------------
    @Override
    public OfferStateType getOfferState() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getOfferId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRead() {
        return isRead;
    }

    @Override
    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

}
