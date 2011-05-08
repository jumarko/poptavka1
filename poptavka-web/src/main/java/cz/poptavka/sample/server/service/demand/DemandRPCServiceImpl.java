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
import cz.poptavka.sample.domain.ResultCriteria;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.demand.DemandStatus;
import cz.poptavka.sample.domain.offer.Offer;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.demand.DemandService;
import cz.poptavka.sample.service.user.ClientService;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;

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
            detail.setId(demand.getId());
            detail.setTitle(demand.getTitle());
            detail.setDescription(demand.getDescription());
            detail.setPrice(demand.getPrice());
            detail.setEndDate(demand.getEndDate());
            detail.setExpireDate(demand.getValidTo());
            detail.setMaxOffers(10);
            detail.setMinRating(10);
            ArrayList<String> catList = new ArrayList<String>();
            for (Category cat : demand.getCategories()) {
                catList.add(cat.getName());
            }
            detail.setCategories(catList);
            ArrayList<String> locList = new ArrayList<String>();
            for (Locality loc : demand.getLocalities()) {
                locList.add(loc.getName());
            }
            detail.setLocalities(locList);
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
        demand.setPrice(detail.getPrice());
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
        Client client = clientService.getById(id);
        return toArrayList(client.getDemands());
    }

    @Override
    public ArrayList<ArrayList<OfferDetail>> getDemandOffers(ArrayList<Long> idList) {
        ArrayList<ArrayList<OfferDetail>> offerList = new ArrayList<ArrayList<OfferDetail>>();
        for (Long id : idList) {
            Demand demand = demandService.getById(id);
            offerList.add(toOfferDetailList(demand.getOffers()));
        }
        return offerList;
    }

    private ArrayList<OfferDetail> toOfferDetailList(List<Offer> offerList) {
        ArrayList<OfferDetail> details = new ArrayList<OfferDetail>();
        for (Offer offer : offerList) {
            OfferDetail detail = new OfferDetail();
            detail.setDemandId(offer.getDemand().getId());
            detail.setFinishDate(offer.getFinishDate());
            detail.setPrice(offer.getPrice());
            detail.setSupplierId(offer.getSupplier().getId());
//            if (offer.getSupplier().getPerson() != null) {
//                detail.setSupplierName(offer.getSupplier().getPerson().getFirstName()
//                        + " " + offer.getSupplier().getPerson().getLastName());
//            }
            if (offer.getSupplier().getCompany() != null) {
                detail.setSupplierName(offer.getSupplier().getCompany().getName());
            } else {
                detail.setSupplierName("unknown");
            }

            details.add(detail);
        }
        return details;
    }
}
