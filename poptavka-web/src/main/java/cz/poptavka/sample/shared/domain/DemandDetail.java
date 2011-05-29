package cz.poptavka.sample.shared.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;

public class DemandDetail implements Serializable {
    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -530982467233195456L;
    private long id;
    private String title;
    private String description;
    // if price = -1, then it was misplaced
    private BigDecimal price;
    private Date endDate;
    private Date expireDate;
    private ArrayList<String> localities;
    private ArrayList<String> categories;
    private long clientId;
    private int maxOffers;
    private int minRating;
    private String demandType;
    private String demandStatus;

    /** for serialization. **/
    public DemandDetail() {
    }

    /**
     * Method created DemandDetail from provided Demand domain object.
     * @param demand
     * @return DemandDetail
     */
    public static DemandDetail createDemandDetail(Demand demand) {

        DemandDetail detail = new DemandDetail();
        detail.setId(demand.getId());
        detail.setTitle(demand.getTitle());
        detail.setDescription(demand.getDescription());
        detail.setPrice(demand.getPrice());
        detail.setEndDate(demand.getEndDate());
        detail.setExpireDate(demand.getValidTo());
        detail.setMaxOffers(demand.getMaxSuppliers() == null ? 0 : demand.getMaxSuppliers());
        detail.setMinRating(demand.getMinRating() == null ? 0 : demand.getMinRating());
        //categories
        ArrayList<String> catList = new ArrayList<String>();
        for (Category cat : demand.getCategories()) {
            catList.add(cat.getName());
        }
        detail.setCategories(catList);
        //localities
        ArrayList<String> locList = new ArrayList<String>();
        for (Locality loc : demand.getLocalities()) {
            locList.add(loc.getName());
        }
        detail.setLocalities(locList);
        detail.setDemandStatus(demand.getStatus().getValue());
        detail.setDemandType(demand.getType().getType().getValue());
        detail.setClientId(demand.getClient().getId());

        return detail;
    }

    public void setBasicInfo(HashMap<String, Object> map) {
        this.title = (String) map.get("title");
        this.description = (String) map.get("description");
        this.price = BigDecimal.valueOf((Long) map.get("price"));
        this.endDate = (Date) map.get("endDate");
        this.expireDate = (Date) map.get("expireDate");
    }

    public void setAdvInfo(HashMap<String, Object> map) {
        this.maxOffers = (Integer) map.get("maxOffers");
        this.minRating = (Integer) map.get("minRating");
        this.demandType = (String) map.get("demandType");
    }

    //---------------------------- GETTERS AND SETTERS --------------------
    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
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

    public String getPriceString() {
        if (price == null) {
            return "";
        }
        return price.toString();
    }

    public void setPrice(String price) {
        if (price.equals("")) {
            this.price = null;
        } else {
            this.price = BigDecimal.valueOf(Long.valueOf(price));
        }
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public ArrayList<String> getLocalities() {
        return localities;
    }

    public void setLocalities(ArrayList<String> localities) {
        this.localities = localities;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
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

    @Override
    public String toString() {
        return "DemandDetail{"
                + "id="
                + id + ", title="
                + title + ", description="
                + description + ", price="
                + price + ", endDate="
                + endDate + ", expireDate="
                + expireDate + ", localities="
                + localities + ", categories="
                + categories + ", clientId="
                + clientId + ", maxOffers="
                + maxOffers + ", minRating="
                + minRating + ", demandType="
                + demandType + ", demandStauts="
                + demandStatus + '}';
    }

    public String getDemandStatus() {
        return demandStatus;
    }

    public void setDemandStatus(String demandStatus) {
        this.demandStatus = demandStatus;
    }
}
