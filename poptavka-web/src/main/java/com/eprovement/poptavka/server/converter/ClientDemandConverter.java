/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectDetail;

public final class ClientDemandConverter extends AbstractConverter<Demand, ClientProjectDetail> {

    private ClientDemandConverter() {
        // Spring instantiates converters - see converters.xml
    }

    @Override
    public ClientProjectDetail convertToTarget(Demand demand) {
        final ClientProjectDetail detail = new ClientProjectDetail();
        detail.setUserMessageId(-1);
        detail.setDemandId(demand.getId());
        detail.setDemandStatus(demand.getStatus());
        detail.setTitle(demand.getTitle());
        detail.setPrice(demand.getPrice());
        detail.setEndDate(convertDate(demand.getEndDate()));
        detail.setValidToDate(convertDate(demand.getValidTo()));
        detail.setRead(false);
        detail.setStarred(false);
        return detail;
    }

    @Override
    public Demand converToSource(ClientProjectDetail clientDemandDetail) {
        throw new UnsupportedOperationException("Conversion from ClientDemandDetail to domain object Demand "
                + "is not implemented yet!");
    }
}
