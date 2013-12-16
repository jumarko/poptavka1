/*
 * Copyright (C) 2013, eProvement s.r.o. All rights reserved.
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
 * This RPC handles all requests for ActionBox module.
 * @author Martin Slavkovsky
 */
@Configurable
public class ActionBoxRPCServiceImpl extends AutoinjectingRemoteService
        implements ActionBoxRPCService {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private GeneralService generalService;
    private UserMessageService userMessageService;

    /**************************************************************************/
    /* Autowired services                                                     */
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
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * Changes 'read' status of sent messages to chosen value.
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
     * Changes 'star' status of sent messages to chosen value.
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