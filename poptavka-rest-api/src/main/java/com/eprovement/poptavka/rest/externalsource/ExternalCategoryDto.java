package com.eprovement.poptavka.rest.externalsource;

import java.util.List;

/**
 * Dto for external category.
 *
 * It does not contain external source code intentionally, because this information is highly repetitive.
 * It also does not reuse {@link com.eprovement.poptavka.rest.common.dto.CategoryDto} but uses simplified version
 * to reduce the amount of data and avoid clutter which wouldn't be useful for client.
 *
 * @see com.eprovement.poptavka.rest.externalsource.SourceCategoryMappingDto
 * @see com.eprovement.poptavka.domain.demand.ExternalCategory
 */
public class ExternalCategoryDto {

    private String externalCategoryId;
    private List<SimpleCategoryDto> categories;

    public String getExternalCategoryId() {
        return externalCategoryId;
    }

    public void setExternalCategoryId(String externalCategoryId) {
        this.externalCategoryId = externalCategoryId;
    }

    public List<SimpleCategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(List<SimpleCategoryDto> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "ExternalCategoryDto{"
                + "externalCategoryId='" + externalCategoryId + '\''
                + ", categories=" + categories
                + '}';
    }
}
