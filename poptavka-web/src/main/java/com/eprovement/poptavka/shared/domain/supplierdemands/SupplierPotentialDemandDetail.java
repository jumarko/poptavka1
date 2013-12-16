/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.domain.supplierdemands;

import com.eprovement.poptavka.client.user.widget.grid.TableDisplayUserMessage;
import com.eprovement.poptavka.client.user.widget.grid.columns.DemandTitleColumn.TableDisplayDemandTitle;
import com.eprovement.poptavka.client.user.widget.grid.columns.PriceColumn.TableDisplayPrice;
import com.eprovement.poptavka.client.user.widget.grid.columns.RatingColumn.TableDisplayRating;
import com.eprovement.poptavka.client.user.widget.grid.columns.UrgencyColumn.TableDisplayValidTo;
import com.eprovement.poptavka.shared.domain.TableDisplayDetailModule;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;
import java.math.BigDecimal;

import java.util.Date;

/**
 *
 * @author Ivan
 */
public class SupplierPotentialDemandDetail implements IsSerializable,
    TableDisplayRating, TableDisplayUserMessage, TableDisplayDemandTitle, TableDisplayPrice,
    TableDisplayValidTo, TableDisplayDetailModule {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private Integer clientRating;
    private long threadRootId;
    private long senderId;
    private long userMessageId;
    private boolean isStarred;
    private int unreadMessagesCount;
    private boolean isRead;
    private long demandId;
    private long supplierId;
    private Date validTo;
    private String title;
    private BigDecimal price;
    // Keyprovider
    //--------------------------------------------------------------------------
    public static final ProvidesKey<SupplierPotentialDemandDetail> KEY_PROVIDER =
        new ProvidesKey<SupplierPotentialDemandDetail>() {
            @Override
            public Object getKey(SupplierPotentialDemandDetail item) {
                return item == null ? null : item.getDemandId();
            }
        };

    /**************************************************************************/
    /* Constructors                                                           */
    /**************************************************************************/
    public SupplierPotentialDemandDetail() {
        //for serialization
    }

    /**************************************************************************/
    /* Getters & Setters                                                      */
    /**************************************************************************/
    /**
     * Overal rating pair.
     */
    @Override
    public Integer getOveralRating() {
        return clientRating;
    }

    public void setOveralRating(int rating) {
        clientRating = rating;
    }

    /**
     * Message thread root id pair.
     */
    @Override
    public long getThreadRootId() {
        return threadRootId;
    }

    public void setThreadRootId(long threadRootId) {
        this.threadRootId = threadRootId;
    }

    /**
     * Message sender id pair.
     */
    @Override
    public long getSenderId() {
        return senderId;
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

    public void setUserMessageId(long userMessageId) {
        this.userMessageId = userMessageId;
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
     * Valid to date pair.
     */
    @Override
    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    /**
     * Demand title pair.
     */
    @Override
    public String getDemandTitle() {
        return title;
    }

    public void setDemandTitle(String title) {
        this.title = title;
    }

    /**
     * Demand price pair.
     */
    @Override
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
}
