package cz.poptavka.sample.shared.domain.supplier;

import cz.poptavka.sample.domain.address.Address;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.shared.domain.AddressDetail;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FullSupplierDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -8271479725303195283L;
    private long supplierId;
    private int overallRating = -1;
    private boolean certified = false;
    private String description = "";
    private String verification = "";
    private HashMap<String, String> localities = new HashMap<String, String>(); //<code, name>
    private HashMap<Long, String> categories = new HashMap<Long, String>();   //<id, name>
    private ArrayList<Integer> services = new ArrayList<Integer>();
    private List<AddressDetail> addresses = new ArrayList<AddressDetail>();
    private String businessType = "";
    private String email = "";
    private String companyName = "";
    private String identificationNumber = "";
    private String firstName = "";
    private String lastName = "";
    private String phone = "";

    public FullSupplierDetail() {
        super();
    }

//    public void createSupplierDetail(Supplier supplier) {
//        supplierId = supplier.getId();
//        overallRating = supplier.getOveralRating();
//        certified = supplier.isCertified();
//        verification = supplier.getVerification();
////        services = supplier.getBusinessUser().getUserServices();
//        addresses = supplier.getBusinessUser().getAddresses();
//        email = supplier.getBusinessUser().getEmail();
//        companyName = supplier.getBusinessUser().getBusinessUserData().getCompanyName();
//    }
    public static FullSupplierDetail createFullSupplierDetail(Supplier supplier) {
        FullSupplierDetail detail = new FullSupplierDetail();
        detail.setSupplierId(supplier.getId());
        if (supplier.getOveralRating() != null) {
            detail.setOverallRating(supplier.getOveralRating());
        }
        if (supplier.isCertified() != null) {
            detail.setCertified(supplier.isCertified());
        }
        if (supplier.getVerification() != null) {
            detail.setVerification(supplier.getVerification().name());
        }
        if (supplier.getCategories() != null) {
            for (Category category : supplier.getCategories()) {
                detail.getCategories().put(category.getId(), category.getName());
            }
        }
        if (supplier.getLocalities() != null) {
            for (Locality locality : supplier.getLocalities()) {
                detail.getLocalities().put(locality.getCode(), locality.getName());
            }
        }
        if (supplier.getBusinessUser() != null) {
            for (Address addr : supplier.getBusinessUser().getAddresses()) {
                detail.getAddresses().add(new AddressDetail(addr));
            }
            detail.setEmail(supplier.getBusinessUser().getEmail());
            if (supplier.getBusinessUser().getBusinessUserData() != null) {
                detail.setDescription(supplier.getBusinessUser().getBusinessUserData().getDescription());
                //        services = supplier.getBusinessUser().getUserServices();
                if (supplier.getBusinessUser().getBusinessType() != null) {
                    detail.setBusinessType(supplier.getBusinessUser().getBusinessType().getValue());
                }
                detail.setCompanyName(supplier.getBusinessUser().getBusinessUserData().getCompanyName());
                if (supplier.getBusinessUser().getBusinessUserData().getIdentificationNumber() != null) {
                    detail.setIdentificationNumber(supplier.getBusinessUser()
                            .getBusinessUserData().getIdentificationNumber());
                }
                detail.setFirstName(supplier.getBusinessUser().getBusinessUserData().getPersonFirstName());
                detail.setLastName(supplier.getBusinessUser().getBusinessUserData().getPersonLastName());
                detail.setPhone(supplier.getBusinessUser().getBusinessUserData().getPhone());
            }
        }
        return detail;
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

    public HashMap<Long, String> getCategories() {
        return categories;
    }

    public void setCategories(HashMap<Long, String> categories) {
        this.categories = categories;
    }

    public HashMap<String, String> getLocalities() {
        return localities;
    }

    public void setLocalities(HashMap<String, String> localities) {
        this.localities = localities;
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

    public List<AddressDetail> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressDetail> addresses) {
        this.addresses = addresses;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }
}
