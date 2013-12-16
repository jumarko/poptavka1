/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.server.service.actionBox;

import com.eprovement.poptavka.client.service.demand.ActionBoxRPCService;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.usermessage.UserMessageService;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Martin Slavkovsky
 */
@Configurable
public class ActionBoxRPCServiceImpl extends AutoinjectingRemoteService
        implements ActionBoxRPCService {

    private GeneralService generalService;
    private UserMessageService userMessageService;

    /**************************************************************************/
    /* Autowired methods                                                      */
    /**************************************************************************/
    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    @Autowired
    public void setUserMessageService(UserMessageService userMessageService) {
        this.userMessageService = userMessageService;
    }

    /**************************************************************************/
    /* Messages methods                                                       */
    /**************************************************************************/
    /**
     * Change 'read' status of sent messages to chosen value.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void setMessageReadStatus(List<Long> userMessageIds, boolean isRead) throws RPCException {
        for (Long userMessageId : userMessageIds) {
            UserMessage userMessage = this.generalService.find(UserMessage.class, userMessageId);
            userMessage.setRead(isRead);
            this.userMessageService.update(userMessage);
        }
    }

    /**
     * COMMON. Change 'star' status of sent messages to chosen value
     */
    @Override
    public void setMessageStarStatus(List<Long> userMessageIds, boolean isStarred) throws RPCException {
        for (Long userMessageId : userMessageIds) {
            UserMessage userMessage = userMessageService.getById(userMessageId);
            userMessage.setStarred(isStarred);
            this.userMessageService.update(userMessage);
        }
    }
}