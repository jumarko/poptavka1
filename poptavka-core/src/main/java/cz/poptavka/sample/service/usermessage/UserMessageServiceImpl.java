/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.service.usermessage;

import com.google.common.base.Preconditions;
import com.googlecode.genericdao.search.Search;
import cz.poptavka.sample.dao.message.MessageFilter;
import cz.poptavka.sample.dao.usermessage.UserMessageDao;
import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.domain.message.MessageContext;
import cz.poptavka.sample.domain.message.UserMessageRoleType;
import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.domain.user.BusinessUser;
import cz.poptavka.sample.domain.user.User;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.GenericServiceImpl;
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
        return getUserMessages(supplier, MessageContext.POTENTIAL_SUPPLIERS_DEMAND);
    }


    @Override
    public List<UserMessage> getOffersUserMessages(BusinessUser potentialClient) {
        return getUserMessages(potentialClient, MessageContext.POTENTIAL_CLIENTS_OFFER);
    }

    @Override
    public List<UserMessage> getUserMessages(BusinessUser user, MessageContext messageContext) {
        Preconditions.checkNotNull("Client must be specified", user);
        Preconditions.checkNotNull("Client's user id must be specified for finding offers",
                user.getId());

        final Search potentialDemandsSearch = new Search(UserMessage.class);
        potentialDemandsSearch.addFilterEqual("user.id", user.getId());
        potentialDemandsSearch.addFilterEqual("roleType", UserMessageRoleType.TO);

        potentialDemandsSearch.addFilterEqual("messageContext", messageContext);

        return this.generalService.search(potentialDemandsSearch);
    }
}
