/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.FullClientDetail;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;

/**
 * Converts Client to FullClientDetail and vice versa.
 * @author Juraj Martinka
 */
public final class ClientConverter extends AbstractConverter<Client, FullClientDetail> {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private Converter<BusinessUser, BusinessUserDetail> businessUserConverter;

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates ClientConverter.
     */
    private ClientConverter(Converter<BusinessUser, BusinessUserDetail> businessUserConverter) {
        // Spring instantiates converters - see converters.xml
        this.businessUserConverter = businessUserConverter;
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public FullClientDetail convertToTarget(Client source) {
        Preconditions.checkArgument(source != null, "Client cannot be null!");
        FullClientDetail detail = new FullClientDetail();
        detail.setClientId(source.getId());
        detail.setOveralRating(source.getOveralRating() == null ? 0 : source.getOveralRating());
        if (source.getBusinessUser() != null) {
            detail.setUserData(businessUserConverter.convertToTarget(source.getBusinessUser()));
        }
        if (source.getSupplierBlacklist() != null) {
            List<Long> supplierBlackListIds = new ArrayList<Long>();
            for (Supplier supplier : source.getSupplierBlacklist().getSuppliers()) {
                supplierBlackListIds.add(supplier.getId());
            }
            detail.setSupplierBlackListIds(supplierBlackListIds);
        }

        List<Long> demandsIds = new ArrayList<Long>();
        for (Demand demand : source.getDemands()) {
            demandsIds.add(demand.getId());
        }
        detail.setDemandsIds(demandsIds);

        return detail;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Client convertToSource(FullClientDetail source) {
        throw new UnsupportedOperationException();
    }
}
