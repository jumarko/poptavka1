/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.rest.category;

import cz.poptavka.sample.domain.demand.Category;
import org.apache.commons.lang.Validate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class CategorySerializer implements Converter<Category, CategoryDto> {
    @Override
    public CategoryDto convert(Category category) {
        Validate.notNull(category);

        final CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCode(category.getCode());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());

        return categoryDto;
    }


}
