/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.dao.usermessage;

import cz.poptavka.sample.dao.GenericDao;
import cz.poptavka.sample.dao.message.MessageFilter;
import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.domain.user.BusinessUser;
import java.util.List;

/**
 *
 * @author ivan.vlcek
 */
public interface UserMessageDao extends GenericDao<UserMessage> {

    /**
     * Returns a userMessage of given message. UserMessage stores attributes like
     * isRead, isStared
     *
     * @param message
     * @param messageFilter
     * @return
     */
    List<UserMessage> getUserMessages(List<Message> messages,
            BusinessUser businessUser, MessageFilter messageFilter);
}