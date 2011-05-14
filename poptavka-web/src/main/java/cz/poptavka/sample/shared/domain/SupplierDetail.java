package cz.poptavka.sample.shared.domain;

import java.util.ArrayList;


public class SupplierDetail extends ClientDetail {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -8271479725303195283L;

    private int overallRating;
    private boolean certified;
    private String description;
    private ArrayList<String> localities;
    private ArrayList<String> categories;
    private int serviceId;

    public SupplierDetail() {
        super();
    }

    public SupplierDetail(String mail, String password) {
        super(mail, password);
    }

    public int getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(int overallRating) {
        this.overallRating = overallRating;
    }

    public boolean isCertified() {
        return certified;
    }

    public void setCertified(boolean certified) {
        this.certified = certified;
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
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getService() {
        return serviceId;
    }

    public void setService(int service) {
        this.serviceId = service;
    }


}
