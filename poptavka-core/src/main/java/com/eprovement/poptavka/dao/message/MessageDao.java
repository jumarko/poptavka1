package com.eprovement.poptavka.dao.message;

import com.eprovement.poptavka.dao.GenericDao;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.user.User;

import java.util.List;

/**
 * Basic interface for dao which is provides methods for messages.
 *
 * @author Juraj Martinka
 *         Date: 4.5.11
 */
public interface MessageDao extends GenericDao<Message> {

    /**
     * Load all message threads' roots for specified <code>user</code>.
     * No default ordering is applied!
     *
     * @param user
     * @param messageFilter additional filtering, e.g. only incoming messages
     * @return all threads of messages for given user represented by thread roots
     * @see Message
     */
    List<Message> getMessageThreads(User user, MessageFilter messageFilter);

    /**
     * Loads (really) all messages for given user without any special structuring of result.
     * <code>resultCriteria</code> parameter can be used for simple ordering or limiting size of results.
     * <p>
     * No default ordering is applied!
     *
     * @param user
     * @param messageFilter additional filter, e.g. only outgoing messages
     * @return
     */
    List<Message> getAllMessages(User user, MessageFilter messageFilter);

    /**
     * Returns a userMessage of given message. UserMessage stores attributes like
     * read, starred
     *
     * @param messages
     * @param messageFilter
     * @return
     */
    List<UserMessage> getUserMessages(List<Message> messages, MessageFilter messageFilter);

    /**
     * Loads conversation between supplier and  client related to potential demand supplier's queries.
     *
     * @param message
     * @param supplierUser
     * @return
     */
    List<Message> getPotentialDemandConversation(Message message, User supplierUser);

    /**
     * Loads conversation between supplier and  client related to potential demand supplier's queries.
     *
     * @param message
     * @param supplierUser
     * @return List of user messages
     */
    List<UserMessage> getConversationUserMessages(Message message, User user);

    /**
     * Returns message thread root assigned to given demand.
     * @param demand
     * @return
     */
    Message getThreadRootMessage(Demand demand);

    /**
     * Loads conversation between supplier and client related to potential offer.
     *
     * @param message
     * @param supplierUser
     * @return
     */
    List<Message> getPotentialOfferConversation(Message threadRoot, User supplierUser);

    /**
     * Gets all the descendants (not just the children) of every item
     * in the given list of messages
     *
     * @param messages
     * @return
     */
    List<Message> getAllDescendants(List<Message> messages);

    /**
     * Gets the child of the message that has been sent the last
     * @param parent
     * @return
     */
    Message getLastChild(Message parent);
}
