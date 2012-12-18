/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.activation.ActivationEmail;
import com.eprovement.poptavka.shared.domain.adminModule.ActivationEmailDetail;

public final class ActivationEmailConverter extends AbstractConverter<ActivationEmail, ActivationEmailDetail> {

    private ActivationEmailConverter() {
        // Spring instantiates converters - see converters.xml
    }

    @Override
    public ActivationEmailDetail convertToTarget(ActivationEmail source) {
        ActivationEmailDetail detail = new ActivationEmailDetail();

        detail.setId(source.getId());
        detail.setActivationCode(source.getActivationCode());
        detail.setTimeout(convertDate(source.getValidTo()));

        return detail;
    }

    @Override
    public ActivationEmail convertToSource(ActivationEmailDetail source) {
        throw new UnsupportedOperationException();
    }
}
