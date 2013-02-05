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
import java.util.List;
import java.util.Map;

/**
 *
 * @author ivan.vlcek
 */
public interface UserMessageService extends GenericService<UserMessage, UserMessageDao> {

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
    long getPotentialDemandsCount(BusinessUser supplier);

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
    Map<UserMessage, Integer> getSupplierConversationsWithoutOffer(User user);

    /**
     * Retrieves a map of the latest <code>UserMessage</code>s in each of the given
     * supplier's conversations along with the counts of messages in each conversation
     * that are accessible to the supplier where an offer has been made
     *
     * @param user the supplier whose conversations to get
     * @return map of the latest <code>UserMessage</code> ids and number of
     * messages in each conversation
     */
    Map<UserMessage, Integer> getSupplierConversationsWithOffer(User user, OfferState pendingState);

    /** Retrieves the count of supplier's conversations where an offer has been
     * made
     *
     * @param user the supplier whose conversations to get
     * @return number of supplier conversations where an offer has been made
     */
    int getSupplierConversationsWithoutOfferCount(User user);

    /** Retrieves the count of supplier's conversations where an offer has not
     * been made
     *
     * @param user the supplier whose conversations to get
     * @return number of supplier conversations where an offer has not been made
     */
    int getSupplierConversationsWithOfferCount(User user);


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
    Map<UserMessage, Integer> getSupplierConversationsWithAcceptedOffer(User user,
            OfferState offerStateAccepted, OfferState offerStateCompleted);

   /**
    * Gets a list of all client's conversation related to their demands where
    * no offer has been made
    *
    * @param user The client whose demands to get
    * @return A map keyed by the latest <code>UserMessage</code> and mapping
    * to the <code>ClientConversation</code> object containing the number of
    * messages in the conversation and the <code>User</code> representing
    * the supplier with whom the conversation is being made
    */
    Map<UserMessage, ClientConversation> getClientConversationsWithoutOffer(
            User user, Message root);

    /** Retrieves the count of client's conversations where an offer has not
     * been made
     *
     * @param user the client whose conversations to get
     * @return number of client's conversations where an offer has not been made
     */
    int getClientConversationsWithoutOfferCount(User user);

     /** Retrieves the count of client's conversations where an offer has
     * been made for given demand
     *
     * @param user the client whose conversations to get
     * @param demand the demand which conversations to get
     * @return number of client's conversations where an offer has been made
     */
    int getClientConversationsWithOfferCount(User user, Demand demand);
}
