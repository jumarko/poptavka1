/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter.admin;

import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.server.converter.AbstractConverter;
import com.eprovement.poptavka.server.converter.LocalityConverter;
import com.eprovement.poptavka.service.message.MessageService;
import com.eprovement.poptavka.shared.domain.adminModule.AdminDemandDetail;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Martin Slavkovsky
 * @since 23.4.2014
 */
public class AdminDemandConverter extends AbstractConverter<Demand, AdminDemandDetail> {

    /**************************************************************************/
    /*  Attributes                                                            */
    /**************************************************************************/
    @Autowired
    private MessageService messageService;
    private LocalityConverter localityConverter;

    /**************************************************************************/
    /*  Initialization                                                        */
    /**************************************************************************/
    public AdminDemandConverter(LocalityConverter localityConverter) {
        this.localityConverter = localityConverter;
    }
    
    /**************************************************************************/
    /*  Convert To Target                                                     */
    /**************************************************************************/
    /**
     * {@inheritDoc}
     */
    @Override
    public AdminDemandDetail convertToTarget(Demand source) {
        final AdminDemandDetail detail = new AdminDemandDetail();
        detail.setUserId(source.getClient().getBusinessUser().getId());
        detail.setClientId(source.getClient().getId());
        detail.setCreated(source.getCreatedDate());
        detail.setDemandId(source.getId());
        detail.setLocalities(localityConverter.convertToTargetList(source.getLocalities()));
        detail.setThreadRootId(messageService.getThreadRootMessage(source).getId());
        detail.setDemandTitle(source.getTitle());
        detail.setValidTo(source.getValidTo());
        return detail;
    }

    /**************************************************************************/
    /*  Convert To Source                                                     */
    /**************************************************************************/
    /**
     * {@inheritDoc}
     */
    @Override
    public Demand convertToSource(AdminDemandDetail target) {
        throw new UnsupportedOperationException("Convertion from AdminDemandDetail to Demand not yet supported.");
    }
}
