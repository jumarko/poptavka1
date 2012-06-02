/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.rest.demand;

import com.eprovement.poptavka.domain.demand.Demand;
import org.apache.commons.lang.Validate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class DemandDeserializer implements Converter<com.eprovement.poptavka.rest.demand.DemandDto, Demand> {
    @Override
    public Demand convert(com.eprovement.poptavka.rest.demand.DemandDto demandDto) {
        Validate.notNull(demandDto);
        final Demand demand = new Demand();
        demand.setTitle(demand.getTitle());
        demand.setDescription(demand.getDescription());
        demand.setPrice(demand.getPrice());
        demand.setCreatedDate(demand.getCreatedDate());
        demand.setEndDate(demand.getEndDate());

        return demand;
    }


}
