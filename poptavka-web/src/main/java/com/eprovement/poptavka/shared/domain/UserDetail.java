package com.eprovement.poptavka.shared.domain;

import com.eprovement.poptavka.domain.address.Address;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.BusinessUserRole;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    private String identificationNumber;
    private String companyName;
    private String description;
    private String taxId;
    private String website;
    private List<AddressDetail> addresses;
    // TODO check if used, otherwise delete
    private ArrayList<String> demandsId = new ArrayList<String>();
    private boolean verified = false;

    public UserDetail() {
    }

    public UserDetail(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserDetail createUserDetail(BusinessUser user) {
        UserDetail detail = new UserDetail();
        detail.setUserId(user.getId());
        List<AddressDetail> addresses = new ArrayList<AddressDetail>();
        for (Address addr : user.getAddresses()) {
            addresses.add(AddressDetail.createAddressDetail(addr));
        }
        detail.setAddresses(addresses);
        for (BusinessUserRole role : user.getBusinessUserRoles()) {
            if (role instanceof Client) {
                detail.setClientId(role.getId());
                detail.getRoleList().add(Role.CLIENT);
            }
            if (role instanceof Supplier) {
                detail.setSupplierId(role.getId());
                detail.getRoleList().add(Role.SUPPLIER);
            }
        }
        if (user.getBusinessUserData() != null) {
            detail.setCompanyName(user.getBusinessUserData().getCompanyName());
            detail.setDescription(user.getBusinessUserData().getDescription());
            detail.setFirstName(user.getBusinessUserData().getPersonFirstName());
            detail.setLastName(user.getBusinessUserData().getPersonLastName());
            detail.setIdentificationNumber(user.getBusinessUserData().getIdentificationNumber());
            detail.setPhone(user.getBusinessUserData().getPhone());
        }
        detail.setPassword(user.getPassword());
        detail.setEmail(user.getEmail());

        return detail;
    }

    public static BusinessUser updateBusinessUser(BusinessUser domain, UserDetail detail) {
        if (!domain.getEmail().equals(detail.getEmail())) {
            domain.setEmail(detail.getEmail());
        }
        if (!domain.getPassword().equals(detail.getPassword())) {
            domain.setPassword(detail.getPassword());
        }
        if (!domain.getBusinessUserData().getPersonFirstName().equals(detail.getFirstName())) {
            domain.getBusinessUserData().setPersonFirstName(detail.getFirstName());
        }
        if (!domain.getBusinessUserData().getPersonLastName().equals(detail.getLastName())) {
            domain.getBusinessUserData().setPersonLastName(detail.getLastName());
        }
        if (!domain.getBusinessUserData().getPhone().equals(detail.getPhone())) {
            domain.getBusinessUserData().setPhone(detail.getPhone());
        }
        if (!domain.getBusinessUserData().getIdentificationNumber().equals(detail.getIdentificationNumber())) {
            domain.getBusinessUserData().setIdentificationNumber(detail.getIdentificationNumber());
        }
        if (!domain.getBusinessUserData().getCompanyName().equals(detail.getCompanyName())) {
            domain.getBusinessUserData().setCompanyName(detail.getCompanyName());
        }
        if (!domain.getBusinessUserData().getDescription().equals(detail.getDescription())) {
            domain.getBusinessUserData().setDescription(detail.getDescription());
        }
        if (!domain.getBusinessUserData().getTaxId().equals(detail.getTaxId())) {
            domain.getBusinessUserData().setTaxId(detail.getTaxId());
        }
        if (!domain.getBusinessUserData().getWebsite().equals(detail.getWebsite())) {
            domain.getBusinessUserData().setWebsite(detail.getWebsite());
        }
        //TODO Martin - how to update following?
        //    private SupplierDetail supplier = null;
        //    private ArrayList<Role> roleList = new ArrayList<Role>();
        //    private Long userId;
        //    private Long clientId = -1L;
        //    private Long supplierId = -1L;
        //    private List<AddressDetail> addresses;
        //    private ArrayList<String> demandsId = new ArrayList<String>();
        //    private boolean verified = false;
        return domain;
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

    public List<AddressDetail> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressDetail> addresses) {
        this.addresses = addresses;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
