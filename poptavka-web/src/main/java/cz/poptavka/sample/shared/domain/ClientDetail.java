package cz.poptavka.sample.shared.domain;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientDetail implements Serializable {

    private String email;
    private String passowrd;
    /** person **/
    private String firstName;
    private String lastName;
    private String phone;
    /** company **/
    private String identifiacationNumber;
    private String companyName;
    private String taxId;

    private ArrayList<String> demandsId;

    /** for serialization **/
    public ClientDetail() {
    }

    public ClientDetail(String email) {
        this.email = email;
        this.passowrd = "x";
        this.firstName = "abel";
        this.lastName = "dabel";
        this.phone = "+420 111 222 333";
        this.identifiacationNumber = null;
        this.taxId = null;
        this.companyName = null;
    }

}
