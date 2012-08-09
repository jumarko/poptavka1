/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.UserMessageDetail;
import org.apache.commons.lang.Validate;

public final class UserMessageConverter extends AbstractConverter<UserMessage, UserMessageDetail> {

    private final Converter<Message, MessageDetail> messageConverter;

    private UserMessageConverter(Converter<Message, MessageDetail> messageConverter) {
        // Spring instantiates converters - see converters.xml
        Validate.notNull(messageConverter);
        this.messageConverter = messageConverter;
    }

    @Override
    public UserMessageDetail convertToTarget(UserMessage userMessage) {
        final UserMessageDetail detail = new UserMessageDetail();
        detail.setId(userMessage.getId());
        detail.setRead(userMessage.isRead());
        detail.setStarred(userMessage.isStarred());
        detail.setMessageDetail(messageConverter.convertToTarget(userMessage.getMessage()));
        detail.setSenderEmail(userMessage.getMessage().getSender().getEmail()); //User().getEmail());

        return detail;

    }

    @Override
    public UserMessage convertToSource(UserMessageDetail userMessageDetail) {
        throw new UnsupportedOperationException("Conversion from UserMessageDetail to domain object UserMessage "
                + "is not implemented yet!");
    }
}
