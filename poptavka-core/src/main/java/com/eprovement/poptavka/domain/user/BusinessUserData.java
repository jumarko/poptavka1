package com.eprovement.poptavka.domain.user;

import com.eprovement.poptavka.domain.common.DomainObject;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.Column;
import javax.persistence.Entity;
import org.hibernate.search.annotations.Boost;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Represents personal or contact data of a business user.
 * That can be both the data of a company and/or a contact person.
 * <p>
 * For naming conventions see
 * <a href="http://www.porada.sk/t73758-nalezitosti-faktury-v-anglickom-a-nemeckom-jazyku.html">
 *     nalezitosti-faktury-v-anglickom-a-nemeckom-jazyku</a>
 *
 * @author Juraj Martinka
 *         Date: 28.1.11
 */
@Entity
@Indexed
@Audited
public class BusinessUserData extends DomainObject {

    /**************************************************************************/
    /*  Attributes                                                            */
    /**************************************************************************/
    /** Constants - Fields that are available for full-text search. */
    public static final String[] USER_FULLTEXT_FIELDS = new String[]{
        "companyName", "personFirstName", "personLastName", "description"};

    /** Attributes. **/
    @Field
    // companyName is most important - boost twice as much as other fields
    @Boost(2)
    private String companyName;
    @Field
    @Column(length = 64)
    @NotBlank
    private String personFirstName;
    @Field
    @Column(length = 64)
    @NotBlank
    private String personLastName;
    @Field
    @NotAudited
    private String description;
    @Column(length = 20)
    private String phone;
    /** aka "ic" in czech republic. */
    @Column(length = 16)
    private String identificationNumber;
    /** aka "dic* in czech republic */
    @Column(length = 16)
    private String taxId;
    /** user's web site. */
    private String website;

    @NotAudited
    @Column(nullable = false)
    private Integer currentCredits = 0;

    /**************************************************************************/
    /*  Initialization                                                        */
    /**************************************************************************/
    public BusinessUserData() {
    }

    /**************************************************************************/
    /*  Getters & Setters                                                     */
    public BusinessUserData(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPersonFirstName() {
        return personFirstName;
    }

    public void setPersonFirstName(String personFirstName) {
        this.personFirstName = personFirstName;
    }

    public String getPersonLastName() {
        return personLastName;
    }

    public void setPersonLastName(String personLastName) {
        this.personLastName = personLastName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
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

    public int getCurrentCredits() {
        return currentCredits;
    }

    public void addCredits(int creditsToAdd) {
        this.currentCredits += creditsToAdd;
    }

    public void substractCredits(int creditsToRemove) {
        this.currentCredits -= creditsToRemove;
    }

    /**
     * Display company name if available, if not, display person's name as
     * concatenated string of first and last name.
     * @return display string
     */
    public String getDisplayName() {
        String result = "";

        if (!getCompanyName().isEmpty()) {
            result = getCompanyName();
        } else {
            if (!getPersonFirstName().isEmpty()) {
                result = getPersonFirstName();
            }
            if (!getPersonLastName().isEmpty()) {
                result = result.concat(" ").concat(getPersonLastName());
            }
        }
        return result;
    }

    /**************************************************************************/
    /*  Override methods                                                      */
    /**************************************************************************/
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("BusinessUserData");
        sb.append("{companyName='").append(companyName).append('\'');
        sb.append(", personFirstName='").append(personFirstName).append('\'');
        sb.append(", personLastName='").append(personLastName).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", identificationNumber='").append(identificationNumber).append('\'');
        sb.append(", taxId='").append(taxId).append('\'');
        sb.append(", www='").append(website).append('\'');
        sb.append('}');
        return sb.toString();
    }

    /**************************************************************************/
    /*  Builder                                                               */
    /**************************************************************************/
    public static class Builder {

        private String companyName;
        private String description;
        private String personFirstName;
        private String personLastName;
        private String phone;
        private String identificationNumber;
        private String taxId;
        private String website;

        public Builder() {
        }

        public Builder companyName(String companyName) {
            this.companyName = companyName;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder personFirstName(String personFirstName) {
            this.personFirstName = personFirstName;
            return this;
        }

        public Builder personLastName(String personLastName) {
            this.personLastName = personLastName;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder identificationNumber(String identificationNumber) {
            this.identificationNumber = identificationNumber;
            return this;
        }

        public Builder taxId(String taxId) {
            this.taxId = taxId;
            return this;
        }

        public Builder website(String website) {
            this.website = website;
            return this;
        }

        public BusinessUserData build() {
            return new BusinessUserData(this);
        }
    }

    private BusinessUserData(Builder builder) {

        this.companyName = builder.companyName;
        this.description = builder.description;
        this.personFirstName = builder.personFirstName;
        this.personLastName = builder.personLastName;
        this.phone = builder.phone;
        this.identificationNumber = builder.identificationNumber;
        this.taxId = builder.taxId;
        this.website = builder.website;
    }
}
