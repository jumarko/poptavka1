/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import org.apache.commons.lang.Validate;

public final class MessageDetailFromUserMessageConverter extends AbstractConverter<UserMessage, MessageDetail> {

    private final Converter<Message, MessageDetail> messageConverter;

    private MessageDetailFromUserMessageConverter(Converter<Message, MessageDetail> messageConverter) {
        // Spring instantiates converters - see converters.xml
        Validate.notNull(messageConverter);

        this.messageConverter = messageConverter;
    }

    @Override
    public MessageDetail convertToTarget(UserMessage userMessage) {
        final MessageDetail messageDetail = messageConverter.convertToTarget(userMessage.getMessage());
        messageDetail.setRead(userMessage.isRead());
        messageDetail.setStarred(userMessage.isStarred());
        // TODO: wtf?
        messageDetail.setMessageCount(messageDetail.getMessageCount());
        messageDetail.setUserMessageId(userMessage.getId());
        // TODO: wtf?
        messageDetail.setUnreadSubmessages(messageDetail.getUnreadSubmessages());
        return messageDetail;
    }

    @Override
    public UserMessage converToSource(MessageDetail messageDetail) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


}
