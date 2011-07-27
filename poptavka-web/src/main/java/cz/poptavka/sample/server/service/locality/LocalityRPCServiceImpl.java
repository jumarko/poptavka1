package cz.poptavka.sample.server.service.locality;


import cz.poptavka.sample.client.service.demand.LocalityRPCService;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.address.LocalityService;
import cz.poptavka.sample.shared.domain.LocalityDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class LocalityRPCServiceImpl extends AutoinjectingRemoteService implements LocalityRPCService {

    private LocalityService localityService;

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalityRPCServiceImpl.class);

    @Autowired
    public void setLocalityService(LocalityService localityService) {
        this.localityService = localityService;
    }

    @Override
    public ArrayList<LocalityDetail> getLocalities(LocalityType type) {
        List<Locality>  localities =  localityService.getLocalities(type);
        System.out.println(localities.size());
        return createLocalityDetails(localities);
    }

    /**
     * Get children of locality specified by LOCALITY_CODE.
     */
    @Override
    public ArrayList<LocalityDetail> getLocalities(String locCode) {
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
    public Locality getLocality(long id) {
        return localityService.getById(id);
    }

}
