package cz.poptavka.sample.service.message;

import cz.poptavka.sample.dao.message.MessageDao;
import cz.poptavka.sample.dao.message.MessageFilter;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.domain.message.MessageState;
import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.domain.message.UserMessageRoleType;
import cz.poptavka.sample.domain.user.User;
import cz.poptavka.sample.exception.MessageException;
import cz.poptavka.sample.service.GenericServiceImpl;
import cz.poptavka.sample.service.usermessage.UserMessageService;
import cz.poptavka.sample.util.strings.ToStringUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Juraj Martinka
 *         Date: 4.5.11
 */
@Transactional
public class MessageServiceImpl extends GenericServiceImpl<Message, MessageDao> implements MessageService {
    private UserMessageService userMessageService;

    public MessageServiceImpl(MessageDao messageDao,
            UserMessageService userMessageService) {
        setDao(messageDao);
        this.userMessageService = userMessageService;
    }

    /** {@inheritDoc} */
    @Override
    public Message newThreadRoot(User user) {
        try {
            final Message message = new Message();
            message.setMessageState(MessageState.COMPOSED);
            message.setCreated(new Date());
            message.setLastModified(new Date());
            message.setThreadRoot(message);
            message.setSender(user);
            this.create(message);

            final UserMessage userMessage = new UserMessage();
            userMessage.setMessage(message);
            userMessage.setUser(user);
            userMessage.setRead(true);
            userMessage.setRoleType(UserMessageRoleType.SENDER);

            // TODO jumar - what exactly should be done here ? this userMessage is created only for admin or whom?

            userMessageService.create(userMessage);

            return message;
        } catch (MessageException ex) {
            Logger.getLogger(MessageServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public Message newReply(Message inReplyTo, User user) {
        // TODO jumar - WTF is this ?? - newThreadRoot(user) method set message.threadRoot to itself
        // and then we set message.threadRoot to "inReplyTo.threadRoot"
        Message message = this.newThreadRoot(user);
        message.setParent(inReplyTo);
        message.setThreadRoot(inReplyTo.getThreadRoot());
        if (inReplyTo.getDemand() != null) {
            message.setDemand(inReplyTo.getDemand());
        }
        if (inReplyTo.getSubject() == null) {
            message.setSubject("Re: ");
        } else {
            if (inReplyTo.getSubject().startsWith("Re:")
                    || inReplyTo.getSubject().startsWith("RE:")) {
                message.setSubject(inReplyTo.getSubject());
            } else {
                message.setSubject("Re: " + inReplyTo.getSubject());
            }
        }
        final UserMessage userMessage = new UserMessage();
        userMessage.setMessage(message);
        userMessage.setUser(inReplyTo.getSender());
        userMessage.setRoleType(UserMessageRoleType.TO);

        // TODO jumar - do this in a clean way - two UserMessage objects are created in this method
        if (CollectionUtils.isNotEmpty(message.getUserMessages())) {
            throw new IllegalStateException("message cannot have nonempty userMessages collection. "
                    + "It should have been"
                    + " created a moment ago via newThreadRoot method and that method cannot set userMessages!");
        }
        message.setUserMessages(Arrays.asList(userMessage));
        update(message);
        return message;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Message> getMessageThreads(User user, MessageFilter messageFilter) {
        return removeDuplicates(getDao().getMessageThreads(user, messageFilter));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> getAllMessages(User user, MessageFilter messageFilter) {
        return removeDuplicates(getDao().getAllMessages(user, messageFilter));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserMessage> getUserMessages(List<Message> messages, MessageFilter messageFilter) {
        return getDao().getUserMessages(messages, messageFilter);
    }

    @Override
    @Transactional(readOnly = true)
    public Message getLastChild(Message parent) {
        return getDao().getLastChild(parent);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<Message> getPotentialDemandConversation(Message threadRoot, User supplierUser) {
        return getDao().getPotentialDemandConversation(threadRoot, supplierUser);
    }

    /**
     * Returns message thread root assigned to given demand.
     * @param demand
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public Message getThreadRootMessage(Demand demand) {
        return getDao().getThreadRootMessage(demand);
    }

    /**
     * Returns offer messages from this threadRoot
     * @param threadRoot
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<Message> getAllOfferMessagesForDemand(Message threadRoot) {
        return getDao().getAllOfferMessagesForDemand(threadRoot);
    }

    /**
     * Loads conversation between supplier and client related to potential offer.
     *
     * @param message
     * @param supplierUser
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<Message> getPotentialOfferConversation(Message threadRoot, User supplierUser) {
        return getDao().getPotentialOfferConversation(threadRoot, supplierUser);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Map<Message, Integer> getListOfClientDemandMessagesAll(User user) {
        return getDao().getListOfClientDemandMessagesAll(user);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Map<Message, Integer> getListOfClientDemandMessagesUnread(User user) {
        return getDao().getListOfClientDemandMessagesUnread(user);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<Message> getAllDescendants(Message message) {
        List<Message> messages = new ArrayList<Message>();
        messages.add(message);
        return getAllDescendants(messages);

    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<Message> getAllDescendants(List<Message> messages) {
        return getDao().getAllDescendants(messages);
    }

    /** {@inheritDoc} */
    @Transactional(readOnly = true)
    public int getAllDescendantsCount(List<Message> messages) {
        return getDao().getAllDescendants(messages).size();
    }

    /** {@inheritDoc} */
    @Transactional(readOnly = true)
    public int getAllDescendantsCount(Message message) {
        List<Message> messages = new ArrayList<Message>();
        messages.add(message);
        return getAllDescendantsCount(messages);
    }

    /** {@inheritDoc} */
    @Transactional(readOnly = true)
    @Override
    public int getAllDescendantsCount(List<Message> messages, User user) {
        List<UserMessage> userMessages = userMessageService.getUserMessages(
                getDao().getAllDescendants(messages), user,
                MessageFilter.EMPTY_FILTER);
        return userMessages.size();
    }

    /** {@inheritDoc} */
    @Transactional(readOnly = true)
    @Override
    public int getAllDescendantsCount(Message message, User user) {
        List<Message> messages = new ArrayList();
        messages.add(message);
        return getAllDescendantsCount(messages, user);
    }

    /** {@inheritDoc} */
    @Transactional(readOnly = true)
    @Override
    public int getUnreadDescendantsCount(List<Message> messages, User user) {
        List<UserMessage> userMessages = userMessageService.getUserMessages(
                getDao().getAllDescendants(messages), user,
                MessageFilter.EMPTY_FILTER);
        int result = 0;
        for (UserMessage userMessage : userMessages) {
            if (!userMessage.isRead()) {
                result++;
            }
        }
        return result;
    }

    /** {@inheritDoc} */
    @Transactional(readOnly = true)
    @Override
    public int getUnreadDescendantsCount(Message message, User user) {
        List<Message> messages = new ArrayList<Message>();
        messages.add(message);
        return getUnreadDescendantsCount(messages, user);
    }

    /** {@inheritDoc} */
    @Override
    public void send(Message message) throws MessageException {
        if (message.getMessageState() != MessageState.COMPOSED) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Message [id=").append(ToStringUtils.printId(message));
            sb.append(" cannot be sent because it is NOT in a COMPOSED state.");
            throw new MessageException(sb.toString());
        }
        if (! hasAtLeastOneRecepient(message)) {
            throw new MessageException(
                    String.format("Message [id=%s] cannot be sent because there are no recipients set in ROLES.",
                            ToStringUtils.printId(message)));
        }

        // TODO jumar - following code is not needed anymore because we have only one UserMessage
        // and MessageUserRole does not exist - required UserMessage object already exist and it is in proper state,
        // i.e. read and starred attributes are set to false
//        for (UserMessage userMessage : message.getUserMessages()) {
//            if (isRecipient(userMessage)) {
//                // we got one of the recipients of the message
//                UserMessage userMessage = new UserMessage();
//                userMessage.setRead(false);
//                userMessage.setStarred(false);
//                userMessage.setMessage(message);
//                userMessage.setUser(userMessage.getUser());
//                generalService.save(userMessage);
//            }
//        }
        message.setMessageState(MessageState.SENT);
        message.setLastModified(new Date());
        message.setSent(new Date());
        // placing the message in the tree structre
        if (message.getParent() != null) {
            Message parent = message.getParent();
            if (parent.getFirstBorn() == null) {
                parent.setFirstBorn(message);
            } else {
                this.getLastChild(parent).setNextSibling(message);
            }
            parent.setMessageState(MessageState.REPLY_RECEIVED);
            parent = update(parent);
        }
        update(message);
    }

    private boolean isRecipient(UserMessage userMessage) {
        return (userMessage.getRoleType() == UserMessageRoleType.TO)
                || (userMessage.getRoleType() == UserMessageRoleType.CC)
                || (userMessage.getRoleType() == UserMessageRoleType.BCC);
    }


    /**
     * Checks whether given {@code message} has at least one recepient.
     * This practically means, that there is at least one {@link UserMessage}
     * with role type == {@link cz.poptavka.sample.domain.message.UserMessageRoleType.TO}
     * @param message message that is checked
     * @return true if {@code message} has at least one recipient, false otherwise
     */
    private boolean hasAtLeastOneRecepient(Message message) {
        if (CollectionUtils.isEmpty(message.getUserMessages())) {
            return false;
        }

        for (UserMessage userMessage : message.getUserMessages()) {
            if (UserMessageRoleType.TO == userMessage.getRoleType()) {
                return true;
            }
        }
        return false;
    }


    private ArrayList<Message> removeDuplicates(List<Message> messages) {
        return new ArrayList<Message>(new HashSet<Message>(messages));
    }



}
