/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.invoice.OurPaymentDetails;
import com.eprovement.poptavka.shared.domain.adminModule.PaymentDetail;

/**
 * Converts OurPaymentDetails to PaymentDetail.
 * @author Juraj Martinka
 */
public final class PaymentConverter extends AbstractConverter<OurPaymentDetails, PaymentDetail> {

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates PaymentConverter.
     */
    private PaymentConverter() {
        // Spring instantiates converters - see converters.xml
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public PaymentDetail convertToTarget(OurPaymentDetails source) {
        PaymentDetail detail = new PaymentDetail();

        detail.setId(source.getId());
        detail.setBankAccount(source.getBankAccount());
        detail.setBankCode(source.getBankCode());
        detail.setCity(source.getCity());
        detail.setCountryVat(source.getCountryVat());
        detail.setEmail(source.getEmail());
        detail.setIban(source.getIban());
        detail.setIdentificationNumber(source.getIdentificationNumber());
        detail.setPhone(source.getPhone());
        detail.setStreet(source.getStreet());
        detail.setSwiftCode(source.getSwiftCode());
        detail.setTaxId(source.getTaxId());
        detail.setTitle(source.getTitle());
        detail.setZipCode(source.getZipCode());

        return detail;

    }

    /**
     * @{inheritDoc}
     */
    @Override
    public OurPaymentDetails convertToSource(PaymentDetail source) {
        throw new UnsupportedOperationException("Cannot convert from PaymentDetail to OurPaymentDetails");
    }
}
