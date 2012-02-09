/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.domain.message;

/**
 * Represents user role in message. This can express various conditions, e.g. that user is a recipient of message,
 * sender or it has to
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
