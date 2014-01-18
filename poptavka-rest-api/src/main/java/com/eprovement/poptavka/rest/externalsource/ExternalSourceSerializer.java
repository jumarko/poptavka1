package com.eprovement.poptavka.rest.externalsource;

import com.eprovement.poptavka.domain.common.ExternalSource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ExternalSourceSerializer implements Converter<ExternalSource, ExternalSourceDto> {


    @Override
    public ExternalSourceDto convert(ExternalSource externalSource) {
        if (externalSource == null) {
            return null;
        }
        final ExternalSourceDto dto = new ExternalSourceDto();
        dto.setCode(externalSource.getCode());
        dto.setUrl(externalSource.getUrl());
        return dto;
    }

    public List<ExternalSourceDto> convertList(List<ExternalSource> sources) {
        if (CollectionUtils.isEmpty(sources)) {
            return Collections.emptyList();
        }

        final List<ExternalSourceDto> sourcesDtos = new ArrayList<>();
        for (ExternalSource source : sources) {
            sourcesDtos.add(convert(source));
        }
        return sourcesDtos;
    }

}
