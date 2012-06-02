/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.shared.domain.converter;

import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.domain.adminModule.ClientDetail;
import java.util.ArrayList;
import java.util.List;

public class ClientConverter extends AbstractConverter<Client, ClientDetail> {
    @Override
    public ClientDetail convertToTarget(Client source) {
        ClientDetail detail = new ClientDetail();
        detail.setId(source.getId());
        if (source.getOveralRating() != null) {
            detail.setOveralRating(source.getOveralRating());
        }
        if (source.getVerification() != null) {
            detail.setVerification(source.getVerification().name());
        }
        detail.setUserDetail(UserDetail.createUserDetail(source.getBusinessUser()));
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

    @Override
    public Client converToSource(ClientDetail source) {
        throw new UnsupportedOperationException();
    }
}
