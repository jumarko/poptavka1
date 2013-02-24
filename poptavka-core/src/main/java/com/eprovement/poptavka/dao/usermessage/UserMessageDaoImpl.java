/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.dao.usermessage;

import com.eprovement.poptavka.dao.GenericHibernateDao;
import com.eprovement.poptavka.dao.message.MessageFilter;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.message.ClientConversation;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.offer.OfferState;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.User;
import java.util.HashMap;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Map;

/**
 *
 * @author ivan.vlcek
 */
public class UserMessageDaoImpl extends GenericHibernateDao<UserMessage> implements UserMessageDao {

    // TODO ivlcek - maybe I will have to replace MessageFilter by UserMessageFiletr
    @Override
    public List<UserMessage> getUserMessages(List<Message> messages, User user, MessageFilter messageFilter) {
        final Criteria userMessageCriteria = buildUserMessageCriteria(messages, user, messageFilter);
        // TODO ivlcek - remove this method. It will be in a separate
        return buildResultCriteria(userMessageCriteria, messageFilter.getResultCriteria()).list();
    }

    @Override
    public UserMessage getUserMessage(Message message, User user) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("user", user);
        queryParams.put("message", message);

        List<UserMessage> userMessages = runNamedQuery("getUserMesage", queryParams);
        if (userMessages.isEmpty()) {
            return null;
        } else {
            return userMessages.get(0);
        }
    }

    @Override
    public List<UserMessage> getInbox(User user) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("user", user);

        return runNamedQuery("getInbox", queryParams);
    }

    @Override
    public List<UserMessage> getSentItems(User user) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("user", user);

        return runNamedQuery("getSentItems", queryParams);
    }

    @Override
    public long getPotentialDemandsCount(BusinessUser supplier) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("supplier", supplier);

        return (Long) runNamedQueryForSingleResult("getPotentialDemandsCount", queryParams);
    }

    @Override
    public List<UserMessage> getPotentialDemands(BusinessUser supplier) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("supplier", supplier);

        return runNamedQuery("getPotentialDemands", queryParams);
    }

    /** {@inheritDoc} */
    @Override
    public Map<UserMessage, Integer> getSupplierConversationsWithoutOffer(User user) {
        return getSupplierConversationsHelper(user,
                "getSupplierConversationsWithoutOffer");
    }

    /** {@inheritDoc} */
    @Override
    public Map<UserMessage, Integer> getSupplierConversationsWithOffer(User user, OfferState pendingState) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("user", user);
        queryParams.put("pendingState", pendingState);

        List<Object[]> unread = runNamedQuery(
                "getSupplierConversationsWithOffer",
                queryParams);
        Map<UserMessage, Integer> unreadMap = new HashMap();
        for (Object[] entry : unread) {
            unreadMap.put((UserMessage) entry[0], ((Long) entry[1]).intValue());
        }
        return unreadMap;
    }

    public Map<UserMessage, Integer> getSupplierConversationsWithOffer(User user) {
        return getSupplierConversationsHelper(user,
                "getSupplierConversationsWithOffer");
    }

    /** {@inheritDoc} */
    @Override
    public long getSupplierConversationsWithOfferCount(User user) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("user", user);

        return ((Long) runNamedQueryForSingleResult(
                "getSupplierConversationsWithOfferCount",
                queryParams));
    }

    /** {@inheritDoc} */
    @Override
    public long getSupplierConversationsWithoutOfferCount(User user) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("user", user);

        return ((Long) runNamedQueryForSingleResult(
                "getSupplierConversationsWithoutOfferCount",
                queryParams));
    }

    /** {@inheritDoc} */
    @Override
    public Map<UserMessage, Integer> getSupplierConversationsWithAcceptedOffer(User user,
        OfferState offerStateAccepted, OfferState offerStateCompleted) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("user", user);
        queryParams.put("statusAccepted", offerStateAccepted);
        queryParams.put("statusCompleted", offerStateCompleted);
        queryParams.put("pendingCompletionStatus", DemandStatus.PENDINGCOMPLETION);

        List<Object[]> unread = runNamedQuery(
                "getSupplierConversationsWithAcceptedOffer",
                queryParams);
        Map<UserMessage, Integer> unreadMap = new HashMap();
        for (Object[] entry : unread) {
            unreadMap.put((UserMessage) entry[0], ((Long) entry[1]).intValue());
        }
        return unreadMap;
    }

    /** {@inheritDoc} */
    @Override
    public Map<UserMessage, Integer> getSupplierConversationsWithClosedDemands(User user) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("user", user);
        queryParams.put("closedStatus", DemandStatus.CLOSED);

        List<Object[]> unread = runNamedQuery(
                "getSupplierConversationsWithClosedDemands",
                queryParams);
        Map<UserMessage, Integer> unreadMap = new HashMap();
        for (Object[] entry : unread) {
            unreadMap.put((UserMessage) entry[0], ((Long) entry[1]).intValue());
        }
        return unreadMap;
    }

    private Map<UserMessage, Integer> getSupplierConversationsHelper(User user,
            String queryName) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("user", user);

        List<Object[]> unread = runNamedQuery(
                queryName,
                queryParams);
        Map<UserMessage, Integer> conversationMap = new HashMap();
        for (Object[] entry : unread) {
            conversationMap.put((UserMessage) entry[0], ((Long) entry[1]).intValue());
        }
        return conversationMap;
    }

    /** {@inheritDoc} */
    @Override
    public Map<UserMessage, ClientConversation> getClientConversationsWithoutOffer(
            User user, Message root) {
        return getClientConversationsHelper(user, root,
                "getClientConversationsWithoutOffer");
    }

    private Map<UserMessage, ClientConversation> getClientConversationsHelper(User user, Message root,
            String queryName) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("user", user);
        queryParams.put("root", root);
        List<Object[]> conversations = runNamedQuery(
                queryName,
                queryParams);
        Map<UserMessage, ClientConversation> userMessageMap = new HashMap();
        for (Object[] entry : conversations) {
            userMessageMap.put((UserMessage) entry[0], new ClientConversation(
                    (UserMessage) entry[0], (User) entry[1],
                    ((Long) entry[2]).intValue()));
        }
        return userMessageMap;
    }

    /** {@inheritDoc} */
    @Override
    public long getClientConversationsWithoutOfferCount(User user) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("user", user);

        return ((Long) runNamedQueryForSingleResult(
                "getClientConversationsWithoutOfferCount",
                queryParams));
    }

    /** {@inheritDoc} */
    @Override
    public long getClientConversationsWithOfferCount(User user, Demand demand) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("user", user);
        queryParams.put("demand", demand);

        return ((Long) runNamedQueryForSingleResult(
                "getClientConversationsWithOfferCount",
                queryParams));
    }


    //---------------------------------------------- HELPER METHODS ---------------------------------------------------
    /**
     * Build criterion which can be used for getting a userMessage of given message.
     *
     * @param message
     * @param messageFilter
     * @return
     */
    private Criteria buildUserMessageCriteria(List<Message> messages, User user, MessageFilter messageFilter) {
        final Criteria userMessageCriteria = getHibernateSession().createCriteria(UserMessage.class);
//        userMessageCriteria.add(Restrictions.eq("message", messages));
        userMessageCriteria.add(Restrictions.eq("user", user));
        if (CollectionUtils.isNotEmpty(messages)) {
            userMessageCriteria.add(Restrictions.in("message", messages));
        }


//        userMessageCriteria.setProjection(Projections.property("message"));
        return userMessageCriteria;
    }
}
