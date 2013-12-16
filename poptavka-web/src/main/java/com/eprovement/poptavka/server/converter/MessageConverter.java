/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.type.MessageType;
import com.google.common.base.Preconditions;

/**
 * Converts Message to MessageDetail.
 * @author Juraj Martinka
 */
public final class MessageConverter extends AbstractConverter<Message, MessageDetail> {

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates MessageConverter.
     */
    private MessageConverter() {
        // Spring instantiates converters - see converters.xml
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public MessageDetail convertToTarget(Message source) {
        Preconditions.checkNotNull(source);
        Preconditions.checkNotNull(source.getParent());

        MessageDetail detail = new MessageDetail();

        //Message
        detail.setMessageId(source.getId());
        if (source.getThreadRoot() != null) {
            detail.setThreadRootId(source.getThreadRoot().getId());
        }
        if (source.getParent() != null) {
            detail.setParentId(source.getParent().getId());
        }
        if (source.getSender() != null) {
            detail.setSenderId(source.getSender().getId());
            detail.setSender(source.getSender().getDisplayName());
        }

        detail.setSubject(source.getSubject());
        detail.setBody(source.getBody());
        detail.setCreated(source.getCreated());

        if (source.getMessageState() != null) {
            detail.setMessageState(source.getMessageState().name());
        }
        detail.setMessageType(MessageType.CONVERSATION.name());

        detail.setCreated(convertDate(source.getCreated()));
        detail.setSent(convertDate(source.getSent()));

        return detail;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Message convertToSource(MessageDetail source) {
        throw new UnsupportedOperationException("Conversion from MessageDetail to domain object Message "
                + "is not implemented yet!");
    }
}
