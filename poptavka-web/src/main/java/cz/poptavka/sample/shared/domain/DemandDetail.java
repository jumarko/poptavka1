package cz.poptavka.sample.shared.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DemandDetail implements Serializable {

    private String title;
    private String description;
    private Long price;
    private Date endDate;
    private Date expireDate;
    private ArrayList<String> localities;
    private ArrayList<String> categories;
    private long clientId;
    private int maxOffers;
    private int minRating;
    private String demandType;

    /** for serialization **/
    public DemandDetail() {
    }

    public void setBasicInfo(HashMap<String, Object> map) {
        this.title = (String) map.get("title");
        this.description = (String) map.get("description");
        this.price = (Long) map.get("price");
        this.endDate = (Date) map.get("endDate");
        this.expireDate = (Date) map.get("expireDate");
    }

    public void setAdvInfo(HashMap<String, Object> map) {
        this.maxOffers = (Integer) map.get("maxOffers");
        this.minRating = (Integer) map.get("minRating");
        this.demandType = (String) map.get("demandType");
    }

    //---------------------------- GETTERS AND SETTERS --------------------
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
    public Long getPrice() {
        return price;
    }
    public void setPrice(Long price) {
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

}
