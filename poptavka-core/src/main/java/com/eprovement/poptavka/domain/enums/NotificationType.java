/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.domain.enums;

/**
 *
 * @author ivan.vlcek
 */
public enum NotificationType {

    /**
     * Notifications of this type are intended for generic <code>User</code>.
     */
    USER("USER"),

    /**
     * Notifications of this type are intended for <code>Supplier</code> only
     *
     * <p>Note, that if you want to get all notifications for Supplier you have to include also
     * this notification type.</p>
     * @see Supplier
     */
    SUPPLIER("SUPPLIER"),
    /**
     * Notifications of this type are intended for <code>Client</code> only
     *
     * <p>Note, that if you want to get all notifications for Client you have to include also
     * this notification type.</p>
     * @see Client
     */
    CLIENT("CLIENT"),
    /**
     * Notifications of this type are intended for <code>Partner</code> only
     *
     * <p>Note, that if you want to get all notifications for Partner you have to include also
     * this notification type.</p>
     * @see Partner
     */
    PARTNER("PARTNER"),
    /**
     * Notifications of this type are intended for non-registered users only
     */
    NONREGISTERED("NONREGISTERED");

    private final String value;

    NotificationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
