package com.eprovement.poptavka.rest.locality;

import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.rest.common.ResourceUtils;
import com.eprovement.poptavka.rest.common.dto.LocalityDto;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class LocalitySerializer implements Converter<Locality, LocalityDto> {
    @Override
    // need to be transactional because it calls locality.getParent()
    @Transactional(readOnly = true)
    public LocalityDto convert(Locality locality) {
        Validate.notNull(locality);

        final LocalityDto localityDto = new LocalityDto();
        localityDto.setId(locality.getId());
        localityDto.setType(locality.getType());
        localityDto.setLinks(ResourceUtils.generateSelfLinks(LocalityResource.LOCALITY_RESOURCE_URI, locality));

        switch (locality.getType()) {
            case COUNTRY:
                // nothing to do - this locality represents the whole country (for example USA)
                break;
            case REGION:
                localityDto.setRegion(locality.getName());
                break;
            case DISTRICT:
                localityDto.setRegion(locality.getParent().getName());
                localityDto.setDistrict(locality.getName());
                break;
            case CITY:
                localityDto.setRegion(locality.getParent().getParent().getName());
                localityDto.setDistrict(locality.getParent().getName());
                localityDto.setCity(locality.getName());
                break;
            default:
                throw new IllegalStateException("Unknown locality type=" + locality.getType());
        }

        return localityDto;
    }


    // need to be transactional because it calls convertLocality (which requires transaction)
    // but internal method calls dont go through transactional proxy
    @Transactional(readOnly = true)
    public List<LocalityDto> convertLocalities(List<Locality> localities) {
        if (CollectionUtils.isEmpty(localities)) {
            return Collections.emptyList();
        }

        final List<LocalityDto> localitiesDtos = new ArrayList<>();
        for (Locality locality : localities) {
            final LocalityDto localityDto = convert(locality);
            localityDto.setLinks(ResourceUtils.generateSelfLinks(LocalityResource.LOCALITY_RESOURCE_URI, locality));
            localitiesDtos.add(localityDto);
        }
        return localitiesDtos;
    }

}
