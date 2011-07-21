package cz.poptavka.sample.shared.domain;

import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.shared.domain.UserDetail.Role;
import java.io.Serializable;
import java.util.ArrayList;


public class SupplierDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -8271479725303195283L;

    private Long supplierId;
    private int overallRating;
    private boolean certified;
    private String description;
    private ArrayList<String> localities;
    private ArrayList<String> categories;
    private ArrayList<Integer> services = new ArrayList<Integer>();
    private UserDetail person = new UserDetail();

    public SupplierDetail() {
    }

    public SupplierDetail(Supplier supplier) {
        //Supplier Detail
        SupplierDetail supplierDetail = new SupplierDetail();
        ArrayList<String> categoriesAsStrings = new ArrayList<String>();
        for (Category category : supplier.getCategories()) {
            categoriesAsStrings.add(category.getName());
        }
        supplierDetail.setCategories(categoriesAsStrings);

        ArrayList<String> localitiesAsStrings = new ArrayList<String>();
        for (Locality locality : supplier.getLocalities()) {
            localitiesAsStrings.add(locality.getName());
        }
        supplierDetail.setLocalities(localitiesAsStrings);

        supplierDetail.setCertified(supplier.isCertified());
        supplierDetail.setOverallRating(supplier.getOveralRating());
        supplierDetail.setSupplierId(supplier.getId());
        //UserDetail
        person.addRole(Role.SUPPLIER);
        person.setCompanyName(supplier.getBusinessUser().getBusinessUserData().getCompanyName());
//        person.setWebsite(supplier.getBusinessUser().);
        //TODO Martin - miss folows
        //supplierDetail.setDescription(description);
        //supplierDetail.setServices(services);
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
