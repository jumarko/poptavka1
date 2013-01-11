/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.domain.offer;

import com.eprovement.poptavka.client.user.widget.grid.IUniversalDetail;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.eprovement.poptavka.shared.domain.message.TableDisplay;
import com.google.gwt.view.client.ProvidesKey;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ivlcek
 */
public class ClientOfferedDemandOffersDetail implements Serializable, TableDisplay, IUniversalDetail {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -830380623938612376L;
    private long demandId;
    private long offerId;
    private long threadRootId;
    private long supplierId;
    private long supplierUserId;
    private boolean isStarred;
    private String supplierName;
    private String price;
    private Integer rating;
    private Date receivedDate;
    private Date deliveryDate;
    private int messageCount;
    private int unreadMessageCount;
    //Keyprovider
    public static final ProvidesKey<IUniversalDetail> KEY_PROVIDER =
            new ProvidesKey<IUniversalDetail>() {
                @Override
                public Object getKey(IUniversalDetail item) {
                    return item == null ? null : item.getOfferId();
                }
            };

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
     * @return the offerId
     */
    @Override
    public long getOfferId() {
        return offerId;
    }

    /**
     * @param offerId the offerId to set
     */
    public void setOfferId(long offerId) {
        this.offerId = offerId;
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
     * @return the supplierId
     */
    @Override
    public long getSupplierId() {
        return supplierId;
    }

    /**
     * @param supplierId the supplierId to set
     */
    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    /**
     * @return the supplierUserId
     */
    public long getSupplierUserId() {
        return supplierUserId;
    }

    /**
     * @param supplierUserId the supplierUserId to set
     */
    public void setSupplierUserId(long supplierUserId) {
        this.supplierUserId = supplierUserId;
    }

    /**
     * @return the supplierName
     */
    @Override
    public String getSupplierName() {
        return supplierName;
    }

    /**
     * @param supplierName the supplierName to set
     */
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
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

    /**
     * @return the rating
     */
    @Override
    public int getRating() {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * @return the receivedDate
     */
    @Override
    public Date getReceivedDate() {
        return receivedDate;
    }

    /**
     * @param receivedDate the receivedDate to set
     */
    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    /**
     * @return the deliveryDate
     */
    @Override
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * @param deliveryDate the deliveryDate to set
     */
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
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
    public boolean isStarred() {
        return isStarred;
    }

    @Override
    public void setStarred(boolean value) {
        this.isStarred = value;
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

    @Override
    public long getClientId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getClientName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getMessageId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getSenderId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Date getMessageSent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getUserMessageId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Date getValidTo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getTitle() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String displayUserNameWithUnreadMessageCounts(int displayWhat) {
        StringBuilder str = new StringBuilder();
        switch (displayWhat) {
            case IUniversalDetail.CLIENT_NAME:
                str.append(getClientName());
                break;
            case IUniversalDetail.SUPPLIER_NAME:
                str.append(getSupplierName());
                break;
            default:
                break;
        }
        str.append(" ");
        str.append(getUnreadMessageCount());
        str.append("/");
        str.append(getMessageCount());
        return str.toString();
    }


    @Override
    public String toString() {
        return "ClientOfferedDemandOffersDetail{" + "demandId=" + demandId + ", offerId=" + offerId
                + ", threadRootId=" + threadRootId + ", supplierId=" + supplierId + ", supplierUserId="
                + supplierUserId + ", isStarred=" + isStarred + ", supplierName="
                + supplierName + ", price=" + price + ", rating=" + rating + ", receivedDate=" + receivedDate
                + ", deliveryDate=" + deliveryDate + ", messageCount=" + messageCount + ", unreadMessageCount="
                + unreadMessageCount + '}';
    }
}