/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.invoice.PaymentMethod;
import com.eprovement.poptavka.shared.domain.adminModule.PaymentMethodDetail;

/**
 * Converts PaymentMethod to PaymentMethodDetail.
 * @author Juraj Martinka
 */
public final class PaymentMethodConverter extends AbstractConverter<PaymentMethod, PaymentMethodDetail> {

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates PaymentMethodConverter.
     */
    private PaymentMethodConverter() {
        // Spring instantiates converters - see converters.xml
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public PaymentMethodDetail convertToTarget(PaymentMethod source) {
        PaymentMethodDetail detail = new PaymentMethodDetail();

        detail.setId(source.getId());
        detail.setName(source.getName());
        detail.setDescription(source.getDescription());

        return detail;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public PaymentMethod convertToSource(PaymentMethodDetail source) {
        throw new UnsupportedOperationException("Cannot conver from PaymentMethodDetail to PaymentMethod!");
    }


}
