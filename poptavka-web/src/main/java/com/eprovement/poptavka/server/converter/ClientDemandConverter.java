/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;

/**
 * Converts Demand to ClientDemandDetail and vice versa.
 * @author Juraj Martinka
 */
public final class ClientDemandConverter extends AbstractConverter<Demand, ClientDemandDetail> {

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates ClientDemandConverter.
     */
    private ClientDemandConverter() {
        // Spring instantiates converters - see converters.xml
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public ClientDemandDetail convertToTarget(Demand demand) {
        final ClientDemandDetail detail = new ClientDemandDetail();
        detail.setDemandId(demand.getId());
        detail.setDemandStatus(demand.getStatus());
        detail.setDemandTitle(demand.getTitle());
        detail.setPrice(demand.getPrice());
        detail.setEndDate(convertDate(demand.getEndDate()));
        detail.setValidTo(convertDate(demand.getValidTo()));
        return detail;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Demand convertToSource(ClientDemandDetail clientDemandDetail) {
        throw new UnsupportedOperationException("Conversion from ClientDemandDetail to domain object Demand "
                + "is not implemented yet!");
    }
}
