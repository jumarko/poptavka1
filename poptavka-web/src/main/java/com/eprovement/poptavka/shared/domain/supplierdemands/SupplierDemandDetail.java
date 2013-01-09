package com.eprovement.poptavka.shared.domain.supplierdemands;

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
public class SupplierDemandDetail implements Serializable, TableDisplay {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -530982467233195456L;
    private long demandId;
    private long messageId;
    private long userMessageId;
    private String clientName;
    private String supplierName;
    private String demandTitle; //title
    private int rating;
    private BigDecimal price = null;
    private Date endDate; //urgency
    private Date receivedDate;
    private Date deliveryDate;
    private boolean read = false;
    private boolean starred = false;
    public static final ProvidesKey<SupplierDemandDetail> KEY_PROVIDER =
            new ProvidesKey<SupplierDemandDetail>() {

                @Override
                public Object getKey(SupplierDemandDetail item) {
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

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
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
        return rating;
    }

    public Date getExpireDate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public Date getAcceptedDate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getTitle() {
        return demandTitle;
    }

    public void setTitle(String title) {
        demandTitle = title;
    }

    public OfferStateType getOfferState() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    @Override
    public DemandStatus getDemandStatus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
