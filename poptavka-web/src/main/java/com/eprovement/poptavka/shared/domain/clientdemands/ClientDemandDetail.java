/*
 * Copyright (C) 2012, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.shared.domain.clientdemands;

import com.eprovement.poptavka.client.user.widget.grid.columns.DemandStatusColumn.TableDisplayDemandStatus;
import com.eprovement.poptavka.client.user.widget.grid.columns.DemandTitleColumn.TableDisplayDemandTitle;
import com.eprovement.poptavka.client.user.widget.grid.columns.EndDateColumn.TableDisplayEndDate;
import com.eprovement.poptavka.client.user.widget.grid.columns.PriceColumn.TableDisplayPrice;
import com.eprovement.poptavka.client.user.widget.grid.columns.UrgencyColumn.TableDisplayValidTo;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.shared.domain.TableDisplayDetailModule;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Represents full detail of demand. Serves for creating new demand or for call of detail, that supports editing.
 *
 * @author Martin Slavkovsky
 */
public class ClientDemandDetail implements IsSerializable,
    TableDisplayDemandTitle, TableDisplayDetailModule,
    TableDisplayDemandStatus, TableDisplayEndDate, TableDisplayValidTo,
    TableDisplayPrice {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private long demandId;
    private long threadRootId;
    private DemandStatus demandStatus;
    private String demandTitle;
    private BigDecimal price;
    private Date endDate;
    private Date validTo;
    private int unreadMessagesCount;
    /**
     * Key provider
     */
    public static final ProvidesKey<ClientDemandDetail> KEY_PROVIDER = new ProvidesKey<ClientDemandDetail>() {
        @Override
        public Object getKey(ClientDemandDetail item) {
            return item == null ? null : item.getDemandId();
        }
    };

    /**************************************************************************/
    /* Constructors                                                           */
    /**************************************************************************/
    /**
     * Creates Client demand detail.
     */
    public ClientDemandDetail() {
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
     * Demand title pair.
     */
    @Override
    public String getDemandTitle() {
        return demandTitle;
    }

    public void setDemandTitle(String demandTitle) {
        this.demandTitle = demandTitle;
    }

    /**
     * Demand status pair.
     */
    @Override
    public DemandStatus getDemandStatus() {
        return demandStatus;
    }

    public void setDemandStatus(DemandStatus demandStatus) {
        this.demandStatus = demandStatus;
    }

    /**
     * End date pair.
     */
    @Override
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Valid to date pair.
     */
    @Override
    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validToDate) {
        this.validTo = validToDate;
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
     * Messages thread root id pair.
     */
    @Override
    public long getThreadRootId() {
        return threadRootId;
    }

    public void setThreadRootId(long threadRootId) {
        this.threadRootId = threadRootId;
    }

    /**
     * Unread messages count pair.
     */
    @Override
    public int getUnreadMessagesCount() {
        return unreadMessagesCount;
    }

    public void setUnreadMessagesCount(int messagesCount) {
        this.unreadMessagesCount = messagesCount;
    }

    @Override
    public long getUserId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getSenderId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
