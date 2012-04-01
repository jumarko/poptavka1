/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.service.usermessage;

import cz.poptavka.sample.dao.message.MessageFilter;
import cz.poptavka.sample.dao.usermessage.UserMessageDao;
import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.domain.user.BusinessUser;
import cz.poptavka.sample.domain.user.User;
import cz.poptavka.sample.service.GenericService;
import java.util.List;

/**
 *
 * @author ivan.vlcek
 */
public interface UserMessageService extends GenericService<UserMessage, UserMessageDao> {

    /**
     * Returns a userMessage of given message. UserMessage stores attributes like
     * isRead, isStared
     *
     * @param message
     * @param messageFilter
     * @return
     */
    List<UserMessage> getUserMessages(List<Message> messages,
            User user, MessageFilter messageFilter);

    /**
     * Returns a userMessage to a given message and user.
     * UserMessage stores attributes like isRead, isStared
     *
     * @param message
     * @param messageFilter
     * @return
     */
    UserMessage getUserMessage(Message message, User user);

    /**
     * Load all potential demands for given supplier.
     * Those are all messages which ware sent by the Poptavka system to the given supplier
     * when some client created new Demand suitable for given supplier.
     *
     *
     *
     * @param supplier supplier for which all potential demands are found. It is of type
     *                 {@link cz.poptavka.sample.domain.user.BusinessUser}
     *                 because that is the most specific descendant of {@link User} we can use.
     *                 {@link cz.poptavka.sample.domain.user.Supplier} cannot be used directly (in a simple way)
     *                 because it is NOT a descendant of User.
     *                 Various implementations can have different requirements for this parameter, but in general,
     *                 at least <code>id</code> attribute should be filled
     * @return all potential demands for given <code>supplier</code>
     *
     * @see cz.poptavka.sample.service.message.MessageServiceImpl
     * @see cz.poptavka.sample.service.demand.DemandService#sendDemandsToSuppliers()
     *
     */
    List<UserMessage> getPotentialDemands(BusinessUser supplier);

    /**
     * Gets the user's inbox
     * @param user the user whose inbox to get
     * @return list of <code>UserMesage</code> of the mesages in the user's
     * inbox
     */
    List<UserMessage> getInbox(User user);

    /**
     * Gets the user's sent items folder
     * @param user the user whose sent items to get
     * @return list of <code>UserMesage</code> of the mesages in the user's
     * sent items' folder
     */
    List<UserMessage> getSentItems(User user);
}
