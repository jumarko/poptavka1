/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.server.converter;

import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.shared.domain.CategoryDetail;

public final class CategoryConverter extends AbstractConverter<Category, CategoryDetail> {

    private CategoryConverter() {
        // Spring instantiates converters - see converters.xml
    }

    @Override
    public CategoryDetail convertToTarget(Category category) {
        CategoryDetail detail = new CategoryDetail();
        detail.setId(category.getId());
        detail.setName(category.getName());
        return detail;

    }

    @Override
    public Category converToSource(CategoryDetail categoryDetail) {
        throw new UnsupportedOperationException("Conversion from AddressDetail to domain object Address "
                + "is not implemented yet!");
    }
}
