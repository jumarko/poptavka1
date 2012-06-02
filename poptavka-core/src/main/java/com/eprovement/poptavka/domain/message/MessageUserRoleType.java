/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eprovement.poptavka.domain.message;

/**
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
