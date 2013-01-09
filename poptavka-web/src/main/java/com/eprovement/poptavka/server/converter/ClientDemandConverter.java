/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;

public final class ClientDemandConverter extends AbstractConverter<Demand, ClientDemandDetail> {

    private ClientDemandConverter() {
        // Spring instantiates converters - see converters.xml
    }

    @Override
    public ClientDemandDetail convertToTarget(Demand demand) {
        final ClientDemandDetail detail = new ClientDemandDetail();
        detail.setUserMessageId(-1);
        detail.setDemandId(demand.getId());
        detail.setDemandStatus(demand.getStatus());
        detail.setDemandTitle(demand.getTitle());
        detail.setPrice(demand.getPrice());
        detail.setEndDate(convertDate(demand.getEndDate()));
        detail.setValidToDate(convertDate(demand.getValidTo()));
        detail.setStarred(false);
        return detail;
    }

    @Override
    public Demand convertToSource(ClientDemandDetail clientDemandDetail) {
        throw new UnsupportedOperationException("Conversion from ClientDemandDetail to domain object Demand "
                + "is not implemented yet!");
    }
}
