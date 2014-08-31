/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.service.demand.CategoryService;
import com.eprovement.poptavka.shared.selectors.catLocSelector.CatLocDetail;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Converts Category to ICatLocDetail and vice versa.
 * @author Juraj Martinka
 */
public final class CategoryConverter extends AbstractConverter<Category, ICatLocDetail> {

    /**************************************************************************/
    /* RPC Services                                                           */
    /**************************************************************************/
    private CategoryService categoryService;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates CategoryConverter.
     */
    private CategoryConverter() {
        // Spring instantiates converters - see converters.xml
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public ICatLocDetail convertToTarget(Category category) {
        CatLocDetail detail = new CatLocDetail(category.getId(), category.getName());
        detail.setDemandsCount(category.getDemandCount());
        detail.setSuppliersCount(category.getSupplierCount());
        detail.setLevel(category.getLevel());
        detail.setLeaf(category.isLeaf());
        // TODO LATER ivlcek - since we decided to have checkboxes on all categories/localities
        // in CellBrowser we don't need the information about category.isLeafsParent(). Maybe in
        // future we can use it but I don't think so. In that case uncomment following line:
//        detail.setLeafsParent(isLeafsParent(category.getId()));
        detail.setLeafsParent(true);
        Category categoryParent = category.getParent();
        if (categoryParent != null) {
            detail.setParentName(categoryParent.getName());
        }
        return detail;

    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Category convertToSource(ICatLocDetail categoryDetail) {
        return categoryService.getById(categoryDetail.getId());
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * @param categoryId
     * @return true if given category has only leafs as children, false otherwise
     */
    private boolean isLeafsParent(Long categoryId) {
        final List<Category> children = categoryService.getById(categoryId).getChildren();
        boolean leafsParent = true;
        for (Category category : children) {
            leafsParent &= category.isLeaf();
            if (!leafsParent) {
                break;
            }
        }
        return leafsParent;
    }
}
