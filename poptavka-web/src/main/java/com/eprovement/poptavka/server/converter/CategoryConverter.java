/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.service.demand.CategoryService;
import com.eprovement.poptavka.service.demand.DemandService;
import com.eprovement.poptavka.service.user.SupplierService;
import com.eprovement.poptavka.shared.selectors.catLocSelector.CatLocDetail;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import org.springframework.beans.factory.annotation.Autowired;

public final class CategoryConverter extends AbstractConverter<Category, ICatLocDetail> {

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
    public ICatLocDetail convertToTarget(Category category) {
        CatLocDetail detail = new CatLocDetail(category.getId(), category.getName());
//        detail.setId(category.getId());
//        detail.setName(category.getName());
        detail.setDemandsCount(demandService.getDemandsCountQuick(category));
        detail.setSuppliersCount(supplierService.getSuppliersCountQuick(category));
        detail.setLevel(category.getLevel());
        detail.setLeaf(category.isLeaf());
        return detail;

    }

    @Override
    public Category convertToSource(ICatLocDetail categoryDetail) {
        return categoryService.getById(categoryDetail.getId());
    }
}
