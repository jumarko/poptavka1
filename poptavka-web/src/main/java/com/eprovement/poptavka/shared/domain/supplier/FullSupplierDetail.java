package com.eprovement.poptavka.shared.domain.supplier;

import com.eprovement.poptavka.domain.address.Address;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FullSupplierDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -8271479725303195283L;
    private long supplierId;
    private int overallRating = -1;
    private boolean certified = false;
    private String description;
    private String verification;
    private Map<String, String> localities; //<codes, value>
    private Map<Long, String> categories;   //<ids, value>
    private ArrayList<Integer> services = new ArrayList<Integer>();
    private List<AddressDetail> addresses = new ArrayList<AddressDetail>();
    private String businessType;
    private String email;
    private String companyName;
    private String identificationNumber;
    private String firstName;
    private String lastName;
    private String phone;

    public FullSupplierDetail() {
    }

    public FullSupplierDetail(FullSupplierDetail supplier) {
        this.updateWholeSupplier(supplier);
    }

    public void updateWholeSupplier(FullSupplierDetail supplier) {
        supplierId = supplier.getSupplierId();
        overallRating = supplier.getOverallRating();
        certified = supplier.isCertified();
        verification = supplier.getVerification();
        //categories
        categories = new HashMap<Long, String>();
        for (Long catId : supplier.getCategories().keySet()) {
            categories.put(catId, supplier.getCategories().get(catId));
        }
        //localities
        localities = new HashMap<String, String>();
        for (String locCode : supplier.getLocalities().keySet()) {
            localities.put(locCode, supplier.getLocalities().get(locCode));
        }
        for (AddressDetail addr : supplier.getAddresses()) {
            addresses.add(new AddressDetail(addr));
        }
        email = supplier.getEmail();
        description = supplier.getDescription();
        businessType = supplier.getBusinessType();
        companyName = supplier.getCompanyName();
        identificationNumber = supplier.getIdentificationNumber();
        firstName = supplier.getFirstName();
        lastName = supplier.getLastName();
        phone = supplier.getPhone();
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

    public Map<Long, String> getCategories() {
        return categories;
    }

    public void setCategories(Map<Long, String> categories) {
        this.categories = categories;
    }

    public Map<String, String> getLocalities() {
        return localities;
    }

    public void setLocalities(Map<String, String> localities) {
        this.localities = localities;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FullSupplierDetail other = (FullSupplierDetail) obj;
        if (this.supplierId != other.supplierId) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (int) (this.supplierId ^ (this.supplierId >>> 32));
        return hash;
    }
}
