package com.eprovement.poptavka.rest.externalsource;

import static com.eprovement.poptavka.rest.common.ResourceUtils.generateSelfLinks;
import static org.apache.commons.lang.Validate.notEmpty;
import static org.apache.commons.lang.Validate.notNull;

import com.eprovement.poptavka.domain.common.ExternalSource;
import com.eprovement.poptavka.domain.common.UnknownSourceException;
import com.eprovement.poptavka.rest.ResourceNotFoundException;
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
    ExternalSourceDto getSourceByName(@PathVariable("source") String sourceCode) {
        notEmpty(sourceCode, "source code cannot be empty!");
        final ExternalSource source = registerService.getValue(sourceCode, ExternalSource.class);
        if (source == null) {
            throw new ResourceNotFoundException("External source '" + sourceCode + "' has not been found!");
        }
        return sourceSerializer.convert(source);
    }

    @RequestMapping(value = "/{source}/categorymapping", method = RequestMethod.GET)
    public @ResponseBody
    SourceCategoryMappingDto getCategoryMapping(@PathVariable("source") String externalSourceCode) {
        notEmpty(externalSourceCode, "external source name has to be specified ");
        final SourceCategoryMappingDto mappingDto;
        try {
            mappingDto = categoryMappingSerializer.convert(categoryService.getCategoryMapping(externalSourceCode));
        } catch (UnknownSourceException use) {
            throw new ResourceNotFoundException("External source '" + externalSourceCode + "' has not been found!");
        }

        mappingDto.setSource(externalSourceCode);
        mappingDto.setLinks(generateSelfLinks(SOURCES_RESOURCE_URI + "/" + externalSourceCode, "categorymapping"));
        return mappingDto;
    }

}
