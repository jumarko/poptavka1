package com.eprovement.poptavka.shared.domain;

import com.eprovement.poptavka.client.user.widget.grid.TableDisplayDisplayName;
import com.eprovement.poptavka.domain.enums.BusinessType;
import com.eprovement.poptavka.domain.enums.Verification;

import java.util.ArrayList;
import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.hibernate.validator.constraints.NotBlank;

/**
 * represents all types of system users
 * @author Beho
 *
 */
public class BusinessUserDetail extends UserDetail implements IsSerializable, TableDisplayDisplayName {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** Enums. **/
    public enum UserField {

        COMPANY_NAME("companyName"),
        IDENTIF_NUMBER("identificationNumber"),
        FIRST_NAME("personFirstName"),
        LAST_NAME("personLastName"),
        EMAIL("email"),
        PASSWORD("password"),
        PHONE("phone"),
        DESCRIPTION("description"),
        OVERALL_RATING("overalRating"),
        TAX_ID("taxId"),
        WEBSITE("website");

        public static final String SEARCH_CLASS = "businessUserData";
        private String value;

        private UserField(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum BusinessRole {

        CLIENT, SUPPLIER, PARTNER, OPERATOR, ADMIN
    }
    /** Base. **/
    private Long clientId = -1L;
    private Long supplierId = -1L;
    /** BusinessUserData. **/
    @NotBlank(message = "{companyNameNotBlank}")
    private String companyName;
    @NotBlank(message = "{identifNumberNotBlank}")
    private String identificationNumber;
    @NotBlank(message = "{firstNameNotBlank}")
    private String personFirstName;
    @NotBlank(message = "{lastNameNotBlank}")
    private String personLastName;
    @Pattern(regexp = "[0-9]+", message = "{patternString}")
    @NotBlank(message = "{phoneNotBlank}")
    private String phone;
    @Size(min = 20, message = "{descriptionSize}")
    @NotBlank(message = "{descriptionNotBlank}")
    private String description;
    @NotBlank(message = "{taxNumberNotBlank}")
    private String taxId;
    @Pattern(regexp = "^((https?|ftp)://|(www|ftp)\\.)[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$",
            message = "{patternWebsite}")
    private String website;
    /** Class lists. **/
    private ArrayList<BusinessRole> businessRoles = new ArrayList<BusinessRole>();
    @Valid
    @Size(min = 1)
    private ArrayList<AddressDetail> addresses;
    /** Others. **/
    private String displayName;
    private BusinessType businessType;
    private Verification verification;

    /**************************************************************************/
    /* Constuctors                                                            */
    /**************************************************************************/
    public BusinessUserDetail() {
        //for serialization
    }

    /**************************************************************************/
    /* Getters/Setters                                                        */
    /**************************************************************************/
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

    public String getPersonFirstName() {
        return personFirstName;
    }

    public void setPersonFirstName(String firstName) {
        this.personFirstName = firstName;
    }

    public String getPersonLastName() {
        return personLastName;
    }

    public void setPersonLastName(String lastName) {
        this.personLastName = lastName;
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

    public ArrayList<BusinessRole> getBusinessRoles() {
        return businessRoles;
    }

    public void setBusinessRoles(Collection<BusinessRole> businessRoleList) {
        this.businessRoles = new ArrayList<BusinessRole>(businessRoleList);
    }

    public void addRole(BusinessRole role) {
        businessRoles.add(role);
    }

    public void removeRole(BusinessRole role) {
        businessRoles.remove(role);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<AddressDetail> getAddresses() {
        return addresses;
    }

    public void setAddresses(Collection<AddressDetail> addresses) {
        this.addresses = new ArrayList<AddressDetail>(addresses);
    }

    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = BusinessType.valueOf(businessType);
    }

    public boolean isVerified() {
        return verification == Verification.VERIFIED;
    }

    public Verification getVerification() {
        return verification;
    }

    public void setVerification(Verification verification) {
        this.verification = verification;
    }

    public void setVerification(String verification) {
        this.verification = Verification.valueOf(verification);
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**************************************************************************/
    /* Other                                                                  */
    /**************************************************************************/
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("FullSupplierDetail{");
        str.append("supplierId=" + supplierId);
        str.append(", companyName=" + companyName);
        str.append(", identificationNumber=" + identificationNumber);
        str.append(", firstName=" + personFirstName);
        str.append(", lastName=" + personLastName);
        str.append(", phone=" + phone);
        str.append(", description=" + description);
        str.append(", taxId=" + taxId);
        str.append(", website=" + website);
        str.append(", addresses=" + addresses);
        str.append(", verification=" + verification);
        str.append(", businessType=" + businessType);
        str.append('}');
        return str.toString();
    }
}
