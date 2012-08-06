/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.service.usermessage;

import com.google.common.base.Preconditions;
import com.googlecode.genericdao.search.Search;
import com.eprovement.poptavka.dao.message.MessageFilter;
import com.eprovement.poptavka.dao.usermessage.UserMessageDao;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.enums.MessageContext;
import com.eprovement.poptavka.domain.enums.MessageUserRoleType;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.GenericServiceImpl;
import com.eprovement.poptavka.util.search.Searcher;
import com.eprovement.poptavka.util.search.SearcherException;
import java.util.Collections;
import java.util.List;
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
    public List<UserMessage> getPotentialDemands(BusinessUser supplier) {
        Preconditions.checkNotNull("Supplier must be specified for finding potential demands", supplier);
        Preconditions.checkNotNull("Supplier's user id must be specified for finding potential demands",
                supplier.getId());

        LOGGER.debug("action=get_potential_demands_supplier status=start supplier{}", supplier);
        final Search potentialDemandsSearch = new Search(UserMessage.class);
        potentialDemandsSearch.addFilterEqual("supplier", supplier);
        potentialDemandsSearch.addFilterEqual("roleType", MessageUserRoleType.TO);
        potentialDemandsSearch.addFilterEqual("messageContext", MessageContext.POTENTIAL_SUPPLIERS_DEMAND);

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

}
