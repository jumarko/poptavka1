package cz.poptavka.sample.shared.domain;

import cz.poptavka.sample.domain.invoice.OurPaymentDetails;
import java.io.Serializable;

/**
 * Represents full detail of demandType. Serves for creating new demandType
 * or for call of detail, that supports editing.
 *
 * @author Beho
 *
 */
public class PaymentDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -530982467233195456L;
    private Long id;
    private String bankAccount;
    private String bankCode;
    private String city;
    private int countryVat;
    private String email;
    private String iban;
    private String identificationNumber;
    private String phone;
    private String street;
    private String swiftCode;
    private String taxId;
    private String title;
    private String zipCode;

    /** for serialization. **/
    public PaymentDetail() {
    }

    public PaymentDetail(PaymentDetail demand) {
        this.updateWholeOurPaymentDetail(demand);
    }

    /**
     * Method created FullDemandDetail from provided Demand domain object.
     * @param demand
     * @return DemandDetail
     */
    public static PaymentDetail createOurPaymentDetailDetail(OurPaymentDetails demand) {
        PaymentDetail detail = new PaymentDetail();

        detail.setId(demand.getId());
        detail.setBankAccount(demand.getBankAccount());
        detail.setBankCode(demand.getBankCode());
        detail.setCity(demand.getCity());
        detail.setCountryVat(demand.getCountryVat());
        detail.setEmail(demand.getEmail());
        detail.setIban(demand.getIban());
        detail.setIdentificationNumber(demand.getIdentificationNumber());
        detail.setPhone(demand.getPhone());
        detail.setStreet(demand.getStreet());
        detail.setSwiftCode(demand.getSwiftCode());
        detail.setTaxId(demand.getTaxId());
        detail.setTitle(demand.getTitle());
        detail.setZipCode(demand.getZipCode());

        return detail;
    }

    //---------------------------- GETTERS AND SETTERS --------------------
    public void updateWholeOurPaymentDetail(PaymentDetail detail) {
        id = detail.getId();
        bankAccount = detail.getBankAccount();
        bankCode = detail.getBankCode();
        city = detail.getCity();
        countryVat = detail.getCountryVat();
        email = detail.getEmail();
        iban = detail.getIban();
        identificationNumber = detail.getIdentificationNumber();
        phone = detail.getPhone();
        street = detail.getStreet();
        swiftCode = detail.getSwiftCode();
        taxId = detail.getTaxId();
        title = detail.getTitle();
        zipCode = detail.getZipCode();
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCountryVat() {
        return countryVat;
    }

    public void setCountryVat(int countryVat) {
        this.countryVat = countryVat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {

        return "\nGlobal OurPaymentDetail Detail Info:"
                + "\n     id:"
                + id + "\n     bankAccount:"
                + bankAccount + "\n     bankCode:"
                + bankCode + "\n     city:"
                + city + "\n     countryVat:"
                + countryVat + "\n     email:"
                + email + "\n     iban:"
                + iban + "\n     identificationNumber:"
                + identificationNumber + "\n     phone:"
                + phone + "\n     street:"
                + street + "\n     swiftCode:"
                + swiftCode + "\n     taxId:"
                + taxId + "\n     title:"
                + title + "\n     zipCode:"
                + zipCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PaymentDetail other = (PaymentDetail) obj;
        if (this.id != other.getId()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
}
