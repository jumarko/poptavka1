package com.eprovement.poptavka.rest.demand;

import static org.apache.commons.lang.Validate.notNull;

import com.eprovement.poptavka.domain.common.Origin;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.rest.client.ClientDeserializer;
import com.eprovement.poptavka.rest.common.serializer.CategoryDeserializer;
import com.eprovement.poptavka.rest.common.serializer.LocalityDeserializer;
import com.eprovement.poptavka.service.register.RegisterService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class DemandDeserializer implements Converter<com.eprovement.poptavka.rest.demand.DemandDto, Demand> {

    private final ClientDeserializer clientDeserializer;
    private final CategoryDeserializer categoryDeserializer;
    private final LocalityDeserializer localityDeserializer;
    private final RegisterService registerService;

    @Autowired
    public DemandDeserializer(ClientDeserializer clientDeserializer,
                              CategoryDeserializer categoryDeserializer, LocalityDeserializer localityDeserializer,
                              RegisterService registerService) {
        notNull(clientDeserializer, "clientDeserializer cannot be null!");
        notNull(categoryDeserializer, "categoryDeserializer cannot be null!");
        notNull(localityDeserializer, "localityDeserializer cannot be null!");
        notNull(registerService, "registerService cannot be null!");
        this.clientDeserializer = clientDeserializer;
        this.categoryDeserializer = categoryDeserializer;
        this.localityDeserializer = localityDeserializer;
        this.registerService = registerService;
    }

    @Override
    public Demand convert(com.eprovement.poptavka.rest.demand.DemandDto demandDto) {
        notNull(demandDto);

        final Demand demand = new Demand();
        if (StringUtils.isNotEmpty(demandDto.getId())) {
            demand.setId(Long.valueOf(demandDto.getId()));
        }
        demand.setStatus(demandDto.getStatus() != null ? demandDto.getStatus() : DemandStatus.NEW);

        if (demandDto.getClient() != null) {
            demand.setClient(clientDeserializer.convert(demandDto.getClient()));
        }

        demand.setTitle(demandDto.getTitle());
        demand.setDescription(demandDto.getDescription());
        demand.setPrice(demandDto.getPrice());
        if (demandDto.getCreatedDate() != null) {
            // demand's createdDate should not be null so set it only if client passed non-null value
            demand.setCreatedDate(demandDto.getCreatedDate());
        }

        demand.setEndDate(demandDto.getEndDate());

        /** localities **/
        if (CollectionUtils.isNotEmpty(demandDto.getLocalities())) {
            demand.setLocalities(localityDeserializer.convertLocalities(demandDto.getLocalities()));
        }

        // categories
        if (CollectionUtils.isNotEmpty(demandDto.getCategories())) {
            demand.setCategories(categoryDeserializer.convertCategories(demandDto.getCategories()));
        }

        if (demandDto.getOrigin() != null) {
            setOrigin(demandDto, demand);
        }

        return demand;
    }

    private void setOrigin(DemandDto demandDto, Demand demand) {
        final Origin origin = registerService.getValue(demandDto.getOrigin(), Origin.class);
        Validate.notNull(origin, String.format("No record found for origin code '%s'. "
                + "Make sure that external system has geen properly registrated", demandDto.getOrigin()));
        demand.setOrigin(origin);
    }

}
