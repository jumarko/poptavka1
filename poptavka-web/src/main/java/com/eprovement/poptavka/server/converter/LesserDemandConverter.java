/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.service.demand.DemandService;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.demand.LesserDemandDetail;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Converts Demand to LesserDemandDetail that is supposed to be used in Universal Grid
 * @author Ivan Vlcek
 */
public final class LesserDemandConverter extends AbstractConverter<Demand, LesserDemandDetail> {

    /**************************************************************************/
    /* RPC Services                                                           */
    /**************************************************************************/
    private DemandService demandService;

    @Autowired
    public void setDemandService(DemandService demandService) {
        this.demandService = demandService;
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private final Converter<Locality, ICatLocDetail> localityConverter;

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates LesserDemandConverter.
     */
    private LesserDemandConverter(Converter<Locality, ICatLocDetail> localityConverter) {
        // Spring instantiates converters - see converters-web.xml
        this.localityConverter = localityConverter;
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public LesserDemandDetail convertToTarget(Demand source) {
        LesserDemandDetail detail = new LesserDemandDetail();
        detail.setDemandId(source.getId());
        detail.setDemandTitle(source.getTitle());
        detail.setCreated(convertDate(source.getCreatedDate()));
        detail.setEndDate(convertDate(source.getEndDate()));
        detail.setValidTo(convertDate(source.getValidTo()));
        //localities
        detail.setLocalities(localityConverter.convertToTargetList(source.getLocalities()));
        if (source.getType() != null) {
            detail.setDemandType(source.getType().getDescription());
        }
        return detail;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Demand convertToSource(LesserDemandDetail source) {
        return demandService.getById(source.getDemandId());
    }
}
