/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.shared.domain.converter;

import cz.poptavka.sample.domain.settings.Preference;
import cz.poptavka.sample.shared.domain.adminModule.PreferenceDetail;

public class PreferenceConverter extends AbstractConverter<Preference, PreferenceDetail> {
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
    public Preference converToSource(PreferenceDetail source) {
        throw new UnsupportedOperationException();
    }
}
