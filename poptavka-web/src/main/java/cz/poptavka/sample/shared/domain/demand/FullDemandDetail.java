package cz.poptavka.sample.shared.domain.demand;

import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import cz.poptavka.sample.shared.domain.type.DemandDetailType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Represents full detail of demand. Serves for creating new demand or for call of detail, that supports editing.
 *
 * @author Beho
 */
public class FullDemandDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -530982467233195456L;
    private final static Logger LOGGER = Logger.getLogger("FullDemandDetail");

    public enum DemandField {

        TITLE, DESCRIPTION, PRICE, FINISH_DATE, VALID_TO_DATE, MAX_OFFERS, MIN_RATING, DEMAND_TYPE
    }

    private Map<String, String> localities;
    private Map<Long, String> categories;
    private long clientId;
    private int maxOffers;
    private int minRating;
    private String demandType;
    private String demandStatus;
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
    private Date endDate;
    private Date validToDate;
    private String title;
    private String description;
    private BigDecimal price;
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

    /**
     * Method created FullDemandDetail from provided Demand domain object.
     *
     * @param demand
     * @return DemandDetail
     */
    public static FullDemandDetail createDemandDetail(Demand demand) {
        FullDemandDetail detail = new FullDemandDetail();
        detail.setDemandId(demand.getId());
        detail.setTitle(demand.getTitle());
        detail.setDescription(demand.getDescription());
        detail.setPrice(demand.getPrice());
        detail.setCreated(demand.getCreatedDate());
        detail.setEndDate(demand.getEndDate());
        detail.setValidToDate(demand.getValidTo());
        detail.setMaxOffers(demand.getMaxSuppliers() == null ? 0 : demand.getMaxSuppliers());
        detail.setMinRating(demand.getMinRating() == null ? 0 : demand.getMinRating());
        //categories
        Map<Long, String> catMap = new HashMap<Long, String>();
        for (Category cat : demand.getCategories()) {
            catMap.put(cat.getId(), cat.getName());
        }
        detail.setCategories(catMap);
        //localities
        Map<String, String> locMap = new HashMap<String, String>();
        for (Locality loc : demand.getLocalities()) {
            locMap.put(loc.getCode(), loc.getName());
        }

        detail.setLocalities(locMap);
        detail.setDemandStatus(demand.getStatus().getValue());

        if (demand.getType() != null) {
            detail.setDemandType(demand.getType().getDescription());
        }

        detail.setClientId(demand.getClient().getId());

        setExcludedSuppliers(demand, detail);

        return detail;
    }

    private static void setExcludedSuppliers(Demand demand, FullDemandDetail detail) {
        final List<FullSupplierDetail> excludedSuppliers = new ArrayList<FullSupplierDetail>();
        if (demand.getExcludedSuppliers() != null) {
            for (Supplier supplier : demand.getExcludedSuppliers()) {
                excludedSuppliers.add(FullSupplierDetail.createFullSupplierDetail(supplier));
            }
        }
        detail.setExcludedSuppliers(excludedSuppliers);
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
        localities = new HashMap<String, String>();
        for (String locCode : demand.getLocalities().keySet()) {
            localities.put(locCode, demand.getLocalities().get(locCode));
        }

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
    public Map<String, String> getLocalities() {
        return localities;
    }

    public void setLocalities(Map<String, String> localities) {
        if (localities != null) {
            this.localities = localities;
        }
    }

    public void addLocality(String code, String value) {
        if (localities == null) {
            localities = new HashMap<String, String>();
        }
        localities.put(code, value);
    }
//    public void setLocalities(LocalitySelectorInterface localitySelector) {
//        if (localities == null) {
//            localities = new HashMap<String, String>();
//        }
//        for (int i = 0; i < localitySelector.getSelectedList().getItemCount(); i++) {
//            this.localities.put(
////                    localitySelector.getSelectedLocalityCodes().get(i),
//                    localitySelector.getSelectedList().getValue(i),
//                    localitySelector.getSelectedList().getItemText(i));
//        }
//        LOGGER.info("XXX: "+localities.toString());
//    }

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
                + "\n    demandId="
                + demandId + "\n     title="
                + title + "\n    Description="
                + description + "\n  Price="
                + price + "\n    endDate="
                + endDate + "\n  validToDate="
                + validToDate + "\n  isRead="
                + read + "\n     isStarred="
                + starred + "\n  detailType="
                + detailType + "\n"
                + "* FullDemandDetail:"
                + "\n     localities="
                + localities + "\n     categories="
                + categories + "\n     clientId="
                + clientId + "\n     maxOffers="
                + maxOffers + "\n     minRating="
                + minRating + "\n     demandType="
                + demandType + "\n     demandStatus="
                + demandStatus;
    }

    public String getDemandStatus() {
        return demandStatus;
    }

    public void setDemandStatus(String demandStatus) {
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

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public Date getEndDate() {
        return endDate;
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

    public BigDecimal getPrice() {
        return price;
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
            }
        }
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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
