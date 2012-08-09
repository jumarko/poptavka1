/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.type.MessageType;

public final class MessageConverter extends AbstractConverter<Message, MessageDetail> {

    private MessageConverter() {
        // Spring instantiates converters - see converters.xml
    }

    @Override
    public MessageDetail convertToTarget(Message source) {
        MessageDetail detail = new MessageDetail();
        detail.setMessageId(source.getId());
        detail.setParentId(source.getParent() == null ? -1 : source.getParent().getId());
        detail.setSenderId(source.getSender() == null ? -1 : source.getSender().getId());
        detail.setThreadRootId(source.getThreadRoot() == null ? -1 : source.getThreadRoot().getId());
        detail.setBody(source.getBody());
        detail.setCreated(source.getCreated());
        //still get annoying nullPE at PotentialDemandsource
        //so that's the reason for this check    -Beho. 29.11.11

//        m.setFirstBornId(serialVersionUID);
        if (source.getMessageState() != null) {
            detail.setMessageState(source.getMessageState().name());
        }
        detail.setMessageType(MessageType.CONVERSATION.name());
//        m.setNexSiblingId(serialVersionUID);
//        m.setReceiverId();

        detail.setCreated(convertDate(source.getCreated()));
        detail.setSent(convertDate(source.getSent()));
        detail.setSubject(source.getSubject());

        return detail;
    }

    @Override
    public Message convertToSource(MessageDetail source) {
        throw new UnsupportedOperationException("Conversion from MessageDetail to domain object Message "
                + "is not implemented yet!");
    }
}
