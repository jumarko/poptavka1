/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.shared.domain.demandsModule.PotentialDemandDetail;

public class PotentialDemandConverter extends AbstractConverter<UserMessage, PotentialDemandDetail> {

    private final DemandTypeConverter demandTypeConverter = new DemandTypeConverter();

    @Override
    public PotentialDemandDetail convertToTarget(UserMessage userMessage) {
        final PotentialDemandDetail detail = new PotentialDemandDetail();
        detail.setUserMessageId(userMessage.getId());
        detail.setRead(userMessage.isRead());
        detail.setStarred(userMessage.isStarred());
        if (userMessage.getMessage() != null) {
            detail.setMessageId(userMessage.getMessage().getId());
            if (userMessage.getMessage().getDemand() != null) {
                detail.setDemandId(userMessage.getMessage().getDemand().getId());
                detail.setTitle(userMessage.getMessage().getDemand().getTitle());
                detail.setPrice(userMessage.getMessage().getDemand().getPrice());
                detail.setDemandType(demandTypeConverter.convertToTarget(
                        userMessage.getMessage().getDemand().getType()));
                detail.setDemandStatus(userMessage.getMessage().getDemand().getStatus());
                detail.setCreatedDate(convertDate(userMessage.getMessage().getCreated()));
                detail.setEndDate(convertDate(userMessage.getMessage().getDemand().getEndDate()));
                detail.setValidToDate(convertDate(userMessage.getMessage().getDemand().getValidTo()));
                if (userMessage.getMessage().getDemand().getClient() != null) {
                    detail.setRating(userMessage.getMessage().getDemand().getClient().getOveralRating());
                    if (userMessage.getMessage().getDemand().getClient().getBusinessUser() != null
                            && userMessage.getMessage().getDemand().getClient()
                            .getBusinessUser().getBusinessUserData() != null) {
                        detail.setSender(userMessage.getMessage().getDemand().getClient()
                                .getBusinessUser().getBusinessUserData().getDisplayName());
                    }
                }
            }
        }

        return detail;

    }

    @Override
    public UserMessage converToSource(PotentialDemandDetail potentialDemandDetail) {
        throw new UnsupportedOperationException("Conversion from PotentialDemandDetail to domain object UserMessage "
                + "is not implemented yet!");
    }
}
