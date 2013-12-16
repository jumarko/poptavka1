package com.eprovement.poptavka.rest.common.dto;

import com.eprovement.poptavka.domain.enums.LocalityType;

import java.util.Map;

/**
 * External representation of {@link com.eprovement.poptavka.domain.address.Locality} usable in outside world.
 */
public class LocalityDto {
    private Long id;
    private LocalityType type;

    /**
     * Can be either full name or region abbreviation.
     * See <a href="http://en.wikipedia.org/wiki/List_of_U.S._state_abbreviations">List of U.S. region abbreviations</a>
     */
    private String region;
    private String district;
    private String city;
    private String street;
    private String houseNum;
    private String zipCode;

    private Map<String, String> links;

    public Long getId() {
        return id;
    }

    public LocalityDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getRegion() {
        return region;
    }

    public LocalityDto setRegion(String region) {
        this.region = region;
        return this;
    }

    public LocalityDto setDistrict(String district) {
        this.district = district;
        return this;
    }

    public String getDistrict() {
        return district;
    }

    public String getCity() {
        return city;
    }

    public LocalityDto setCity(String city) {
        this.city = city;
        return this;
    }

    public String getStreet() {
        return street;
    }

    public LocalityDto setStreet(String street) {
        this.street = street;
        return this;
    }

    public String getHouseNum() {
        return houseNum;
    }

    public LocalityDto setHouseNum(String houseNum) {
        this.houseNum = houseNum;
        return this;
    }

    public String getZipCode() {
        return zipCode;
    }

    public LocalityDto setZipCode(String zipCode) {
        this.zipCode = zipCode;
        return this;
    }

    public Map<String, String> getLinks() {
        return links;
    }

    public LocalityDto setLinks(Map<String, String> links) {
        this.links = links;
        return this;
    }

    public LocalityDto setType(LocalityType type) {
        this.type = type;
        return this;
    }

    public LocalityType getType() {
        return type;
    }

}

