package com.eprovement.poptavka.shared.domain;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.ArrayList;
import java.util.List;

public class SupplierDetail implements IsSerializable {

    private Long supplierId;
    private Integer overallRating;
    private boolean certified;
    private String description;
    private ArrayList<LocalityDetail> localities;
    private ArrayList<CategoryDetail> categories;
    private ArrayList<ServiceDetail> services = new ArrayList<ServiceDetail>();

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

    public ArrayList<ServiceDetail> getServices() {
        return services;
    }

    public void setServices(List<ServiceDetail> serviceList) {
        this.services = new ArrayList<ServiceDetail>(serviceList);
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Long getSupplierId() {
        return supplierId;
    }
}
