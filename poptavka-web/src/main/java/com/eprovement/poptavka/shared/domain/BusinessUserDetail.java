package com.eprovement.poptavka.shared.domain;

import com.eprovement.poptavka.domain.enums.BusinessType;
import com.eprovement.poptavka.domain.enums.Verification;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

/**
 * represents all types of system users
 * @author Beho
 *
 */
public class BusinessUserDetail extends UserDetail implements Serializable {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** Generated serialVersionUID. **/
    private static final long serialVersionUID = 6224665779446848218L;

    /** Enums. **/
    public enum BusinessRole {

        CLIENT, SUPPLIER, PARTNER, OPERATOR, ADMIN
    }
    /** Base. **/
    private Long clientId = -1L;
    private Long supplierId = -1L;
    /** BusinessUserData. **/
    @NotBlank(message = "{supplierNotBlankCompanyName}")
    private String companyName;
    @NotBlank(message = "{supplierNotBlankIdentifNumber}")
    private String identificationNumber;
    @NotBlank(message = "{supplierNotBlankFirstName}")
    private String firstName;
    @NotBlank(message = "{supplierNotBlankLastName}")
    private String lastName;
    @Pattern(regexp = "[0-9]+", message = "{supplierPatternPhone}")
    @NotBlank(message = "{supplierNotBlankPhone}")
    private String phone;
    @NotBlank(message = "{supplierNotBlankPassword}")
    private String password;
    @NotBlank(message = "{supplierNotBlankDescription}")
    private String description;
    @NotBlank(message = "{supplierNotBlankTaxNumber}")
    private String taxId;
    @Pattern(regexp =
        "^((https?|ftp)://|(www|ftp)\\.)[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$",
        message = "{supplierPatternWebsite}")
    private String website;
    /** Class lists. **/
    private ArrayList<BusinessRole> businessRoles = new ArrayList<BusinessRole>();
    @Valid
    @Size(min = 1)
    private ArrayList<AddressDetail> addresses;
    /** Others. **/
    private BusinessType businessType;
    private Verification verification;
    private int overallRating = -1;

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

    public int getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(int overallRating) {
        this.overallRating = overallRating;
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
        str.append(", firstName=" + firstName);
        str.append(", lastName=" + lastName);
        str.append(", phone=" + phone);
        str.append(", password=" + password);
        str.append(", description=" + description);
        str.append(", taxId=" + taxId);
        str.append(", website=" + website);
        str.append(", addresses=" + addresses);
        str.append(", verification=" + verification);
        str.append(", businessType=" + businessType);
        str.append(", ranking=" + overallRating);
        str.append('}');
        return str.toString();
    }
}
