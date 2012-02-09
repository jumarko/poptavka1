/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.server.service.offer;

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
import cz.poptavka.sample.service.audit.AuditService;
import cz.poptavka.sample.service.demand.DemandService;
import cz.poptavka.sample.service.message.MessageService;
import cz.poptavka.sample.service.offer.OfferService;
import cz.poptavka.sample.service.user.ClientService;
import cz.poptavka.sample.service.user.SupplierService;
import cz.poptavka.sample.service.usermessage.UserMessageService;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.demand.OfferDemandDetail;
import cz.poptavka.sample.shared.domain.offer.FullOfferDetail;
import java.util.ArrayList;
import java.util.Date;
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
    private UserMessageService userMessageService;
    private OfferService offerService;
    private SupplierService supplierService;
    private AuditService auditService;

    @Autowired
    public void setAuditService(AuditService auditService) {
        this.auditService = auditService;
    }

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

    public void setSupplierService(SupplierService supplierService) {
        this.supplierService = supplierService;
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
    public ArrayList<FullOfferDetail> getDemandOffers(long demandId, long threadRootId) {
//        List<Offer> offers = this.demandService.getById(demandId).getOffers();
        final Message threadRoot = this.messageService.getById(threadRootId);
        final ArrayList<Message> messages =
                (ArrayList<Message>) this.messageService.getAllOfferMessagesForDemand(threadRoot);
        // retrieve userMessages for each message
        if (messages.isEmpty()) {
            return new ArrayList<FullOfferDetail>(0);
        }
        final List<UserMessage> userMessages = this.userMessageService.getUserMessages(
                messages, (BusinessUser) threadRoot.getSender(), MessageFilter.EMPTY_FILTER);
        userMessages.size();

        final ArrayList<FullOfferDetail> offerDetails = new ArrayList<FullOfferDetail>();
        for (UserMessage userMessage : userMessages) {
            final Message message = userMessage.getMessage();

            final FullOfferDetail fullOfferDetail = FullOfferDetail.createOfferDetail(message);

            final List<Number> revisions = auditService.getRevisions(Offer.class, message.getOffer().getId());
            //get first occurrence, should by the oldest
            final Date createdDate = auditService.getRevisionDate(revisions.get(0));

            fullOfferDetail.getOfferDetail().setCreatedDate(createdDate);
            // TODO ivlcek what is this?
            fullOfferDetail.setIsRead(userMessage.isRead());

            offerDetails.add(fullOfferDetail);

        }
        return offerDetails;
    }

    @Override
    public OfferDetail changeOfferState(OfferDetail offerDetail) {
        System.out.println("OFFER ID: " + offerDetail.getId());
        System.out.println("is service: " + (generalService == null));

        Offer offer = this.generalService.find(Offer.class, offerDetail.getId());

        System.out.println("is Offer null: " + (offer == null));

        OfferState offerState = this.offerService.getOfferState(offerDetail.getState());
        offer.setState(offerState);
        offer = (Offer) this.generalService.save(offer);
        offerDetail.setState(offer.getState().getCode());
        return offerDetail;
    }

    @Override
    public FullOfferDetail updateOffer(FullOfferDetail newOffer) {
        Offer offer = offerService.getById(newOffer.getOfferDetail().getId());

        OfferState state = offerService.getOfferState(newOffer.getOfferDetail().getState());
        if (state != null) {
            offer.setState(state);
        }
        offer.setPrice(newOffer.getOfferDetail().getPrice());
        offer.setFinishDate(newOffer.getOfferDetail().getFinishDate());

        offerService.update(offer);
        return newOffer;
    }
}
