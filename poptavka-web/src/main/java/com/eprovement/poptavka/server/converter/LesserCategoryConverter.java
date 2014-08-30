/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.service.demand.CategoryService;
import com.eprovement.poptavka.shared.selectors.catLocSelector.LesserCatLocDetail;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ILesserCatLocDetail;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Converts Category to ILesserCatLocDetail and vice versa.
 * @author Ivan Vlcek
 */
public final class LesserCategoryConverter extends AbstractConverter<Category, ILesserCatLocDetail> {

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
     * Creates LesserCategoryConverter.
     */
    private LesserCategoryConverter() {
        // Spring instantiates converters - see converters.xml
    }

    /**************************************************************************/
    /* Convert methods                                                        */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public ILesserCatLocDetail convertToTarget(Category category) {
        return new LesserCatLocDetail(
                category.getId(),
                category.getName(),
                category.getDemandCount());
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public Category convertToSource(ILesserCatLocDetail lesserCategoryDetail) {
        return categoryService.getById(lesserCategoryDetail.getId());
    }
}
