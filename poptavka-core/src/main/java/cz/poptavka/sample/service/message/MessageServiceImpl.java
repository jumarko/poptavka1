package cz.poptavka.sample.service.message;

import cz.poptavka.sample.dao.message.MessageDao;
import cz.poptavka.sample.dao.message.MessageFilter;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.domain.user.User;
import cz.poptavka.sample.service.GenericServiceImpl;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

/**
 * @author Juraj Martinka
 *         Date: 4.5.11
 */
public class MessageServiceImpl extends GenericServiceImpl<Message, MessageDao> implements MessageService {

    public MessageServiceImpl(MessageDao messageDao) {
        setDao(messageDao);
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

    /**
     * Loads all root messages related to <code>client</code>'s demands
     * along with the number of unread messages that span from each of the root
     * demand message.
     *
     * @param client
     * @return
     */
    @Override
    public Map<Message, Long> getListOfClientDemandMessages(User user) {
        return getDao().getListOfClientDemandMessages(user);
    }

    public List<Message> getAllDescendants(Message message) {
        List<Message> messages = new ArrayList();
        messages.add(message);
        return getAllDescendants(messages);

    }

    public List<Message> getAllDescendants(List<Message> messages) {
        return getDao().getAllDescendants(messages);
    }


}
