/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.service.demand.DemandService;
import com.eprovement.poptavka.service.user.SupplierService;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import org.springframework.beans.factory.annotation.Autowired;

public final class LocalityConverter extends AbstractConverter<Locality, LocalityDetail> {

    /**************************************************************************/
    /* RPC Services                                                           */
    /**************************************************************************/
    private LocalityService localityService;
    private DemandService demandService;
    private SupplierService supplierService;

    @Autowired
    public void setLocalityService(LocalityService localityService) {
        this.localityService = localityService;
    }

    @Autowired
    public void setDemandService(DemandService demandService) {
        this.demandService = demandService;
    }

    @Autowired
    public void setSupplierService(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    private LocalityConverter() {
        // Spring instantiates converters - see converters.xml
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    @Override
    public LocalityDetail convertToTarget(Locality locality) {
        LocalityDetail detail = new LocalityDetail();
        detail.setId(locality.getId());
        detail.setName(locality.getName());
        detail.setLocalityType(locality.getType());
        detail.setDemandsCount(demandService.getDemandsCountQuick(locality));
        detail.setSuppliersCount(supplierService.getSuppliersCountQuick(locality));
        detail.setLevel(locality.getLevel());
        detail.setLeaf(locality.getChildren().isEmpty());
        return detail;

    }

    @Override
    public Locality convertToSource(LocalityDetail localityDetail) {
        return localityService.getLocality(localityDetail.getId());
    }
}
