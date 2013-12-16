package com.eprovement.poptavka.shared.domain.clientdemands;

import com.eprovement.poptavka.client.user.widget.grid.TableDisplayUserMessage;
import com.eprovement.poptavka.client.user.widget.grid.columns.DisplayNameColumn.TableDisplayDisplayName;
import com.eprovement.poptavka.client.user.widget.grid.columns.MessageSentDateColumn.TableDisplayMessageSentDate;
import com.eprovement.poptavka.client.user.widget.grid.columns.MessageTextColumn.TableDisplayMessageText;
import com.eprovement.poptavka.client.user.widget.grid.columns.RatingColumn.TableDisplayRating;
import com.eprovement.poptavka.shared.domain.TableDisplayDetailModule;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;


import java.util.Date;

/**
 * Represents full detail of demand. Serves for creating new demand or for call of detail, that supports editing.
 *
 * @author Beho
 */
public class ClientDemandConversationDetail implements IsSerializable,
    TableDisplayDetailModule, TableDisplayDisplayName, TableDisplayMessageText,
    TableDisplayMessageSentDate, TableDisplayUserMessage, TableDisplayRating {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private long demandId;
    private long threadMessageId;
    private long userMessageId;
    private long supplierId;
    private long supplierUserId;
    private Date messageSent;
    private String displayName;
    private String messageText;
    private boolean isRead;
    private boolean isStarred;
    private int unreadMessagesCount;
    private Integer rating;
    /**
     * Key provider.
     */
    public static final ProvidesKey<ClientDemandConversationDetail> KEY_PROVIDER =
        new ProvidesKey<ClientDemandConversationDetail>() {
            @Override
            public Object getKey(ClientDemandConversationDetail item) {
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
    /**
     * Demand is pair.
     */
    @Override
    public long getDemandId() {
        return demandId;
    }

    public void setDemandId(long demandId) {
        this.demandId = demandId;
    }

    /**
     * Unread messages count pair.
     */
    @Override
    public int getUnreadMessagesCount() {
        return unreadMessagesCount;
    }

    public void setUnreadMessagesCount(int unreadMessagesCount) {
        this.unreadMessagesCount = unreadMessagesCount;
    }

    /**
     * Is message starred pair.
     */
    @Override
    public boolean isStarred() {
        return isStarred;
    }

    public void setStarred(boolean isStarred) {
        this.isStarred = isStarred;
    }

    /**
     * Supplier id pair.
     */
    @Override
    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * User message id pair.
     */
    @Override
    public long getUserMessageId() {
        return userMessageId;
    }

    public void setUserMessageId(long userMessageId) {
        this.userMessageId = userMessageId;
    }

    /**
     * Message text pair.
     */
    @Override
    public String getMessageText() {
        return messageText;
    }

    public void setMessageBody(String messageBody) {
        this.messageText = messageBody;
    }

    /**
     * Display name pair.
     */
    @Override
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Overal rating pair.
     */
    @Override
    public Integer getOveralRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;;
    }

    /**
     * Message thread root id pair.
     */
    @Override
    public long getThreadRootId() {
        return threadMessageId;
    }

    public void setThreadRootId(long threadMessageId) {
        this.threadMessageId = threadMessageId;
    }

    /**
     * Message's sender id pair.
     */
    @Override
    public long getSenderId() {
        return supplierUserId;
    }

    public void setSenderId(long supplierUserId) {
        this.supplierUserId = supplierUserId;
    }

    /**
     * Message sent date pair.
     */
    @Override
    public Date getMessageSentDate() {
        return messageSent;
    }

    public void setMessageSent(Date messageSent) {
        this.messageSent = messageSent;
    }

    /**
     * Is message read pair.
     */
    @Override
    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }
}
