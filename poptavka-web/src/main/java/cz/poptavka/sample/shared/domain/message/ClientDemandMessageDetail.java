/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.shared.domain.message;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.google.gwt.view.client.ProvidesKey;

import cz.poptavka.sample.domain.demand.DemandStatus;
import cz.poptavka.sample.domain.message.Message;

/**
 *
 * @author ivan.vlcek
 */
public class ClientDemandMessageDetail implements Serializable, TableDisplay {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private long messageId;
    private long threadRoodId;
    private long demandId;
    private long senderId;
    private int submessages;
    private int unreadSubmessages;
    // demand fields
    private String demandTitle;
    private DemandStatus demandStatus;
    private Date endDate;
    private Date expiryDate;
    private BigDecimal price;
    private boolean read;
    private Date createdDate;
    private String clientName;
    private int clientRating;

    public static ClientDemandMessageDetail createDetail(Message message,
            long submessageCount, long unreadSubmessageCount) {
        ClientDemandMessageDetail detail = new ClientDemandMessageDetail();
        detail.setMessageId(message.getId());
        detail.setThreadRoodId(message.getThreadRoot().getId());
        detail.setDemandId(message.getDemand().getId());
        detail.setSenderId(message.getSender().getId());
        detail.setSubmessages((int) submessageCount);
        detail.setUnreadSubmessages((int) unreadSubmessageCount);
        detail.setDemandTitle(message.getDemand().getTitle());
        detail.setDemandStatus(message.getDemand().getStatus());
        detail.setPrice(message.getDemand().getPrice());
        detail.setEndDate(message.getDemand().getEndDate());
        detail.setExpiryDate(message.getDemand().getValidTo());

        return detail;
    }


    /**
     * @return the messageId
     */
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
     * @return the threadRoodId
     */
    public long getThreadRoodId() {
        return threadRoodId;
    }

    /**
     * @param threadRoodId the threadRoodId to set
     */
    public void setThreadRoodId(long threadRoodId) {
        this.threadRoodId = threadRoodId;
    }

    /**
     * @return the demandId
     */
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
     * @return the senderId
     */
    public long getSenderId() {
        return senderId;
    }

    /**
     * @param senderId the senderId to set
     */
    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public int getSubmessages() {
        return submessages;
    }

    public void setSubmessages(int submessages) {
        this.submessages = submessages;
    }

    /**
     * @return the unreadSubmessages
     */
    public int getUnreadSubmessages() {
        return unreadSubmessages;
    }

    /**
     * @param unreadSubmessages the unreadSubmessages to set
     */
    public void setUnreadSubmessages(int unreadSubmessages) {
        this.unreadSubmessages = unreadSubmessages;
    }

    /**
     * @return the demandTitle
     */
    public String getDemandTitle() {
        return demandTitle;
    }

    /**
     * @param demandTitle the demandTitle to set
     */
    public void setDemandTitle(String demandTitle) {
        this.demandTitle = demandTitle;
    }

    /**
     * @return the demandStatus
     */
    public DemandStatus getDemandStatus() {
        return demandStatus;
    }

    /**
     * @param demandStatus the demandStatus to set
     */
    public void setDemandStatus(DemandStatus demandStatus) {
        this.demandStatus = demandStatus;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the expiryDate
     */
    public Date getExpiryDate() {
        return expiryDate;
    }

    /**
     * @param expiryDate the expiryDate to set
     */
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        if (price == null) {
            this.price = BigDecimal.ZERO;
        } else {
            this.price = price;
        }
    }

    @Override
    public String toString() {
        return "ClientDemandMessageDetail{\n" + "messageId=" + messageId
                + ",\n threadRoodId=" + threadRoodId
                + ",\n demandId=" + demandId
                + ",\n senderId=" + senderId
                + ",\n unreadSubmessages=" + unreadSubmessages
                + ",\n demandTitle=" + demandTitle
                + ",\n demandStatus=" + demandStatus
                + ",\n endDate=" + endDate
                + ",\n expiryDate=" + expiryDate
                + ",\n price=" + price + "}\n\n";
    }

    public static final ProvidesKey<ClientDemandMessageDetail> KEY_PROVIDER =
        new ProvidesKey<ClientDemandMessageDetail>() {
            @Override
            public Object getKey(ClientDemandMessageDetail item) {
                return item == null ? null : item.getDemandId();
            }
        };

    @Override
    public String getTitle() {
        return demandTitle;
    }


    @Override
    public boolean isRead() {
        return read;
    }


    @Override
    public void setRead(boolean value) {
        this.read = value;
    }


    @Override
    public boolean isStarred() {
        return false;
    }


    @Override
    public void setStarred(boolean value) {
        // TODO Auto-generated method stub
    }


    @Override
    public Date getCreated() {
        return createdDate;
    }


    @Override
    public String getDemandPrice() {
        return price + "";
    }


    @Override
    public String getFormattedMessageCount() {
        return "(" + submessages + ")";
    }


    @Override
    public int getMessageCount() {
        return submessages;
    }


    @Override
    public String getClientName() {
        return clientName;
    }


    @Override
    public int getClientRating() {
        return clientRating;
    }


    @Override
    public Date getExpireDate() {
        return expiryDate;
    }

}
