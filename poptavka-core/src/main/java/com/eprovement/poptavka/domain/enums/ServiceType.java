/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.domain.enums;

/**
 *
 * @author ivan.vlcek
 */
public enum ServiceType {

    /**
     * Services of this type are intended for <code>Supplier</code> only
     * @see Supplier
     */
    SUPPLIER("SUPPLIER"),
    /**
     * Services of this type are intended for <code>Client</code> only
     * @see Client
     */
    CLIENT("CLIENT"),
    /**
     * Services of this type are intended for <code>Partner</code> only
     * @see Partner
     */
    PARTNER("PARTNER");

    private final String value;

    ServiceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
