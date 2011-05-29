/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.service.usermessage;

import cz.poptavka.sample.dao.usermessage.UserMessageDao;
import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.service.GenericServiceImpl;
import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.dao.message.MessageFilter;


import java.util.List;

/**
 *
 * @author ivan.vlcek
 */
public class UserMessageServiceImpl extends GenericServiceImpl<UserMessage, UserMessageDao>
        implements UserMessageService {

    public UserMessageServiceImpl(UserMessageDao userMessageDao) {
        setDao(userMessageDao);
    }

    @Override
    public List<UserMessage> getUserMessages(List<Message> messages, MessageFilter messageFilter) {
        return getDao().getUserMessages(messages, messageFilter);
    }
}
