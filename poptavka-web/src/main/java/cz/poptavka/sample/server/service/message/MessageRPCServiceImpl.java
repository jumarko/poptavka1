/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.server.service.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;

import cz.poptavka.sample.client.service.demand.MessageRPCService;
import cz.poptavka.sample.dao.message.MessageFilter;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.domain.message.MessageContext;
import cz.poptavka.sample.domain.message.MessageState;
import cz.poptavka.sample.domain.message.MessageUserRole;
import cz.poptavka.sample.domain.message.MessageUserRoleType;
import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.domain.user.BusinessUser;
import cz.poptavka.sample.domain.user.User;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.common.TreeItemService;
import cz.poptavka.sample.service.demand.DemandService;
import cz.poptavka.sample.service.message.MessageService;
import cz.poptavka.sample.service.user.ClientService;
import cz.poptavka.sample.service.usermessage.UserMessageService;
import cz.poptavka.sample.shared.domain.message.ClientDemandMessageDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetailImpl;
import cz.poptavka.sample.shared.domain.message.OfferDemandMessageImpl;
import cz.poptavka.sample.shared.domain.message.OfferMessageDetailImpl;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessage;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessageImpl;


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
    private ClientService clientService;
    private TreeItemService treeItemService;
    private DemandService demandService;

    @Autowired
    public void setDemandService(DemandService demandService) {
        this.demandService = demandService;
    }

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

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Autowired
    public void setTreeItemService(TreeItemService treeItemService) {
        this.treeItemService = treeItemService;
    }

    // TODO verify
    /**
     * if used somewhere. In the past (before 14.7.) used for MyDemands list of clients
     * demands. Now it is maybe unused. MAYBE I'm wrong
     */
    /**
     * Tato metoda ma zrejme vratit vsetky spravy (dotazy od dodavatelov a dotazy
     * operatora) tykajuce sa klientovych poptaviek?
     * @param businessUserId - jediny vsetupny parameter
     * @param fakeParam - ignorovat
     * @return messageDetails je ArrayList, ktory obsahuje objekty MessageDetail
     */
    public ArrayList<MessageDetailImpl> getClientDemands(long businessUserId, int fakeParam) {
        BusinessUser businessUser = this.generalService.find(BusinessUser.class, businessUserId);
        final List<Message> messages = this.messageService.getAllMessages(
                businessUser,
                MessageFilter.MessageFilterBuilder.messageFilter().
                withMessageUserRoleType(MessageUserRoleType.SENDER).
                withMessageContext(MessageContext.NEW_CLIENTS_DEMAND).
                withResultCriteria(ResultCriteria.EMPTY_CRITERIA).build());
        ArrayList<MessageDetailImpl> details = new ArrayList<MessageDetailImpl>();

        for (Message m : messages) {
            MessageDetailImpl md = new MessageDetailImpl();
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
     * Metoda, ktora vrati zoznam klientovych poptavkovych sprav.
     *
     * Pouzitie na UI: odkaz na wireframe - webgres.cz/axure/ -> UC.73 Moje poptavky_new
     *
     * Metoda vrati vsetky spravy #Message, pre ktore plati:
     * - sprava je koren (tj.poptavkova sprava) #message.threadRoot is not null
     * - odosielatel spravy je businessUserId tj. nas klient #message.sender = businessUserId
     * - sprava ma priradenu poptavku #message.demand is not null
     *
     * Pre kazdu poptavkovu spravu z vyselektovaneho zoznamu si musime dotiahnut
     * pocet neprecitanych podsprav. Selekt neprecitanych podspravby mal vyzera nasledovne
     * - message.threadRoot = poptavkova sprava z vyssieho zoznamu
     * - message.messageUserRole.messageContext = MessageContext.QUERY_TO_POTENTIAL_SUPPLIERS_DEMAND
     * (vid metodu sendQueryToPotentialDemand)
     * - UserMessage.message = message.id (konkretna podsprava)
     * - UserMessage.user = businessUserId
     *
     * @param businessUserId - parameter z UI
     * @param clientId - parameter z UI
     * @return ClientDemandMessageDetail
     */
    public ArrayList<ClientDemandMessageDetail> getListOfClientDemandMessages(long businessUserId, long clientId) {
        ArrayList<ClientDemandMessageDetail> result = new ArrayList();
        BusinessUser businessUser = this.generalService.find(BusinessUser.class, businessUserId);
        Map<Message, Long> submessageCounts = this.messageService
                .getListOfClientDemandMessagesAll(businessUser);
        Map<Message, Long> unreadSubmessageCounts = this.messageService
                .getListOfClientDemandMessagesUnread(businessUser);
        for (Entry<Message, Long> entry : submessageCounts.entrySet()) {
            Message message = entry.getKey();
            long count = entry.getValue();
            long unreadCount = unreadSubmessageCounts.get(entry.getKey());
            result.add(ClientDemandMessageDetail.createDetail(message, count,
                    unreadCount));
        }
        return result;
    }


    /**
     * Message sent by supplier about a query to potential demand.
     * @param messageDetailImpl
     * @return message
     */
    public MessageDetailImpl sendQueryToPotentialDemand(MessageDetailImpl messageDetailImpl) {
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
        MessageDetailImpl messageDetailPersisted = MessageDetailImpl.createMessageDetail(this.messageService.create(m));
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
    public OfferMessageDetailImpl sendOffer(OfferMessageDetailImpl offer) {
//        Offer o = new Offer();
//        o.setCreated(new Date());
//        o.setDemand(this.generalService.find(Demand.class, offer.getDemandId()));
//        o.setFinishDate(offer.getEndDate());
//        o.setPrice(offer.getPrice());
//        // TODO ivlcek - load DB object by code and not by id
//        o.setState(this.generalService.find(OfferState.class, 2L));
//        o.setSupplier(this.generalService.find(Supplier.class, offer.getSupplierId()));
//        o = this.generalService.save(o);
//
//        Message m = new Message();
//        m.setBody(offer.getBody());
//        m.setCreated(new Date());
//        m.setLastModified(new Date());
//        m.setMessageState(MessageState.SENT);
//        // TODO ivlcek - how to set this next sibling?
////        m.setNextSibling(null);
//        Message parentMessage = this.messageService.getById(offer.getThreadRootId());
//        m.setParent(parentMessage);
//        BusinessUser supplier = this.generalService.find(BusinessUser.class, offer.getSenderId());
//        m.setSender(supplier);
//        m.setSent(new Date());
//        m.setSubject(supplier.getBusinessUserData().getCompanyName());
//        // TODO ivlcek - threadRoot is loaded two times. See above
//        m.setThreadRoot(this.messageService.getById(offer.getThreadRootId()));
//        // set message roles
//        List<MessageUserRole> messageUserRoles = new ArrayList<MessageUserRole>();
//        // messageToUserRole
//        MessageUserRole messageToUserRole = new MessageUserRole();
//        messageToUserRole.setMessage(m);
//        User receiver = this.generalService.find(User.class, offer.getReceiverId());
//        messageToUserRole.setUser(receiver);
//        messageToUserRole.setType(MessageUserRoleType.TO);
//        messageToUserRole.setMessageContext(MessageContext.POTENTIAL_CLIENTS_OFFER);
//        messageUserRoles.add(messageToUserRole);
//        // messageFromUserRole
//        MessageUserRole messageFromUserRole = new MessageUserRole();
//        messageFromUserRole.setMessage(m);
//        messageFromUserRole.setType(MessageUserRoleType.SENDER);
//        messageFromUserRole.setMessageContext(MessageContext.POTENTIAL_OFFER_FROM_SUPPLIER);
//        messageFromUserRole.setUser(supplier);
//        messageUserRoles.add(messageFromUserRole);
//        m.setRoles(messageUserRoles);
//        // set the offer to message
//        m.setOffer(o);
//        m = this.messageService.create(m);
//        OfferDetail offerDetailPersisted = OfferDetail.generateOfferDetail(m);
//        // create UserMessage for Client receiving this message
//        UserMessage userMessage = new UserMessage();
//        userMessage.setIsRead(false);
//        userMessage.setIsStarred(false);
//        userMessage.setMessage(m);
//        userMessage.setUser(receiver);
//        generalService.save(userMessage);
//        // TODO set children for parent message - check if it is correct
//        parentMessage.getChildren().add(m);
//        parentMessage.setMessageState(MessageState.REPLY_RECEIVED);
//        parentMessage = this.messageService.update(parentMessage);
//
//        return offerDetailPersisted;
        return new OfferMessageDetailImpl();
    }

    @Override
    // TODO call setMessageReadStatus in body
    public ArrayList<MessageDetailImpl> loadSuppliersPotentialDemandConversation(
            long threadId, long userId, long userMessageId) {
        Message threadRoot = messageService.getById(threadId);

        setMessageReadStatus(Arrays.asList(new Long[]{userMessageId}), true);

        User user = this.generalService.find(User.class, userId);
        ArrayList<Message> messages = (ArrayList<Message>) this.messageService.getPotentialDemandConversation(
                threadRoot, user);
        ArrayList<MessageDetailImpl> messageDetailImpls = new ArrayList<MessageDetailImpl>();
        for (Message message : messages) {
            messageDetailImpls.add(MessageDetailImpl.createMessageDetail(message));
        }
        return messageDetailImpls;
    }

    public ArrayList<MessageDetailImpl> loadClientsPotentialOfferConversation(long threadId, long userId) {
        Message threadRoot = messageService.getById(threadId);
        User user = this.generalService.find(User.class, userId);
        ArrayList<Message> messages = (ArrayList<Message>) this.messageService.getPotentialOfferConversation(
                threadRoot, user);
        ArrayList<MessageDetailImpl> messageDetailImpls = new ArrayList<MessageDetailImpl>();
        for (Message message : messages) {
            messageDetailImpls.add(MessageDetailImpl.createMessageDetail(message));
        }
        return messageDetailImpls;
    }

    /**
     * COMMON.
     * Change 'read' status of sent messages to chosen value
     */
    @Override
    public void setMessageReadStatus(List<Long> userMessageIds, boolean isRead) {
        for (Long userMessageId : userMessageIds) {
            UserMessage userMessage = this.generalService.find(UserMessage.class, userMessageId);
            userMessage.setIsRead(isRead);
            this.generalService.save(userMessage);
        }
    }

    /**
     * SUPPLIER.
     * Returns messages for PotentialDemandsView's table
     */
    @Override
    public ArrayList<PotentialDemandMessage> getPotentialDemands(long businessUserId) {
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
        ArrayList<PotentialDemandMessage> potentailDemands = new ArrayList<PotentialDemandMessage>();
        for (UserMessage um : userMessages) {
            PotentialDemandMessage detail = PotentialDemandMessageImpl.createMessageDetail(um);

            potentailDemands.add(detail);
        }
        return potentailDemands;
    }

    /**
     * CLIENT.
     *
     * Vrati zoznam ponukovych sprav.
     *
     * TODO: nacitat zoznam ponukovych sprav tak, aby pri kazdej sprave bolo jasne, ci je oznacena ako precitana alebo
     * nie.
     */
    @Override
    public ArrayList<OfferDemandMessageImpl> getOfferDemands(long businessUserId) {

        BusinessUser businessUser = this.generalService.find(BusinessUser.class, businessUserId);
        final List<Message> messages = this.messageService.getAllMessages(
                businessUser,
                MessageFilter.MessageFilterBuilder.messageFilter().
                withMessageUserRoleType(MessageUserRoleType.TO).
                withMessageContext(MessageContext.POTENTIAL_CLIENTS_OFFER).
                withResultCriteria(ResultCriteria.EMPTY_CRITERIA).build());

        // TODO ivlcek - prerobit tak aby som nemusel nacitavat list messages, ktoru sluzit len ako
        // parameter pre dalsi dotaz do DB na ziskanie userMessages
        final List<UserMessage> userMessages =
                userMessageService.getUserMessages(messages, businessUser, MessageFilter.EMPTY_FILTER);
        // fill list

        ArrayList<OfferDemandMessageImpl> offerDemands = new ArrayList<OfferDemandMessageImpl>();
        for (UserMessage m : userMessages) {
            OfferDemandMessageImpl om = OfferDemandMessageImpl.createMessageDetail(m);
            System.out.println("X X " + m.getMessage().getDemand().getEndDate());
            offerDemands.add(om);
        }
        return offerDemands;
    }

    /**
     * Metoda vrati vsetky konverzacie (s dodavatelmi alebo operatorom) pre danu
     * poptavku (poptavkovu spravu).
     *
     * Pohlad v ktorom sa RPC metoda pouziva vid Wireframe:
     * UR -> Menu Klient -> Nove poptavky -> Konkretna poptavka
     *
     * Detail objekt musi vratit vsetky atributy, ktore su zobrazene na pohlade
     * vo Wireframoch. Nebudem ich vypisovat
     * TODO Vojto
     *
     * @param threadRootId
     * @return
     */
    @Override
    public ArrayList<MessageDetailImpl> getClientDemandConversations(long threadRootId) {
        // TODO Vojto
        ArrayList<MessageDetailImpl> childrenList = new ArrayList<MessageDetailImpl>();

        Message root = messageService.getById(threadRootId);
        List<Message> threads = root.getChildren();
        for (Message msg : threads) {
            childrenList.add(MessageDetailImpl.createMessageDetail(msg));
        }
        return childrenList;
    }

    public ArrayList<MessageDetailImpl> getConversationMessages(long threadRootId, long subRootId) {
        Message root = messageService.getById(threadRootId);
        Message subRoot = messageService.getById(subRootId);
        List<Message> conversation = messageService.getAllDescendants(subRoot);

        ArrayList<MessageDetailImpl> result = new ArrayList<MessageDetailImpl>();
        // add root and subRoot message
        result.add(MessageDetailImpl.createMessageDetail(root));
        result.add(MessageDetailImpl.createMessageDetail(subRoot));
        for (Message m : conversation) {
            result.add(MessageDetailImpl.createMessageDetail(m));
        }
        return result;
    }

}
