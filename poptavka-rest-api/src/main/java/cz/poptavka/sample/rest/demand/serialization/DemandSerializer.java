/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.rest.demand.serialization;

import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.rest.demand.dto.DemandDto;
import org.apache.commons.lang.Validate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class DemandSerializer implements Converter<Demand, DemandDto> {
    @Override
    public DemandDto convert(Demand demand) {
        Validate.notNull(demand);
        final DemandDto demandDto = new DemandDto();
        demandDto.setTitle(demand.getTitle());
        demandDto.setDescription(demand.getDescription());
        demandDto.setPrice(demand.getPrice());
        demandDto.setCreatedDate(demand.getCreatedDate());
        demandDto.setEndDate(demand.getEndDate());

        return demandDto;
    }
}
