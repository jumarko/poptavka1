/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.shared.domain.message;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cz.poptavka.sample.domain.message.Message;

/**
 *
 * @author ivan.vlcek
 */
public class ClientDemandMessageDetail implements Serializable {

    private long messageId;
    private long threadRoodId;
    private long demandId;
    private long senderId;
    private int unreadSubmessages;
    // demand fields
    private String demandTitle;
    private String demandStatus;
    private Date endDate;
    private Date expiryDate;
    private BigDecimal price;

    public static ClientDemandMessageDetail createDetail(Message message, long unreadMessageCount) {
        ClientDemandMessageDetail detail = new ClientDemandMessageDetail();
        detail.setMessageId(message.getId());
        detail.setThreadRoodId(message.getThreadRoot().getId());
        detail.setDemandId(message.getDemand().getId());
        detail.setSenderId(message.getSender().getId());
        detail.setUnreadSubmessages((int) unreadMessageCount);
        detail.setDemandTitle(message.getDemand().getTitle());
        detail.setDemandStatus(message.getDemand().getStatus().toString());
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
    public String getDemandStatus() {
        return demandStatus;
    }

    /**
     * @param demandStatus the demandStatus to set
     */
    public void setDemandStatus(String demandStatus) {
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
        this.price = price;
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
}
