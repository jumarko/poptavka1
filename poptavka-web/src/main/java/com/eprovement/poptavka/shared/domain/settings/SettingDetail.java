package com.eprovement.poptavka.shared.domain.settings;



import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.SupplierDetail;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;

/**
 * represents all types of system users
 * @author Beho
 *
 */
public class SettingDetail implements IsSerializable {


    public enum Role {

        CLIENT, SUPPLIER, PARTNER, OPERATOR, ADMIN
    }
    /** Instances of roles. **/
    private long userId;
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
    private ArrayList<AddressDetail> addresses;
    private ArrayList<NotificationDetail> notifications;
    private int clientRating;

    public SettingDetail() {
    }

    public SettingDetail(String email, String password) {
        this.email = email;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public void setAddresses(List<AddressDetail> addresses) {
        this.addresses = new ArrayList<AddressDetail>(addresses);
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

    public ArrayList<NotificationDetail> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationDetail> notifications) {
        if (notifications != null) {
            this.notifications = new ArrayList<NotificationDetail>(notifications);
        }
    }
}
