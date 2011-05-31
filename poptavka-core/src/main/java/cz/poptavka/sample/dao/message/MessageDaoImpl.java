package cz.poptavka.sample.dao.message;

import cz.poptavka.sample.dao.GenericHibernateDao;
import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.domain.message.MessageUserRole;
import cz.poptavka.sample.domain.message.MessageUserRoleType;
import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.domain.user.User;
import java.util.ArrayList;
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

    /**
     * Loads conversation between supplier and  client related to potential demand supplier's queries.
     *
     * Juraj tato metoda mi musi vratit vsetky spravy medzi Dodavatelom(parameter User user) a
     * Klientom (ziskas z parametru message.getSender()). Vsetky tieto spravy maju spolocne
     * vlakno messageThread (vstupny parameter message). Snazil som sa tento SQL zostavit cez
     * Criteria ale ako vidis nizsie nepodarilo sa mi to. Chcel som si vyselektovat vsetky
     * spravy s danym threadRoot a s danymi rolami. Tieto role my mali vymedzit 2 typy
     * sprav (
     * 1. vsetky spravy od Dodavatela(SENDER) ku Klientovi(TO) a
     * 2. vsetky spravy od Klienta(SENDER) ku Dodavatelovi(TO). Tymto by som mal ziskat
     * uplne vsetky spravy tohto vlakna obmedzene len na daneho dodavatela a klienta.
     *
     * PS: Tieto role si vytvorim ako objetky ale je treba ich nacitat priamo z DB,
     * aby konecne vytvorenie selectu nehadzalo chybu parameter je null.  ROle si z DB
     * nenacitavam pretoze neviem ako a cela tato metoda sa mi nepaci. Potrebujem tvoju
     * expertizu.
     *
     * @param message
     * @param user
     * @param messageFilter
     * @return
     */
    @Override
    public List<Message> getPotentialDemandConversation(Message message, User user, MessageFilter messageFilter) {
        return buildPotentialDemandConversationCriteria(message, user, MessageFilter.EMPTY_FILTER).list();
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
//        userMessageCriteria.add(Restrictions.eq("user", this));
//        userMessageCriteria.setProjection(Projections.property("message"));
        return userMessageCriteria;
    }

    /**
     * Build criterion which can be used for loading conversation for supplier
     * regarding potential demand conversation with client.
     *
     * @param message
     * @param user
     * @param messageFilter
     * @return
     */
    private Criteria buildPotentialDemandConversationCriteria(
            Message threadRoot, User user, MessageFilter messageFilter) {
        // TODO ivlcek - refactoring with Juraj. This is real mess!
        final Criteria userMessageCriteria = getHibernateSession().createCriteria(Message.class);
        userMessageCriteria.add(Restrictions.eq("threadRoot", threadRoot));
        // From Client role
        MessageUserRole roleFromClient = new MessageUserRole();
        roleFromClient.setUser(threadRoot.getSender());
        roleFromClient.setType(MessageUserRoleType.SENDER);
        // To Supplier role
        MessageUserRole roleToSupplier = new MessageUserRole();
        roleToSupplier.setUser(user);
        roleToSupplier.setType(MessageUserRoleType.TO);
        List<MessageUserRole> rolesFromClientToSupplier = new ArrayList<MessageUserRole>();
        rolesFromClientToSupplier.add(roleToSupplier);
        rolesFromClientToSupplier.add(roleFromClient);

        // From Supplier role
        MessageUserRole roleFromSupplier = new MessageUserRole();
        roleFromSupplier.setUser(user);
        roleFromSupplier.setType(MessageUserRoleType.SENDER);
        // To Client Role
        MessageUserRole roleToClient = new MessageUserRole();
        roleToClient.setUser(threadRoot.getSender());
        roleToClient.setType(MessageUserRoleType.TO);
        List<MessageUserRole> rolesFromSupplierToClient = new ArrayList<MessageUserRole>();
        rolesFromSupplierToClient.add(roleFromSupplier);
        rolesFromSupplierToClient.add(roleToClient);

        userMessageCriteria.add(Restrictions.or(
                Restrictions.eq("roles", rolesFromClientToSupplier),
                Restrictions.eq("roles", rolesFromSupplierToClient)));
//        userMessageCriteria.setProjection(Projections.property("message"));
        return userMessageCriteria;
    }
}
