/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.server.service.offer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import cz.poptavka.sample.client.service.demand.OfferRPCService;
import cz.poptavka.sample.dao.message.MessageFilter;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.domain.offer.Offer;
import cz.poptavka.sample.domain.offer.OfferState;
import cz.poptavka.sample.domain.user.BusinessUser;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.demand.DemandService;
import cz.poptavka.sample.service.message.MessageService;
import cz.poptavka.sample.service.offer.OfferService;
import cz.poptavka.sample.service.user.ClientService;
import cz.poptavka.sample.service.user.SupplierService;
import cz.poptavka.sample.service.usermessage.UserMessageService;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.demand.OfferDemandDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.offer.FullOfferDetail;

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
    private UserMessageService userMessageService;
    private OfferService offerService;
    private SupplierService supplierSerivce;

    @Autowired
    public void setUserMessageService(UserMessageService userMessageService) {
        this.userMessageService = userMessageService;
    }

    @Autowired
    public void setOfferService(OfferService offerService) {
        this.offerService = offerService;
    }

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

    public void setSupplierSerivce(SupplierService supplierSerivce) {
        this.supplierSerivce = supplierSerivce;
    }

    /**
     * Metoda vrati vsetky klientove poptavky (poptavkove spravy), ktore obdrzali
     * aspon jednu ponuku (ponukovu spravu) od nejakeho dodavatela. Tieto poptavkove
     * spravy su reprezentovane detail objektom DemandMessageWithOffersDetail. Klient tieto
     * poptavky vidi v svojom pohlade:
     * MENU KLIENTA -> PONUKY (vid wireframes)
     *
     * @param clientId
     * @return zoznam DemandMessageWithOffersDetail objektov
     */
    @Override
    public ArrayList<OfferDemandDetail> getClientDemands(long clientId) {
        Client client = this.clientService.getById(clientId);
        ArrayList<OfferDemandDetail> offerDemandDetails = new ArrayList<OfferDemandDetail>();
        List<Demand> demands = client.getDemands();
        for (Demand demand : demands) {
            OfferDemandDetail offerDemandDetail = new OfferDemandDetail();
            // TODO - set to bold if new
            // TODO load userMessage
            offerDemandDetail.setDemandId(demand.getId());
            offerDemandDetail.setEndDate(demand.getEndDate());
            offerDemandDetail.setValidToDate(demand.getValidTo());
            offerDemandDetail.setMaxOffers(demand.getMaxSuppliers());
            offerDemandDetail.setPrice(demand.getPrice());
            offerDemandDetail.setTitle(demand.getTitle());
            // TODO ivlcek - ked si pytam len size tak nacitavaju HIbernaty vsekty Offers? dufam, ze nie
            // inak to musime prekopat do servisy, ktora vrati len pocet
            offerDemandDetail.setNumberOfOffers(demand.getOffers().size());
            offerDemandDetail.setMessageId(this.messageService.getThreadRootMessage(demand).getId());
            offerDemandDetails.add(offerDemandDetail);
        }
        return offerDemandDetails;
    }

    @Override
    public ArrayList<OfferDetail> getDemandOffers(long demandId, long threadRootId) {
//        List<Offer> offers = this.demandService.getById(demandId).getOffers();
        ArrayList<OfferDetail> offerDetails = new ArrayList<OfferDetail>();
        Message threadRoot = this.messageService.getById(threadRootId);
        ArrayList<Message> messages = (ArrayList<Message>) this.messageService.getAllOfferMessagesForDemand(threadRoot);
        // retrieve userMessages for each message
        if (messages.isEmpty()) {
            return offerDetails;
        }
        List<UserMessage> userMessages = this.userMessageService.getUserMessages(
                messages, (BusinessUser) threadRoot.getSender(), MessageFilter.EMPTY_FILTER);
        userMessages.size();

        for (UserMessage userMessage : userMessages) {
            Message message = userMessage.getMessage();
            OfferDetail o = new OfferDetail();
            Offer offer = message.getOffer();
            o.setOfferId(offer.getId());
            o.setMessageDetail(MessageDetail.createMessageDetail(message));
            // TODO zmenit LOng na maly long v setovacej metode
            o.setDemandId(Long.valueOf(demandId));
            // TODO ivlcek what is this?
            o.setIsRead(userMessage.isIsRead());
            o.setFinishDate(offer.getFinishDate());
            o.setMessageId(message.getId());
            // TODO add offer state to OfferDetail
            o.setState(offer.getState().getCode());
            o.setPrice(offer.getPrice());
            o.setSupplierId(offer.getSupplier().getId());
            o.setSupplierName(offer.getSupplier().getBusinessUser().getBusinessUserData().getCompanyName());
            offerDetails.add(o);

        }
        return offerDetails;
    }

    @Override
    public OfferDetail changeOfferState(OfferDetail offerDetail) {
        System.out.println("OFFER ID: " + offerDetail.getOfferId());
        System.out.println("is service: " + (generalService == null));

        Offer offer = this.generalService.find(Offer.class, offerDetail.getOfferId());

        System.out.println("is Offer null: " + (offer == null));

        OfferState offerState = this.offerService.getOfferState(offerDetail.getState());
        offer.setState(offerState);
        offer = (Offer) this.generalService.save(offer);
        offerDetail.setState(offer.getState().getCode());
        return offerDetail;
    }

    @Override
    public FullOfferDetail updateOffer(FullOfferDetail newOffer) {
        Offer offer = offerService.getById(newOffer.getOfferId());
        //      demand.setCategories(null);
        offer.setSupplier(supplierSerivce.getById(newOffer.getSupplierId()));
        //      demand.setDescription(null);
        offer.setFinishDate(newOffer.getFinishDate());
        //      demand.setExcludedSuppliers(null);
        //      demand.setLocalities(null);
        offerService.update(offer);
        return newOffer;
    }
}
