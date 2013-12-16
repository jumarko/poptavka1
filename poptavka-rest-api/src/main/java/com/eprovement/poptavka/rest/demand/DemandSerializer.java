/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.rest.demand;

import static org.springframework.util.Assert.notNull;

import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.rest.category.CategorySerializer;
import com.eprovement.poptavka.rest.client.ClientSerializer;
import com.eprovement.poptavka.rest.locality.LocalitySerializer;
import org.apache.commons.lang.Validate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class DemandSerializer implements Converter<Demand, com.eprovement.poptavka.rest.demand.DemandDto> {

    private final CategorySerializer categorySerializer;
    private final LocalitySerializer localitySerializer;
    private final ClientSerializer clientSerializer;

    public DemandSerializer(CategorySerializer categorySerializer, LocalitySerializer localitySerializer,
                            ClientSerializer clientSerializer) {
        notNull(categorySerializer, "categorySerializer cannot be null!");
        notNull(localitySerializer, "localitySerializer cannot be null!");
        notNull(clientSerializer, "clientSerializer cannot be null!");
        this.categorySerializer = categorySerializer;
        this.localitySerializer = localitySerializer;
        this.clientSerializer = clientSerializer;
    }

    @Override
    public com.eprovement.poptavka.rest.demand.DemandDto convert(Demand demand) {
        Validate.notNull(demand);
        final com.eprovement.poptavka.rest.demand.DemandDto demandDto =
                new com.eprovement.poptavka.rest.demand.DemandDto();
        demandDto.setId(Long.toString(demand.getId()));
        demandDto.setTitle(demand.getTitle());
        demandDto.setStatus(demand.getStatus());
        demandDto.setDescription(demand.getDescription());
        demandDto.setPrice(demand.getPrice());
        demandDto.setCreatedDate(demand.getCreatedDate());
        demandDto.setEndDate(demand.getEndDate());
        if (demand.getClient() != null) {
            demandDto.setClient(clientSerializer.convert(demand.getClient()));
        }
        demandDto.setCategories(categorySerializer.convertCategories(demand.getCategories()));
        demandDto.setLocalities(localitySerializer.convertLocalities(demand.getLocalities()));
        if (demand.getOrigin() != null) {
            demandDto.setOrigin(demand.getOrigin().getCode());
        }
        return demandDto;
    }


}
