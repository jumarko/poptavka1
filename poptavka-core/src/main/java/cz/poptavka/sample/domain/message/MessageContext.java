/*
 * MessageContext is basically a helper class to recognize us easily into which
 * context the Message belongs to. Message can be associated with Problem/
 * Demand/Offer/ or ordinary mail.
 */
package cz.poptavka.sample.domain.message;

/**
 *
 * @author ivan.vlcek
 */
public enum MessageContext {
    /* Message for supplier with potential demand. */
    POTENTIAL_SUPPLIERS_DEMAND(Integer.valueOf(1)),

    /* Message for client with potential offer. */
    POTENTIAL_CLIENTS_OFFER(Integer.valueOf(2)),

    /* Message from client when new demand is created. */
    NEW_CLIENTS_DEMAND(Integer.valueOf(3));


    private final Integer value;

    MessageContext(Integer value) {
        this.value = value;
    }
}
