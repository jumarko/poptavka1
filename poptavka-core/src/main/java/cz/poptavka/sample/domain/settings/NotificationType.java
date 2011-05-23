/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.domain.settings;

/**
 *
 * @author ivan.vlcek
 */
public enum NotificationType {

    /**
     * Notifications of this type are intended for <code>Supplier</code> only
     * @see Supplier
     */
    SUPPLIER("SUPPLIER"),
    /**
     * Notifications of this type are intended for <code>Client</code> only
     * @see Client
     */
    CLIENT("CLIENT"),
    /**
     * Notifications of this type are intended for <code>Partner</code> only
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
