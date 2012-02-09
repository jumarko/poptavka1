/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.rest.demand.resource;

import com.google.common.base.Preconditions;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.rest.demand.dto.DemandDto;
import cz.poptavka.sample.service.demand.DemandService;
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

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/demands")
public class DemandResource {

    @Autowired
    private DemandService demandService;

    @Autowired
    private Converter<Demand, DemandDto> demandSerializer;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody List<DemandDto> listDemands() {
        final List<Demand> allDemands = this.demandService.getAll();
        final ArrayList<DemandDto> allDemandsDtos = new ArrayList<DemandDto>();
        for (Demand demand : allDemands) {
            final DemandDto demandDto = this.demandSerializer.convert(demand);
            allDemandsDtos.add(demandDto);
        }

        return allDemandsDtos;
    }


    @RequestMapping(method = RequestMethod.POST, headers = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Demand createDemand(@Valid Demand demand, BindingResult result, HttpServletResponse response)
        throws BindException {

        if (result.hasErrors()) {
            throw new BindException(result);
        }

        final Demand createdDemand = this.demandService.create(demand);
        response.setHeader("Location", "/demands/" + createdDemand.getId());
        return demand;
    }



    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody Demand getDemandById(@PathVariable Long id) {
        Preconditions.checkNotNull(id);
        return this.demandService.getById(id);
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
    public void updateDemand(@RequestBody Demand demand) {
        this.demandService.update(demand);
    }

}
