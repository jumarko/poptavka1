package com.eprovement.poptavka.shared.domain;

import com.eprovement.poptavka.client.common.validation.Extended;
import com.eprovement.poptavka.client.common.validation.SearchGroup;
import com.eprovement.poptavka.domain.enums.BusinessType;
import com.eprovement.poptavka.domain.enums.Verification;
import com.eprovement.poptavka.shared.search.ISortField;
import javax.validation.constraints.Pattern;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.ArrayList;
import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

/**
 * represents all types of system users
 * @author Beho
 *
 */
public class BusinessUserDetail extends UserDetail implements IsSerializable {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** Enums. **/
    public enum UserField implements ISortField {

        ID("id"),
        CREATED("created"),
        EMAIL("email"),
        PASSWORD("password");

        private String value;

        private UserField(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    public enum UserDataField implements ISortField {

        COMPANY_NAME("companyName"),
        FIRST_NAME("personFirstName"),
        LAST_NAME("personLastName"),
        PHONE("phone"),
        DESCRIPTION("description"),
        OVERALL_RATING("overalRating"),
        TAX_NUMBER("taxId"),
        VAT_NUMBER("identificationNumber"),
        WEBSITE("website");

        private String value;

        private UserDataField(String value) {
            this.value = value;
        }

        @Override
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
    @Pattern(regexp = "[a-zA-Z0-9&.\\ ]*", message = "{patternCompany}", groups = {Extended.class, SearchGroup.class })
    @Size(max = 50, message = "{companyNameSize}", groups = {Extended.class, SearchGroup.class })
    private String companyName;

    @Size(max = 20, message = "{identifNumberSize}")
    @Pattern(regexp = "[\\d\\-]+", message = "{patternNoSpecChars}", groups = Extended.class)
    private String identificationNumber;

    @NotBlank(message = "{firstNameNotBlank}")
    @Pattern(regexp = "[a-zA-Z\\ ]+", message = "{patternNoSpecCharsNoNumbers}", groups = Extended.class)
    @Size(max = 255, message = "{firstNameSize}", groups = Extended.class)
    private String personFirstName;

    @NotBlank(message = "{lastNameNotBlank}")
    @Pattern(regexp = "[a-zA-Z\\ ]+", message = "{patternNoSpecCharsNoNumbers}", groups = Extended.class)
    @Size(max = 255, message = "{lastNameSize}", groups = Extended.class)
    private String personLastName;
    /**
     * The following regular expression for validating US and Canada phone number formats.
     * Link: http://sanjaal.com/java/tag/united-states-phone-number-validation-regex-java/
     * The regular expression used can handle the following rules:
     * <ul>
     *      <li>The starting character may be ‘+’ but is optional,</li>
     *      <li>If the country code is used, it can be either 0 or 1 and is optional,</li>
     *      <li>Various codes (country, area) might be separated with ‘-’ or ‘.’ but is optional,</li>
     *      <li>Area code should not start with 0 but may be optionally enclosed in round brackets.</li>
     * </ul>
     */
//    Commented this strict regex, but leaving here for furthture usage...maybe
//    @Pattern(regexp = "^[+]?[01]?[- .]?(\\([2-9]\\d{2}\\)|[2-9]\\d{2})[- .]?\\d{3}[- .]?\\d{4}$",
//            message = "{patternPhone}")
    @Pattern(regexp = "[+]?\\d{8,13}", message = "{patternPhone}", groups = Extended.class)
    @NotBlank(message = "{phoneNotBlank}")
    private String phone;

    @NotBlank(message = "{descriptionNotBlank}")
    @Size(min = 20, max = 255, message = "{descriptionSize}", groups = {Extended.class, SearchGroup.class })
    private String description;

    /**
     * Individual Taxpayer Identification Number (or ITIN) it's a nine-digit number that begins with
     * the number 9 and has a range of 70 to 99 (excluding 89 and 93) in the fourth and fifth digit,
     * example 9XX-70-XXXX or 9XX-99-XXXX
     * Link: http://en.wikipedia.org/wiki/Individual_Taxpayer_Identification_Number
     */
//    Commented this strict regex, but reconsider it because we should inform user that something
//    might be wrong and the invoice might be filled with invalid data. -- TODO LATER reconsider
//    @Pattern(regexp = "^(9\\d{2})([\\ \\-]?)(7\\d+|8[0-8]|9([0-2]|[4-9]))([\\ \\-]?)(\\d{4})$",
//            message = "{patternItin}")
    @NotBlank(message = "{taxNumberNotBlank}")
    @Pattern(regexp = "[\\d\\-]+", message = "{patternNoSpecChars}", groups = Extended.class)
    @Size(min = 8, max = 15, message = "{itinSize}", groups = Extended.class)
    private String taxId;

    @Pattern(regexp = "^(|((https?|ftp)://|(www|ftp)\\.)[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?)$",
        message = "{patternWebsite}")
    private String website;

    /** Class lists. **/
    private ArrayList<BusinessRole> businessRoles = new ArrayList<BusinessRole>();

    @Valid
    @Size(min = 1)
    private ArrayList<AddressDetail> addresses = new ArrayList<AddressDetail>();

    /** Others. **/
    private String displayName;
    private BusinessType businessType;
    private Verification verification;
    private boolean external;

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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isExternal() {
        return external;
    }

    public void setExternal(boolean external) {
        this.external = external;
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
        str.append(", external=" + external);
        str.append('}');
        return str.toString();
    }
}
