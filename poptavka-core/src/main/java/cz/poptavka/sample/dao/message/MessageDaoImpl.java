package cz.poptavka.sample.dao.message;

import cz.poptavka.sample.dao.GenericHibernateDao;
import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.domain.message.MessageUserRole;
import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.domain.user.User;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 4.5.11
 */
public class MessageDaoImpl extends GenericHibernateDao<Message> implements MessageDao {

    @Override
    public List<Message> getMessageThreads(User user, MessageFilter messageFilter) {
        final Criteria userMessagesCriteria = buildUserMessagesCriteria(user, messageFilter);

        // only messages without parent are considered to be thread roots
        userMessagesCriteria.createCriteria("message").add(Restrictions.isNull("parent"));

        return buildResultCriteria(userMessagesCriteria, messageFilter.getResultCriteria()).list();
    }

    @Override
    public List<Message> getAllMessages(User user, MessageFilter messageFilter) {
        if (messageFilter == null) {
            messageFilter = MessageFilter.EMPTY_FILTER;
        }

        return buildResultCriteria(buildUserMessagesCriteria(user, messageFilter),
                messageFilter.getResultCriteria()).list();
    }


    @Override
    public List<UserMessage> getUserMessages(List<Message> messages, MessageFilter messageFilter) {
        final Criteria userMessageCriteria = buildUserMessageCriteria(messages, messageFilter);
        // TODO ivlcek - remove this method. It will be in a separate
        return buildResultCriteria(userMessageCriteria, messageFilter.getResultCriteria()).list();
    }

    //---------------------------------------------- HELPER METHODS ---------------------------------------------------
    /**
     * Build criterion which can be used for getting all messages of given user restricted by
     * <code>messageFilter</code>.
     *
     * @param user
     * @param messageFilter
     * @return
     */
    private Criteria buildUserMessagesCriteria(User user, MessageFilter messageFilter) {
        final Criteria userMessagesCriteria = getHibernateSession().createCriteria(MessageUserRole.class);
        userMessagesCriteria.add(Restrictions.eq("user", user));
        userMessagesCriteria.setProjection(Projections.property("message"));
        if (messageFilter != null && messageFilter.getMessageUserRoleType() != null) {
            userMessagesCriteria.createCriteria("message").createCriteria("roles")
                    .add(Restrictions.eq("user", user))
                    .add(Restrictions.eq("type", messageFilter.getMessageUserRoleType()));
            if (messageFilter.getMessageContext() != null) {
                userMessagesCriteria.add(Restrictions.eq("messageContext", messageFilter.getMessageContext()));
            }
        }
        return userMessagesCriteria;
    }

    /**
     * Build criterion which can be used for getting a userMessage of given message.
     *
     * @param message
     * @param messageFilter
     * @return
     */
    private Criteria buildUserMessageCriteria(List<Message> messages, MessageFilter messageFilter) {
        final Criteria userMessageCriteria = getHibernateSession().createCriteria(UserMessage.class);
//        userMessageCriteria.add(Restrictions.eq("message", messages));
        userMessageCriteria.add(Restrictions.in("message", messages));
        userMessageCriteria.setProjection(Projections.property("message"));
        return userMessageCriteria;
    }
}
