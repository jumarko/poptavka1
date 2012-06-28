/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.activation.ActivationEmail;
import com.eprovement.poptavka.shared.domain.adminModule.ActivationEmailDetail;

public class ActivationEmailConverter extends AbstractConverter<ActivationEmail, ActivationEmailDetail> {

    @Override
    public ActivationEmailDetail convertToTarget(ActivationEmail source) {
        ActivationEmailDetail detail = new ActivationEmailDetail();

        detail.setId(source.getId());
        detail.setActivationLink(source.getActivationLink());
        detail.setTimeout(convertDate(source.getValidTo()));

        return detail;
    }

    @Override
    public ActivationEmail converToSource(ActivationEmailDetail source) {
        throw new UnsupportedOperationException();
    }
}
