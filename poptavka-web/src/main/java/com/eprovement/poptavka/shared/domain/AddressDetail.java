package com.eprovement.poptavka.shared.domain;

import java.io.Serializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

public class AddressDetail implements Serializable {

    private static final int ZIP_SIZE = 4;
    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = 803719527031189460L;
    @NotEmpty(message = "{addressNotBlankCountry}")
    private String country;
    @NotEmpty(message = "{addressNotBlankRegion}")
    private String region;
    @NotEmpty(message = "{addressNotBlankCity}")
    private String city;
    @NotEmpty(message = "{addressNotBlankDistrict}")
    private String district;
    //
    @NotEmpty(message = "{addressNotBlankStreet}")
    private String street;
    private String houseNum;
    @NotEmpty(message = "{addressNotBlankZipCode}")
    @Pattern(regexp = "[0-9]+", message = "{addressPatternZipCode}")
    @Size(min = ZIP_SIZE, message = "{addressSizeZipCode}")
    private String zipCode;

    public AddressDetail() {
    }

    public AddressDetail(AddressDetail address) {
        this.country = address.getCountry();
        this.region = address.getRegion();
        this.city = address.getCity();
        this.district = address.getDistrict();
        //
        this.street = address.getStreet();
        this.zipCode = address.getZipCode();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
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

    public String getHouseNum() {
        return houseNum;
    }

    public void setHouseNum(String houseNum) {
        this.houseNum = houseNum;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public String toString() {
        return street + ", " + zipCode + " " + city;
    }
}
