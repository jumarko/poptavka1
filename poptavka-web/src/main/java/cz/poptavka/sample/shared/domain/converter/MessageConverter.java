/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.shared.domain.converter;

import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.shared.domain.message.MessageDetail;

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
