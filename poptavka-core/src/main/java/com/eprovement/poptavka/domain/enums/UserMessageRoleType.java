package com.eprovement.poptavka.domain.enums;

/**
 * Represents user role in message.
 *
 * @author Vojtech Hubr
 *         Date 12.4.11
 * @author Juraj Martinka
 */
public enum UserMessageRoleType {
    SENDER("SENDER"),

    TO("TO"),
    /* carbon copy (will be displayed to all the addressees) */
    CC("CC"),
    /* blind carbon copy (will NOT be displayed to the addresses) */
    BCC("BCC");

    private final String value;

    UserMessageRoleType(String value) {
        this.value = value;
    }
}
