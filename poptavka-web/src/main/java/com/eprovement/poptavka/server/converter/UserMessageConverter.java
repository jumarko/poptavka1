/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.shared.domain.message.UserMessageDetail;

public class UserMessageConverter extends AbstractConverter<UserMessage, UserMessageDetail> {
    @Override
    public UserMessageDetail convertToTarget(UserMessage userMessage) {
        final UserMessageDetail detail = new UserMessageDetail();
        detail.setId(userMessage.getId());
        detail.setRead(userMessage.isRead());
        detail.setStarred(userMessage.isStarred());
        detail.setMessageDetail(new MessageConverter().convertToTarget(userMessage.getMessage()));
        detail.setSenderEmail(userMessage.getMessage().getSender().getEmail()); //User().getEmail());

        return detail;

    }

    @Override
    public UserMessage converToSource(UserMessageDetail userMessageDetail) {
        throw new UnsupportedOperationException("Conversion from UserMessageDetail to domain object UserMessage "
                + "is not implemented yet!");
    }
}
