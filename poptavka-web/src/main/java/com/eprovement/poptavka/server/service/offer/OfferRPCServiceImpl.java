/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.server.service.offer;

import com.eprovement.poptavka.server.converter.Converter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

import com.eprovement.poptavka.client.service.demand.OfferRPCService;
import com.eprovement.poptavka.dao.message.MessageFilter;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.offer.Offer;
import com.eprovement.poptavka.domain.offer.OfferState;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.audit.AuditService;
import com.eprovement.poptavka.service.demand.DemandService;
import com.eprovement.poptavka.service.message.MessageService;
import com.eprovement.poptavka.service.offer.OfferService;
import com.eprovement.poptavka.service.user.ClientService;
import com.eprovement.poptavka.service.user.SupplierService;
import com.eprovement.poptavka.service.usermessage.UserMessageService;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.demand.OfferDemandDetail;
import com.eprovement.poptavka.shared.domain.offer.FullOfferDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;

import java.util.Date;

/**
 *
 * @author ivan.vlcek
 */
@Configurable
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

    private Converter<Message, FullOfferDetail> fullOfferConverter;

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

    @Autowired
    public void setFullOfferConverter(
            @Qualifier("fullOfferConverter") Converter<Message, FullOfferDetail> fullOfferConverter) {
        this.fullOfferConverter = fullOfferConverter;
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
    public ArrayList<OfferDemandDetail> getClientDemands(long clientId) throws RPCException {
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
    public ArrayList<FullOfferDetail> getDemandOffers(long demandId, long threadRootId) throws RPCException {
//        List<Offer> offers = this.demandService.getById(demandId).getOffers();
        ArrayList<FullOfferDetail> offerDetails = new ArrayList<FullOfferDetail>();
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

            FullOfferDetail fullOfferDetail = fullOfferConverter.convertToTarget(message);

            List<Number> revisions = auditService.getRevisions(Offer.class, message.getOffer().getId());
            //get first occurance, should by the oldest
            Date createdDate = auditService.getRevisionDate(revisions.get(0));

            fullOfferDetail.getOfferDetail().setCreatedDate(createdDate);
            // TODO ivlcek what is this?
            fullOfferDetail.setRead(userMessage.isRead());

            offerDetails.add(fullOfferDetail);

        }
        return offerDetails;
    }

    @Override
    public OfferDetail changeOfferState(OfferDetail offerDetail) throws RPCException {
        System.out.println("OFFER ID: " + offerDetail.getId());
        System.out.println("is service: " + (generalService == null));

        Offer offer = this.generalService.find(Offer.class, offerDetail.getId());

        System.out.println("is Offer null: " + (offer == null));

        OfferState offerState = this.offerService.getOfferState(offerDetail.getState().getValue());
        offer.setState(offerState);
        offer = (Offer) this.generalService.save(offer);
        offerDetail.setState(offer.getState().getType());
        return offerDetail;
    }

    @Override
    public FullOfferDetail updateOffer(FullOfferDetail newOffer) throws RPCException {
        Offer offer = offerService.getById(newOffer.getOfferDetail().getId());

        OfferState state = offerService.getOfferState(newOffer.getOfferDetail().getState().getValue());
        if (state != null) {
            offer.setState(state);
        }
        offer.setPrice((BigDecimal) newOffer.getOfferDetail().getPrice());
        offer.setFinishDate(newOffer.getOfferDetail().getFinishDate());

        offerService.update(offer);
        return newOffer;
    }
}
