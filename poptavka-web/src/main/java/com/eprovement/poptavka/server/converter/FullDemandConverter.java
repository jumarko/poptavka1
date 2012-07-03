/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;


import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.Validate;

public final class FullDemandConverter extends AbstractConverter<Demand, FullDemandDetail> {

    private static Converter<Supplier, FullSupplierDetail> supplierConverter;

    private FullDemandConverter(Converter<Supplier, FullSupplierDetail> supplierConverter) {
        // Spring instantiates converters - see converters.xml
        Validate.notNull(supplierConverter);
        this.supplierConverter = supplierConverter;
    }

    @Override
    public FullDemandDetail convertToTarget(Demand source) {
        FullDemandDetail detail = new FullDemandDetail();
        detail.setDemandId(source.getId());
        detail.setTitle(source.getTitle());
        detail.setDescription(source.getDescription());
        detail.setPrice(source.getPrice());
        detail.setCreated(convertDate(source.getCreatedDate()));
        detail.setEndDate(convertDate(source.getEndDate()));
        detail.setValidToDate(convertDate(source.getValidTo()));
        detail.setMaxOffers(source.getMaxSuppliers() == null ? 0 : source.getMaxSuppliers());
        detail.setMinRating(source.getMinRating() == null ? 0 : source.getMinRating());
        //categories
        Map<Long, String> catMap = new HashMap<Long, String>();
        for (Category cat : source.getCategories()) {
            catMap.put(cat.getId(), cat.getName());
        }
        detail.setCategories(catMap);
        //localities
        Map<String, String> locMap = new HashMap<String, String>();
        for (Locality loc : source.getLocalities()) {
            locMap.put(loc.getCode(), loc.getName());
        }

        detail.setLocalities(locMap);
        detail.setDemandStatus(source.getStatus().getValue());

        if (source.getType() != null) {
            detail.setDemandType(source.getType().getDescription());
        }

        detail.setClientId(source.getClient().getId());

        setExcludedSuppliers(source, detail);

        return detail;

    }

    @Override
    public Demand converToSource(FullDemandDetail source) {
        throw new UnsupportedOperationException("Conversion from FullDemandDetail to domain object Demand"
                + " is not implemented yet!");
    }


    //--------------------------------------------------- PRIVATE METHODS ----------------------------------------------
    private static void setExcludedSuppliers(Demand demand, FullDemandDetail detail) {
        final List<FullSupplierDetail> excludedSuppliers = new ArrayList<FullSupplierDetail>();
        if (demand.getExcludedSuppliers() != null) {
            for (Supplier supplier : demand.getExcludedSuppliers()) {
                excludedSuppliers.add(supplierConverter.convertToTarget(supplier));
            }
        }
        detail.setExcludedSuppliers(excludedSuppliers);
    }

}
