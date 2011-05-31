package cz.poptavka.sample.dao.message;

import cz.poptavka.sample.dao.GenericDao;
import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.domain.user.User;

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
     * isRead, isStared
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
}
