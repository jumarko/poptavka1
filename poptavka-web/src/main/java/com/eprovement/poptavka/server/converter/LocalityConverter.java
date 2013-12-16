/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.service.demand.DemandService;
import com.eprovement.poptavka.service.user.SupplierService;
import com.eprovement.poptavka.shared.selectors.catLocSelector.CatLocDetail;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import org.springframework.beans.factory.annotation.Autowired;

public final class LocalityConverter extends AbstractConverter<Locality, ICatLocDetail> {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private static final String DISTRICT_SUFIX = " Co.";

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
    public ICatLocDetail convertToTarget(Locality locality) {
        CatLocDetail detail = new CatLocDetail(locality.getId(), constructName(locality));
        detail.setDemandsCount(demandService.getDemandsCountQuick(locality));
        detail.setSuppliersCount(supplierService.getSuppliersCountQuick(locality));
        detail.setLevel(locality.getLevel());
        detail.setLeaf(locality.isLeaf());
        return detail;

    }

    private String constructName(Locality locality) {
        switch (locality.getType()) {
            case DISTRICT:
                return locality.getName() + DISTRICT_SUFIX;
            default:
                return locality.getName();
        }
    }

    @Override
    public Locality convertToSource(ICatLocDetail catLocDetail) {
        return localityService.getLocality(catLocDetail.getId());
    }
}
