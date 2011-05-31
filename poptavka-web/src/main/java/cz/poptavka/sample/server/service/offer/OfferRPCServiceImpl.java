/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.server.service.offer;

import cz.poptavka.sample.client.service.demand.OfferRPCService;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.demand.DemandService;
import cz.poptavka.sample.service.user.ClientService;
import cz.poptavka.sample.shared.domain.OfferDemandDetail;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ivan.vlcek
 */
public class OfferRPCServiceImpl extends AutoinjectingRemoteService implements OfferRPCService {

    /**
     * generated serialVersonUID.
     */
    private static final long serialVersionUID = -926580601239912398L;
    private DemandService demandService;
    private GeneralService generalService;
    private ClientService clientService;

    @Autowired
    public void setDemandService(DemandService demandService) {
        this.demandService = demandService;
    }

    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public ArrayList<OfferDemandDetail> getClientDemands(long clientId) {
        Client client = clientService.getById(clientId);
        ArrayList<OfferDemandDetail> offerDemandDetails = new ArrayList<OfferDemandDetail>();
        List<Demand> demands = client.getDemands();
        for (Demand demand : demands) {
            OfferDemandDetail offerDemandDetail = new OfferDemandDetail();
            // TODO - set to bold if new
            offerDemandDetail.setBold(true);
            offerDemandDetail.setDemandId(demand.getId());
            offerDemandDetail.setEndDate(demand.getEndDate());
            offerDemandDetail.setValidToDate(demand.getValidTo());
            offerDemandDetail.setMaxOffers(demand.getMaxSuppliers());
            offerDemandDetail.setPrice(demand.getPrice());
            offerDemandDetail.setTitle(demand.getTitle());
            // TODO ivlcek - ked si pytam len size tak nacitavaju HIbernaty vsekty Offers? dufam, ze nie
            // inak to musime prekopat do servisy, ktora vrati len pocet
            offerDemandDetail.setNumberOfOffers(demand.getOffers().size());
            offerDemandDetails.add(offerDemandDetail);
        }
        return offerDemandDetails;
    }
}
