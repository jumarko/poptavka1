package com.eprovement.poptavka.service.message;

import com.eprovement.poptavka.dao.message.MessageDao;
import com.eprovement.poptavka.dao.message.MessageFilter;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.enums.MessageState;
import com.eprovement.poptavka.domain.message.MessageUserRole;
import com.eprovement.poptavka.domain.enums.MessageUserRoleType;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.offer.OfferState;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.exception.MessageException;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.GenericServiceImpl;
import com.eprovement.poptavka.service.usermessage.UserMessageService;
import com.eprovement.poptavka.util.search.Searcher;
import com.eprovement.poptavka.util.strings.ToStringUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Juraj Martinka
 *         Date: 4.5.11
 */
@Transactional
public class MessageServiceImpl extends GenericServiceImpl<Message, MessageDao> implements MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageServiceImpl.class);

    private GeneralService generalService;
    private UserMessageService userMessageService;

    public MessageServiceImpl(MessageDao messageDao,
            GeneralService generalService,
            UserMessageService userMessageService) {
        setDao(messageDao);
        this.generalService = generalService;
        this.userMessageService = userMessageService;
    }

    /** {@inheritDoc} */
    @Override
    public Message newThreadRoot(User user) {
        try {
            LOGGER.debug("action=new_thread_root_async status=start user={}");
            Message message = new Message();
            message.setMessageState(MessageState.COMPOSED);
            message.setCreated(new Date());
            message.setLastModified(new Date());
            message.setThreadRoot(message);
            message.setSender(user);
            this.create(message);
            UserMessage userMessage = new UserMessage();
            userMessage.setMessage(message);
            userMessage.setUser(user);
            userMessage.setRead(true);
            userMessageService.create(userMessage);
            LOGGER.debug("action=new_thread_root_async status=finish user={} message={}", user, message);
            return message;
        } catch (MessageException ex) {
            LOGGER.error(ex.getMessage(), ex);
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public Message newReply(Message inReplyTo, User user) {
        Message message = this.newThreadRoot(user);
        message.setParent(inReplyTo);
        message.setThreadRoot(inReplyTo.getThreadRoot());
        if (inReplyTo.getDemand() != null) {
            message.setDemand(inReplyTo.getDemand());
        }
        if (inReplyTo.getOffer() != null) {
            message.setOffer(inReplyTo.getOffer());
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
        MessageUserRole messageUserRole = new MessageUserRole();
        messageUserRole.setMessage(message);
        Message addresseeMessage = inReplyTo;
        while (addresseeMessage.getParent() != null
                && !addresseeMessage.equals(addresseeMessage.getParent())
                && addresseeMessage.getSender().equals(user)) {
            addresseeMessage = addresseeMessage.getParent();
        }
        messageUserRole.setUser(addresseeMessage.getSender());
        messageUserRole.setType(MessageUserRoleType.TO);
        List<MessageUserRole> messageUserRoles = new ArrayList();
        messageUserRoles.add(messageUserRole);
        message.setRoles(messageUserRoles);
        update(message);
        return message;
    }


    @Override
    @Transactional(readOnly = true)
    public List<Message> getMessageThreads(User user, MessageFilter messageFilter) {
        return getDao().getMessageThreads(user, messageFilter);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> getAllMessages(User user, MessageFilter messageFilter) {
        return getDao().getAllMessages(user, messageFilter);
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

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<UserMessage> getConversationUserMessages(Message threadRoot, User supplierUser, Search search) {

        final List<UserMessage> allConversionMessages = getDao().getConversationUserMessages(threadRoot, supplierUser);
        if (search == null) {
            return allConversionMessages;
        }

        // search class must always be UserMessage - do not allow any other setting!
        search.setSearchClass(UserMessage.class);
        return Searcher.searchCollection(allConversionMessages, search);
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
    public Map<Long, Integer> getListOfClientDemandMessagesUnread(User user) {
        return getDao().getListOfClientDemandMessagesUnread(user);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Map<Long, Integer> getLatestSupplierUserMessagesWithoutOfferForDemand(User user, Message threadRoot) {
        return getDao().getLatestSupplierUserMessagesWithoutOfferForDemand(user, threadRoot);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Map<Long, Integer> getLatestSupplierUserMessagesWithOfferForDemand(User user, Message threadRoot,
        OfferState pendingState) {
        return getDao().getLatestSupplierUserMessagesWithOfferForDemand(user, threadRoot, pendingState);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Map<Long, Integer> getListOfClientDemandMessagesWithOfferUnreadSub(User user) {
        return getDao().getListOfClientDemandMessagesWithOfferUnreadSub(user);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<Message> getAllDescendants(Message message) {
        List<Message> messages = new ArrayList();
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
        List<Message> messages = new ArrayList();
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
        List<Message> messages = new ArrayList();
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
        for (MessageUserRole role : message.getRoles()) {
            if ((role.getType() == MessageUserRoleType.TO)
                    || (role.getType() == MessageUserRoleType.CC)
                    || (role.getType() == MessageUserRoleType.BCC)) {
                // we got one of the recipients of the message
                createUserMessage(message, role.getUser());
            }
        }
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

    public void createUserMessage(Message message, User user) {
        Validate.notNull(message, "message cannot be null!");
        Validate.notNull(user, "user cannot be null!");
        final UserMessage userMessage = new UserMessage();
        userMessage.setRead(false);
        userMessage.setStarred(false);
        userMessage.setMessage(message);
        userMessage.setUser(user);
        generalService.save(userMessage);
    }

    /**
     * A status message will be sent into conversation between client and supplier.
     *
     * @param latestUserMessageId of latest UserMessage
     * @param user user on whose behalf system sends a status message
     * @param messageBody localized text of the message
     */
    public void sendGeneratedMessage(long latestUserMessageId, User user, String messageBody) {
        UserMessage latestUserMessage = userMessageService.getById(latestUserMessageId);
        Message message = newReply(latestUserMessage.getMessage(), user);
        message.setBody(messageBody);
        send(message);
    }

}
