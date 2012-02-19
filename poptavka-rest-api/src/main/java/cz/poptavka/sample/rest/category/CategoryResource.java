/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.rest.category;

import com.google.common.base.Preconditions;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.rest.common.resource.AbstractPageableResource;
import cz.poptavka.sample.rest.common.ResourceUtils;
import cz.poptavka.sample.service.demand.CategoryService;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(CategoryResource.CATEGORY_RESOURCE_URI)
public class CategoryResource extends AbstractPageableResource<Category, CategoryDto> {

    static final String CATEGORY_RESOURCE_URI = "/categories";

    private final CategoryService categoryService;
    private final Converter<Category, CategoryDto> categorySerializer;

    @Autowired
    public CategoryResource(CategoryService categoryService, Converter<Category, CategoryDto> categorySerializer) {
        super(Category.class, CATEGORY_RESOURCE_URI);

        Validate.notNull(categoryService);
        Validate.notNull(categorySerializer);
        this.categoryService = categoryService;
        this.categorySerializer = categorySerializer;
    }


    @Override
    public Collection<CategoryDto> convertToDtos(Collection<Category> domainObjectsPage) {
        final ArrayList<CategoryDto> categoryDtosPage = new ArrayList<CategoryDto>();
        for (Category category : domainObjectsPage) {
            final CategoryDto categoryDto = this.categorySerializer.convert(category);
            setLinks(categoryDto, category);
            categoryDtosPage.add(categoryDto);
        }

        return categoryDtosPage;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody
    CategoryDto getCategoryById(@PathVariable Long id) {
        Preconditions.checkNotNull(id);
        final Category category = this.categoryService.getById(id);
        final CategoryDto categoryDto = this.categorySerializer.convert(category);
        setLinks(categoryDto, category);
        return categoryDto;
    }


    //--------------------------------------------------- PRIVATE STUFF ------------------------------------------------
    private void setLinks(CategoryDto categoryDto, Category category) {
        categoryDto.setLinks(ResourceUtils.generateSelfLinks(CATEGORY_RESOURCE_URI, category));
    }

}
