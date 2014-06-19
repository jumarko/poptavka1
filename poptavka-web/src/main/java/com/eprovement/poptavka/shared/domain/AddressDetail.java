package com.eprovement.poptavka.shared.domain;

import com.eprovement.poptavka.client.common.validation.Extended;
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

    @NotEmpty(message = "{streetNotBlank}")
    @Pattern(regexp = "[a-zA-Z0-9/\\-()\\ \\.,]+", message = "{patternNoSpecChars}", groups = Extended.class)
    private String street;
    private String houseNum;

    @NotEmpty(message = "{zipCodeNotBlank}")
    @Pattern(regexp = "\\d+", message = "{patternNonString}", groups = Extended.class)
    @Size(min = ZIP_SIZE, message = "{zipCodeSize}", groups = Extended.class)
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

    /**************************************************************************/
    /* Override methods                                                       */
    /**************************************************************************/
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

    @Override
    public int hashCode() {
        return this.cityId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AddressDetail other = (AddressDetail) obj;
        if (!this.country.equals(other.country)) {
            return false;
        }
        if (!this.region.equals(other.region)) {
            return false;
        }
        if (!this.city.equals(other.city)) {
            return false;
        }
        if (!this.district.equals(other.district)) {
            return false;
        }
        if (!this.street.equals(other.street)) {
            return false;
        }
        if (!this.houseNum.equals(other.houseNum)) {
            return false;
        }
        if (!this.zipCode.equals(other.zipCode)) {
            return false;
        }
        return true;
    }
}
