package com.eprovement.poptavka.rest.category;

import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.rest.common.ResourceUtils;
import com.eprovement.poptavka.rest.common.dto.CategoryDto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CategorySerializer implements Converter<Category, CategoryDto> {
    @Override
    public CategoryDto convert(Category category) {
        Validate.notNull(category);

        final CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());

        return categoryDto;
    }


    public List<CategoryDto> convertCategories(List<Category> categories) {
        if (CollectionUtils.isEmpty(categories)) {
            return Collections.emptyList();
        }

        final List<CategoryDto> categoriesDtos = new ArrayList<>();
        for (Category category : categories) {
            final CategoryDto categoryDto = convert(category);
            categoryDto.setLinks(ResourceUtils.generateSelfLinks(CategoryResource.CATEGORY_RESOURCE_URI, category));
            categoriesDtos.add(categoryDto);
        }
        return categoriesDtos;
    }

}
