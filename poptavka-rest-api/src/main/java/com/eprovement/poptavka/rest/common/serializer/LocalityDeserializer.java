package com.eprovement.poptavka.rest.common.serializer;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.apache.commons.lang.Validate.notNull;

import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.rest.common.dto.LocalityDto;
import com.eprovement.poptavka.service.address.LocalityService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class LocalityDeserializer implements Converter<LocalityDto, Locality> {

    private final LocalityService localityService;

    public LocalityDeserializer(LocalityService localityService) {
        notNull(localityService, "localityService cannot be null!");
        this.localityService = localityService;
    }


    public List<Locality> convertLocalities(Collection<LocalityDto> localityDtos) {
        final List<Locality> localities = new ArrayList<>();
        for (LocalityDto localityDto : localityDtos) {
            localities.add(convert(localityDto));
        }
        return localities;
    }

    @Override
    public Locality convert(LocalityDto localityDto) {
        // try to find locality - ID always takes a precedence
        Locality locality;
        if (localityDto.getId() != null) {
            locality = localityService.getLocality(localityDto.getId());
        } else if (isNotEmpty(localityDto.getCity())) {
            locality = findCity(localityDto);
        } else if (isNotEmpty(localityDto.getDistrict())) {
            locality = localityService.findDistrictByName(localityDto.getRegion(), localityDto.getDistrict());
        } else {
            locality = localityService.findRegion(localityDto.getRegion());
        }
        notNull(locality, "No locality has been found for dto=" + localityDto);
        return locality;
    }

    private Locality findCity(LocalityDto localityDto) {
        if (isNotEmpty(localityDto.getRegion()) && isNotEmpty(localityDto.getDistrict())) {
            // we have full locality info (region, district, city) - use it!
            return localityService.findCityByName(localityDto.getRegion(), localityDto.getDistrict(),
                localityDto.getCity());
        } else  if (isNotEmpty(localityDto.getZipCode())) {
            // (city, zipcode) should be a unique identifier - try to find proper locality
            // this case is especially frequent in data imports (demands, suppliers) from external systems
            return localityService.findCityByZipCode(localityDto.getCity(), localityDto.getZipCode());
        }
        return null;
    }
}
