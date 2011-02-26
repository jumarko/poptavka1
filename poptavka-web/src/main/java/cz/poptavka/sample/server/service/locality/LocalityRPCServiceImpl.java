package cz.poptavka.sample.server.service.locality;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import cz.poptavka.sample.client.service.demand.LocalityRPCService;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.address.LocalityService;
import cz.poptavka.sample.service.common.TreeItemService;
import cz.poptavka.sample.shared.domain.LocalityDetail;

public class LocalityRPCServiceImpl extends AutoinjectingRemoteService implements LocalityRPCService {

    private LocalityService localityService;
    private TreeItemService treeItemService;

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalityRPCServiceImpl.class);

    @Autowired
    @Required
    public void setLocalityService(LocalityService localityService) {
        this.localityService = localityService;
    }

    @Autowired
    @Required
    public void setTreeItemService(TreeItemService treeItemService) {
        this.treeItemService = treeItemService;
    }

    @Override
    public ArrayList<LocalityDetail> getLocalities(LocalityType type) {
        List<Locality>  localities =  localityService.getLocalities(type);

        return createLocalityDetails(localities);
    }

    @Override
    public List<LocalityDetail> getLocalities(String locCode) {
        LOGGER.info("Getting children localities ");

        Locality locality = localityService.getLocality(locCode);

        List<Locality> locs = treeItemService.getAllChildren(locality, Locality.class);

        return createLocalityDetails(locs);
    }

    private ArrayList<LocalityDetail> createLocalityDetails(List<Locality> localities) {
        ArrayList<LocalityDetail> localityDetails = new ArrayList<LocalityDetail>();

        for (Locality loc : localities) {
            localityDetails.add(new LocalityDetail(loc.getName(), loc.getCode()));
        }

        return localityDetails;
    }

}
