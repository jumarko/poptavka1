package com.eprovement.poptavka.domain.enums;

/**
 * Service types.
 * Since we use credit system we don't need to differ types of between business roles.
 * Users just buy credits and then we charge appropriate features.
 * @author Ivan Vlcek
 */
public enum ServiceType {

    /**
     * Services of this type are intended for <code>new users</code> only
     */
    PROMOTION("PROMOTION"),
    /**
     * Services of this type are intended for <code>new and existing users</code> only
     */
    RECHARGE("RECHARGE");

    private final String value;

    ServiceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
