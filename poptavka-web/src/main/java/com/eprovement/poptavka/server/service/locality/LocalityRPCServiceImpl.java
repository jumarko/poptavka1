package com.eprovement.poptavka.server.service.locality;

import com.eprovement.poptavka.client.service.demand.LocalityRPCService;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.LocalitySuggestionDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

@Configurable
public class LocalityRPCServiceImpl extends AutoinjectingRemoteService implements LocalityRPCService {

    private GeneralService generalService;
    private LocalityService localityService;
    private Converter<Locality, LocalityDetail> localityConverter;
    private Converter<Locality, LocalitySuggestionDetail> localitySuggestionConverter;
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalityRPCServiceImpl.class);

    @Autowired
    public void setLocalityService(LocalityService localityService) {
        this.localityService = localityService;
    }

    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
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
     * Get children of locality specified by LOCALITY_CODE.
     */
    @Override
    public List<LocalityDetail> getLocalities(String locCode) throws RPCException {
        LOGGER.info("Getting children localities ");
        final Locality locality = localityService.getLocality(locCode);
        if (locality != null) {
            return localityConverter.convertToTargetList(locality.getChildren());
        }
        return new ArrayList<LocalityDetail>();
    }

    @Override
    public LocalityDetail getLocality(long id) throws RPCException {
        final Locality locality = localityService.getById(id);
        if (locality == null) {
            throw new IllegalArgumentException("Cannot find locality with id=" + id);
        }
        return new LocalityDetail(locality.getId(), locality.getName(), locality.getCode());
    }

    @Override
    public List<LocalityDetail> getAllRootLocalities() throws RPCException {
        return localityConverter.convertToTargetList(localityService.getLocalities(LocalityType.REGION));
    }

    @Override
    public List<LocalityDetail> getSubLocalities(String locCode) throws RPCException {
        return localityConverter.convertToTargetList(localityService.getLocality(locCode).getChildren());
    }

    @Override
    public List<LocalitySuggestionDetail> getCityWithStateSuggestions(String cityLike) throws RPCException {
        Search locSearch = new Search(Locality.class);

        locSearch.addFilterAnd(
                Filter.equal("type", LocalityType.CITY),
                Filter.or(
                /**/Filter.ilike("name", cityLike + "%"),
                /**/ Filter.ilike("name", "% " + cityLike + "%")));
        List<Locality> list = generalService.search(locSearch);
        return localitySuggestionConverter.convertToTargetList(list);
    }

    @Override
    public List<LocalitySuggestionDetail> getShortCityWithStateSuggestions(String cityLike) throws RPCException {
        Search locSearch = new Search(Locality.class);

        locSearch.addFilterAnd(
                Filter.equal("type", LocalityType.CITY),
                Filter.ilike("name", "__"),
                Filter.or(
                /**/Filter.ilike("name", cityLike + "%"),
                /**/ Filter.ilike("name", "% " + cityLike + "%")));
        List<Locality> list = generalService.search(locSearch);

        List<LocalitySuggestionDetail> result = new ArrayList<LocalitySuggestionDetail>();
        LocalitySuggestionDetail d1 = new LocalitySuggestionDetail();
        d1.setCityId(1L);
        d1.setCityName("AB");
        d1.setStateId(2L);
        d1.setStateName("Alabama");
        if ("AB".contains(cityLike)) {
            result.add(d1);
        }

        LocalitySuggestionDetail d2 = new LocalitySuggestionDetail();
        d2.setCityId(3L);
        d2.setCityName("CD");
        d2.setStateId(4L);
        d2.setStateName("California");
        if ("CD".contains(cityLike)) {
            result.add(d2);
        }

//        return localitySuggestionConverter.convertToTargetList(list);
        return result;
    }
}
