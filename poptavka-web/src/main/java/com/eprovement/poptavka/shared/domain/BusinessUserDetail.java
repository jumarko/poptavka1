package com.eprovement.poptavka.shared.domain;

import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * represents all types of system users
 * @author Beho
 *
 */
public class BusinessUserDetail extends UserDetail implements Serializable {

    private static final long serialVersionUID = 6224665779446848218L;

    public enum BusinessRole {

        CLIENT, SUPPLIER, PARTNER//, OPERATOR, ADMIN
    }
    /** Instances of roles. **/
    private FullSupplierDetail supplier = null;
    //others
    /** List of roles. **/
    private ArrayList<BusinessRole> businessRoles = new ArrayList<BusinessRole>();
    private Long clientId = -1L;
    private Long supplierId = -1L;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private String identificationNumber;
    private String companyName;
    private String description;
    private String taxId;
    private String website;
    private ArrayList<AddressDetail> addresses = new ArrayList<AddressDetail>();
    private boolean verified = false;

    public BusinessUserDetail() {
        super();
    }


    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identifiacationNumber) {
        this.identificationNumber = identifiacationNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public ArrayList<AddressDetail> getAddresses() {
        return addresses;
    }

    public void setAddresses(ArrayList<AddressDetail> addresses) {
        this.addresses = addresses;
    }

    public void addAddress(AddressDetail addressDetail) {
        this.addresses.add(addressDetail);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public FullSupplierDetail getSupplier() {
        return supplier;
    }

    public void setSupplier(FullSupplierDetail supplier) {
        this.supplier = supplier;
    }

    public ArrayList<BusinessRole> getBusinessRoles() {
        return businessRoles;
    }

    public void setBusinessRoles(ArrayList<BusinessRole> businessRoleList) {
        this.businessRoles = businessRoleList;
    }

    public void addRole(BusinessRole role) {
        businessRoles.add(role);
    }

    public void removeRole(BusinessRole role) {
        businessRoles.remove(role);
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
