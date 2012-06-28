/*
 * MessageContext is basically a helper class to recognize us easily into which
 * context the Message belongs to. Message can be associated with Problem/
 * Demand/Offer/ or ordinary mail.
 */
package com.eprovement.poptavka.domain.enums;

/**
 * MessageContext gives meaning to the Message.
 * E.g. {@link #POTENTIAL_OFFER_FROM_SUPPLIER} means that message has been sent by supplier
 * to the client and it is a potential offer.
 *
 * <p>
 *     WATCH OUT for storing these values to the DB in entity
 *     {@link com.eprovement.poptavka.domain.message.UserMessage}.
 *     They are stored as integers numbered from 0!
 * </p>
 *
 * @see com.eprovement.poptavka.domain.message.UserMessage#messageContext
 * @see com.eprovement.poptavka.domain.message.MessageUserRole#messageContext
 * @author ivan.vlcek
 */
public enum MessageContext {
    /* Message for supplier with potential demand.
    TODO Vojto rethink the contexts */
    POTENTIAL_SUPPLIERS_DEMAND,
    /* Message for client with potential offer. */
    POTENTIAL_CLIENTS_OFFER,
    /* Message from client when new demand is created. */
    NEW_CLIENTS_DEMAND,
    /* Message from supplier about query to potential demand. */
    QUERY_TO_POTENTIAL_SUPPLIERS_DEMAND,
    /* Offer sent by Supplier. */
    POTENTIAL_OFFER_FROM_SUPPLIER;
}
