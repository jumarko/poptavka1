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
import com.eprovement.poptavka.domain.message.MessageContext;
import com.eprovement.poptavka.domain.message.MessageUserRoleType;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.GenericServiceImpl;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ivan.vlcek
 */
public class UserMessageServiceImpl extends GenericServiceImpl<UserMessage, UserMessageDao>
        implements UserMessageService {

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
    public List<UserMessage> getPotentialDemands(BusinessUser supplier) {
        Preconditions.checkNotNull("Supplier must be specified for finding potential demands", supplier);
        Preconditions.checkNotNull("Supplier's user id must be specified for finding potential demands",
                supplier.getId());

        final Search potentialDemandsSearch = new Search(UserMessage.class);
        potentialDemandsSearch.addFilterEqual("supplier", supplier);
        potentialDemandsSearch.addFilterEqual("roleType", MessageUserRoleType.TO);
        potentialDemandsSearch.addFilterEqual("messageContext", MessageContext.POTENTIAL_SUPPLIERS_DEMAND);

        return getDao().getPotentialDemands(supplier);
    }

    @Override
    public List<UserMessage> getInbox(User user) {
        Preconditions.checkNotNull("User must be specified.", user);
        Preconditions.checkNotNull("User's user id must be specified.",
                user.getId());
        return getDao().getInbox(user);
    }

    @Override
    public List<UserMessage> getSentItems(User user) {
        Preconditions.checkNotNull("User must be specified.", user);
        Preconditions.checkNotNull("User's user id must be specified.",
                user.getId());
        return getDao().getSentItems(user);
    }

}
