/*
 * MessageType is basically a helper class to recognize us easily into which
 * context the Message belongs to. Message can be associated with Problem/
 * Demand/Offer/ or ordinary mail.
 */
package cz.poptavka.sample.domain.message;

/**
 *
 * @author ivan.vlcek
 */
public enum MessageType {
    /* Message for supplier with potential demand. */
    POTENTIAL_SUPPLIERS_DEMAND(Integer.valueOf(1)),

    /* Message for client with potential offer. */
    POTENTIAL_CLIENTS_OFFER(Integer.valueOf(2));


    private final Integer value;

    MessageType(Integer value) {
        this.value = value;
    }
}
