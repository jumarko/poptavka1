package cz.poptavka.sample.service.message;

import cz.poptavka.sample.dao.message.MessageDao;
import cz.poptavka.sample.dao.message.MessageFilter;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.domain.message.MessageState;
import cz.poptavka.sample.domain.message.MessageUserRole;
import cz.poptavka.sample.domain.message.MessageUserRoleType;
import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.domain.user.User;
import cz.poptavka.sample.exception.MessageCannotBeSentException;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.GenericServiceImpl;
import cz.poptavka.sample.util.strings.ToStringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Juraj Martinka
 *         Date: 4.5.11
 */
public class MessageServiceImpl extends GenericServiceImpl<Message, MessageDao> implements MessageService {
    private GeneralService generalService;

    public MessageServiceImpl(MessageDao messageDao,
            GeneralService generalService) {
        setDao(messageDao);
        this.generalService = generalService;
    }

    @Override
    public List<Message> getMessageThreads(User user, MessageFilter messageFilter) {
        return getDao().getMessageThreads(user, messageFilter);
    }

    @Override
    public List<Message> getAllMessages(User user, MessageFilter messageFilter) {
        return getDao().getAllMessages(user, messageFilter);
    }

    @Override
    public List<UserMessage> getUserMessages(List<Message> messages, MessageFilter messageFilter) {
        return getDao().getUserMessages(messages, messageFilter);
    }

    /** {@inheritDoc} */
    @Override
    public List<Message> getPotentialDemandConversation(Message threadRoot, User supplierUser) {
        return getDao().getPotentialDemandConversation(threadRoot, supplierUser);
    }

    /**
     * Returns message thread root assigned to given demand.
     * @param demand
     * @return
     */
    @Override
    public Message getThreadRootMessage(Demand demand) {
        return getDao().getThreadRootMessage(demand);
    }

    /**
     * Returns offer messages from this threadRoot
     * @param threadRoot
     * @return
     */
    @Override
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
    public List<Message> getPotentialOfferConversation(Message threadRoot, User supplierUser) {
        return getDao().getPotentialOfferConversation(threadRoot, supplierUser);
    }

    /** {@inheritDoc} */
    @Override
    public Map<Message, Long> getListOfClientDemandMessagesAll(User user) {
        return getDao().getListOfClientDemandMessagesAll(user);
    }

    /** {@inheritDoc} */
    @Override
    public Map<Message, Long> getListOfClientDemandMessagesUnread(User user) {
        return getDao().getListOfClientDemandMessagesUnread(user);
    }


    public List<Message> getAllDescendants(Message message) {
        List<Message> messages = new ArrayList();
        messages.add(message);
        return getAllDescendants(messages);

    }

    @Override
    public List<Message> getAllDescendants(List<Message> messages) {
        return getDao().getAllDescendants(messages);
    }

    @Override
    public void send(Message message) throws MessageCannotBeSentException {
        if (message.getMessageState() != MessageState.COMPOSED) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Message [id=").append(ToStringUtils.printId(message));
            sb.append(" cannot be sent because it is NOT in a COMPOSED state.");
            throw new MessageCannotBeSentException(sb.toString());
        }
        if ((message.getRoles() == null) || (message.getRoles().isEmpty())) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Message [id=").append(ToStringUtils.printId(message));
            sb.append(" cannot be sent because there are no recipients set"
                    + "in ROLES.");
            sb.append("Roles: ").append(message.getRoles());
            throw new MessageCannotBeSentException(sb.toString());
        }

        for (MessageUserRole role : message.getRoles()) {
            if ((role.getType() == MessageUserRoleType.TO)
                    || (role.getType() == MessageUserRoleType.CC)
                    || (role.getType() == MessageUserRoleType.BCC)) {
                // we got one of the recipients of the message
                UserMessage userMessage = new UserMessage();
                userMessage.setIsRead(false);
                userMessage.setIsStarred(false);
                userMessage.setMessage(message);
                userMessage.setUser(role.getUser());
                generalService.save(userMessage);
            }
        }
        message.setMessageState(MessageState.SENT);
        message.setLastModified(new Date());
        message.setSent(new Date());
    }


}
