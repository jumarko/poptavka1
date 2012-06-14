/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.rest.demand;

import com.eprovement.poptavka.domain.demand.Demand;
import org.apache.commons.lang.Validate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class DemandSerializer implements Converter<Demand, com.eprovement.poptavka.rest.demand.DemandDto> {
    @Override
    public com.eprovement.poptavka.rest.demand.DemandDto convert(Demand demand) {
        Validate.notNull(demand);
        final com.eprovement.poptavka.rest.demand.DemandDto demandDto =
                new com.eprovement.poptavka.rest.demand.DemandDto();
        demandDto.setTitle(demand.getTitle());
        demandDto.setDescription(demand.getDescription());
        demandDto.setPrice(demand.getPrice());
        demandDto.setCreatedDate(demand.getCreatedDate());
        demandDto.setEndDate(demand.getEndDate());

        return demandDto;
    }


}
