/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.dao.usermessage;

import com.eprovement.poptavka.dao.GenericDao;
import com.eprovement.poptavka.dao.message.MessageFilter;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.User;
import java.util.List;

/**
 *
 * @author ivan.vlcek
 */
public interface UserMessageDao extends GenericDao<UserMessage> {

    /**
     * Returns a userMessage of given message. UserMessage stores attributes like
     * read, starred
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
     * Gets the user's inbox
     * @param user the user whose inbox to get
     * @return list of <code>UserMesage</code> of the mesages in the user's
     * inbox
     */
    List<UserMessage> getInbox(User user);

    /**
     * Gets the user's outbox
     * @param user the user whose inbox to get
     * @return list of <code>UserMesage</code> of the mesages in the user's
     * outbox
     */
    List<UserMessage> getSentItems(User user);

    /**
     * Get's all the user's valid potential demand messages count
     * @param supplier the supplier to retreive the demand messages for
     * @return count of <code>UserMesage</code> of the potential demand mesages
     */
    long getPotentialDemandsCount(BusinessUser supplier);

    /**
     * Get's all the user's valid potential demand messages
     * @param supplier the supplier to retreive the demand messages for
     * @return list of <code>UserMesage</code> of the potential demand mesages
     */
    List<UserMessage> getPotentialDemands(BusinessUser supplier);

}
