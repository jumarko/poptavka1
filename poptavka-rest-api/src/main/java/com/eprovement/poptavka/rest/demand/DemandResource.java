package com.eprovement.poptavka.rest.demand;

import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.rest.ResourceNotFoundException;
import com.eprovement.poptavka.rest.common.ResourceUtils;
import com.eprovement.poptavka.rest.common.resource.AbstractPageableResource;
import com.eprovement.poptavka.service.demand.DemandService;
import com.google.common.base.Preconditions;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping(DemandResource.DEMAND_RESOURCE_URI)
public class DemandResource extends AbstractPageableResource<Demand, DemandDto> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemandResource.class);

    static final String DEMAND_RESOURCE_URI = "/demands";

    private final DemandService demandService;
    private final DemandSerializer demandSerializer;
    private final DemandDeserializer demandDeserializer;

    @Autowired
    public DemandResource(DemandService demandService,
            DemandSerializer demandSerializer,
            DemandDeserializer demandDeserializer) {
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
        final List<DemandDto> demandsDtos = new ArrayList<>();
        for (Demand demand : domainObjects) {
            final DemandDto demandDto = this.demandSerializer.convert(demand);
            setLinks(demandDto, demand);
            demandsDtos.add(demandDto);
        }
        return demandsDtos;
    }


    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody DemandDto createDemand(@RequestBody DemandDto demandDto, HttpServletResponse response) {
        try {
            final Demand demand = this.demandDeserializer.convert(demandDto);
            final Demand createdDemand = this.demandService.create(demand);
            response.setHeader("Location", ResourceUtils.generateSelfLink(DEMAND_RESOURCE_URI, createdDemand));
            final DemandDto createdDemandDto = this.demandSerializer.convert(createdDemand);
            setLinks(createdDemandDto, createdDemand);
            return createdDemandDto;
        } catch (Exception e) {
            LOGGER.error("action=createDemand status=error", e);
            throw e;
        }
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody DemandDto getDemandById(@PathVariable String id) {
        Preconditions.checkNotNull(id);

        final Demand demand = demandService.getById(Long.valueOf(id));

        if (demand == null) {
            throw new ResourceNotFoundException("No demand with id=" + id + " has been found!");
        }

        final DemandDto demandDto = this.demandSerializer.convert(demand);
        setLinks(demandDto, demand);
        return demandDto;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
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
