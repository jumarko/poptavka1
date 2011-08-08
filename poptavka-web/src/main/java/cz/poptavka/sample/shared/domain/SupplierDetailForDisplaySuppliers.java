package cz.poptavka.sample.shared.domain;

import cz.poptavka.sample.domain.address.Address;
import cz.poptavka.sample.domain.user.BusinessType;
import cz.poptavka.sample.domain.user.BusinessUserRole;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.domain.user.Verification;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class SupplierDetailForDisplaySuppliers implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -8271479725303195283L;

    private Long supplierId;
    private int overallRating;
    private boolean certified = false;
    private String description;
    private Verification verification;
    private ArrayList<String> localities;
    private ArrayList<String> categories;
    private ArrayList<Integer> services = new ArrayList<Integer>();

    private List<BusinessUserRole> bsuRoles;
    private List<Address> addresses;
    private BusinessType businessType;
    private String email;
    private String companyName;
    private String identificationNumber;
    private String firstName;
    private String lastName;
    private String phone;

    public SupplierDetailForDisplaySuppliers() {
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

    public SupplierDetailForDisplaySuppliers(Supplier supplier) {
        supplierId = supplier.getId();
        overallRating = supplier.getOveralRating();
        certified = supplier.isCertified();
        verification = supplier.getVerification();
//        services = supplier.getBusinessUser().getUserServices();
        addresses = supplier.getBusinessUser().getAddresses();
        businessType = supplier.getBusinessUser().getBusinessType();
        email = supplier.getBusinessUser().getEmail();
        companyName = supplier.getBusinessUser().getBusinessUserData().getCompanyName();
        identificationNumber = supplier.getBusinessUser().getBusinessUserData().getIdentificationNumber();
        firstName = supplier.getBusinessUser().getBusinessUserData().getPersonFirstName();
        lastName = supplier.getBusinessUser().getBusinessUserData().getPersonLastName();
        phone = supplier.getBusinessUser().getBusinessUserData().getPhone();
        bsuRoles = supplier.getBusinessUser().getBusinessUserRoles();
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

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
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

    public Verification getVerification() {
        return verification;
    }

    public void setVerification(Verification verification) {
        this.verification = verification;
    }
}
