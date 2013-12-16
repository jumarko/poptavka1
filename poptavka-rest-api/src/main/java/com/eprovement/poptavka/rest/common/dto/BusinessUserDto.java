package com.eprovement.poptavka.rest.common.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BusinessUserDto {

    /** Identifier of external system from which the user origins. */
    private String origin;
    private Long id;
    private String email;
    private String password;
    private String personFirstName;
    private String personLastName;
    private String companyName;
    private String website;
    private String phone;

    private List<LocalityDto> addresses = new ArrayList<>();

    public String getOrigin() {
        return origin;
    }

    public BusinessUserDto setOrigin(String origin) {
        this.origin = origin;
        return this;
    }

    public Long getId() {
        return id;
    }

    public BusinessUserDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public BusinessUserDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public BusinessUserDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getPersonFirstName() {
        return personFirstName;
    }

    public BusinessUserDto setPersonFirstName(String personFirstName) {
        this.personFirstName = personFirstName;
        return this;
    }

    public String getPersonLastName() {
        return personLastName;
    }

    public BusinessUserDto setPersonLastName(String personLastName) {
        this.personLastName = personLastName;
        return this;
    }

    public String getCompanyName() {
        return companyName;
    }

    public BusinessUserDto setCompanyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    public BusinessUserDto setAddresses(List<LocalityDto> addresses) {
        if (addresses == null) {
            this.addresses.clear();
            return null;
        }
        for (LocalityDto address : addresses) {
            addAddress(address);
        }
        return this;
    }

    public void addAddress(LocalityDto address) {
        this.addresses.add(address);
    }

    public List<LocalityDto> getAddresses() {
        return Collections.unmodifiableList(addresses);
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
