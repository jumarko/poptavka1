/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter.admin;

import com.eprovement.poptavka.domain.settings.SystemProperties;
import com.eprovement.poptavka.server.converter.AbstractConverter;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.shared.domain.PropertiesDetail;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Converts Properties to PropertiesDetail and vice versa.
 * @author Martin Slavkovsky
 * @since 1.8.2014
 */
public final class PropertiesConverter extends AbstractConverter<SystemProperties, PropertiesDetail> {

    @Autowired
    private GeneralService generalService;

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates PreferenceConverter.
     */
    private PropertiesConverter() {
        // Spring instantiates converters - see converters.xml
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public PropertiesDetail convertToTarget(SystemProperties source) {
        Validate.notNull(source);

        PropertiesDetail detail = new PropertiesDetail();
        detail.setId(source.getId());
        detail.setKey(source.getKey());
        detail.setValue(source.getValue());
        detail.setTitle(source.getTitle());
        detail.setDescription(source.getDescription());

        return detail;

    }

    /**
     * @{inheritDoc}
     */
    @Override
    public SystemProperties convertToSource(PropertiesDetail target) {
        Validate.notNull(target);

        SystemProperties source = generalService.find(SystemProperties.class, target.getId());
        source.setValue(target.getValue());

        return source;
    }
}
