package cz.poptavka.sample.domain.address;

import cz.poptavka.sample.domain.common.DomainObject;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

/**
 * @author Juraj Martinka
 *         Date: 29.1.11
 */
@Entity
public class Address extends DomainObject {

    @Enumerated(value = EnumType.STRING)
    private AddressType addressType;

    @ManyToOne
    private Locality country;
    @ManyToOne
    private Locality region;
    @ManyToOne
    private Locality district;
    @ManyToOne
    private Locality township;
    @ManyToOne
    private Locality city;

    private String street;
    private String zipCode;
    private String houseNum;
    private String flatNum;


    public Locality getCountry() {
        return country;
    }

    public void setCountry(Locality country) {
        this.country = country;
    }

    public Locality getRegion() {
        return region;
    }

    public void setRegion(Locality region) {
        this.region = region;
    }

    public Locality getDistrict() {
        return district;
    }

    public void setDistrict(Locality district) {
        this.district = district;
    }

    public Locality getTownship() {
        return township;
    }

    public void setTownship(Locality township) {
        this.township = township;
    }

    public Locality getCity() {
        return city;
    }

    public void setCity(Locality city) {
        this.city = city;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
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

    public String getHouseNum() {
        return houseNum;
    }

    public void setHouseNum(String houseNum) {
        this.houseNum = houseNum;
    }

    public String getFlatNum() {
        return flatNum;
    }

    public void setFlatNum(String flatNum) {
        this.flatNum = flatNum;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Address");
        sb.append("{addressType=").append(addressType);
        sb.append(", country=").append(country);
        sb.append(", region=").append(region);
        sb.append(", district=").append(district);
        sb.append(", township=").append(township);
        sb.append(", city=").append(city);
        sb.append(", street='").append(street).append('\'');
        sb.append(", zipCode='").append(zipCode).append('\'');
        sb.append(", houseNum='").append(houseNum).append('\'');
        sb.append(", flatNum='").append(flatNum).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
