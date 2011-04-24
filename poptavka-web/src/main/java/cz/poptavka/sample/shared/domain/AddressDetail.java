package cz.poptavka.sample.shared.domain;

import java.io.Serializable;

public class AddressDetail implements Serializable {

    private String cityCode;
    private String street;
    private String zipCode;

    public AddressDetail() {
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
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

}
