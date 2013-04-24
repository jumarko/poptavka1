package com.eprovement.poptavka.shared.domain.demand;

import com.eprovement.poptavka.client.common.validation.SearchGroup;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.message.TableDisplay;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.domain.type.DemandDetailType;
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
import javax.validation.constraints.Size;
import javax.validation.groups.Default;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Represents full detail of domain object <b>FullDemand</b> used in
 * <i>Administration Module</i>. Contains 2 static methods: 1. creating detail
 * object 2. updating domain object
 *
 * @author Beho, Martin Slavkovsky
 */
public class FullDemandDetail implements IsSerializable, TableDisplay {

    //Only fields that can be updated
    public enum DemandField {

        TITLE("title"),
        DESCRIPTION("description"),
        PRICE("price"),
        END_DATE("endDate"),
        VALID_TO("validTo"),
        MAX_OFFERS("maxSuppliers"),
        MIN_RATING("minRating"),
        RATING("rating"),
        DEMAND_TYPE("type"),
        CATEGORIES("categories"),
        LOCALITIES("localities"),
        DEMAND_STATUS("status"),
        CREATED("createdDate"),
        EXCLUDE_SUPPLIER("excludedSuppliers");
        private String value;

        private DemandField(String value) {
            this.value = value;
        }

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
    private ArrayList<LocalityDetail> localities;
    private ArrayList<CategoryDetail> categories;
    private long clientId;
    @Min(value = 0, message = "{maxSuppliersMin}")
    private int maxSuppliers;
    private int minRating;
    private int clientRating;
    private String demandType;
    private DemandStatus status;
    private DemandDetailType detailType = DemandDetailType.BASE;
    private long demandId;
    // messageId = threadRoot
    private long messageId;
    private long userMessageId;
    private boolean read;
    private boolean starred;
    private int unreadSubmessagesCount;
    private Date created;
    //Basic Demand Info
    @NotBlank(message = "{titleNotBlank}")
    @Size(min = 5, max = 50, message = "{titleSize}")
    private String title;
    @NotNull(message = "{priceNotNull}")
    @Min(value = 0, message = "{priceMin}", groups = {Default.class, SearchGroup.class })
    @Digits(integer = 12, fraction = 0, message = "{priceDigits}",
    groups = {Default.class, SearchGroup.class })
    private BigDecimal price;
    @NotNull(message = "{endDateNotNull}")
    @Future(message = "{endDateFuture}")
    private Date endDate;
    @NotNull(message = "{validToNotNull}")
    @Future(message = "{validToFuture}")
    private Date validTo;
    @NotBlank(message = "{descriptinoNotBlank}")
    @Size(min = 20, max = 1500, message = "{descriptoinSize}")
    private String description;
    //
    private List<FullSupplierDetail> excludedSuppliers;
    //KeyProvider
    public static final ProvidesKey<FullDemandDetail> KEY_PROVIDER =
            new ProvidesKey<FullDemandDetail>() {
                @Override
                public Object getKey(FullDemandDetail item) {
                    return item == null ? null : item.getDemandId();
                }
            };

    /**
     * for serialization. *
     */
    public FullDemandDetail() {
        super();
        setType(DemandDetailType.FULL);
    }

    public FullDemandDetail(FullDemandDetail demand) {
        this.updateWholeDemand(demand);
    }

    //---------------------------- GETTERS AND SETTERS --------------------
    public void updateWholeDemand(FullDemandDetail demand) {
        demandId = demand.getDemandId();
        title = demand.getTitle();
        description = demand.getDescription();
        price = demand.getPrice();
        created = demand.getCreated();
        endDate = demand.getEndDate();
        validTo = demand.getValidTo();
        maxSuppliers = demand.getMaxSuppliers();
        minRating = demand.getMinRating();
        //categories
        categories = new ArrayList<CategoryDetail>(demand.getCategories());

        //localities
        localities = new ArrayList<LocalityDetail>(demand.getLocalities());

        status = demand.getDemandStatus();
        demandType = demand.getDemandType();
        clientId = demand.getClientId();

        excludedSuppliers = new ArrayList<FullSupplierDetail>();
        for (FullSupplierDetail supplier : demand.getExcludedSuppliers()) {
//            excludedSuppliers.add(new FullSupplierDetail(supplier));
        }
    }

    public ArrayList<CategoryDetail> getCategories() {
        return categories;
    }

    public void setCategories(Collection<CategoryDetail> categories) {
        this.categories = new ArrayList<CategoryDetail>(categories);
    }

    public ArrayList<LocalityDetail> getLocalities() {
        return localities;
    }

    public void setLocalities(Collection<LocalityDetail> localities) {
        this.localities = new ArrayList<LocalityDetail>(localities);
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public int getMaxSuppliers() {
        return maxSuppliers;
    }

    public void setMaxSuppliers(int maxSuppliers) {
        this.maxSuppliers = maxSuppliers;
    }

    public int getMinRating() {
        return minRating;
    }

    public void setMinRating(int minRating) {
        this.minRating = minRating;
    }

    public String getDemandType() {
        return demandType;
    }

    public void setDemandType(String demandType) {
        this.demandType = demandType;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

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
                + "\n     detailType=" + detailType
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
    public DemandStatus getDemandStatus() {
        return status;
    }

    public void setDemandStatus(DemandStatus demandStatus) {
        this.status = demandStatus;
    }

    public DemandDetailType getDetailType() {
        return detailType;
    }

    public void setDetailType(DemandDetailType detailType) {
        this.detailType = detailType;
    }

    public long getDemandId() {
        return demandId;
    }

    public void setDemandId(long demandId) {
        this.demandId = demandId;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public long getUserMessageId() {
        return userMessageId;
    }

    public void setUserMessageId(long userMessageId) {
        this.userMessageId = userMessageId;
    }

    @Override
    public boolean isStarred() {
        return starred;
    }

    @Override
    public void setIsStarred(boolean starred) {
        this.starred = starred;
    }

    public Date getEndDate() {
        return endDate;
    }

    @Override
    public OfferStateType getOfferState() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validToDate) {
        this.validTo = validToDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setType(DemandDetailType demandDetailType) {
        this.detailType = demandDetailType;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<FullSupplierDetail> getExcludedSuppliers() {
        return excludedSuppliers;
    }

    public int getClientRating() {
        return clientRating;
    }

    public void setClientRating(int clientRating) {
        this.clientRating = clientRating;
    }

    public void setExcludedSuppliers(List<FullSupplierDetail> excludedSuppliers) {
        this.excludedSuppliers = excludedSuppliers;
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

    /**
     * @return the unreadSubmessagesCount
     */
    @Override
    public int getUnreadSubmessagesCount() {
        return unreadSubmessagesCount;
    }

    /**
     * @param unreadSubmessagesCount the unreadSubmessagesCount to set
     */
    public void setUnreadSubmessagesCount(int unreadSubmessagesCount) {
        this.unreadSubmessagesCount = unreadSubmessagesCount;
    }
}
