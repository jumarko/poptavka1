package com.eprovement.poptavka.rest.externalsource;

import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.demand.ExternalCategory;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ExternalCategorySerializer implements Converter<ExternalCategory, ExternalCategoryDto> {

    @Override
    public ExternalCategoryDto convert(ExternalCategory externalCategory) {
        if (externalCategory == null) {
            return null;
        }
        final ExternalCategoryDto dto = new ExternalCategoryDto();
        dto.setExternalCategoryId(externalCategory.getExternalId());
        dto.setCategories(convertCategories(externalCategory.getCategories()));
        return dto;
    }

    public List<ExternalCategoryDto> convertList(List<ExternalCategory> categories) {
        if (CollectionUtils.isEmpty(categories)) {
            return Collections.emptyList();
        }

        final List<ExternalCategoryDto> categoriesDtos = new ArrayList<>();
        for (ExternalCategory category : categories) {
            categoriesDtos.add(convert(category));
        }
        return categoriesDtos;
    }

    //--------------------------------------------------- HELPER METHODS -----------------------------------------------


    private List<SimpleCategoryDto> convertCategories(List<Category> categories) {
        if (CollectionUtils.isEmpty(categories)) {
            return Collections.emptyList();
        }

        final List<SimpleCategoryDto> categoriesDtos = new ArrayList<>();
        for (Category category : categories) {
            final SimpleCategoryDto categoryDto = new SimpleCategoryDto();
            categoryDto.setId(category.getId());
            categoryDto.setName(category.getName());
            categoriesDtos.add(categoryDto);
        }
        return categoriesDtos;
    }

}
