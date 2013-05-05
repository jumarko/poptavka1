/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.dao.usermessage;

import com.eprovement.poptavka.dao.GenericHibernateDao;
import com.eprovement.poptavka.dao.message.MessageFilter;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.eprovement.poptavka.domain.message.ClientConversation;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.offer.OfferState;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.User;
import java.util.ArrayList;
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

    // TODO LATER ivlcek - consider creating UserMessageFilter instead of using MessageFilter
    @Override
    public List<UserMessage> getUserMessages(List<Message> messages, User user, MessageFilter messageFilter) {
        final Criteria userMessageCriteria = buildUserMessageCriteria(messages, user, messageFilter);
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
    public Map<UserMessage, Integer> getSupplierConversationsWithoutOffer(BusinessUser user) {
        return getSupplierConversationsHelper(user,
                "getSupplierConversationsWithoutOffer");
    }

    /** {@inheritDoc} */
    @Override
    public Map<UserMessage, Integer> getSupplierConversationsWithOffer(BusinessUser user, OfferState pendingState) {
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

    public Map<UserMessage, Integer> getSupplierConversationsWithOffer(BusinessUser user) {
        return getSupplierConversationsHelper(user,
                "getSupplierConversationsWithOffer");
    }

    /** {@inheritDoc} */
    @Override
    public long getSupplierConversationsWithOfferCount(BusinessUser user) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("user", user);

        return ((Long) runNamedQueryForSingleResult(
                "getSupplierConversationsWithOfferCount",
                queryParams));
    }

    /** {@inheritDoc} */
    @Override
    public long getSupplierConversationsWithoutOfferCount(BusinessUser user) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("user", user);

        return ((Long) runNamedQueryForSingleResult(
                "getSupplierConversationsWithoutOfferCount",
                queryParams));
    }

    /** {@inheritDoc} */
    @Override
    public Map<UserMessage, Integer> getSupplierConversationsWithAcceptedOffer(BusinessUser user) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("user", user);
        queryParams.put("statusAccepted", OfferStateType.ACCEPTED.getValue());
        queryParams.put("statusCompleted", OfferStateType.COMPLETED.getValue());
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
    public Map<UserMessage, Integer> getSupplierConversationsWithClosedDemands(BusinessUser user) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("user", user);
        queryParams.put("offerStatusClosed", OfferStateType.CLOSED.getValue());

        List<Object[]> unread = runNamedQuery(
                "getSupplierConversationsWithClosedDemands",
                queryParams);
        Map<UserMessage, Integer> unreadMap = new HashMap();
        for (Object[] entry : unread) {
            unreadMap.put((UserMessage) entry[0], ((Long) entry[1]).intValue());
        }
        return unreadMap;
    }

    private Map<UserMessage, Integer> getSupplierConversationsHelper(BusinessUser user,
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
            BusinessUser user, Demand demand) {
        return getClientConversationsHelper(user, demand,
                "getClientConversationsForDemandWithoutOffer", null);
    }

    /** {@inheritDoc} */
    @Override
    public Map<UserMessage, ClientConversation> getClientConversationsWithOffer(
            BusinessUser user, Demand demand) {
        return getClientConversationsHelper(user, demand,
                "getClientConversationsForDemandWithOffer", null);
    }

    /** {@inheritDoc} */
    @Override
    public Map<UserMessage, ClientConversation> getClientConversationsWithOffer(
            BusinessUser user, Demand demand, OfferState offerState) {
        return getClientConversationsHelper(user, demand,
                "getClientConversationsForDemandWithOfferState", offerState);
    }

    private Map<UserMessage, ClientConversation> getClientConversationsHelper(BusinessUser user, Demand demand,
            String queryName, OfferState offerState) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("user", user);
        queryParams.put("demand", demand);
        if (offerState != null) {
            queryParams.put("offerState", offerState);
        }
        List<Object[]> conversations = runNamedQuery(
                queryName,
                queryParams);
        Map<UserMessage, ClientConversation> userMessageMap = new HashMap();
        for (Object[] entry : conversations) {
            userMessageMap.put((UserMessage) entry[0], new ClientConversation(
                    (UserMessage) entry[0], (BusinessUser) entry[1],
                    ((Long) entry[2]).intValue()));
        }
        return userMessageMap;
    }

    /** {@inheritDoc} */
    @Override
    public long getClientConversationsWithoutOfferCount(BusinessUser user, Demand demand) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("user", user);
        queryParams.put("demand", demand);

        return ((Long) runNamedQueryForSingleResult(
                "getClientConversationsForDemandWithoutOfferCount",
                queryParams));
    }

    /** {@inheritDoc} */
    @Override
    public long getClientConversationsWithOfferCount(BusinessUser user, Demand demand) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("user", user);
        queryParams.put("demand", demand);

        return ((Long) runNamedQueryForSingleResult(
                "getClientConversationsForDemandWithOfferCount",
                queryParams));
    }

    /** {@inheritDoc} */
    @Override
    public List<UserMessage> getConversation(User user, User counterparty, Message rootMessage) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("user", user);
        queryParams.put("counterparty", counterparty);
        queryParams.put("rootMessage", rootMessage);

        List<Object[]> conversationRaw = runNamedQuery(
                "getConversationWithCounterparty",
                queryParams);
        List<UserMessage> usermessages = new ArrayList();
        for (Object entry : conversationRaw) {
            usermessages.add((UserMessage) entry);
        }
        return usermessages;
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
