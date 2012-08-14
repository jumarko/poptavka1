/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.service.demand.CategoryService;
import com.eprovement.poptavka.service.demand.DemandService;
import com.eprovement.poptavka.service.user.SupplierService;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import org.springframework.beans.factory.annotation.Autowired;

public final class CategoryConverter extends AbstractConverter<Category, CategoryDetail> {

    /**************************************************************************/
    /* RPC Services                                                           */
    /**************************************************************************/
    private CategoryService categoryService;
    private DemandService demandService;
    private SupplierService supplierService;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
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
    private CategoryConverter() {
        // Spring instantiates converters - see converters.xml
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    @Override
    public CategoryDetail convertToTarget(Category category) {
        CategoryDetail detail = new CategoryDetail();
        detail.setId(category.getId());
        detail.setName(category.getName());
        detail.setDemands(demandService.getDemandsCountQuick(category));
        detail.setSuppliers(supplierService.getSuppliersCountQuick(category));
        detail.setLeaf(category.getChildren().isEmpty());
        return detail;

    }

    @Override
    public Category convertToSource(CategoryDetail categoryDetail) {
        return categoryService.getById(categoryDetail.getId());
    }
}
