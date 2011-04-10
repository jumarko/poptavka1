package cz.poptavka.sample.domain.invoice;

import cz.poptavka.sample.domain.common.DomainObject;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @see Invoice for similar attributes
 * @author Juraj Martinka
 *         Date: 11.4.11
 */
@Entity
@Audited
public class OurPaymentDetails extends DomainObject {

    // TODO analyze if some attributes could be replaced with existing domain objects (e.g. {@link Address}
    // Some attribute migth be also extracted into the new domain objects (e.g. identificationNumber and taxId)
    private String title;

    private String street;
    private String zipCode;
    private String city;

    /** aka "ic" in czech republic. */
    @Column(length = 16)
    private String identificationNumber;

    /** aka "dic* in czech republic. */
    @Column(length = 16)
    private String taxId;

    private String email;
    private String phone;

    @Column(length = 24)
    private String bankAccount;
    @Column(length = 8)
    private String bankCode;
    @Column(length = 24)
    private String swiftCode;

    private String iban;

    private Integer countryVat;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Integer getCountryVat() {
        return countryVat;
    }

    public void setCountryVat(Integer countryVat) {
        this.countryVat = countryVat;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OurPaymentDetails");
        sb.append("{title='").append(title).append('\'');
        sb.append(", street='").append(street).append('\'');
        sb.append(", zipCode='").append(zipCode).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", identificationNumber='").append(identificationNumber).append('\'');
        sb.append(", taxId='").append(taxId).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", phone='").append(phone).append('\'');
        sb.append(", bankAccount='").append(bankAccount).append('\'');
        sb.append(", bankCode='").append(bankCode).append('\'');
        sb.append(", swiftCode='").append(swiftCode).append('\'');
        sb.append(", iban='").append(iban).append('\'');
        sb.append(", countryVat=").append(countryVat);
        sb.append('}');
        return sb.toString();
    }
}
