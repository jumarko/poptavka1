/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.shared.domain.converter;

import cz.poptavka.sample.domain.invoice.PaymentMethod;
import cz.poptavka.sample.shared.domain.adminModule.PaymentMethodDetail;

public class PaymentMethodConverter extends AbstractConverter<PaymentMethod, PaymentMethodDetail> {

    @Override
    public PaymentMethodDetail convertToTarget(PaymentMethod source) {
        PaymentMethodDetail detail = new PaymentMethodDetail();

        detail.setId(source.getId());
        detail.setName(source.getName());
        detail.setDescription(source.getDescription());

        return detail;
    }

    @Override
    public PaymentMethod converToSource(PaymentMethodDetail source) {
        throw new UnsupportedOperationException("Cannot conver from PaymentMethodDetail to PaymentMethod!");
    }


}