package com.eprovement.poptavka.rest.common.serializer;

import static org.apache.commons.lang.Validate.notNull;

import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.rest.common.dto.LocalityDto;
import com.eprovement.poptavka.service.address.LocalityService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class LocalityDeserializer {

    private final LocalityService localityService;

    public LocalityDeserializer(LocalityService localityService) {
        notNull(localityService, "localityService cannot be null!");
        this.localityService = localityService;
    }


    public List<Locality> convertLocalities(Collection<LocalityDto> localityDtos) {
        final List<Locality> localities = new ArrayList<>();
        for (LocalityDto localityDto : localityDtos) {
            final Locality locality;
            // try to find locality - ID always takes a precedence
            if (localityDto.getId() != null) {
                locality = localityService.getLocality(localityDto.getId());
            } else if (StringUtils.isNotEmpty(localityDto.getCity())) {
                locality = localityService.findCityByName(localityDto.getRegion(), localityDto.getDistrict(),
                        localityDto.getCity());
            } else if (StringUtils.isNotEmpty(localityDto.getDistrict())) {
                locality = localityService.findDistrictByName(localityDto.getRegion(), localityDto.getDistrict());
            } else {
                locality = localityService.findRegion(localityDto.getRegion());
            }
            notNull(locality, "No locality has been found for dto=" + localityDto);
            localities.add(locality);

        }
        return localities;
    }

}
