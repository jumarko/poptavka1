package cz.poptavka.sample.shared.domain;

import cz.poptavka.sample.domain.address.Address;
import java.io.Serializable;

public class AddressDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = 803719527031189460L;
    private String cityName;
    private String street;
    private String zipCode;

    public AddressDetail() {
    }

    public AddressDetail(AddressDetail address) {
        this.cityName = address.getCityName();
        this.street = address.getStreet();
        this.zipCode = address.getZipCode();
    }

    public AddressDetail(Address address) {
        if (address == null) {
            return;
        }
        if (address.getCity() != null) {
            this.cityName = address.getCity().getName();
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
        this.street = address.getStreet();
        this.zipCode = address.getZipCode();
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
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
        return street + ", " + zipCode + " " + cityName;
    }
}
