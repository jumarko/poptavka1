/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.shared.selectors.catLocSelector.CatLocDetail;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Converts Locality to ICatLocDetail.
 * @author Juraj Martinka
 */
public final class LocalityConverter extends AbstractConverter<Locality, ICatLocDetail> {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private static final String DISTRICT_SUFIX = " Co.";

    /**************************************************************************/
    /* RPC Services                                                           */
    /**************************************************************************/
    private boolean involveParentName;
    private LocalityService localityService;

    @Autowired
    public void setLocalityService(LocalityService localityService) {
        this.localityService = localityService;
    }

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates LocalityConverter.
     */
    private LocalityConverter() {
        // Spring instantiates converters - see converters.xml
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public ICatLocDetail convertToTarget(Locality locality) {
        CatLocDetail detail = new CatLocDetail(locality.getId(), constructName(locality));
        detail.setDemandsCount(locality.getDemandCount());
        detail.setSuppliersCount(locality.getSupplierCount());
        detail.setLevel(locality.getLevel());
        detail.setLeaf(locality.isLeaf());
        if (involveParentName) {
            if (locality.getParent() != null) {
                detail.setParentName(locality.getParent().getName());
            }
        }
        return detail;

    }

    /**
     * Converts list of domain objects to list of detail objects with possible parent name within detail.
     * @param domainObjects to be converted
     * @param involveParentName true to involve parent name in category detail, false otherwise
     * @return converted list
     */
    public ArrayList<ICatLocDetail> convertToTargetList(Collection<Locality> domainObjects, boolean involveParentName) {
        this.involveParentName = involveParentName;
        return super.convertToTargetList(domainObjects);
    }

    /**
     * If locality is a district, append {@value #DISTRICT_SUFIX} string.
     * @param locality
     * @return appended locality name string.
     */
    private String constructName(Locality locality) {
        switch (locality.getType()) {
            case DISTRICT:
                return locality.getName() + DISTRICT_SUFIX;
            default:
                return locality.getName();
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Locality convertToSource(ICatLocDetail catLocDetail) {
        return localityService.getLocality(catLocDetail.getId());
    }
}
