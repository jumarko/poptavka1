/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.server.service.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import cz.poptavka.sample.client.service.demand.MessageRPCService;
import cz.poptavka.sample.dao.message.MessageFilter;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.domain.message.MessageContext;
import cz.poptavka.sample.domain.message.MessageState;
import cz.poptavka.sample.domain.message.MessageUserRole;
import cz.poptavka.sample.domain.message.MessageUserRoleType;
import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.domain.offer.Offer;
import cz.poptavka.sample.domain.offer.OfferState;
import cz.poptavka.sample.domain.user.BusinessUser;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.domain.user.User;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.message.MessageService;
import cz.poptavka.sample.service.usermessage.UserMessageService;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetailImpl;
import cz.poptavka.sample.shared.domain.message.PotentialMessageDetail;
import cz.poptavka.sample.shared.domain.message.PotentialMessageDetailImpl;

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

    public ArrayList<MessageDetail> getClientDemands(long businessUserId, int fakeParam) {
        BusinessUser businessUser = this.generalService.find(BusinessUser.class, businessUserId);
        final List<Message> messages = this.messageService.getAllMessages(
                businessUser,
                MessageFilter.MessageFilterBuilder.messageFilter().
                withMessageUserRoleType(MessageUserRoleType.SENDER).
                withMessageContext(MessageContext.NEW_CLIENTS_DEMAND).
                withResultCriteria(ResultCriteria.EMPTY_CRITERIA).build());
        ArrayList<MessageDetail> details = new ArrayList<MessageDetail>();

        for (Message m : messages) {
            MessageDetail md = new MessageDetailImpl();
            md.setMessageId(m.getId());
            md.setThreadRootId(md.getMessageId());
            md.setParentId(md.getMessageId());
            md.setSenderId(m.getSender().getId());
            md.setCreated(m.getCreated());
            md.setSent(m.getSent());
            md.setDemandId(m.getDemand().getId());
            md.setSubject(m.getDemand().getTitle());
            md.setBody(m.getDemand().getDescription());
            details.add(md);
        }
        return details;
    }

    /**
     * Message sent by supplier about a query to potential demand.
     * @param messageDetailImpl
     * @return message
     */
    public MessageDetail sendQueryToPotentialDemand(MessageDetail messageDetailImpl) {
        Message m = new Message();
        m.setBody(messageDetailImpl.getBody());
        m.setCreated(new Date());
        m.setLastModified(new Date());
        m.setMessageState(MessageState.SENT);
        // TODO ivlcek - how to set this next sibling?
//        m.setNextSibling(null);
        Message parentMessage = this.messageService.getById(messageDetailImpl.getParentId());
        m.setParent(parentMessage);
        User sender = this.generalService.find(User.class, messageDetailImpl.getSenderId());
        m.setSender(sender);
        m.setSent(new Date());
        m.setSubject(QUERY_TO_POTENTIAL_DEMAND_SUBJECT);
        m.setThreadRoot(this.messageService.getById(messageDetailImpl.getThreadRootId()));
        // set message roles
        List<MessageUserRole> messageUserRoles = new ArrayList<MessageUserRole>();
        // handles events when I send reply to my own message
        if (messageDetailImpl.getSenderId() == messageDetailImpl.getReceiverId()) {
            messageDetailImpl.setReceiverId(m.getThreadRoot().getSender().getId().longValue());
        }
        // messageToUserRole
        MessageUserRole messageToUserRole = new MessageUserRole();
        messageToUserRole.setMessage(m);
        messageToUserRole.setUser(this.generalService.find(User.class, messageDetailImpl.getReceiverId()));
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
        MessageDetail messageDetailPersisted = MessageDetailImpl.createMessageDetail(this.messageService.create(m));
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
        MessageDetail messageDetailImpl = offer.getMessageDetail();
        m.setBody(messageDetailImpl.getBody());
        m.setCreated(new Date());
        m.setLastModified(new Date());
        m.setMessageState(MessageState.SENT);
        // TODO ivlcek - how to set this next sibling?
//        m.setNextSibling(null);
        Message parentMessage = this.messageService.getById(messageDetailImpl.getThreadRootId());
        m.setParent(parentMessage);
        BusinessUser supplier = this.generalService.find(BusinessUser.class, messageDetailImpl.getSenderId());
        m.setSender(supplier);
        m.setSent(new Date());
        m.setSubject(supplier.getBusinessUserData().getCompanyName());
        // TODO ivlcek - threadRoot is loaded two times. See above
        m.setThreadRoot(this.messageService.getById(messageDetailImpl.getThreadRootId()));
        // set message roles
        List<MessageUserRole> messageUserRoles = new ArrayList<MessageUserRole>();
        // messageToUserRole
        MessageUserRole messageToUserRole = new MessageUserRole();
        messageToUserRole.setMessage(m);
        User receiver = this.generalService.find(User.class, messageDetailImpl.getReceiverId());
        messageToUserRole.setUser(receiver);
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
        m = this.messageService.create(m);
        OfferDetail offerDetailPersisted = OfferDetail.generateOfferDetail(m);
        // create UserMessage for Client receiving this message
        UserMessage userMessage = new UserMessage();
        userMessage.setIsRead(false);
        userMessage.setIsStarred(false);
        userMessage.setMessage(m);
        userMessage.setUser(receiver);
        generalService.save(userMessage);
        // TODO set children for parent message - check if it is correct
        parentMessage.getChildren().add(m);
        parentMessage.setMessageState(MessageState.REPLY_RECEIVED);
        parentMessage = this.messageService.update(parentMessage);

        return offerDetailPersisted;
    }

    @Override
    // TODO call setMessageReadStatus in body
    public ArrayList<MessageDetail> loadSuppliersPotentialDemandConversation(
            long threadId, long userId, long userMessageId) {
        Message threadRoot = messageService.getById(threadId);

        setMessageReadStatus(Arrays.asList(new Long[]{userMessageId}), true);

        User user = this.generalService.find(User.class, userId);
        ArrayList<Message> messages = (ArrayList<Message>) this.messageService.getPotentialDemandConversation(
                threadRoot, user);
        ArrayList<MessageDetail> messageDetailImpls = new ArrayList<MessageDetail>();
        for (Message message : messages) {
            messageDetailImpls.add(MessageDetailImpl.createMessageDetail(message));
        }
        return messageDetailImpls;
    }

    public ArrayList<MessageDetail> loadClientsPotentialOfferConversation(long threadId, long userId) {
        Message threadRoot = messageService.getById(threadId);
        User user = this.generalService.find(User.class, userId);
        ArrayList<Message> messages = (ArrayList<Message>) this.messageService.getPotentialOfferConversation(
                threadRoot, user);
        ArrayList<MessageDetail> messageDetailImpls = new ArrayList<MessageDetail>();
        for (Message message : messages) {
            messageDetailImpls.add(MessageDetailImpl.createMessageDetail(message));
        }
        return messageDetailImpls;
    }

    @Override
    public void setMessageReadStatus(List<Long> userMessageIds, boolean isRead) {
        for (Long userMessageId : userMessageIds) {
            UserMessage userMessage = this.generalService.find(UserMessage.class, userMessageId);
            userMessage.setIsRead(isRead);
            this.generalService.save(userMessage);
        }
    }

    @Override
    public ArrayList<PotentialMessageDetail> getPotentialDemands(long businessUserId) {
        BusinessUser businessUser = this.generalService.find(BusinessUser.class, businessUserId);
        final List<Message> messages = this.messageService.getAllMessages(
                businessUser,
                MessageFilter.MessageFilterBuilder.messageFilter().
                withMessageUserRoleType(MessageUserRoleType.TO).
                withMessageContext(MessageContext.POTENTIAL_SUPPLIERS_DEMAND).
                withResultCriteria(ResultCriteria.EMPTY_CRITERIA).build());
        // TODO ivlcek - prerobit tak aby som nemusel nacitavat list messages, ktoru sluzit len ako
        // parameter pre dalsi dotaz do DB na ziskanie userMessages
        final List<UserMessage> userMessages =
                userMessageService.getUserMessages(messages, businessUser, MessageFilter.EMPTY_FILTER);
        // fill list
        ArrayList<PotentialMessageDetail> potentailDemands = new ArrayList<PotentialMessageDetail>();
        for (UserMessage um : userMessages) {
            PotentialMessageDetail detail = PotentialMessageDetailImpl.createMessageDetail(um);
            potentailDemands.add(detail);
        }
        return potentailDemands;
    }
}
