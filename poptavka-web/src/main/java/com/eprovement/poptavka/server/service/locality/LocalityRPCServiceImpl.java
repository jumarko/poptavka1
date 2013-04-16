package com.eprovement.poptavka.server.service.locality;

import com.eprovement.poptavka.client.service.demand.LocalityRPCService;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.LocalitySuggestionDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Configurable
public class LocalityRPCServiceImpl extends AutoinjectingRemoteService implements LocalityRPCService {

    private LocalityService localityService;
    private Converter<Locality, LocalityDetail> localityConverter;
    private Converter<Locality, LocalitySuggestionDetail> localitySuggestionConverter;

    @Autowired
    public void setLocalityService(LocalityService localityService) {
        this.localityService = localityService;
    }

    @Autowired
    public void setLocalityConverter(
            @Qualifier("localityConverter") Converter<Locality, LocalityDetail> localityConverter) {
        this.localityConverter = localityConverter;
    }

    @Autowired
    public void setLocalitySuggestionConverter(
            @Qualifier("localitySuggestionConverter") Converter<
                    Locality, LocalitySuggestionDetail> localitySuggestionConverter) {
        this.localitySuggestionConverter = localitySuggestionConverter;
    }

    @Override
    public List<LocalityDetail> getLocalities(LocalityType type) throws RPCException {
        List<Locality> localities = localityService.getLocalities(type);
        return localityConverter.convertToTargetList(localities);
    }

    /**
     * Get children of locality specified by LOCALITY_ID.
     */
    @Override
    public List<LocalityDetail> getSubLocalities(Long id) throws RPCException {
        return localityConverter.convertToTargetList(localityService.getSubLocalities(id));
    }

    @Override
    public LocalityDetail getLocality(long id) throws RPCException {
        final Locality locality = localityService.getById(id);
        if (locality == null) {
            throw new IllegalArgumentException("Cannot find locality with id=" + id);
        }
        return new LocalityDetail(locality.getName(), locality.getId());
    }

    @Override
    public List<LocalityDetail> getAllRootLocalities() throws RPCException {
        return localityConverter.convertToTargetList(localityService.getLocalities(LocalityType.REGION));
    }

    @Override
    public List<LocalitySuggestionDetail> getCityWithStateSuggestions(
            String cityLike, int wordLength) throws RPCException {
        return localitySuggestionConverter.convertToTargetList(
                localityService.getLocalitiesByMinLength(wordLength, cityLike, LocalityType.CITY));
    }

    @Override
    public List<LocalitySuggestionDetail> getShortCityWithStateSuggestions(
            String cityLike, int wordLength) throws RPCException {
        return localitySuggestionConverter.convertToTargetList(
                localityService.getLocalitiesByMaxLengthExcl(wordLength, cityLike, LocalityType.CITY));
    }
}
