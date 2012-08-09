/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.invoice.PaymentMethod;
import com.eprovement.poptavka.shared.domain.adminModule.PaymentMethodDetail;

public final class PaymentMethodConverter extends AbstractConverter<PaymentMethod, PaymentMethodDetail> {

    private PaymentMethodConverter() {
        // Spring instantiates converters - see converters.xml
    }

    @Override
    public PaymentMethodDetail convertToTarget(PaymentMethod source) {
        PaymentMethodDetail detail = new PaymentMethodDetail();

        detail.setId(source.getId());
        detail.setName(source.getName());
        detail.setDescription(source.getDescription());

        return detail;
    }

    @Override
    public PaymentMethod convertToSource(PaymentMethodDetail source) {
        throw new UnsupportedOperationException("Cannot conver from PaymentMethodDetail to PaymentMethod!");
    }


}
