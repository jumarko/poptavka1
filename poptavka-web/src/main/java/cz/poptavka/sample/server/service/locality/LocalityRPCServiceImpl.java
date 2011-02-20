package cz.poptavka.sample.server.service.locality;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import cz.poptavka.sample.client.service.demand.LocalityRPCService;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.address.LocalityService;
import java.util.List;

public class LocalityRPCServiceImpl extends AutoinjectingRemoteService implements LocalityRPCService {

    private LocalityService localityService;
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalityRPCServiceImpl.class);

    @Override
    public List<Locality> getLocalities(LocalityType type) {
        List<Locality>  localities =  localityService.getLocalities(type);
        return localities;
    }

    @Autowired
    @Required
    public void setLocalityService(LocalityService localityService) {
        this.localityService = localityService;
    }

}
