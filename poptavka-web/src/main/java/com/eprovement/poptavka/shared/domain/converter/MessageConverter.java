/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.shared.domain.converter;

import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;

public class MessageConverter extends AbstractConverter<Message, MessageDetail> {
    @Override
    public MessageDetail convertToTarget(Message source) {
        return MessageDetail.fillMessageDetail(new MessageDetail(), source);
    }

    @Override
    public Message converToSource(MessageDetail source) {
        throw new UnsupportedOperationException();
    }
}
