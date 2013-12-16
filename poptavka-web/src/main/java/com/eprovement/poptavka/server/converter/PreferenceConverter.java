/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.settings.Preference;
import com.eprovement.poptavka.shared.domain.adminModule.PreferenceDetail;

/**
 * Converts Preference to PreferenceDetail.
 * @author Juraj Martinka
 */
public final class PreferenceConverter extends AbstractConverter<Preference, PreferenceDetail> {

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates PreferenceConverter.
     */
    private PreferenceConverter() {
        // Spring instantiates converters - see converters.xml
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public PreferenceDetail convertToTarget(Preference source) {
        PreferenceDetail detail = new PreferenceDetail();

        detail.setId(source.getId());
        detail.setKey(source.getKey());
        detail.setValue(source.getValue());
        detail.setDescription(source.getDescription());

        return detail;

    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Preference convertToSource(PreferenceDetail source) {
        throw new UnsupportedOperationException();
    }
}
