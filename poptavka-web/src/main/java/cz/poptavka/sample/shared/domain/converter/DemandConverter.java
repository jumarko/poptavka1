/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.shared.domain.converter;


import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DemandConverter extends AbstractConverter<Demand, FullDemandDetail> {

    private static SupplierConverter supplierConverter = new SupplierConverter();

    @Override
    public FullDemandDetail convertToTarget(Demand source) {
        FullDemandDetail detail = new FullDemandDetail();
        detail.setDemandId(source.getId());
        detail.setTitle(source.getTitle());
        detail.setDescription(source.getDescription());
        detail.setPrice(source.getPrice());
        detail.setCreated(source.getCreatedDate());
        detail.setEndDate(source.getEndDate());
        detail.setValidToDate(source.getValidTo());
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
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
