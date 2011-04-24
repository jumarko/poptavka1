package cz.poptavka.sample.shared.domain;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientDetail implements Serializable {

    private String email;
    private String login;
    private String passowrd;
    /** person **/
    private String firstName;
    private String lastName;
    private String phone;
    /** company **/
    private String identifiacationNumber;
    private String companyName = null;
    private String taxId = null;

    private AddressDetail address;

    private ArrayList<String> demandsId;

    /** for serialization **/
    public ClientDetail() {
    }

    public ClientDetail(String email, String password) {
        this.email = email;
        this.passowrd = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassowrd() {
        return passowrd;
    }

    public void setPassowrd(String passowrd) {
        this.passowrd = passowrd;
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

    public String getIdentifiacationNumber() {
        return identifiacationNumber;
    }

    public void setIdentifiacationNumber(String identifiacationNumber) {
        this.identifiacationNumber = identifiacationNumber;
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

    public ArrayList<String> getDemandsId() {
        return demandsId;
    }

    public AddressDetail getAddress() {
        return address;
    }

    public void setAddress(AddressDetail address) {
        this.address = address;
    }

    public void setDemandsId(ArrayList<String> demandsId) {
        this.demandsId = demandsId;
    }
}
