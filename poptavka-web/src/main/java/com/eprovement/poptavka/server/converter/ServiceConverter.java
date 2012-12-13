/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.enums.ServiceType;
import com.eprovement.poptavka.domain.product.Service;
import com.eprovement.poptavka.shared.domain.ServiceDetail;

public final class ServiceConverter extends AbstractConverter<Service, ServiceDetail> {

    private ServiceConverter() {
        // Spring instantiates converters - see converters.xml
    }

    @Override
    public ServiceDetail convertToTarget(Service service) {
        ServiceDetail detail = new ServiceDetail();
        if (service.isValid() && service.getServiceType().equals(ServiceType.SUPPLIER)) {
            detail.setId(service.getId());
            detail.setTitle(service.getTitle());
            detail.setDescription(service.getDescription());
            detail.setPrice(service.getPrice());
            detail.setPrepaidMonths(service.getPrepaidMonths());
            detail.setType(service.getServiceType().getValue());
        }
        return detail;
    }

    @Override
    public Service convertToSource(ServiceDetail serviceDetail) {
        throw new UnsupportedOperationException("Conversion from ServiceDetail to domain object Service "
                + "is not implemented yet!");
    }
}
