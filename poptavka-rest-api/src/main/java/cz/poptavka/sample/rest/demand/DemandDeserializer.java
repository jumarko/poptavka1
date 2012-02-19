/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.rest.demand;

import cz.poptavka.sample.domain.demand.Demand;
import org.apache.commons.lang.Validate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class DemandDeserializer implements Converter<cz.poptavka.sample.rest.demand.DemandDto, Demand> {
    @Override
    public Demand convert(cz.poptavka.sample.rest.demand.DemandDto demandDto) {
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
