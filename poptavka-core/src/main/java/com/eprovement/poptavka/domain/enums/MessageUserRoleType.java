package com.eprovement.poptavka.domain.enums;

/**
 * Users as addressees, copy addressees etc. are associated to the messages via
 * MessageUserRole.
 * This enum specifies the type of the role.
 *
  * @author Vojtech Hubr
 *         Date 12.4.11

 */
public enum MessageUserRoleType {
    SENDER("SENDER"),

    TO("TO"),
    /* carbon copy (will be displayed to all the addressees) */
    CC("CC"),
    /* blind carbon copy (will NOT be displayed to the addresses) */
    BCC("BCC");

    private final String value;

    MessageUserRoleType(String value) {
        this.value = value;
    }
}
