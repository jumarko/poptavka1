package com.eprovement.poptavka.shared.domain.clientdemands;

import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.TableDisplay;
import com.google.gwt.view.client.ProvidesKey;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents full detail of demand. Serves for creating new demand or for call of detail, that supports editing.
 *
 * @author Beho
 */
public class ClientDemandConversationDetail implements Serializable, TableDisplay {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -530982467233195456L;
    private long demandId;
    private long messageId;
    private long threadMessageId;
    private long userMessageId;
    private long supplierId;
    private String supplierName;
    private MessageDetail messageDetail;
    private Date date;
    private boolean read = false;
    private boolean starred = false;
    private int messageCount = -1;
    private int unreadSubmessages = -1;
    public static final ProvidesKey<ClientDemandConversationDetail> KEY_PROVIDER =
            new ProvidesKey<ClientDemandConversationDetail>() {
                @Override
                public Object getKey(ClientDemandConversationDetail item) {
                    return item == null ? null : item.getUserMessageId();
                }
            };

    //---------------------------- GETTERS AND SETTERS --------------------
    public long getDemandId() {
        return demandId;
    }

    public void setDemandId(long demandId) {
        this.demandId = demandId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
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

    public int getUnreadSubmessages() {
        return unreadSubmessages;
    }

    public void setUnreadSubmessages(int unreadSubmessages) {
        this.unreadSubmessages = unreadSubmessages;
    }

    public long getUserMessageId() {
        return userMessageId;
    }

    public void setUserMessageId(long userMessageId) {
        this.userMessageId = userMessageId;
    }

    public MessageDetail getMessageDetail() {
        return messageDetail;
    }

    public void setMessageDetail(MessageDetail messageDetail) {
        this.messageDetail = messageDetail;
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
     * @return the threadMessageId
     */
    public long getThreadMessageId() {
        return threadMessageId;
    }

    /**
     * @param threadMessageId the threadMessageId to set
     */
    public void setThreadMessageId(long threadMessageId) {
        this.threadMessageId = threadMessageId;
    }

    /**
     * Display string as HTML. We suppose calling of this method always come from trusted (programmed) source.
     * User CANNOT call this nethod due to security issues.
     * @param trustedHtml
     * @return string in html tags
     */
    public static String displayHtml(String trustedHtml, int unReadSubmessagesCount) {
        if (unReadSubmessagesCount > 0) {
            return "<strong>" + trustedHtml + "</strong>";
        } else {
            return trustedHtml;
        }
    }

    public static String displaySupplierNameHtml(ClientDemandConversationDetail detail) {
        StringBuilder str = new StringBuilder();
        str.append(detail.getSupplierName());
        str.append(" ");
        str.append(detail.getUnreadSubmessages());
        str.append("/");
        str.append(detail.getMessageCount());
        if (detail.getUnreadSubmessages() > 0) {
            return "<strong>" + str.toString() + "</strong>";
        } else {
            return str.toString();
        }
    }
}
