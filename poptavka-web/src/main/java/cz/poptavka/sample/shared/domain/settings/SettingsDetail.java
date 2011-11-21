package cz.poptavka.sample.shared.domain.settings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cz.poptavka.sample.domain.address.Address;
import cz.poptavka.sample.domain.user.BusinessUser;
import cz.poptavka.sample.shared.domain.AddressDetail;
import cz.poptavka.sample.shared.domain.SupplierDetail;

/**
 * represents all types of system users
 * @author Beho
 *
 */
public class SettingsDetail implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6224665779446848218L;

    public enum Role {

        CLIENT, SUPPLIER, PARTNER, OPERATOR, ADMIN
    }
    /** Instances of roles. **/
    private SupplierDetail supplier = null;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String identificationNumber;
    private String companyName;
    private String description;
    private String taxId;
    private String website;
    private List<AddressDetail> addresses;
    private int clientRating;

    public SettingsDetail() {
    }

    public SettingsDetail(String email, String password) {
        this.email = email;
    }

    public static SettingsDetail createUserDetail(BusinessUser user) {
        SettingsDetail detail = new SettingsDetail();
        List<AddressDetail> addresses = new ArrayList<AddressDetail>();
        for (Address addr : user.getAddresses()) {
            addresses.add(AddressDetail.createAddressDetail(addr));
        }
        detail.setAddresses(addresses);
        if (user.getBusinessUserData() != null) {
            detail.setCompanyName(user.getBusinessUserData().getCompanyName());
            detail.setDescription(user.getBusinessUserData().getDescription());
            detail.setFirstName(user.getBusinessUserData().getPersonFirstName());
            detail.setLastName(user.getBusinessUserData().getPersonLastName());
            detail.setIdentificationNumber(user.getBusinessUserData().getIdentificationNumber());
            detail.setPhone(user.getBusinessUserData().getPhone());
        }
        detail.setEmail(user.getEmail());

        return detail;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SupplierDetail getSupplier() {
        return supplier;
    }

    public void setSupplier(SupplierDetail supplier) {
        this.supplier = supplier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getClientRating() {
        return clientRating;
    }

    public void setClientRating(int clientRating) {
        this.clientRating = clientRating;
    }
}
