package com.eprovement.poptavka.server.service.locality;


import com.eprovement.poptavka.client.service.demand.LocalityRPCService;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.address.LocalityService;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component(LocalityRPCService.URL)
public class LocalityRPCServiceImpl extends AutoinjectingRemoteService implements LocalityRPCService {

    private LocalityService localityService;

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalityRPCServiceImpl.class);

    @Autowired
    public void setLocalityService(LocalityService localityService) {
        this.localityService = localityService;
    }

    @Override
    public ArrayList<LocalityDetail> getLocalities(LocalityType type) throws RPCException {
        List<Locality>  localities =  localityService.getLocalities(type);
        System.out.println(localities.size());
        return createLocalityDetails(localities);
    }

    /**
     * Get children of locality specified by LOCALITY_CODE.
     */
    @Override
    public ArrayList<LocalityDetail> getLocalities(String locCode) throws RPCException {
        LOGGER.info("Getting children localities ");
        final Locality locality = localityService.getLocality(locCode);
        if (locality != null) {
            return createLocalityDetails(locality.getChildren());
        }
        return new ArrayList<LocalityDetail>();
    }

    /** converts domain entities to front-end classes. **/
    private ArrayList<LocalityDetail> createLocalityDetails(List<Locality> localities) {
        ArrayList<LocalityDetail> localityDetails = new ArrayList<LocalityDetail>();

        for (Locality loc : localities) {
            localityDetails.add(new LocalityDetail(loc.getId(), loc.getName(), loc.getCode()));
        }

        return localityDetails;
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
    public ArrayList<LocalityDetail> getAllRootLocalities() throws RPCException {
        return createLocalityDetails(localityService.getLocalities(LocalityType.REGION));
    }

    @Override
    public ArrayList<LocalityDetail> getSubLocalities(String locCode) throws RPCException {
        return createLocalityDetails(localityService.getLocality(locCode).getChildren());
    }
}
