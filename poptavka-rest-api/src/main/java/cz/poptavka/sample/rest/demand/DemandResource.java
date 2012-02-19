/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.rest.demand;

import com.google.common.base.Preconditions;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.rest.common.ResourceUtils;
import cz.poptavka.sample.rest.common.resource.AbstractPageableResource;
import cz.poptavka.sample.service.demand.DemandService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping(DemandResource.DEMAND_RESOURCE_URI)
public class DemandResource extends AbstractPageableResource<Demand, DemandDto> {

    static final String DEMAND_RESOURCE_URI = "/demands";

    private final DemandService demandService;
    private final Converter<Demand, DemandDto> demandSerializer;
    private final Converter<DemandDto, Demand> demandDeserializer;

    @Autowired
    public DemandResource(DemandService demandService,
            Converter<Demand, DemandDto> demandSerializer,
            Converter<DemandDto, Demand> demandDeserializer) {
        super(Demand.class, DEMAND_RESOURCE_URI);

        Validate.notNull(demandService);
        Validate.notNull(demandSerializer);
        Validate.notNull(demandDeserializer);

        this.demandService = demandService;
        this.demandSerializer = demandSerializer;
        this.demandDeserializer = demandDeserializer;
    }


    @Override
    public Collection<DemandDto> convertToDtos(Collection<Demand> domainObjects) {
        final List<DemandDto> demandsDtos = new ArrayList<DemandDto>();
        for (Demand demand : domainObjects) {
            final DemandDto demandDto = this.demandSerializer.convert(demand);
            setLinks(demandDto, demand);
            demandsDtos.add(demandDto);
        }
        return demandsDtos;
    }


    @RequestMapping(method = RequestMethod.POST, headers = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public@ResponseBody DemandDto createDemand(@Valid DemandDto demandDto,
            BindingResult result, HttpServletResponse response) throws BindException {

        if (result.hasErrors()) {
            throw new BindException(result);
        }
        final Demand createdDemand = this.demandService.create(this.demandDeserializer.convert(demandDto));
        response.setHeader("Location", ResourceUtils.generateSelfLink(DEMAND_RESOURCE_URI, createdDemand));
        final DemandDto createdDemandDto = this.demandSerializer.convert(createdDemand);
        setLinks(createdDemandDto, createdDemand);
        return createdDemandDto;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public
    @ResponseBody DemandDto getDemandById(@PathVariable Long id) {
        Preconditions.checkNotNull(id);
        final Demand demand = this.demandService.getById(id);
        final DemandDto demandDto = this.demandSerializer.convert(demand);
        setLinks(demandDto, demand);
        return demandDto;
    }


//
//    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
//    public Demand getDemandByFilter(@PathVariable DemandFilter demandFilter) {
//        Preconditions.checkNotNull(id);
//        return this.demandService.getById(id);
//    }
//
//


    @RequestMapping(value = "/{id}",
            method = RequestMethod.PUT,
            headers = "application/json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDemand(@RequestBody DemandDto demandDto, BindingResult result, HttpServletResponse response)
        throws BindException {
        if (result.hasErrors()) {
            throw new BindException(result);
        }
        final Demand updatedDemand = this.demandService.update(this.demandDeserializer.convert(demandDto));
        response.setHeader("Location", ResourceUtils.generateSelfLink(DEMAND_RESOURCE_URI, updatedDemand));
    }


    //--------------------------------------------------- PRIVATE STUFF ------------------------------------------------
    private void setLinks(DemandDto demandDto, Demand demand) {
        demandDto.setLinks(ResourceUtils.generateSelfLinks(DEMAND_RESOURCE_URI, demand));
    }

}
