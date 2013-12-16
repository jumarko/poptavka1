/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.rest.common.serializer;

import static org.apache.commons.lang.Validate.notNull;

import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.rest.common.dto.CategoryDto;
import com.eprovement.poptavka.service.demand.CategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CategoryDeserializer {

    private final CategoryService categoryService;

    public CategoryDeserializer(CategoryService categoryService) {
        notNull(categoryService, "categoryService cannot be null!");
        this.categoryService = categoryService;
    }


    public List<Category> convertCategories(Collection<CategoryDto> categoryDtos) {
        final List<Category> categories = new ArrayList<>();
        for (CategoryDto categoryDto : categoryDtos) {
            final Category category;
            if (categoryDto.getId() != null) {
                category = categoryService.getCategory(categoryDto.getId());
            } else if (StringUtils.isNotEmpty(categoryDto.getSicCode())) {
                category = categoryService.getCategoryBySicCode(categoryDto.getSicCode());
            } else {
                throw new IllegalArgumentException("Either 'id' or 'sicCode' must be filled in category dto="
                        + categoryDto);
            }
            notNull(category, "No category has been found for dto=" + categoryDto);
            categories.add(category);
        }
        return categories;
    }

}
