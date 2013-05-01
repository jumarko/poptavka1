package com.eprovement.poptavka.shared.domain;

import com.google.gwt.user.client.rpc.IsSerializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;

public class AddressDetail implements IsSerializable {

    public enum AddressField {

        CITY("city"),
        STREET("street"),
        ZIP_CODE("zipCode");

        private String value;

        private AddressField(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
    private static final int ZIP_SIZE = 4;
    @NotEmpty(message = "{countryNotBlank}")
    private String country;
    @NotEmpty(message = "{regionNotBlank}")
    private String region;
    @NotEmpty(message = "{cityNotBlank}")
    private String city;
    private Long cityId;
    @NotEmpty(message = "{districtNotBlank}")
    private String district;
    //
    @NotEmpty(message = "{streetNotBlank}")
    private String street;
    private String houseNum;
    @NotEmpty(message = "{zipCodeNotBlank}")
    @Pattern(regexp = "\\d+", message = "{patternNonString}")
    @Size(min = ZIP_SIZE, message = "{zipCodeSize}")
    private String zipCode;

    public AddressDetail() {
    }

    public AddressDetail(AddressDetail address) {
        this.country = address.getCountry();
        this.region = address.getRegion();
        this.city = address.getCity();
        this.cityId = address.getCityId();
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

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
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
        StringBuilder address = new StringBuilder();
        address.append(street);
        address.append(", ");
        address.append(zipCode);
        address.append(", ");
        address.append(city);
        address.append(", ");
        address.append(region);
        return address.toString();
    }
}
