/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.rest.locality;

import com.eprovement.poptavka.domain.address.Locality;
import org.apache.commons.lang.Validate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class LocalitySerializer implements Converter<Locality, LocalityDto> {
    @Override
    public LocalityDto convert(Locality locality) {
        Validate.notNull(locality);

        final LocalityDto localityDto = new LocalityDto();
        localityDto.setId(locality.getId());
        localityDto.setName(locality.getName());
        localityDto.setType(locality.getType().toString());

        return localityDto;
    }


}
