package com.eprovement.poptavka.domain.enums;

/**
 * Types of notifications by their intended recipient
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
     * @see com.eprovement.poptavka.domain.user.Supplier
     */
    SUPPLIER("SUPPLIER"),
    /**
     * Notifications of this type are intended for <code>Client</code> only
     *
     * <p>Note, that if you want to get all notifications for Client you have to include also
     * this notification type.</p>
     * @see com.eprovement.poptavka.domain.user.Client
     */
    CLIENT("CLIENT"),
    /**
     * Notifications of this type are intended for <code>Partner</code> only
     *
     * <p>Note, that if you want to get all notifications for Partner you have to include also
     * this notification type.</p>
     * @see com.eprovement.poptavka.domain.user.Partner
     */
    PARTNER("PARTNER"),
    /**
     * Notifications of this type are intended for non-registered users only
     */
    NONREGISTERED("NONREGISTERED"),

    /**
     * Notification type used for welcome messages sent after user's registration.
     * Not related to any particular group or type of users.
     * A regular user SHOULD NOT BE ABLE to subscribe to this type of notifications.
     */
    WELCOME("WELCOME");

    private final String value;

    NotificationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
