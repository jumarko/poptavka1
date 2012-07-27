package com.eprovement.poptavka.shared.domain.demand;

import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.message.TableDisplay;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.domain.type.DemandDetailType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Represents full detail of domain object <b>FullDemand</b> used in
 * <i>Administration Module</i>. Contains 2 static methods: 1. creating detail
 * object 2. updating domain object
 *
 * @author Beho, Martin Slavkovsky
 */
public class FullDemandDetail implements Serializable, TableDisplay {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -530982467233195456L;
    private final static Logger LOGGER = Logger.getLogger("FullDemandDetail");

    public enum DemandField {

        TITLE, DESCRIPTION, PRICE, FINISH_DATE, VALID_TO_DATE, MAX_OFFERS, MIN_RATING, DEMAND_TYPE
    }
    private List<LocalityDetail> localities;
    private Map<Long, String> categories;
    private long clientId;
    private int maxOffers;
    private int minRating;
    private String demandType;
    private DemandStatus demandStatus;
    private static final String HTML_UNREAD_START = "<strong>";
    private static final String HTML_UNREAD_END = "</strong>";
    private DemandDetailType detailType = DemandDetailType.BASE;
    private long demandId;
    // messageId = threadRoot
    private long messageId;
    private long userMessageId;
    private boolean read;
    private boolean starred;
    private Date created;
    //Basic Demand Info
    @NotNull(message = "{demandNotNullTitle}")
    @Size(min = 5, max = 50, message = "{demandSizeTitle}")
    private String title;
    @NotNull(message = "{demandNotNullPrice}")
    @Pattern(regexp = "\\d+(\\.\\d{2})*", message = "{demandPatternPrice}")
    private String priceString;
    private BigDecimal price;
    @NotNull(message = "{demandNotNullEndDate}")
    @Future(message = "{demandFutureEndDate}")
    private Date endDate;
    @NotNull(message = "{demandNotNullValidToDate}")
    @Future(message = "{demandFutureValidToDate}")
    private Date validToDate;
    @NotNull(message = "{demandNotNullDescription}")
    @Size(min = 5, message = "{demandSizeDescription}")
    private String description;
    //
    private List<FullSupplierDetail> excludedSuppliers;

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

    public void setBasicInfo(HashMap<DemandField, Object> map) {
        this.setTitle((String) map.get(DemandField.TITLE));
        this.setDescription((String) map.get(DemandField.DESCRIPTION));
        this.setPrice((String) map.get(DemandField.PRICE));
        this.setEndDate((Date) map.get(DemandField.FINISH_DATE));
        this.setValidToDate((Date) map.get(DemandField.VALID_TO_DATE));
    }

    public void setAdvInfo(HashMap<DemandField, Object> map) {
        this.maxOffers = (Integer) map.get(DemandField.MAX_OFFERS);
        this.minRating = (Integer) map.get(DemandField.MIN_RATING);
        this.demandType = (String) map.get(DemandField.DEMAND_TYPE);
    }

    //---------------------------- GETTERS AND SETTERS --------------------
    public void updateWholeDemand(FullDemandDetail demand) {
        demandId = demand.getDemandId();
        title = demand.getTitle();
        description = demand.getDescription();
        price = demand.getPrice();
        created = demand.getCreated();
        endDate = demand.getEndDate();
        validToDate = demand.getValidToDate();
        maxOffers = demand.getMaxOffers();
        minRating = demand.getMinRating();
        //categories
        categories = new HashMap<Long, String>();
        for (Long catId : demand.getCategories().keySet()) {
            categories.put(catId, demand.getCategories().get(catId));
        }
        //localities
        localities = new ArrayList<LocalityDetail>(demand.getLocalities());

        demandStatus = demand.getDemandStatus();
        demandType = demand.getDemandType();
        clientId = demand.getClientId();

        excludedSuppliers = new ArrayList<FullSupplierDetail>();
        for (FullSupplierDetail supplier : demand.getExcludedSuppliers()) {
            excludedSuppliers.add(new FullSupplierDetail(supplier));
        }
    }

    public Map<Long, String> getCategories() {
        return categories;
    }

    public void setCategories(Map<Long, String> categories) {
        this.categories = categories;
    }

    public void addCategory(Long id, String value) {
        if (categories == null) {
            categories = new HashMap<Long, String>();
        }
        categories.put(id, value);
    }

    //    public void setCategories(CategorySelectorInterface categorySelector) {
//        if (categories == null) {
//            categories = new HashMap<Long, String>();
//        }
//        for (int i = 0; i < categorySelector.getSelectedList().getItemCount(); i++) {
//            this.categories.put(Long.valueOf(
//                    categorySelector.getSelectedList().getValue(i)),
//                    //                    categorySelector.getSelectedCategoryCodes().get(i)),
//                    categorySelector.getSelectedList().getItemText(i));
//        }
//        LOGGER.info("XXX: " + categories.toString());
//    }
    public List<LocalityDetail> getLocalities() {
        return localities;
    }

    public void setLocalities(List<LocalityDetail> localities) {
        this.localities = localities;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public int getMaxOffers() {
        return maxOffers;
    }

    public void setMaxOffers(int maxOffers) {
        this.maxOffers = maxOffers;
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
                + "\n     validToDate=" + validToDate
                + "\n     isRead=" + read
                + "\n     isStarred=" + starred
                + "\n     detailType=" + detailType
                + "\n"
                + "* FullDemandDetail:"
                + "\n     localities=" + localities
                + "\n     categories=" + categories
                + "\n     clientId=" + clientId
                + "\n     maxOffers=" + maxOffers
                + "\n     minRating=" + minRating
                + "\n     demandType=" + demandType
                + "\n     demandStatus=" + demandStatus;
    }

    @Override
    public DemandStatus getDemandStatus() {
        return demandStatus;
    }

    public void setDemandStatus(DemandStatus demandStatus) {
        this.demandStatus = demandStatus;
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
    public boolean isRead() {
        return read;
    }

    @Override
    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public boolean isStarred() {
        return starred;
    }

    @Override
    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    @Override
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

    public Date getValidToDate() {
        return validToDate;
    }

    public void setValidToDate(Date validToDate) {
        this.validToDate = validToDate;
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

    public void setPriceString(String priceStr) {
        this.priceString = priceStr;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getPriceString() {
        return priceString;
    }

    public void setType(DemandDetailType demandDetailType) {
        this.detailType = demandDetailType;
    }

    public void setPrice(String price) {
        if (price != null) {
            if (price.equals("") || price.equals("null")) {
                this.price = null;
            } else {
                this.price = BigDecimal.valueOf(Long.valueOf(price));
                this.priceString = price;
            }
        }
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
        // TODO: WTF is this ??
        this.priceString = price.toString();
    }

    public List<FullSupplierDetail> getExcludedSuppliers() {
        return excludedSuppliers;
    }

    public void setExcludedSuppliers(List<FullSupplierDetail> excludedSuppliers) {
        this.excludedSuppliers = excludedSuppliers;
    }

    public static String displayHtml(String trustedHtml, boolean isRead) {
        if (isRead) {
            return trustedHtml;
        } else {
            return HTML_UNREAD_START + trustedHtml + HTML_UNREAD_END;
        }
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
