package cz.poptavka.sample.shared.domain;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * represents all types of system users
 * @author Beho
 *
 */
public class UserDetail implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6224665779446848218L;

    public enum Role {
        CLIENT, SUPPLIER, PARTNER, OPERATOR, ADMIN
    }

    /** Instances of roles. **/
    private SupplierDetail supplier = null;
    //others

    /** List of roles. **/
    private ArrayList<Role> roleList = new ArrayList<Role>();

    private Long userId;
    private Long clientId = -1L;
    private Long supplierId = -1L;
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
    // TODO check if used, otherwise delete
    private ArrayList<String> demandsId = new ArrayList<String>();

    private boolean verified = false;

    public UserDetail() {
    }

    public UserDetail(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long id) {
        this.userId = id;
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

    public void removeRole(Role role) {
        roleList.remove(role);
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

}
