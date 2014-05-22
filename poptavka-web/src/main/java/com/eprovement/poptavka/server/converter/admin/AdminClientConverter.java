/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter.admin;

import com.eprovement.poptavka.domain.common.Origin;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.server.converter.AbstractConverter;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.shared.domain.adminModule.AdminClientDetail;
import com.eprovement.poptavka.shared.domain.demand.OriginDetail;
import com.google.common.base.Preconditions;

/**
 * Converts Client to FullClientDetail and vice versa.
 * @author Juraj Martinka
 */
public final class AdminClientConverter extends AbstractConverter<Client, AdminClientDetail> {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private Converter<Origin, OriginDetail> originConverter;

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates ClientConverter.
     */
    private AdminClientConverter(Converter<Origin, OriginDetail> originConverter) {
        // Spring instantiates converters - see converters.xml
        this.originConverter = originConverter;
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public AdminClientDetail convertToTarget(Client source) {
        Preconditions.checkArgument(source != null, "Client cannot be null!");

        AdminClientDetail detail = new AdminClientDetail();
        detail.setId(source.getId());
        detail.setUserId(source.getBusinessUser().getId());
        detail.setCreated(source.getBusinessUser().getCreated());
        detail.setEmail(source.getBusinessUser().getEmail());
        detail.setFirstName(source.getBusinessUser().getBusinessUserData().getPersonFirstName());
        detail.setLastName(source.getBusinessUser().getBusinessUserData().getPersonLastName());
        detail.setOrigin(originConverter.convertToTarget(source.getBusinessUser().getOrigin()));

        return detail;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Client convertToSource(AdminClientDetail source) {
        throw new UnsupportedOperationException("Conversion from AdminClientDetail to Client isnt' yet implemented.");
    }
}
