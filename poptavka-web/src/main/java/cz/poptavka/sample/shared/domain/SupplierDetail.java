package cz.poptavka.sample.shared.domain;

import java.util.ArrayList;

import cz.poptavka.sample.domain.product.Service;


public class SupplierDetail extends ClientDetail {

    private int overallRating;
    private boolean certified;
    private String description;
    private ArrayList<String> localities;
    private ArrayList<String> categories;
    private Service service;

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

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }


}
