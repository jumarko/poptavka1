/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.settings.Preference;
import com.eprovement.poptavka.shared.domain.adminModule.PreferenceDetail;

public final class PreferenceConverter extends AbstractConverter<Preference, PreferenceDetail> {

    private PreferenceConverter() {
        // Spring instantiates converters - see converters.xml
    }

    @Override
    public PreferenceDetail convertToTarget(Preference source) {
        PreferenceDetail detail = new PreferenceDetail();

        detail.setId(source.getId());
        detail.setKey(source.getKey());
        detail.setValue(source.getValue());
        detail.setDescription(source.getDescription());

        return detail;

    }

    @Override
    public Preference convertToSource(PreferenceDetail source) {
        throw new UnsupportedOperationException();
    }
}
