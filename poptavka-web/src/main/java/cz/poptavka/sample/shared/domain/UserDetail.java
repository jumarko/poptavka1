package cz.poptavka.sample.shared.domain;

import java.io.Serializable;
import java.util.ArrayList;

public class UserDetail implements Serializable {

    public enum Role {
        CLIENT, SUPPLIER, PARTNER, OPERATOR, ADMIn
    }

    /** Instances of roles. **/
    private SupplierDetail supplier = null;
    //others

    /** List of roles. **/
    private ArrayList<Role> roleList = new ArrayList<Role>();

    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private String identifiacationNumber;
    private String companyName = null;
    private String taxId = null;
    private String website;
    private AddressDetail address;
    private ArrayList<String> demandsId;

    private boolean verified = false;

    public UserDetail() {
    }

    public UserDetail(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getIdentifiacationNumber() {
        return identifiacationNumber;
    }

    public void setIdentifiacationNumber(String identifiacationNumber) {
        this.identifiacationNumber = identifiacationNumber;
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

    public AddressDetail getAddress() {
        return address;
    }

    public void setAddress(AddressDetail address) {
        this.address = address;
    }

    public ArrayList<String> getDemandsId() {
        return demandsId;
    }

    public void setDemandsId(ArrayList<String> demandsId) {
        this.demandsId = demandsId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SupplierDetail getSupplier() {
        return supplier;
    }

    public void setSupplier(SupplierDetail supplier) {
        this.supplier = supplier;
    }

    public ArrayList<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(ArrayList<Role> roleList) {
        this.roleList = roleList;
    }

    public void addRole(Role role) {
        roleList.add(role);
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

}
