package com.eprovement.poptavka.shared.domain;

import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.ArrayList;
import java.util.List;

public class SupplierDetail implements IsSerializable {

    private Long supplierId;
    private Integer overalRating;
    private boolean certified;
    private String description;
    private ArrayList<ICatLocDetail> localities;
    private ArrayList<ICatLocDetail> categories;
    private ArrayList<ServiceDetail> services = new ArrayList<ServiceDetail>();

    public SupplierDetail() {
    }

    public Integer getOveralRating() {
        return overalRating;
    }

    public void setOveralRating(int overalRating) {
        this.overalRating = overalRating;
    }

    public boolean isCertified() {
        return certified;
    }

    public void setCertified(boolean certified) {
        this.certified = certified;
    }

    public ArrayList<ICatLocDetail> getLocalities() {
        return localities;
    }

    public void setLocalities(List<ICatLocDetail> localities) {
        if (localities != null) {
            this.localities = new ArrayList<ICatLocDetail>(localities);
        }
    }

    public ArrayList<ICatLocDetail> getCategories() {
        return categories;
    }

    public void setCategories(List<ICatLocDetail> categories) {
        if (categories != null) {
            this.categories = new ArrayList<ICatLocDetail>(categories);
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
