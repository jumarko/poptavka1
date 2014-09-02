/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.address.Address;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.eprovement.poptavka.shared.domain.LesserBusinessUserDetail;
import org.apache.commons.lang.Validate;

/**
 * Converts BusinessUser to BusinessUserDetail and vice versa.
 * @author Juraj Martinka
 */
public final class LesserBusinessUserConverter extends AbstractConverter<BusinessUser, LesserBusinessUserDetail> {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private final Converter<Address, AddressDetail> addressConverter;

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates BusinessUserConverter.
     */
    private LesserBusinessUserConverter(Converter<Address, AddressDetail> addressConverter) {
        // Spring instantiates converters - see converters.xml
        Validate.notNull(addressConverter);
        this.addressConverter = addressConverter;
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public LesserBusinessUserDetail convertToTarget(BusinessUser source) {
        final LesserBusinessUserDetail detail = new LesserBusinessUserDetail();
        detail.setUserId(source.getId());
        detail.setAddresses(addressConverter.convertToTargetList(source.getAddresses()));
        //BusinessUserData
        if (source.getBusinessUserData() != null) {
            detail.setDisplayName(source.getBusinessUserData().getDisplayName());
        }
        return detail;

    }

    /**
     * @{inheritDoc}
     */
    @Override
    public BusinessUser convertToSource(LesserBusinessUserDetail userDetail) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
