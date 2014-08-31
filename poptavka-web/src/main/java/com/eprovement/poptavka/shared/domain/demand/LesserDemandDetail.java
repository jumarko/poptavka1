/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.shared.domain.demand;

import com.eprovement.poptavka.client.user.widget.grid.columns.CreatedDateColumn.TableDisplayCreatedDate;
import com.eprovement.poptavka.client.user.widget.grid.columns.DemandStatusColumn.TableDisplayDemandStatus;
import com.eprovement.poptavka.client.user.widget.grid.columns.DemandTitleColumn.TableDisplayDemandTitle;
import com.eprovement.poptavka.client.user.widget.grid.columns.LocalityColumn.TableDisplayLocality;
import com.eprovement.poptavka.client.user.widget.grid.columns.UrgencyColumn.TableDisplayValidTo;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.search.ISortField;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Represents lesser detail of domain object <b>Demand</b> used in
 * <i>Universal Grid</i> table.
 *
 * @author Ivan Vlcek
 */
public class LesserDemandDetail implements IsSerializable, TableDisplayCreatedDate,
    TableDisplayValidTo, TableDisplayDemandTitle, TableDisplayDemandStatus, TableDisplayLocality {

    /**************************************************************************/
    /* Enums                                                                  */
    /**************************************************************************/
    //Only fields that can be updated
    public enum DemandField implements ISortField {

        TITLE("title"),
        END_DATE("endDate"),
        VALID_TO("validTo"),
        LOCALITIES("localities"),
        DEMAND_STATUS("status"),
        CREATED("createdDate");

        private String value;

        private DemandField(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }

        public static DemandField toDemandField(String value) {
            for (DemandField field : DemandField.values()) {
                if (field.getValue().equals(value)) {
                    return field;
                }
            }
            return null;
        }
    }

    public enum DemandType {

        NORMAL("normal"),
        ATTRACTIVE("attractive");
        private String value;

        private DemandType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private ArrayList<ICatLocDetail> localities = new ArrayList<ICatLocDetail>();
    private String demandType;
    private long demandId;
    private Date created;
    private String title;
    private Date endDate;
    private Date validTo;
    private DemandStatus status;
    //KeyProvider
    public static final ProvidesKey<LesserDemandDetail> KEY_PROVIDER =
        new ProvidesKey<LesserDemandDetail>() {
            @Override
            public Object getKey(LesserDemandDetail item) {
                return item == null ? null : item.getDemandId();
            }
        };

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates LesserDemandDetail.
     */
    public LesserDemandDetail() {
        //For serialization.
    }

    /**************************************************************************/
    /* Getter & Setter pairs                                                  */
    /**************************************************************************/
    /*
     * Localities pair
     */
    @Override
    public ArrayList<ICatLocDetail> getLocalities() {
        return localities;
    }

    public void setLocalities(Collection<ICatLocDetail> localities) {
        this.localities = new ArrayList<ICatLocDetail>(localities);
    }

    /*
     * Demand type pair.
     */
    public String getDemandType() {
        return demandType;
    }

    public void setDemandType(String demandType) {
        this.demandType = demandType;
    }

    /*
     * Demand created date pair.
     */
    @Override
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    /*
     * Demand id pair.
     */
    public long getDemandId() {
        return demandId;
    }

    public void setDemandId(long demandId) {
        this.demandId = demandId;
    }

    /*
     * End date pair.
     */
    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /*
     * Valid to date.
     */
    @Override
    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validToDate) {
        this.validTo = validToDate;
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
     * Offer status pair.
     */
    @Override
    public DemandStatus getDemandStatus() {
        return status;
    }

    public void setDemandStatus(DemandStatus demandStatus) {
        this.status = demandStatus;
    }

    /**************************************************************************/
    /* Overriden methods                                                      */
    /**************************************************************************/
    @Override
    public String toString() {
        return "\nLesser Demand Detail Info"
            + "\n     demandId=" + demandId
            + "\n     title=" + title
            + "\n     endDate=" + endDate
            + "\n     validToDate=" + validTo
            + "\n     localities=" + localities
            + "\n     demandType=" + demandType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LesserDemandDetail other = (LesserDemandDetail) obj;
        if (this.demandId != other.demandId) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (int) (this.demandId ^ (this.demandId >>> 32));
        return hash;
    }
}
