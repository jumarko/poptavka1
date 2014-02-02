/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.domain.offer;

import com.eprovement.poptavka.client.user.widget.grid.IUniversalDetail;
import com.eprovement.poptavka.client.user.widget.grid.TableDisplayUserMessage;
import com.eprovement.poptavka.client.user.widget.grid.columns.DemandTitleColumn.TableDisplayDemandTitle;
import com.eprovement.poptavka.client.user.widget.grid.columns.DisplayNameColumn.TableDisplayDisplayName;
import com.eprovement.poptavka.client.user.widget.grid.columns.FinishDateColumn.TableDisplayFinishDate;
import com.eprovement.poptavka.client.user.widget.grid.columns.OfferReceivedDateColumn.TableDisplayOfferReceivedDate;
import com.eprovement.poptavka.client.user.widget.grid.columns.PriceColumn.TableDisplayPrice;
import com.eprovement.poptavka.client.user.widget.grid.columns.RatingColumn.TableDisplayRating;
import com.eprovement.poptavka.shared.domain.TableDisplayDetailModule;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;
import java.math.BigDecimal;

import java.util.Date;

/**
 *
 * @author ivlcek, Martin Slavkovsky
 */
public class SupplierOffersDetail implements IsSerializable, IUniversalDetail,
    TableDisplayDetailModule, TableDisplayOfferReceivedDate, TableDisplayFinishDate,
    TableDisplayDemandTitle, TableDisplayPrice, TableDisplayRating, TableDisplayDisplayName,
    TableDisplayUserMessage {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private long demandId;
    private long offerId;
    private long threadRootId;
    private long supplierId;
    private long supplierUserId;
    private long userMessageId;
    private boolean isStarred;
    private boolean isRead;
    private String displayName;
    private BigDecimal price;
    private Integer rating;
    private Date receivedDate;
    private Date finishDate;
    private int messagesCount;
    private long senderId;
    private String title;
    //Keyprovider
    public static final ProvidesKey<SupplierOffersDetail> KEY_PROVIDER =
        new ProvidesKey<SupplierOffersDetail>() {
            @Override
            public Object getKey(SupplierOffersDetail item) {
                return item == null ? null : item.getOfferId();
            }
        };

    /**************************************************************************/
    /* Constructors                                                           */
    /**************************************************************************/
    public SupplierOffersDetail() {
        //for serialization
    }

    /**************************************************************************/
    /* Getters & Setters                                                      */
    /**************************************************************************/
    /**
     * Demand id pair.
     */
    @Override
    public long getDemandId() {
        return demandId;
    }

    public void setDemandId(long demandId) {
        this.demandId = demandId;
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
     * Offer id pair.
     */
    @Override
    public long getOfferId() {
        return offerId;
    }

    public void setOfferId(long offerId) {
        this.offerId = offerId;
    }

    /**
     * Message's thread root id pair.
     */
    @Override
    public long getThreadRootId() {
        return threadRootId;
    }

    public void setThreadRootId(long threadRootId) {
        this.threadRootId = threadRootId;
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
     * Offer price pair.
     */
    @Override
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Client overal rating pair.
     */
    @Override
    public Integer getOveralRating() {
        return rating;
    }

    public void setOveralRating(int rating) {
        this.rating = rating;
    }

    /**
     * Offer received date pair.
     */
    @Override
    public Date getOfferReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }

    /**
     * Demand finish date pair.
     */
    @Override
    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    /**
     * Unread messages count pair.
     */
    @Override
    public int getMessagesCount() {
        return messagesCount;
    }

    @Override
    public void setMessagesCount(int messagesCount) {
        this.messagesCount = messagesCount;
    }

    /**
     * @return unread messages count
     */
    @Override
    public int getUnreadMessagesCount() {
        return 0;
    }

    /**
     * Is message starred pair.
     */
    @Override
    public boolean isStarred() {
        return isStarred;
    }

    @Override
    public void setStarred(boolean value) {
        this.isStarred = value;
    }

    /**
     * Sender id pair.
     */
    @Override
    public long getSenderId() {
        return this.senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    /**
     * User message id pair.
     */
    @Override
    public long getUserMessageId() {
        return userMessageId;
    }

    @Override
    public void setUserMessageId(long userMessageId) {
        this.userMessageId = userMessageId;
    }

    /**
     * Demand title pair.
     */
    @Override
    public String getDemandTitle() {
        return this.title;
    }

    public void setDemandTitle(String title) {
        this.title = title;
    }

    /**
     * Is message read pair.
     */
    @Override
    public boolean isRead() {
        return isRead;
    }

    @Override
    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    /**
     * To string method.
     */
    @Override
    public String toString() {
        return "ClientOfferedDemandOffersDetail{" + "demandId=" + demandId + ", offerId=" + offerId
            + ", threadRootId=" + threadRootId + ", supplierId=" + supplierId + ", supplierUserId="
            + supplierUserId + ", isStarred=" + isStarred + ", supplierName="
            + displayName + ", price=" + price + ", rating=" + rating + ", receivedDate=" + receivedDate
            + ", deliveryDate=" + finishDate + ", messageCount=" + messagesCount + ", isRead="
            + isRead + '}';
    }
}