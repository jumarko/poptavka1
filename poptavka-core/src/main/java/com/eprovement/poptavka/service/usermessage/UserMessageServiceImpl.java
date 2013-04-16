/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.service.usermessage;

import com.google.common.base.Preconditions;
import com.googlecode.genericdao.search.Search;
import com.eprovement.poptavka.dao.message.MessageFilter;
import com.eprovement.poptavka.dao.usermessage.UserMessageDao;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.message.ClientConversation;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.offer.OfferState;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.GenericServiceImpl;
import com.eprovement.poptavka.util.search.Searcher;
import com.eprovement.poptavka.util.search.SearcherException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ivan.vlcek
 */
public class UserMessageServiceImpl extends GenericServiceImpl<UserMessage, UserMessageDao>
        implements UserMessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserMessageServiceImpl.class);
    private GeneralService generalService;

    public UserMessageServiceImpl(UserMessageDao userMessageDao, GeneralService generalService) {
        setDao(userMessageDao);
        this.generalService = generalService;
    }

    @Override
    @Transactional(readOnly = false)
    public UserMessage createUserMessage(Message message, User user) {
        Validate.notNull(message, "message cannot be null!");
        Validate.notNull(user, "user cannot be null!");
        final UserMessage userMessage = new UserMessage();
        userMessage.setRead(false);
        userMessage.setStarred(false);
        userMessage.setMessage(message);
        userMessage.setUser(user);
        generalService.save(userMessage);
        return userMessage;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserMessage> getUserMessages(List<Message> messages,
            User user, MessageFilter messageFilter) {
        return getDao().getUserMessages(messages, user, messageFilter);
    }

    @Override
    @Transactional(readOnly = true)
    public UserMessage getUserMessage(Message message, User user) {
        return getDao().getUserMessage(message, user);
    }

    /**
     *  {@inheritDoc}
     * <p>
     *     This implementation requires only <code>id</code> attribute of parameter <code>supplier</code> to be filled.
     * </p>
     */
    @Override
    @Transactional(readOnly = true)
    public int getPotentialDemandsCount(BusinessUser supplier) {
        Preconditions.checkNotNull("Supplier must be specified for finding potential demands", supplier);
        Preconditions.checkNotNull("Supplier's user id must be specified for finding potential demands",
                supplier.getId());

//        LOGGER.debug("action=get_potential_demands_supplier status=start supplier{}", supplier);
//        final Search potentialDemandsSearch = new Search(UserMessage.class);
//        potentialDemandsSearch.addFilterEqual("supplier", supplier);
//        potentialDemandsSearch.addFilterEqual("roleType", MessageUserRoleType.TO);
//        potentialDemandsSearch.addFilterEqual("messageContext", MessageContext.POTENTIAL_SUPPLIERS_DEMAND);

        final long potentialDemandsCount = getDao().getPotentialDemandsCount(supplier);
        LOGGER.debug("action=get_potential_demands_supplier status=finish supplier{} potential_demands_size={}",
                supplier, potentialDemandsCount);
        return (int) potentialDemandsCount;
    }

    /**
     *  {@inheritDoc}
     * <p>
     *     This implementation requires only <code>id</code> attribute of parameter <code>supplier</code> to be filled.
     * </p>
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserMessage> getPotentialDemands(BusinessUser supplier) {
        Preconditions.checkNotNull("Supplier must be specified for finding potential demands", supplier);
        Preconditions.checkNotNull("Supplier's user id must be specified for finding potential demands",
                supplier.getId());

//        LOGGER.debug("action=get_potential_demands_supplier status=start supplier{}", supplier);
//        final Search potentialDemandsSearch = new Search(UserMessage.class);
//        potentialDemandsSearch.addFilterEqual("supplier", supplier);
//        potentialDemandsSearch.addFilterEqual("roleType", MessageUserRoleType.TO);
//        potentialDemandsSearch.addFilterEqual("messageContext", MessageContext.POTENTIAL_SUPPLIERS_DEMAND);

        final List<UserMessage> potentialDemands = getDao().getPotentialDemands(supplier);
        LOGGER.debug("action=get_potential_demands_supplier status=finish supplier{} potential_demands_size={}",
                supplier, potentialDemands.size());
        return potentialDemands;
    }

    /**
     *  {@inheritDoc}
     * <p>
     *     This implementation requires only <code>id</code> attribute of parameter <code>supplier</code> to be filled.
     * </p>
     * TODO LATER Backend - ivan: I'm not sure if we need method that retrieves count of search results?
     * If so how should it work? Rework this solution. This will be pain in the ass. We will have to
     * make a count for search result. The best solution would be to move HQLs into Search-es and Filter-s like we have
     * for HomeDemands and HomeSuppliers.
     */
    @Override
    @Transactional(readOnly = true)
    public long getPotentialDemandsCount(BusinessUser supplier, Search search) {
        Preconditions.checkNotNull("Search object must be specified.", search);
        try {
            LOGGER.debug("action=get_potential_demands_supplier_search status=start supplier{}", supplier);
            final List<UserMessage> potentialDemands = Searcher.searchCollection(getPotentialDemands(supplier), search);
            LOGGER.debug("action=get_potential_demands_supplier_search status=start supplier{} "
                    + "potential_demands_size={}", supplier, potentialDemands.size());
            return potentialDemands.size();

        } catch (SearcherException ex) {
            LOGGER.error("action=get_potential_demands_supplier_search status=error supplier{}", ex);
        }
        return 0L;
    }

    /**
     *  {@inheritDoc}
     * <p>
     *     This implementation requires only <code>id</code> attribute of parameter <code>supplier</code> to be filled.
     * </p>
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserMessage> getPotentialDemands(BusinessUser supplier, Search search) {
        Preconditions.checkNotNull("Search object must be specified.", search);
        try {
            LOGGER.debug("action=get_potential_demands_supplier_search status=start supplier{}", supplier);
            final List<UserMessage> potentialDemands = Searcher.searchCollection(getPotentialDemands(supplier), search);
            LOGGER.debug("action=get_potential_demands_supplier_search status=start supplier{} "
                    + "potential_demands_size={}", supplier, potentialDemands.size());
            return potentialDemands;

        } catch (SearcherException ex) {
            LOGGER.error("action=get_potential_demands_supplier_search status=error supplier{}", ex);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserMessage> getInbox(User user) {
        Preconditions.checkNotNull("User must be specified.", user);
        Preconditions.checkNotNull("User's user id must be specified.",
                user.getId());
        return getDao().getInbox(user);
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserMessage> getInbox(User user, Search search) {
        try {
            Preconditions.checkNotNull("Search object must be specified.", search);
            return Searcher.searchCollection(getInbox(user), search);
        } catch (SearcherException ex) {
            LOGGER.error("action=get_user_inbox_messages status=cannot_find", ex);
        }
        return Collections.EMPTY_LIST;
    }


    @Override
    @Transactional(readOnly = true)
    public List<UserMessage> getSentItems(User user) {
        Preconditions.checkNotNull("User must be specified.", user);
        Preconditions.checkNotNull("User's user id must be specified.",
                user.getId());
        return getDao().getSentItems(user);
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserMessage> getSentItems(User user, Search search) {
        try {
            Preconditions.checkNotNull("Search object must be specified.", search);
            return Searcher.searchCollection(getSentItems(user), search);
        } catch (SearcherException ex) {
            LOGGER.error("action=get_sent_items status=cannot_find", ex);
        }
        return Collections.EMPTY_LIST;
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Map<UserMessage, Integer> getSupplierConversationsWithoutOffer(User user) {
        Preconditions.checkNotNull("Supplier specified must not be empty.", user);
        return getDao().getSupplierConversationsWithoutOffer(user);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Map<UserMessage, Integer> getSupplierConversationsWithoutOffer(User user, Search search) {
        return Searcher.searchMapByKeys(getSupplierConversationsWithoutOffer(user), search);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Map<UserMessage, Integer> getSupplierConversationsWithOffer(User user, OfferState pendingState) {
        Preconditions.checkNotNull("Supplier specified must not be empty.", user);

        return getDao().getSupplierConversationsWithOffer(user, pendingState);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Map<UserMessage, Integer> getSupplierConversationsWithOffer(User user, OfferState pendingState,
            Search search) {
        return Searcher.searchMapByKeys(getSupplierConversationsWithOffer(user, pendingState),
                search);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public int getSupplierConversationsWithoutOfferCount(User user) {
        Preconditions.checkNotNull("Supplier specified must not be empty.", user);
        return (int) getDao().getSupplierConversationsWithoutOfferCount(user);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public int getSupplierConversationsWithOfferCount(User user) {
        Preconditions.checkNotNull("Supplier specified must not be empty.", user);
        return (int) getDao().getSupplierConversationsWithOfferCount(user);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Map<UserMessage, Integer> getSupplierConversationsWithAcceptedOffer(User user,
            OfferState offerStateAccepted, OfferState offerStateCompleted) {
        Preconditions.checkNotNull("Supplier specified must not be empty.", user);
        return getDao().getSupplierConversationsWithAcceptedOffer(user, offerStateAccepted,
                offerStateCompleted);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Map<UserMessage, Integer> getSupplierConversationsWithAcceptedOffer(User user,
            OfferState offerStateAccepted, OfferState offerStateCompleted, Search search) {
        return Searcher.searchMapByKeys(getSupplierConversationsWithAcceptedOffer(user, offerStateAccepted,
                offerStateCompleted), search);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Map<UserMessage, Integer> getSupplierConversationsWithClosedDemands(User user, OfferState offerClosed) {
        Preconditions.checkNotNull("User specified must not be empty.", user);
        return getDao().getSupplierConversationsWithClosedDemands(user, offerClosed);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Map<UserMessage, Integer> getSupplierConversationsWithClosedDemands(User user, OfferState offerClosed,
        Search search) {
        return Searcher.searchMapByKeys(getSupplierConversationsWithClosedDemands(user, offerClosed), search);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Map<UserMessage, ClientConversation> getClientConversationsWithoutOffer(
            User user, Message root) {
        return getDao().getClientConversationsWithoutOffer(user, root);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Map<UserMessage, ClientConversation> getClientConversationsWithoutOffer(
            User user, Message root, Search search) {
        return Searcher.searchMapByKeys(getClientConversationsWithoutOffer(user, root), search);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public int getClientConversationsWithoutOfferCount(User user) {
        return (int) getDao().getClientConversationsWithoutOfferCount(user);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public int getClientConversationsWithOfferCount(User user, Demand demand) {
        return (int) getDao().getClientConversationsWithOfferCount(user, demand);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public UserMessage getAdminUserMessage(Message message, User user) {
        UserMessage userMessage = getUserMessage(message, user);
        if (userMessage != null) {
            return userMessage;
        }
        return createUserMessage(message, user);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<User, List<UserMessage>> getUserMessagesFromDateGroupedByUser(Date dateFrom) {
        final Search messageSearch = new Search(UserMessage.class);
        messageSearch.addFilterGreaterOrEqual("message.created", dateFrom);
        final List<UserMessage> userMessagesToBeNotified = generalService.search(messageSearch);
        final HashMap<User, List<UserMessage>> userMessagesGroupedByUser = new HashMap<>();
        for (UserMessage userMessage : userMessagesToBeNotified) {
            final List<UserMessage> userMessages = userMessagesGroupedByUser.containsKey(userMessage.getUser())
                    ? userMessagesGroupedByUser.get(userMessage.getUser())
                    : new ArrayList<UserMessage>();
            userMessages.add(userMessage);
            userMessagesGroupedByUser.put(userMessage.getUser(), userMessages);
        }
        return userMessagesGroupedByUser;
    }
}
