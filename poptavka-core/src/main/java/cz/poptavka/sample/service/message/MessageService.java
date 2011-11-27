package cz.poptavka.sample.service.message;

import cz.poptavka.sample.dao.message.MessageDao;
import cz.poptavka.sample.dao.message.MessageFilter;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.domain.user.User;
import cz.poptavka.sample.exception.MessageException;
import cz.poptavka.sample.service.GenericService;

import java.util.List;
import java.util.Map;

/**
 * @author Juraj Martinka
 *         Date: 3.5.11
 */
public interface MessageService extends GenericService<Message, MessageDao> {

    /**
     * Cretaes a new message which is a threadRoot of a new thread.
     * It creates a <code>UserMessage</code> for the sneder so that they can
     * see and work with the message.
     *
     * @param user The user who composes (and will send) the message
     * @return the message, whose subject, body... should then be filled and
     * which should then be sent
     */
    Message newThreadRoot(User user);
    /**
     * Creates a new message in a reply to a message provided. It constructs
     * the subject from the original message's sbject by prefixing "Re: " to it
     * (if it hasn't been done so before) It also sets the recipient to the
     * sender of the original message
     * It creates a <code>UserMessage</code> for the sneder so that they can
     * see and work with the message.
     *
     * @param inReplyTo the message to which the new reply should be created
     * @param user the sender of the reply
     * @return the reply message to be completed and sent
     */
    Message newReply(Message inReplyTo, User user);

    /**
     * Load all message threads' roots for specified <code>user</code>.
     * No default ordering is applied! (if <code>resultCriteria</code> is null or ordering is not specified by it).
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

    /**
     * Returns message thread root assigned to given demand.
     * @param demand
     * @return
     */
    Message getThreadRootMessage(Demand demand);

    /**
     * Returns offer messages from this threadRoot
     * @param threadRoot
     * @return
     */
    List<Message> getAllOfferMessagesForDemand(Message threadRoot);

    /**
     * Loads conversation between supplier and client related to potential offer.
     *
     * @param message
     * @param supplierUser
     * @return
     */
    List<Message> getPotentialOfferConversation(Message threadRoot, User supplierUser);

    /**
     * Gets all the demand messages of the given user along with the number of
     * ALL the messages that span from the demand message (including the demand
     * message itself)
     *
     * @param user
     * @return a map keyed by the user's demand messages containing the number
     * of ALL the messages spanning from the demand message (including the
     * demand message itself)
     */
    Map<Message, Long> getListOfClientDemandMessagesAll(User user);

    /**
     * Gets all the demand messages of the given user along with the number of
     * UNREAD messages that span from the demand message (including the demand
     * message itself)
     *
     * @param user
     * @return a map keyed by the user's demand messages containing the number
     * of UNREAD messages spanning from the demand message (including the
     * demand message itself)
     */
    Map<Message, Long> getListOfClientDemandMessagesUnread(User user);

    /**
     * Gets all the descendants (not just the children) of a given message
     * @param message
     * @return
     */
    List<Message> getAllDescendants(Message message);

    /**
     * Gets all the descendants (not just the children) of every item
     * in the given list of messages
     *
     * @param messages
     * @return
     */
    List<Message> getAllDescendants(List<Message> messages);

    /**
     * Gets count of all the descendants (not just the children) of every item
     * in the given list of messages
     *
     * @param messages
     * @return
     */
    int getAllDescendantsCount(List<Message> messages);

        /**
     * Gets count of all the descendants (not just the children) of the given
     * message
     *
     * @param message
     * @return
     */
    int getAllDescendantsCount(Message message);

    /**
     * Gets count of all the descendants (not just the children) of every item
     * in the given list of messages that the given user can see, i.e. there
     * is a UserMessage for the message
     *
     * @param messages
     * @param user the user, whose message count should be retrieved
     * @return
     */
    int getAllDescendantsCount(List<Message> messages, User user);

    /**
     * Gets count of all the descendants (not just the children) of the given
     * message that the given user can see, i.e. there
     * is a UserMessage for the message
     *
     * @param message
     * @param user the user, whose message count should be retrieved
     * @return
     */
    int getAllDescendantsCount(Message message, User user);

    /**
     * Gets count of all the descendants (not just the children) of every item
     * in the given list of messages that the given user can see and hasn't
     * read
     *
     * @param messages
     * @return
     */
    int getUnreadDescendantsCount(List<Message> messages, User user);

    /**
     * Gets count of all the descendants (not just the children) of the given
     * message that the given user can see and hasn't read
     *
     * @param messages
     * @return
     */
    int getUnreadDescendantsCount(Message message, User user);

    /**
     * Sends a message to the recipients stored in the message.
     * It creates <code>UserMessage</code>s for all the recipients enabling
     * them to see the message.
     * It also places the message properly within the message tree structure.
     *
     * @param message
     * @param to direct recipients
     * @param cc carbon copy (to the attention of)
     * @param bcc blind carbon copy
     *
     * @throws MessageCannotBeSentException if Message is not in COMPOSED state or message.roles are empty
     */
    void send(Message message) throws MessageException;

    /**
     * Gets the child of the message that has been sent the last
     * @param parent
     * @return
     */
    Message getLastChild(Message parent);

}
