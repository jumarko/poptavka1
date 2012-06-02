/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.shared.domain.converter;

import com.eprovement.poptavka.domain.invoice.OurPaymentDetails;
import com.eprovement.poptavka.shared.domain.adminModule.PaymentDetail;

public class PaymentConverter extends AbstractConverter<OurPaymentDetails, PaymentDetail> {
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

    @Override
    public OurPaymentDetails converToSource(PaymentDetail source) {
        throw new UnsupportedOperationException("Cannot convert from PaymentDetail to OurPaymentDetails");
    }
}
