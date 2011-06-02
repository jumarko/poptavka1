/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.server.service.offer;

import cz.poptavka.sample.client.service.demand.OfferRPCService;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.domain.offer.Offer;
import cz.poptavka.sample.domain.offer.OfferState;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.demand.DemandService;
import cz.poptavka.sample.service.message.MessageService;
import cz.poptavka.sample.service.user.ClientService;
import cz.poptavka.sample.shared.domain.MessageDetail;
import cz.poptavka.sample.shared.domain.OfferDemandDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;
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
    private MessageService messageService;
    private ClientService clientService;

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

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
        Client client = this.clientService.getById(clientId);
        ArrayList<OfferDemandDetail> offerDemandDetails = new ArrayList<OfferDemandDetail>();
        List<Demand> demands = client.getDemands();
        for (Demand demand : demands) {
            OfferDemandDetail offerDemandDetail = new OfferDemandDetail();
            // TODO - set to bold if new
            // TODO load userMessage
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
            offerDemandDetail.setThreadRootId(this.messageService.getThreadRootMessage(demand).getId());
            offerDemandDetails.add(offerDemandDetail);
        }
        return offerDemandDetails;
    }

    @Override
    public ArrayList<OfferDetail> getDemandOffers(long demandId, long threadRootId) {
        List<Offer> offers = this.demandService.getById(demandId).getOffers();
        Message threadRoot = this.messageService.getById(threadRootId);
        // todo get all children messages where OfferId is not null -> all offers to this demand
        ArrayList<Message> messages = (ArrayList<Message>) this.messageService.getAllOfferMessagesForDemand(threadRoot);
        ArrayList<OfferDetail> offerDetails = new ArrayList<OfferDetail>();
        for (Message message : messages) {
            OfferDetail o = new OfferDetail();
            Offer offer = message.getOffer();
            o.setMessageDetail(MessageDetail.generateMessageDetail(message));
            // TODO zmenit LOng na maly long v setovacej metode
            o.setDemandId(Long.valueOf(demandId));
            // TODO ivlcek what is this?
            o.setDisplayed(true);
            o.setFinishDate(offer.getFinishDate());
            // TODO set messageDetail

            // TODO set messageId
            o.setMessageId(message.getId());
            // TODO add offer state to OfferDetail
            o.setPrice(offer.getPrice());
            o.setSupplierId(offer.getSupplier().getId());
            o.setSupplierName(offer.getSupplier().getBusinessUser().getBusinessUserData().getCompanyName());
            offerDetails.add(o);

        }
        return offerDetails;
    }

    public OfferDetail changeOfferState(OfferDetail offerDetail) {
        Offer offer = this.generalService.find(Offer.class, offerDetail.getOfferId());
        OfferState offerState = this.generalService.find(OfferState.class,
                Long.parseLong(offerDetail.getState().toString()));
        offer.setState(offerState);
        offer = (Offer) this.generalService.save(offer);
        offerDetail.setState(Integer.valueOf(offer.getState().getId().intValue()));
        return offerDetail;
    }
}
