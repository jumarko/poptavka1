package com.eprovement.poptavka.rest.externalsource;

import static com.eprovement.poptavka.rest.common.ResourceUtils.generateSelfLinks;
import static org.apache.commons.lang.Validate.notEmpty;
import static org.apache.commons.lang.Validate.notNull;

import com.eprovement.poptavka.domain.common.ExternalSource;
import com.eprovement.poptavka.rest.common.dto.CategoryDto;
import com.eprovement.poptavka.service.demand.CategoryService;
import com.eprovement.poptavka.service.register.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(ExternalSourceResource.SOURCES_RESOURCE_URI)
public class ExternalSourceResource {

    public static final String SOURCES_RESOURCE_URI = "/sources";

    private final CategoryService categoryService;
    private final SourceCategoryMappingSerializer categoryMappingSerializer;

    private final RegisterService registerService;
    private final ExternalSourceSerializer sourceSerializer;

    @Autowired
    public ExternalSourceResource(CategoryService categoryService,
                                  SourceCategoryMappingSerializer categoryMappingSerializer,
                                  RegisterService registerService,
                                  ExternalSourceSerializer sourceSerializer) {
        notNull(categoryService, "categoryService cannot be null!");
        notNull(categoryMappingSerializer, "categoryMappingSerializer cannot be null!");
        notNull(registerService, "registerService cannot be null");
        notNull(sourceSerializer, "sourceSerializer cannot be null!");
        this.categoryService = categoryService;
        this.categoryMappingSerializer = categoryMappingSerializer;
        this.registerService = registerService;
        this.sourceSerializer = sourceSerializer;
    }



    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    List<ExternalSourceDto> listSources() {
        final List<ExternalSource> sources = registerService.getAllValues(ExternalSource.class);
        return sourceSerializer.convertList(sources);
    }


    @RequestMapping(value = "/{source}", method = RequestMethod.GET)
    public @ResponseBody
    CategoryDto getSourceByName(@PathVariable("source") String sourceName) {
        notEmpty(sourceName, "source name cannot be empty!");

        // TODO: implemente me!
//        final Category category = this.categoryService.getById(id);
//        final CategoryDto categoryDto = this.categorySerializer.convert(category);
//        setLinks(categoryDto, category);
//        return categoryDto;
        return null;
    }

    @RequestMapping(value = "/{source}/categorymapping", method = RequestMethod.GET)
    public @ResponseBody
    SourceCategoryMappingDto getCategoryMapping(@PathVariable("source") String externalSourceName) {
        notEmpty(externalSourceName, "external source name has to be specified ");
        final SourceCategoryMappingDto mappingDto =
                categoryMappingSerializer.convert(categoryService.getCategoryMapping(externalSourceName));
        mappingDto.setSource(externalSourceName);
        mappingDto.setLinks(generateSelfLinks(SOURCES_RESOURCE_URI + "/" + externalSourceName, "categorymapping"));
        return mappingDto;
    }

}
