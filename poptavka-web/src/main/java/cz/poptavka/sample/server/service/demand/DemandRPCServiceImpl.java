/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.server.service.demand;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import cz.poptavka.sample.client.service.demand.DemandRPCService;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.demand.DemandStatus;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.demand.DemandService;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;

/**
 * @author Excalibur
 */
public class DemandRPCServiceImpl extends AutoinjectingRemoteService implements DemandRPCService {
    private static final Logger LOGGER = Logger.getLogger(DemandRPCServiceImpl.class.getName());
    /**
     * generated serialVersonUID.
     */
    private static final long serialVersionUID = -4661806018739964100L;
    private DemandService demandService;
    private GeneralService generalService;

    public DemandService getDemandService() {
        return demandService;
    }

    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    @Autowired
    public void setDemandService(DemandService demandService) {
        this.demandService = demandService;
    }

    @Override
    public String createNewDemand(DemandDetail detail, Long cliendId) {
        final Demand demand = new Demand();
        demand.setTitle(detail.getTitle());
        demand.setDescription(detail.getDescription());
        demand.setType(this.demandService.getDemandType(detail.getDemandType()));
        demand.setPrice(detail.getPrice());
        demand.setMaxSuppliers(detail.getMaxOffers());
        demand.setMinRating(detail.getMinRating());
        demand.setStatus(DemandStatus.NEW);
        demand.setEndDate(detail.getEndDate());
        demand.setValidTo(detail.getExpireDate());

        demand.setClient(this.generalService.find(Client.class, cliendId));
        System.out.println("RPCImpl Client: " + (demand.getClient() == null));
        demandService.create(demand);
        return "Done";
    }

    @Override
    public DemandDetail updateDemand(DemandDetail newDemand) {
        // TODO ivlcek - update entity by sa mal robit jednoduchsie ako toto?
        Demand demand = demandService.getById(newDemand.getId());
        // TODO ivlcek - vytvor metody, ktora nastetuje Demand objekt z DemandDetail
        demand.setTitle(newDemand.getTitle());
        demandService.update(demand);
        return newDemand;
    }

    @Override
    public List<DemandDetail> getAllDemands() {
        List<DemandDetail> demandDetails = new ArrayList<DemandDetail>();
        for (Demand demand : demandService.getAll()) {
            demandDetails.add(DemandDetail.createDemandDetail(demand));
        }
        return demandDetails;
    }

    @Override
    public Set<Demand> getDemands(Locality... localities) {
        return demandService.getDemands(localities);
    }

    @Override
    public Set<Demand> getDemands(Category... categories) {
        return demandService.getDemands(categories);
    }

    @Override
    public List<Demand> getDemands(ResultCriteria resultCriteria) {
        return demandService.getAll(resultCriteria);
    }

    @Override
    public Set<Demand> getDemands(ResultCriteria resultCriteria, Locality[] localities) {
        return demandService.getDemands(resultCriteria, localities);
    }

    @Override
    public Set<Demand> getDemands(ResultCriteria resultCriteria, Category[] categories) {
        return demandService.getDemands(resultCriteria, categories);
    }

    public ArrayList<DemandDetail> getClientDemands(long id) {
        Client client = this.generalService.find(Client.class, id);
        return this.toDemandDetailList(client.getDemands());
    }

    @Override
    public ArrayList<ArrayList<OfferDetail>> getDemandOffers(ArrayList<Long> idList) {
        ArrayList<ArrayList<OfferDetail>> offerList = new ArrayList<ArrayList<OfferDetail>>();
        for (Long id : idList) {
            Demand demand = demandService.getById(id);
            offerList.add(this.toOfferDetailList(demand.getOffers()));
        }
        return offerList;
    }
}
