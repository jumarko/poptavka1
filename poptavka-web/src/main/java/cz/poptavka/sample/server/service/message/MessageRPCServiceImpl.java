/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.server.service.message;

import cz.poptavka.sample.client.service.demand.MessageRPCService;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.domain.message.MessageContext;
import cz.poptavka.sample.domain.message.MessageState;
import cz.poptavka.sample.domain.message.MessageUserRole;
import cz.poptavka.sample.domain.message.MessageUserRoleType;
import cz.poptavka.sample.domain.offer.Offer;
import cz.poptavka.sample.domain.offer.OfferState;
import cz.poptavka.sample.domain.user.BusinessUser;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.domain.user.User;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.message.MessageService;
import cz.poptavka.sample.service.usermessage.UserMessageService;
import cz.poptavka.sample.shared.domain.MessageDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ivan.vlcek
 */
public class MessageRPCServiceImpl extends AutoinjectingRemoteService implements MessageRPCService {

    // TODO ivlcek - konstanty nacitat cez lokalizovane rozhranie
    public static final String QUERY_TO_POTENTIAL_DEMAND_SUBJECT = "Dotaz na Vasu zadanu poptavku";
    public static final String OFFER_TO_POTENTIAL_DEMAND_SUBJECT = "Ponuka na vasu poptavku/nazov dodavatela";
    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -2239531608577928736L;
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageRPCServiceImpl.class);
    private GeneralService generalService;
    private MessageService messageService;
    private UserMessageService userMessageService;

    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setUserMessageService(UserMessageService userMessageService) {
        this.userMessageService = userMessageService;
    }

    /**
     * Message sent by supplier about a query to potential demand.
     * @param messageDetail
     * @return message
     */
    public MessageDetail sendQueryToPotentialDemand(MessageDetail messageDetail) {
        Message m = new Message();
        m.setBody(messageDetail.getBody());
        m.setCreated(new Date());
        m.setLastModified(new Date());
        m.setMessageState(MessageState.SENT);
        // TODO ivlcek - how to set this next sibling?
//        m.setNextSibling(null);
        Message parentMessage = this.messageService.getById(messageDetail.getParentId());
        m.setParent(parentMessage);
        User sender = this.generalService.find(User.class, messageDetail.getSenderId());
        m.setSender(sender);
        m.setSent(new Date());
        m.setSubject(QUERY_TO_POTENTIAL_DEMAND_SUBJECT);
        m.setThreadRoot(this.messageService.getById(messageDetail.getThreadRootId()));
        // set message roles
        List<MessageUserRole> messageUserRoles = new ArrayList<MessageUserRole>();
        // handles events when I send reply to my own message
        if (messageDetail.getSenderId() == messageDetail.getReceiverId()) {
            messageDetail.setReceiverId(m.getThreadRoot().getSender().getId().longValue());
        }
        // messageToUserRole
        MessageUserRole messageToUserRole = new MessageUserRole();
        messageToUserRole.setMessage(m);
        messageToUserRole.setUser(this.generalService.find(User.class, messageDetail.getReceiverId()));
        messageToUserRole.setType(MessageUserRoleType.TO);
        messageToUserRole.setMessageContext(MessageContext.QUERY_TO_POTENTIAL_SUPPLIERS_DEMAND);
        messageUserRoles.add(messageToUserRole);
        // messageFromUserRole
        MessageUserRole messageFromUserRole = new MessageUserRole();
        messageFromUserRole.setMessage(m);
        messageFromUserRole.setType(MessageUserRoleType.SENDER);
        messageFromUserRole.setMessageContext(MessageContext.QUERY_TO_POTENTIAL_SUPPLIERS_DEMAND);
        messageFromUserRole.setUser(sender);
        messageUserRoles.add(messageFromUserRole);
        m.setRoles(messageUserRoles);
        // TODO set the id correctly, check it
        MessageDetail messageDetailPersisted = MessageDetail.generateMessageDetail(this.messageService.create(m));
        // TODO set children for parent message - check if it is correct
        parentMessage.getChildren().add(m);
        parentMessage.setMessageState(MessageState.REPLY_RECEIVED);
        parentMessage = this.messageService.update(parentMessage);

        return messageDetailPersisted;
    }

    /**
     * Offer sent by supplier to potential demand.
     * TODO replace this into OfferRPCServiceImpl
     * @param offer
     * @return message
     */
    public OfferDetail sendOffer(OfferDetail offer) {
        Offer o = new Offer();
        o.setCreated(new Date());
        o.setDemand(this.generalService.find(Demand.class, offer.getDemandId()));
        o.setFinishDate(offer.getFinishDate());
        o.setPrice(offer.getPrice());
        // TODO ivlcek - load DB object by code and not by id
        o.setState(this.generalService.find(OfferState.class, 2L));
        o.setSupplier(this.generalService.find(Supplier.class, offer.getSupplierId()));
        o = this.generalService.save(o);

        Message m = new Message();
        MessageDetail messageDetail = offer.getMessageDetail();
        m.setBody(messageDetail.getBody());
        m.setCreated(new Date());
        m.setLastModified(new Date());
        m.setMessageState(MessageState.SENT);
        // TODO ivlcek - how to set this next sibling?
//        m.setNextSibling(null);
        Message parentMessage = this.messageService.getById(messageDetail.getThreadRootId());
        m.setParent(parentMessage);
        BusinessUser supplier = this.generalService.find(BusinessUser.class, messageDetail.getSenderId());
        m.setSender(supplier);
        m.setSent(new Date());
        m.setSubject(supplier.getBusinessUserData().getCompanyName());
        // TODO ivlcek - threadRoot is loaded two times. See above
        m.setThreadRoot(this.messageService.getById(messageDetail.getThreadRootId()));
        // set message roles
        List<MessageUserRole> messageUserRoles = new ArrayList<MessageUserRole>();
        // messageToUserRole
        MessageUserRole messageToUserRole = new MessageUserRole();
        messageToUserRole.setMessage(m);
        messageToUserRole.setUser(this.generalService.find(User.class, messageDetail.getReceiverId()));
        messageToUserRole.setType(MessageUserRoleType.TO);
        messageToUserRole.setMessageContext(MessageContext.POTENTIAL_CLIENTS_OFFER);
        messageUserRoles.add(messageToUserRole);
        // messageFromUserRole
        MessageUserRole messageFromUserRole = new MessageUserRole();
        messageFromUserRole.setMessage(m);
        messageFromUserRole.setType(MessageUserRoleType.SENDER);
        messageFromUserRole.setMessageContext(MessageContext.POTENTIAL_OFFER_FROM_SUPPLIER);
        messageFromUserRole.setUser(supplier);
        messageUserRoles.add(messageFromUserRole);
        m.setRoles(messageUserRoles);
        // set the offer to message
        m.setOffer(o);
        OfferDetail offerDetailPersisted = OfferDetail.generateOfferDetail(this.messageService.create(m));
        // TODO set children for parent message - check if it is correct
        parentMessage.getChildren().add(m);
        parentMessage.setMessageState(MessageState.REPLY_RECEIVED);
        parentMessage = this.messageService.update(parentMessage);

        return offerDetailPersisted;
    }

    @Override
    public ArrayList<MessageDetail> loadSuppliersPotentialDemandConversation(long threadId, long userId) {
        Message threadRoot = messageService.getById(threadId);
        User user = this.generalService.find(User.class, userId);
        ArrayList<Message> messages = (ArrayList<Message>) this.messageService.getPotentialDemandConversation(
                threadRoot, user);
        ArrayList<MessageDetail> messageDetails = new ArrayList<MessageDetail>();
        for (Message message : messages) {
            messageDetails.add(MessageDetail.generateMessageDetail(message));
        }
        return messageDetails;
    }
}
