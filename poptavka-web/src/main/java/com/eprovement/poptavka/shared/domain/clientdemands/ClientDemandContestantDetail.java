package com.eprovement.poptavka.shared.domain.clientdemands;

import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.eprovement.poptavka.shared.domain.message.TableDisplay;
import com.google.gwt.view.client.ProvidesKey;
import java.io.Serializable;
import java.util.Date;

/**
 * Represents full detail of demand. Serves for creating new demand or for call of detail, that supports editing.
 *
 * @author Beho
 */
public class ClientDemandContestantDetail implements Serializable, TableDisplay {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -530982467233195456L;
    private long demandId;
    private long messageId;
    private long userMessageId;
    private long supplierId;
    private DemandStatus demandStatus;
    private String supplierName;
    private String price;
    private Date receiveDate;
    private Date acceptedDate;
    private int rating;
    private boolean read = false;
    private boolean starred = false;
    private int messageCount = -1;
    private int unreadSubmessages = -1;

    public static final ProvidesKey<ClientDemandContestantDetail> KEY_PROVIDER =
            new ProvidesKey<ClientDemandContestantDetail>() {

                @Override
                public Object getKey(ClientDemandContestantDetail item) {
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

    public Date getAcceptedDate() {
        return acceptedDate;
    }

    public void setAcceptedDate(Date acceptedDate) {
        this.acceptedDate = acceptedDate;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
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

    @Override
    public Date getEndDate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DemandStatus getDemandStatus() {
        return demandStatus;
    }

    public void setDemandStatus(DemandStatus demandStatus) {
        this.demandStatus = demandStatus;
    }

    @Override
    public OfferStateType getOfferState() {
        //TODO Martin - temporary , correct later - clientAssignedProjects assked for this
        return OfferStateType.ACCEPTED;
    }


}
