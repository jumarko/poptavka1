/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.shared.domain.demand;

import com.eprovement.poptavka.client.common.validation.Extended;
import com.eprovement.poptavka.client.common.validation.SearchGroup;
import com.eprovement.poptavka.client.user.widget.grid.columns.CreatedDateColumn.TableDisplayCreatedDate;
import com.eprovement.poptavka.client.user.widget.grid.columns.DemandStatusColumn.TableDisplayDemandStatus;
import com.eprovement.poptavka.client.user.widget.grid.columns.DemandTitleColumn.TableDisplayDemandTitle;
import com.eprovement.poptavka.client.user.widget.grid.columns.LocalityColumn.TableDisplayLocality;
import com.eprovement.poptavka.client.user.widget.grid.columns.UrgencyColumn.TableDisplayValidTo;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.search.ISortField;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Represents full detail of domain object <b>FullDemand</b> used in
 * <i>Administration Module</i>. Contains 2 static methods: 1. creating detail
 * object 2. updating domain object
 *
 * @author Beho, Martin Slavkovsky
 */
public class FullDemandDetail implements IsSerializable, TableDisplayCreatedDate,
    TableDisplayValidTo, TableDisplayDemandTitle, TableDisplayDemandStatus, TableDisplayLocality {

    /**************************************************************************/
    /* Enums                                                                  */
    /**************************************************************************/
    //Only fields that can be updated
    public enum DemandField implements ISortField {

        TITLE("title"),
        DESCRIPTION("description"),
        PRICE("price"),
        END_DATE("endDate"),
        VALID_TO("validTo"),
        MAX_OFFERS("maxSuppliers"),
        MIN_RATING("minRating"),
        DEMAND_TYPE("type.description"),
        CATEGORIES("categories"),
        LOCALITIES("localities"),
        DEMAND_STATUS("status"),
        CREATED("createdDate"),
        EXCLUDE_SUPPLIER("excludedSuppliers");

        public static final String SEARCH_CLASS = "demand";

        private String value;

        private DemandField(String value) {
            this.value = value;
        }

        @Override
        public String getFieldClass() {
            return SEARCH_CLASS;
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
    private ArrayList<ICatLocDetail> categories = new ArrayList<ICatLocDetail>();
    private long clientId;
    @Min(value = 0, message = "{maxSuppliersMin}")
    private int maxSuppliers;
    private int minRating;
    private int clientRating;
    private String demandType;
    private DemandStatus status;
    private long demandId;
    private boolean starred;
    private Date created;
    //Basic Demand Info
    @NotBlank(message = "{titleNotBlank}")
    @Pattern(regexp = "[a-zA-Z0-9\\ ,]*", message = "{patternNoSpecChars}",
    groups = {Extended.class, SearchGroup.class })
    @Size(min = 5, max = 100, message = "{titleSize}", groups = Extended.class)
    private String title;
    @NotNull(message = "{priceNotNull}")
    //@Pattern cannot be used for non String attributes
    @Digits(integer = 12, fraction = 0, message = "{priceDigits}", groups = {Extended.class, SearchGroup.class })
    @Min(value = 0, message = "{priceMin}", groups = {Extended.class, SearchGroup.class })
    private BigDecimal price;
    @NotNull(message = "{endDateNotNull}")
    @Future(message = "{endDateFuture}", groups = Extended.class)
    private Date endDate;
    @NotNull(message = "{validToNotNull}")
    @Future(message = "{validToFuture}", groups = Extended.class)
    private Date validTo;
    @NotBlank(message = "{descriptionNotBlank}")
    @Size(min = 20, max = 5000, message = "{descriptionSize}", groups = Extended.class)
    private String description;
    private List<FullSupplierDetail> excludedSuppliers;
    //KeyProvider
    public static final ProvidesKey<FullDemandDetail> KEY_PROVIDER =
        new ProvidesKey<FullDemandDetail>() {
            @Override
            public Object getKey(FullDemandDetail item) {
                return item == null ? null : item.getDemandId();
            }
        };

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates FullDemandDetail.
     */
    public FullDemandDetail() {
        //For serialization.
    }

    /**************************************************************************/
    /* Getter & Setter pairs                                                  */
    /**************************************************************************/
    /*
     * Categories pair
     */
    public ArrayList<ICatLocDetail> getCategories() {
        return categories;
    }

    public void setCategories(Collection<ICatLocDetail> categories) {
        this.categories = new ArrayList<ICatLocDetail>(categories);
    }

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
     * Client id pair.
     */
    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    /*
     * Max suppliers pair.
     */
    public int getMaxSuppliers() {
        return maxSuppliers;
    }

    public void setMaxSuppliers(int maxSuppliers) {
        this.maxSuppliers = maxSuppliers;
    }

    /*
     * Min rating pair.
     */
    public int getMinRating() {
        return minRating;
    }

    public void setMinRating(int minRating) {
        this.minRating = minRating;
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

    /**
     * @return the unread messages count
     */
    @Override
    public int getUnreadMessagesCount() {
        return 0;
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
     * Description pair.
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /*
     * Price pair.
     */
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /*
     * Exclude suppliers pair
     */
    public List<FullSupplierDetail> getExcludedSuppliers() {
        return excludedSuppliers;
    }

    public void setExcludedSuppliers(List<FullSupplierDetail> excludedSuppliers) {
        this.excludedSuppliers = excludedSuppliers;
    }

    /*
     * Client rating pair
     */
    public int getClientRating() {
        return clientRating;
    }

    public void setClientRating(int clientRating) {
        this.clientRating = clientRating;
    }

    /**************************************************************************/
    /* Overriden methods                                                      */
    /**************************************************************************/
    @Override
    public String toString() {
        return "\nGlobal Demand Detail Info"
            + "\n- BaseDemandDetail:"
            + "\n     demandId=" + demandId
            + "\n     title=" + title
            + "\n     Description=" + description
            + "\n     Price=" + price
            + "\n     endDate=" + endDate
            + "\n     validToDate=" + validTo
            + "\n     isStarred=" + starred
            + "\n"
            + "* FullDemandDetail:"
            + "\n     localities=" + localities
            + "\n     categories=" + categories
            + "\n     clientId=" + clientId
            + "\n     maxOffers=" + maxSuppliers
            + "\n     minRating=" + minRating
            + "\n     demandType=" + demandType
            + "\n     demandStatus=" + status;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FullDemandDetail other = (FullDemandDetail) obj;
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
}
