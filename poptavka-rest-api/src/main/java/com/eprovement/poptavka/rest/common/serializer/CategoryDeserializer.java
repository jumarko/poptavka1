package com.eprovement.poptavka.rest.common.serializer;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.apache.commons.lang.Validate.notEmpty;
import static org.apache.commons.lang.Validate.notNull;

import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.demand.ExternalCategory;
import com.eprovement.poptavka.rest.common.dto.CategoryDto;
import com.eprovement.poptavka.service.demand.CategoryService;
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
            // list of matching categories, typically only single category but can be multiple ones
            // if external category has been specified and the mapping contains for this external category
            // contains for example two categories
            final List<Category> cats = new ArrayList<>();
            if (categoryDto.getId() != null) {
                cats.add(categoryService.getCategory(categoryDto.getId()));
            } else if (isNotEmpty(categoryDto.getExternalId())) {
                final ExternalCategory externalCategory =
                        categoryService.getExternalCategory(categoryDto.getExternalId());
                if (externalCategory == null) {
                    throw new IllegalArgumentException("No external category found for external id="
                            + categoryDto.getExternalId());
                }
                cats.addAll(externalCategory.getCategories());
            } else {
                throw new IllegalArgumentException("Either 'id' or 'externalId' must be filled in category dto="
                        + categoryDto);
            }
            notEmpty(cats, "No category has been found for dto=" + categoryDto);
            categories.addAll(cats);
        }
        return categories;
    }

}
