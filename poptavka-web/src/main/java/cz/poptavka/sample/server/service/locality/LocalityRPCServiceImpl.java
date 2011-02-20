package cz.poptavka.sample.server.service.locality;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import cz.poptavka.sample.client.service.demand.LocalityRPCService;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.address.LocalityService;

public class LocalityRPCServiceImpl extends AutoinjectingRemoteService implements LocalityRPCService {

    private LocalityService localityService;

    @Override
    public ArrayList<Locality> getLocalities(LocalityType type) {
        return (ArrayList<Locality>) localityService.getLocalities(type);
    }

    public LocalityService getLocalityService() {
        return localityService;
    }

    @Autowired
    public void setLocalityService(LocalityService localityService) {
        this.localityService = localityService;
    }

}
