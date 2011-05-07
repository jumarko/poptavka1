/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.server.service.demand;

import cz.poptavka.sample.client.service.demand.DemandRPCService;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.demand.DemandStatus;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.demand.DemandService;
import cz.poptavka.sample.service.user.ClientService;
import cz.poptavka.sample.shared.domain.DemandDetail;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author Excalibur
 */
public class DemandRPCServiceImpl extends AutoinjectingRemoteService implements DemandRPCService {


    private static final Logger LOGGER = Logger
            .getLogger(DemandRPCServiceImpl.class.getName());

    /**
     * generated serialVersonUID.
     */
    private static final long serialVersionUID = -4661806018739964100L;

    private DemandService demandService;

    private ClientService clientService;

    public DemandService getDemandService() {
        return demandService;
    }


    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Autowired
    public void setDemandService(DemandService demandService) {
        this.demandService = demandService;
    }

    private ArrayList<DemandDetail> toArrayList(List<Demand> list) {
        ArrayList<DemandDetail> details = new ArrayList<DemandDetail>();
        for (Demand demand : list) {
            DemandDetail detail = new DemandDetail();
            details.add(detail);
        }
        return details;
    }

    @Override
    public String createNewDemand(DemandDetail detail, Long cliendId) {
        final Demand demand = new Demand();
        demand.setTitle(detail.getTitle());
        demand.setDescription(detail.getDescription());
        demand.setType(this.demandService.getDemandType(detail.getDemandType()));
        demand.setPrice(BigDecimal.valueOf(detail.getPrice()));
        demand.setMaxSuppliers(detail.getMaxOffers());
        demand.setMinRating(detail.getMinRating());
        demand.setStatus(DemandStatus.NEW);
        demand.setEndDate(detail.getEndDate());
        demand.setValidTo(detail.getExpireDate());
        demand.setClient(clientService.getById(cliendId));
        demandService.create(demand);
        return "Done";
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
