package com.eprovement.poptavka.rest.externalsource;

import static org.apache.commons.lang.Validate.notNull;

import com.eprovement.poptavka.domain.demand.ExternalCategory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Converts external categories list to wrapper dto.
 * Only the {@link com.eprovement.poptavka.rest.externalsource.SourceCategoryMappingDto#mappings} are set.
 * The links and the source should be set outside of this class!
 */
@Service
public class SourceCategoryMappingSerializer implements Converter<List<ExternalCategory>, SourceCategoryMappingDto> {

    private final ExternalCategorySerializer externalCategorySerializer;

    public SourceCategoryMappingSerializer(ExternalCategorySerializer externalCategorySerializer) {
        notNull(externalCategorySerializer, "externalCategorySerializer cannot be null");
        this.externalCategorySerializer = externalCategorySerializer;
    }

    @Override
    public SourceCategoryMappingDto convert(List<ExternalCategory> source) {
        final SourceCategoryMappingDto mappingDto = new SourceCategoryMappingDto();
        mappingDto.setMappings(externalCategorySerializer.convertList(source));
        return mappingDto;
    }
}
