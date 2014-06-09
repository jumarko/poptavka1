/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.product.Service;
import com.eprovement.poptavka.shared.domain.ServiceDetail;

/**
 * Converts Service to ServiceDetail.
 * @author Juraj Martinka
 */
public final class ServiceConverter extends AbstractConverter<Service, ServiceDetail> {

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates ServiceConverter.
     */
    private ServiceConverter() {
        // Spring instantiates converters - see converters.xml
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public ServiceDetail convertToTarget(Service service) {
        if (service == null) {
            return null;
        }
        ServiceDetail detail = new ServiceDetail();
        detail.setId(service.getId());
        detail.setTitle(service.getTitle());
        detail.setDescription(service.getDescription());
        detail.setPrice(service.getPrice());
        detail.setPrepaidMonths(service.getPrepaidMonths());
        detail.setType(service.getServiceType().getValue());
        return detail;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Service convertToSource(ServiceDetail serviceDetail) {
        throw new UnsupportedOperationException("Conversion from ServiceDetail to domain object Service "
            + "is not implemented yet!");
    }
}
