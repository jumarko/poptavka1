/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.service.demand.CategoryService;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import org.springframework.beans.factory.annotation.Autowired;

public final class CategoryConverter extends AbstractConverter<Category, CategoryDetail> {

    private CategoryService categoryService;

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    private CategoryConverter() {
        // Spring instantiates converters - see converters.xml
    }

    @Override
    public CategoryDetail convertToTarget(Category category) {
        CategoryDetail detail = new CategoryDetail();
        detail.setId(category.getId());
        detail.setName(category.getName());
        detail.setLeaf(category.getChildren().isEmpty());
        return detail;

    }

    @Override
    public Category convertToSource(CategoryDetail categoryDetail) {
        return categoryService.getById(categoryDetail.getId());
    }
}
