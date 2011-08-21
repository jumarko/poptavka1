package cz.poptavka.sample.domain.user;

import cz.poptavka.sample.domain.common.DomainObject;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Represents personal or contact data abou business user.
 * That can be both the data abou company and/or contact person.
 * <p>
 * For naming conventions see
 * <a href="http://www.porada.sk/t73758-nalezitosti-faktury-v-anglickom-a-nemeckom-jazyku.html">
 *     nalezitosti-faktury-v-anglickom-a-nemeckom-jazyku</a>
 *
 * @author Juraj Martinka
 *         Date: 28.1.11
 */
@Entity
@Audited
public class BusinessUserData extends DomainObject {

    private String companyName;

    @Column(length = 64)
    private String personFirstName;

    @Column(length = 64)
    private String personLastName;

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


    public BusinessUserData() {
    }

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
        sb.append('}');
        return sb.toString();
    }


    public static class Builder {
        private String companyName;
        private String personFirstName;
        private String personLastName;
        private String phone;
        private String identificationNumber;
        private String taxId;

        public Builder() {
        }

        public Builder companyName(String companyName) {
            this.companyName = companyName;
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

        public BusinessUserData build() {
            return new BusinessUserData(this);
        }
    }

    private BusinessUserData(Builder builder) {
        this.companyName = builder.companyName;
        this.personFirstName = builder.personFirstName;
        this.personLastName = builder.personLastName;
    }

}
