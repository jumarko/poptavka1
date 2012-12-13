package com.eprovement.poptavka.shared.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SupplierDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -8271479725303195283L;
    private Long supplierId;
    private Integer overallRating;
    private boolean certified;
    private String description;
    private ArrayList<LocalityDetail> localities;
    private ArrayList<CategoryDetail> categories;
    private ArrayList<Integer> services = new ArrayList<Integer>();

    public SupplierDetail() {
    }

    public Integer getOverallRating() {
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

    public ArrayList<LocalityDetail> getLocalities() {
        return localities;
    }

    public void setLocalities(List<LocalityDetail> localities) {
        if (localities != null) {
            this.localities = new ArrayList<LocalityDetail>(localities);
        }
    }

    public ArrayList<CategoryDetail> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryDetail> categories) {
        if (categories != null) {
            this.categories = new ArrayList<CategoryDetail>(categories);
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Integer> getServices() {
        return services;
    }

    public void setServices(ArrayList<Integer> serviceList) {
        this.services = serviceList;
    }

    public void addService(int selectedService) {
        services.add(Integer.valueOf(selectedService));
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getSupplierId() {
        return supplierId;
    }
}
