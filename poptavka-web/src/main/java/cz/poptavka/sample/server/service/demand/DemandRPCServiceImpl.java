/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.server.service.demand;

import cz.poptavka.sample.client.service.demand.DemandRPCService;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.demand.DemandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import java.util.List;
import java.util.Set;

/**
 * @author Excalibur
 */
public class DemandRPCServiceImpl extends AutoinjectingRemoteService implements DemandRPCService {

    /**
     * generated serialVersonUID.
     */
    private static final long serialVersionUID = -4661806018739964100L;

    private DemandService demandService;


    public DemandService getDemandService() {
        return demandService;
    }

    @Autowired
    @Required
    public void setDemandService(DemandService demandService) {
        this.demandService = demandService;
    }

    @Override
    public List<Demand> getAllDemands() {
        return demandService.getAll();
    }


    @Override
    public Set<Demand> getDemands(Locality... localities) {
        return demandService.getDemands(localities);
    }

    @Override
    public Set<Demand> getDemands(Category... categories) {
        return demandService.getDemands(categories);
    }
}
