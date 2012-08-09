/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.Validate;

public final class FullDemandConverter extends AbstractConverter<Demand, FullDemandDetail> {

    private final Converter<Supplier, FullSupplierDetail> supplierConverter;
    private final Converter<Locality, LocalityDetail> localityConverter;
    private final Converter<Category, CategoryDetail> categoryConverter;

    private FullDemandConverter(
            Converter<Supplier, FullSupplierDetail> supplierConverter,
            Converter<Locality, LocalityDetail> localityConverter,
            Converter<Category, CategoryDetail> categoryConverter) {
        // Spring instantiates converters - see converters.xml
        Validate.notNull(supplierConverter);
        this.supplierConverter = supplierConverter;
        this.localityConverter = localityConverter;
        this.categoryConverter = categoryConverter;
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
        detail.setCategories(categoryConverter.convertToTargetList(source.getCategories()));
        //localities
        detail.setLocalities(localityConverter.convertToTargetList(source.getLocalities()));

        detail.setDemandStatus(source.getStatus());

        if (source.getType() != null) {
            detail.setDemandType(source.getType().getDescription());
        }

        detail.setClientId(source.getClient().getId());

        setExcludedSuppliers(source, detail);

        return detail;

    }

    @Override
    public Demand convertToSource(FullDemandDetail source) {
        throw new UnsupportedOperationException("Conversion from FullDemandDetail to domain object Demand"
                + " is not implemented yet!");
    }

    //--------------------------------------------------- PRIVATE METHODS ----------------------------------------------
    private void setExcludedSuppliers(Demand demand, FullDemandDetail detail) {
        final List<FullSupplierDetail> excludedSuppliers = new ArrayList<FullSupplierDetail>();
        if (demand.getExcludedSuppliers() != null) {
            excludedSuppliers.addAll(supplierConverter.convertToTargetList(demand.getExcludedSuppliers()));
        }
        detail.setExcludedSuppliers(excludedSuppliers);
    }
}
