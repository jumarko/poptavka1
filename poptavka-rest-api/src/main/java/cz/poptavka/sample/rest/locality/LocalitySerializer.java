/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.rest.locality;

import cz.poptavka.sample.domain.address.Locality;
import org.apache.commons.lang.Validate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class LocalitySerializer implements Converter<Locality, LocalityDto> {
    @Override
    public LocalityDto convert(Locality locality) {
        Validate.notNull(locality);

        final LocalityDto localityDto = new LocalityDto();
        localityDto.setCode(locality.getCode());
        localityDto.setName(locality.getName());
        localityDto.setType(locality.getType().toString());

        return localityDto;
    }


}
