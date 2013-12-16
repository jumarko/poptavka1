/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.activation.ActivationEmail;
import com.eprovement.poptavka.shared.domain.adminModule.ActivationEmailDetail;

/**
 * Converts ActivationEmail to ActivationEmailDetail
 * @author Juraj Martinka
 */
public final class ActivationEmailConverter extends AbstractConverter<ActivationEmail, ActivationEmailDetail> {

    /**
     * Creates ActivationEmailConverter.
     */
    private ActivationEmailConverter() {
        // Spring instantiates converters - see converters.xml
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public ActivationEmailDetail convertToTarget(ActivationEmail source) {
        ActivationEmailDetail detail = new ActivationEmailDetail();

        detail.setId(source.getId());
        detail.setActivationCode(source.getActivationCode());
        detail.setTimeout(convertDate(source.getValidTo()));

        return detail;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public ActivationEmail convertToSource(ActivationEmailDetail source) {
        throw new UnsupportedOperationException();
    }
}
