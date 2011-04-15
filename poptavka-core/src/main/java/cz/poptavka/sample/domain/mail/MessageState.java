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
public enum MessageState {
    UNSENT("UNSENT"),

    SENT("SENT"),

    REVOKED("REVOKED"),

    DELETED("DELETED");


    private final String value;

    MessageState(String value) {
        this.value = value;
    }

}
