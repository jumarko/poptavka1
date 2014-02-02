/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.type.MessageType;
import com.google.common.base.Preconditions;

/**
 * Converts UserMessage to MessageDetail.
 * @author Juraj Martinka
 */
public final class UserMessageConverter extends AbstractConverter<UserMessage, MessageDetail> {

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates UserMessageConverter.
     */
    private UserMessageConverter() {
        // Spring instantiates converters - see converters.xml
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public MessageDetail convertToTarget(UserMessage source) {
        Preconditions.checkNotNull(source);
        Preconditions.checkNotNull(source.getMessage());

        MessageDetail detail = new MessageDetail();
        //User message
        detail.setRead(source.isRead());
        detail.setStarred(source.isStarred());
        detail.setUserMessageId(source.getId());
        detail.setMessagesCount(0);

        //Message
        detail.setMessageId(source.getMessage().getId());
        if (source.getMessage().getThreadRoot() != null) {
            detail.setThreadRootId(source.getMessage().getThreadRoot().getId());
        }
        if (source.getMessage().getParent() != null) {
            detail.setParentId(source.getMessage().getParent().getId());
        }
        if (source.getMessage().getSender() != null) {
            detail.setSenderId(source.getMessage().getSender().getId());
            detail.setSender(source.getMessage().getSender().getDisplayName());
        }

        detail.setSubject(source.getMessage().getSubject());
        detail.setBody(source.getMessage().getBody());
        detail.setCreated(source.getMessage().getCreated());

        if (source.getMessage().getMessageState() != null) {
            detail.setMessageState(source.getMessage().getMessageState().name());
        }
        detail.setMessageType(MessageType.CONVERSATION.name());

        detail.setCreated(convertDate(source.getMessage().getCreated()));
        detail.setSent(convertDate(source.getMessage().getSent()));


        return detail;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public UserMessage convertToSource(MessageDetail messageDetail) {
        throw new UnsupportedOperationException("Conversion from UserMessageDetail to domain object UserMessage "
                + "is not implemented yet!");
    }
}
