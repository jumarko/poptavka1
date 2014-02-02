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
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;
import java.math.BigDecimal;

import java.util.Date;

/**
 *
 * @author ivlcek, Martin Slavkovsky
 */
public class ClientOfferedDemandOffersDetail implements IsSerializable, IUniversalDetail,
    TableDisplayUserMessage, TableDisplayPrice, TableDisplayDemandTitle, TableDisplayRating,
    TableDisplayDisplayName, TableDisplayOfferReceivedDate, TableDisplayFinishDate {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private long demandId;
    private long offerId;
    private long threadRootId;
    private long supplierId;
    private long senderId;
    private long userMessageId;
    private boolean isStarred;
    private boolean isRead;
    private String displayName;
    private BigDecimal price;
    private String demandTitle;
    private Integer rating;
    private Date receivedDate;
    private Date finishDate;
    private int messagesCount;
    //Keyprovider
    public static final ProvidesKey<IUniversalDetail> KEY_PROVIDER =
        new ProvidesKey<IUniversalDetail>() {
            @Override
            public Object getKey(IUniversalDetail item) {
                return item == null ? null : item.getOfferId();
            }
        };

    /**************************************************************************/
    /* Constructors                                                           */
    /**************************************************************************/
    public ClientOfferedDemandOffersDetail() {
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
     * Message's thread root id.
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
     * Client rating pair.
     */
    @Override
    public Integer getOveralRating() {
        return rating;
    }

    public void setRating(int rating) {
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

    public void setDeliveryDate(Date deliveryDate) {
        this.finishDate = deliveryDate;
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
     * @return unread messages count, 0 couse its unvailable
     */
    @Override
    public int getUnreadMessagesCount() {
        return 0;
    }

    /**
     * Is messsage starred pair.
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
     * Message sender id pair.
     */
    @Override
    public long getSenderId() {
        return this.senderId;
    }

    public void setSenderId(long senderId) {
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
        return demandTitle;
    }

    public void setDemandTitle(String title) {
        demandTitle = title;
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
            + ", threadRootId=" + threadRootId + ", supplierId=" + supplierId + ", senderId="
            + senderId + ", isStarred=" + isStarred + ", supplierName="
            + displayName + ", price=" + price + ", rating=" + rating + ", receivedDate=" + receivedDate
            + ", deliveryDate=" + finishDate + ", messageCount=" + messagesCount + ", isRead="
            + isRead + '}';
    }
}