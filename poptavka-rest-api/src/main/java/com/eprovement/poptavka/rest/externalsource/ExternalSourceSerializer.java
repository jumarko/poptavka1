package com.eprovement.poptavka.rest.externalsource;

import com.eprovement.poptavka.domain.common.ExternalSource;
import com.eprovement.poptavka.domain.demand.ExternalCategory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExternalSourceSerializer implements Converter<ExternalSource, ExternalSourceDto> {


    @Override
    public ExternalSourceDto convert(ExternalSource externalSource) {
//        if (externalSource == null) {
//            return null;
//        }
//        final ExternalCategoryDto dto = new ExternalCategoryDto();
//        dto.setExternalCategoryId(externalCategory.getExternalId());
//        dto.setExternalSourceCode(externalCategory.getExternalSource().getCode());
//        dto.setCategories(categorySerializer.convertCategories(externalCategory.getCategories()));
//        return dto;
        return null;
    }

    public List<ExternalCategoryDto> convertCategories(List<ExternalCategory> categories) {
//        if (CollectionUtils.isEmpty(categories)) {
//            return Collections.emptyList();
//        }
//
//        final List<ExternalCategoryDto> categoriesDtos = new ArrayList<>();
//        for (ExternalCategory category : categories) {
//            categoriesDtos.add(convert(category));
//        }
//        return categoriesDtos;
        return null;
    }

}
