/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.service.usermessage;

import com.eprovement.poptavka.dao.message.MessageFilter;
import com.eprovement.poptavka.dao.usermessage.UserMessageDao;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.message.ClientConversation;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.offer.OfferState;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.service.GenericService;
import com.googlecode.genericdao.search.Search;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ivan.vlcek
 */
public interface UserMessageService extends GenericService<UserMessage, UserMessageDao> {

    /**
     * Creates new User message for given {@code user}.
     *
     * @param message parent message for which the new message will be created, cannot be null
     * @param user user for who the new UserMessage will be created, cannot be null
     */
    UserMessage createUserMessage(Message message, User user);

    /**
     * Returns a userMessage of given message. UserMessage stores attributes like
     * isRead, isStared
     *
     * @param message
     * @param messageFilter
     * @return
     */
    List<UserMessage> getUserMessages(List<Message> messages,
            User user, MessageFilter messageFilter);

    /**
     * Returns a userMessage to a given message and user.
     * UserMessage stores attributes like isRead, isStared
     *
     * @param message
     * @param messageFilter
     * @return
     */
    UserMessage getUserMessage(Message message, User user);

    /**
     * Load all potential demands count for given supplier.
     * Those are all messages which ware sent by the Poptavka system to the given supplier
     * when some client created new Demand suitable for given supplier.
     *
     *
     *
     * @param supplier supplier for which all potential demands are found. It is of type
     *                 {@link com.eprovement.poptavka.domain.user.BusinessUser}
     *                 because that is the most specific descendant of {@link User} we can use.
     *                 {@link com.eprovement.poptavka.domain.user.Supplier} cannot be used directly (in a simple way)
     *                 because it is NOT a descendant of User.
     *                 Various implementations can have different requirements for this parameter, but in general,
     *                 at least <code>id</code> attribute should be filled
     * @return all potential demands count for given <code>supplier</code>
     *
     * @see com.eprovement.poptavka.service.message.MessageServiceImpl
     * @see com.eprovement.poptavka.service.demand.DemandService#sendDemandsToSuppliers()
     *
     */
    int getPotentialDemandsCount(BusinessUser supplier);

    /**
     * Load all potential demands for given supplier.
     * Those are all messages which ware sent by the Poptavka system to the given supplier
     * when some client created new Demand suitable for given supplier.
     *
     *
     *
     * @param supplier supplier for which all potential demands are found. It is of type
     *                 {@link com.eprovement.poptavka.domain.user.BusinessUser}
     *                 because that is the most specific descendant of {@link User} we can use.
     *                 {@link com.eprovement.poptavka.domain.user.Supplier} cannot be used directly (in a simple way)
     *                 because it is NOT a descendant of User.
     *                 Various implementations can have different requirements for this parameter, but in general,
     *                 at least <code>id</code> attribute should be filled
     * @return all potential demands for given <code>supplier</code>
     *
     * @see com.eprovement.poptavka.service.message.MessageServiceImpl
     * @see com.eprovement.poptavka.service.demand.DemandService#sendDemandsToSuppliers()
     *
     */
    List<UserMessage> getPotentialDemands(BusinessUser supplier);

    /**
     * Load all potential demands count for given supplier.
     * Those are all messages which ware sent by the Poptavka system to the given supplier
     * when some client created new Demand suitable for given supplier.
     *
     *
     *
     * @param supplier supplier for which all potential demands are found. It is of type
     *                 {@link com.eprovement.poptavka.domain.user.BusinessUser}
     *                 because that is the most specific descendant of {@link User} we can use.
     *                 {@link com.eprovement.poptavka.domain.user.Supplier} cannot be used directly (in a simple way)
     *                 because it is NOT a descendant of User.
     *                 Various implementations can have different requirements for this parameter, but in general,
     *                 at least <code>id</code> attribute should be filled
     * @param search specifies how the resulting list should be filtered/sorted/
     *               paged
     * @return all potential demands count for given <code>supplier</code>
     *
     * @see com.eprovement.poptavka.service.message.MessageServiceImpl
     * @see com.eprovement.poptavka.service.demand.DemandService#sendDemandsToSuppliers()
     *
     */
    long getPotentialDemandsCount(BusinessUser supplier, Search search);

    /**
     * Load all potential demands for given supplier.
     * Those are all messages which ware sent by the Poptavka system to the given supplier
     * when some client created new Demand suitable for given supplier.
     *
     *
     *
     * @param supplier supplier for which all potential demands are found. It is of type
     *                 {@link com.eprovement.poptavka.domain.user.BusinessUser}
     *                 because that is the most specific descendant of {@link User} we can use.
     *                 {@link com.eprovement.poptavka.domain.user.Supplier} cannot be used directly (in a simple way)
     *                 because it is NOT a descendant of User.
     *                 Various implementations can have different requirements for this parameter, but in general,
     *                 at least <code>id</code> attribute should be filled
     * @param search specifies how the resulting list should be filtered/sorted/
     *               paged
     * @return all potential demands for given <code>supplier</code>
     *
     * @see com.eprovement.poptavka.service.message.MessageServiceImpl
     * @see com.eprovement.poptavka.service.demand.DemandService#sendDemandsToSuppliers()
     *
     */
    List<UserMessage> getPotentialDemands(BusinessUser supplier, Search search);

    /**
     * Gets the user's inbox
     * @param user the user whose inbox to get
     * @return list of <code>UserMesage</code> of the mesages in the user's
     * inbox
     */
    List<UserMessage> getInbox(User user);

    /**
     * Gets the user's inbox
     * @param user the user whose inbox to get
     * @param search specifies how the resulting list should be filtered/sorted/
     *               paged
     * @return list of <code>UserMesage</code> of the mesages in the user's
     *         inbox
     */
    List<UserMessage> getInbox(User user, Search search);

    /**
     * Gets the user's sent items folder
     * @param user the user whose sent items to get
     * @return list of <code>UserMesage</code> of the mesages in the user's
     * sent items' folder
     */
    List<UserMessage> getSentItems(User user);

    /**
     * Gets the user's sent items folder
     * @param user the user whose sent items to get
     * @param search specifies how the resulting list should be filtered/sorted/
     *               paged
     * @return list of <code>UserMesage</code> of the mesages in the user's
     * sent items' folder
     */
    List<UserMessage> getSentItems(User user, Search search);

    /**
     * Retrieves a map of the latest <code>UserMessage</code>s in each of the given
     * supplier's conversations along with the counts of messages in each conversation
     * that are accessible to the supplier
     *
     * @param user the supplier whose conversations to get
     * @return map of the latest <code>UserMessage</code> ids and number of
     * messages in each conversation
     */
    Map<UserMessage, Integer> getSupplierConversationsWithoutOffer(BusinessUser user);

    /**
     * Retrieves a map of the latest <code>UserMessage</code>s in each of the given
     * supplier's conversations along with the counts of messages in each conversation
     * that are accessible to the supplier
     *
     * @param user the supplier whose conversations to get
     * @param search defines how to filter, sort and trim the result
     * @return map of the latest <code>UserMessage</code> ids and number of
     * messages in each conversation
     */
    Map<UserMessage, Integer> getSupplierConversationsWithoutOffer(BusinessUser user, Search search);

    /**
     * Retrieves a map of the latest <code>UserMessage</code>s in each of the given
     * supplier's conversations along with the counts of messages in each conversation
     * that are accessible to the supplier where an offer has been made
     *
     * @param user the supplier whose conversations to get
     * @return map of the latest <code>UserMessage</code> ids and number of
     * messages in each conversation
     */
    Map<UserMessage, Integer> getSupplierConversationsWithOffer(BusinessUser user);

    /**
     * Retrieves a map of the latest <code>UserMessage</code>s in each of the given
     * supplier's conversations along with the counts of messages in each conversation
     * that are accessible to the supplier where an offer has been made
     *
     * @param user the supplier whose conversations to get
     * @param search defines how to filter, sort and trim the result
     * @return map of the latest <code>UserMessage</code> ids and number of
     * messages in each conversation
     */
    Map<UserMessage, Integer> getSupplierConversationsWithOffer(BusinessUser user,
            Search search);

    /** Retrieves the count of supplier's conversations where an offer has been
     * made
     *
     * @param user the supplier whose conversations to get
     * @return number of supplier conversations where an offer has been made
     */
    int getSupplierConversationsWithoutOfferCount(BusinessUser user);

    /** Retrieves the count of supplier's conversations where an offer has not
     * been made
     *
     * @param user the supplier whose conversations to get
     * @return number of supplier conversations where an offer has not been made
     */
    int getSupplierConversationsWithOfferCount(BusinessUser user);


    /**
     * Retrieves a map of the latest <code>UserMessage</code>s in each of the given
     * supplier's conversations along with the counts of messages in each conversation
     * that are accessible to the supplier where an offer is in state ACCEPTED or COMPLETED.
     *
     * @param user the supplier whose conversations to get
     * @param queryName
     * @return map of the latest <code>UserMessage</code> ids and number of
     * messages in each conversation
     */
    Map<UserMessage, Integer> getSupplierConversationsWithAcceptedOffer(BusinessUser user);

    /**
     * Retrieves a map of the latest <code>UserMessage</code>s in each of the given
     * supplier's conversations along with the counts of messages in each conversation
     * that are accessible to the supplier where an offer is in state ACCEPTED or COMPLETED.
     *
     * @param user the supplier whose conversations to get
     * @param queryName
     * @param search defines how to filter, sort and trim the result
     * @return map of the latest <code>UserMessage</code> ids and number of
     * messages in each conversation
     */
    Map<UserMessage, Integer> getSupplierConversationsWithAcceptedOffer(BusinessUser user,
            Search search);

    /**
     * Retrieves a map of the latest <code>UserMessage</code>s in each of the given
     * supplier's conversations along with the counts of messages in each conversation
     * that are accessible to the supplier where where <code>DemandStatus</code> is CLOSED.
     *
     * @param user the supplier whose conversations to get
     * @return map of the latest <code>UserMessage</code> ids and number of
     * messages in each conversation
     */
    Map<UserMessage, Integer> getSupplierConversationsWithClosedDemands(BusinessUser user);

    /**
     * Retrieves a map of the latest <code>UserMessage</code>s in each of the given
     * supplier's conversations along with the counts of messages in each conversation
     * that are accessible to the supplier where where <code>DemandStatus</code> is CLOSED.
     *
     * @param user the supplier whose conversations to get
     * @param search defines how to filter, sort and trim the result
     * @return map of the latest <code>UserMessage</code> ids and number of
     * messages in each conversation
     */
    Map<UserMessage, Integer> getSupplierConversationsWithClosedDemands(BusinessUser user,
            Search search);

   /**
    * Gets a list of all client's conversations related to the given demand where
    * no offer has been made
    *
    * @param user The client whose demands to get
    * @param demand the demand for which to get the conversations
    * @return A map keyed by the latest <code>UserMessage</code> and mapping
    * to the <code>ClientConversation</code> object containing the number of
    * messages in the conversation and the <code>BusinessUser</code> representing
    * the supplier with whom the conversation is being made
    */
    Map<UserMessage, ClientConversation> getClientConversationsWithoutOffer(
            BusinessUser user, Demand demand);

   /**
    * Gets a list of all client's conversations related to the given demand where
    * no offer has been made
    *
    * @param user The client whose demands to get
    * @param demand the demand for which to get the conversations
    * @param search to search in the result
    * @return A map keyed by the latest <code>UserMessage</code> and mapping
    * to the <code>ClientConversation</code> object containing the number of
    * messages in the conversation and the <code>BusinessUser</code> representing
    * the supplier with whom the conversation is being made
    */
    Map<UserMessage, ClientConversation> getClientConversationsWithoutOffer(
            BusinessUser user, Demand demand, Search search);

    /** Retrieves the count of client's conversations where an offer has not
     * been made
     *
     * @param user the client whose conversations to get
     * @param demand the demand for which to get the conversations
     * @return number of client's conversations where an offer has not been made
     */
    int getClientConversationsWithoutOfferCount(BusinessUser user, Demand demand);

   /**
    * Gets a list of all client's conversations related to the given demand where
    * an offer has been made
    *
    * @param user The client whose demands to get
    * @param demand the demand for which to get the conversations
    * @return A map keyed by the latest <code>UserMessage</code> and mapping
    * to the <code>ClientConversation</code> object containing the number of
    * messages in the conversation and the <code>BusinessUser</code> representing
    * the supplier with whom the conversation is being made
    */
    Map<UserMessage, ClientConversation> getClientConversationsWithOffer(
            BusinessUser user, Demand demand);

   /**
    * Gets a list of all client's conversations related to the given demand where
    * an offer has been made
    *
    * @param user The client whose demands to get
    * @param demand the demand for which to get the conversations
    * @param search to search in the result
    * @return A map keyed by the latest <code>UserMessage</code> and mapping
    * to the <code>ClientConversation</code> object containing the number of
    * messages in the conversation and the <code>BusinessUser</code> representing
    * the supplier with whom the conversation is being made
    */
    Map<UserMessage, ClientConversation> getClientConversationsWithOffer(
            BusinessUser user, Demand demand, Search search);

   /**
    * Gets a list of all client's conversations related to the given demand where
    * an offer has been made
    *
    * @param user The client whose demands to get
    * @param demand the demand for which to get the conversations
    * @param offerState the state of the offer linked to the latest conversation
    * messages
    * @return A map keyed by the latest <code>UserMessage</code> and mapping
    * to the <code>ClientConversation</code> object containing the number of
    * messages in the conversation and the <code>BusinessUser</code> representing
    * the supplier with whom the conversation is being made
    */
    Map<UserMessage, ClientConversation> getClientConversationsWithOffer(
            BusinessUser user, Demand demand, OfferState offerState);

   /**
    * Gets a list of all client's conversations related to the given demand where
    * an offer has been made
    *
    * @param user The client whose demands to get
    * @param demand the demand for which to get the conversations
    * @param offerState the state of the offer linked to the latest conversation
    * messages
    * @param search to search in the result
    * @return A map keyed by the latest <code>UserMessage</code> and mapping
    * to the <code>ClientConversation</code> object containing the number of
    * messages in the conversation and the <code>BusinessUser</code> representing
    * the supplier with whom the conversation is being made
    */
    Map<UserMessage, ClientConversation> getClientConversationsWithOffer(
            BusinessUser user, Demand demand, OfferState offerState, Search search);

    /** Retrieves the count of client's conversations where an offer has
     * been made for given demand
     *
     * @param user the client whose conversations to get
     * @param demand the demand for which to get the conversations
     * @return number of client's conversations where an offer has been made
     */
    int getClientConversationsWithOfferCount(BusinessUser user, Demand demand);

    /**
     * Creates a <code>UserMessage</code> for a given <code>Message</code>
     * and <code>User</code>. This is to be used when an admin or operator user needs
     * to work with a message from a conversation in which they haven't been involved in
     * so a <code>UserMessage</code> for them couldn't be created.
     * @param message The <code>Message</code> the admin user needs to work with
     * @param user The admin user for whom the new <code>UserMessage</code> is to be created
     * @return the newly created (or existing if there was such) <code>UserMessage</code>
     */
    UserMessage getAdminUserMessage(Message message, User user);

    /**
     * Loads all {@link UserMessage}-s that have associated message
     * with {@link Message#created} equal or later than {@code dateFrom}.
     * @param dateFrom start date for message created date
     * @return users mapped to all their user messages which has associated created date staring from given period
     */
    Map<User, List<UserMessage>> getUserMessagesFromDateGroupedByUser(Date dateFrom);

    /**
     * Gets client's conversations where an offer has been ACCEPTED or COMPLETED
     * @param user the client
     * @param search to search in the result
     * @return <code>Map</code> of the latest <code>UserMessage</code> in each conversation
     * to <code>ClientConversation</code>
     */
    Map<UserMessage, ClientConversation> getClientConversationsWithAcceptedOffer(BusinessUser user,
            Search search);

    /**
     * Gets client's conversations where an offer has been CLOSED
     * @param user the client
     * @param search to search in the result
     * @return <code>Map</code> of the latest <code>UserMessage</code> in each conversation
     * to <code>ClientConversation</code>
     */
    Map<UserMessage, ClientConversation> getClientConversationsWithClosedOffer(BusinessUser user,
            Search search);

    /**
     * Gets conversation of the <code>user</code> with the <code>counetrparty</<code>
     * that originated in the <code>threadRoot</code>
     * @param user the user
     * for whom to get the UserMessages
     * @param counterparty the user with whom we are communicating
     * @param rootMessage the first (topmost) message in the conversation
     * @return  list of UserMessages belonging to <code>user</code> in the order from
     * the newest to the oldest
     */
    List<UserMessage> getConversation(User user, User counterparty, Message rootMessage);
}
