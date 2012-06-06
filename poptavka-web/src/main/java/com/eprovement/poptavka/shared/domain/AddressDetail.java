package com.eprovement.poptavka.shared.domain;

import com.eprovement.poptavka.domain.address.Address;
import java.io.Serializable;

public class AddressDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = 803719527031189460L;
    private String city;
    private String street;
    private String zipCode;

    public AddressDetail() {
    }

    public AddressDetail(AddressDetail address) {
        this.city = address.getCity();
        this.street = address.getStreet();
        this.zipCode = address.getZipCode();
    }

    public static AddressDetail createAddressDetail(Address address) {
        AddressDetail detail = new AddressDetail();
        if (address.getCity() != null) {
            detail.setCity(address.getCity().getName());
        }
        if (address.getStreet() != null) {
            StringBuilder fullStreet = new StringBuilder(address.getStreet());
            fullStreet.append(" ");
            if (address.getFlatNum() != null && !address.getFlatNum().equals("")) {
                fullStreet.append(address.getFlatNum());
            }
            if (address.getHouseNum() != null && !address.getHouseNum().equals("")) {
                fullStreet.append(address.getHouseNum());
            }
        }
        detail.setStreet(address.getStreet());
        detail.setZipCode(address.getZipCode());
        return detail;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String cityName) {
        this.city = cityName;
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

    @Override
    public String toString() {
        return street + ", " + zipCode + " " + city;
    }
}
