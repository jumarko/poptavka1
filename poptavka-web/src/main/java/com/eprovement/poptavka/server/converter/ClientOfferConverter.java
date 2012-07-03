/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.shared.domain.demandsModule.ClientOfferDetail;

public final class ClientOfferConverter extends AbstractConverter<UserMessage, ClientOfferDetail> {

    private ClientOfferConverter() {
        // Spring instantiates converters - see converters.xml
    }

    @Override
    public ClientOfferDetail convertToTarget(UserMessage userMessage) {
        ClientOfferDetail detail = new ClientOfferDetail();
        //TODO Martin
        return detail;
    }

    @Override
    public UserMessage converToSource(ClientOfferDetail clientOfferDetail) {
        throw new UnsupportedOperationException("Conversion from ClientOfferDetail to domain object UserMessage "
                + "is not implemented yet!");
    }
}
