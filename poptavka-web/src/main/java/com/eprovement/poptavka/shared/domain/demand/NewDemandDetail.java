/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.shared.domain.demand;

import com.eprovement.poptavka.client.user.widget.grid.columns.CreatedDateColumn.TableDisplayCreatedDate;
import com.eprovement.poptavka.client.user.widget.grid.columns.DemandTitleColumn.TableDisplayDemandTitle;
import com.eprovement.poptavka.client.user.widget.grid.columns.LocalityColumn.TableDisplayLocality;
import com.eprovement.poptavka.client.user.widget.grid.columns.UrgencyColumn.TableDisplayValidTo;
import com.eprovement.poptavka.shared.domain.TableDisplayDetailModule;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

import java.util.ArrayList;
import java.util.Date;

/**
 * Represents full detail of domain object <b>FullDemand</b> used in
 * <i>Administration Module</i>. Contains 2 static methods: 1. creating detail
 * object 2. updating domain object
 *
 * @author Martin Slavkovsky
 */
public class NewDemandDetail implements IsSerializable, TableDisplayDetailModule,
    TableDisplayDemandTitle, TableDisplayCreatedDate, TableDisplayValidTo, TableDisplayLocality {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private ArrayList<ICatLocDetail> localities;
    private long senderId;
    private long demandId;
    private long threadRootId;
    private Date created;
    private String title;
    private Date validTo;
    public static final ProvidesKey<NewDemandDetail> KEY_PROVIDER =
        new ProvidesKey<NewDemandDetail>() {
            @Override
            public Object getKey(NewDemandDetail item) {
                return item == null ? null : item.getDemandId();
            }
        };

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates NewDemandDetail.
     */
    public NewDemandDetail() {
        // for serialization.
    }

    /**************************************************************************/
    /* Getter & Setter pairs                                                  */
    /**************************************************************************/
    /*
     * Localities pair.
     */
    @Override
    public ArrayList<ICatLocDetail> getLocalities() {
        return localities;
    }

    public void setLocalities(ArrayList<ICatLocDetail> localities) {
        this.localities = localities;
    }

    /*
     * Demand id pair.
     */
    @Override
    public long getDemandId() {
        return demandId;
    }

    public void setDemandId(long demandId) {
        this.demandId = demandId;
    }

    /*
     * Message's thread root id pair.
     */
    @Override
    public long getThreadRootId() {
        return threadRootId;
    }

    public void setThreadRootId(long threadRootId) {
        this.threadRootId = threadRootId;
    }

    /*
     * Demand's creation date pair.
     */
    @Override
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    /*
     * Demand title pair.
     */
    @Override
    public String getDemandTitle() {
        return title;
    }

    public void setDemandTitle(String title) {
        this.title = title;
    }

    /*
     * Demand valid to date pair.
     */
    @Override
    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    /*
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
     * @return the unread messages count
     */
    @Override
    public int getUnreadMessagesCount() {
        //always return 0.
        return 0;
    }

    /**************************************************************************/
    /* Overriden methods                                                      */
    /**************************************************************************/
    @Override
    public String toString() {
        return "\nNew Demand Detail Info"
            + "\n     demandId=" + demandId
            + "\n     threadRootId=" + threadRootId
            + "\n     title=" + title
            + "\n     validToDate=" + validTo
            + "\n     localities=" + localities.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NewDemandDetail other = (NewDemandDetail) obj;
        if (this.demandId != other.demandId) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (int) (this.demandId ^ (this.demandId >>> 32));
        return hash;
    }

    @Override
    public long getSupplierId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}