/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.domain.mail;

/**
 *
  * @author Vojtech Hubr
 *         Date 12.4.11

 */
public enum RoleType {
    SENDER("SENDER"),

    TO("TO"),
    /* carbon coby (will be displayed to all the addressees) */
    CC("CC"),
    /* blind carbon copy (will NOT be displayed to the addresses) */
    BCC("BCC");

    private final String value;

    RoleType(String value) {
        this.value = value;
    }
}
