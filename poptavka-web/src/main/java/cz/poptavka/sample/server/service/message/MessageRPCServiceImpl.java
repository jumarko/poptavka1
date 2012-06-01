/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.server.service.message;

import com.googlecode.genericdao.search.Search;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.main.common.search.dataHolders.FilterItem;
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
import cz.poptavka.sample.exception.MessageException;
import cz.poptavka.sample.shared.domain.converter.MessageConverter;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.common.TreeItemService;
import cz.poptavka.sample.service.demand.DemandService;
import cz.poptavka.sample.service.demand.RatingService;
import cz.poptavka.sample.service.message.MessageService;
import cz.poptavka.sample.service.user.ClientService;
import cz.poptavka.sample.service.usermessage.UserMessageService;
import cz.poptavka.sample.shared.domain.message.ClientDemandMessageDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.message.OfferDemandMessage;
import cz.poptavka.sample.shared.domain.message.OfferMessageDetail;
import cz.poptavka.sample.shared.domain.message.PotentialDemandMessage;
import cz.poptavka.sample.shared.domain.message.UserMessageDetail;
import cz.poptavka.sample.shared.exceptions.RPCException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ivan.vlcek
 */
@Component(MessageRPCService.URL)
public class MessageRPCServiceImpl extends AutoinjectingRemoteService implements MessageRPCService {

    // TODO ivlcek - konstanty nacitat cez lokalizovane rozhranie
    public static final String QUERY_TO_POTENTIAL_DEMAND_SUBJECT = "Dotaz na Vasu zadanu poptavku";
    public static final String OFFER_TO_POTENTIAL_DEMAND_SUBJECT = "Ponuka na vasu poptavku/nazov dodavatela";
    public static final String INTERNAL_MESSAGE = "Interna sprava";
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
    private RatingService ratingService;
    private MessageConverter messageConverter = new MessageConverter();

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

    public void setRatingService(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    // TODO verify
    /**
     * if used somewhere. In the past (before 14.7.) used for MyDemands list of clients
     * demands. Now it is maybe unused. MAYBE I'm wrong
     * -- USED in MessageHandler#onGetClientDemands
     */
    /**
     * Tato metoda ma zrejme vratit vsetky spravy (dotazy od dodavatelov a dotazy
     * operatora) tykajuce sa klientovych poptaviek?
     * @param businessUserId - jediny vsetupny parameter
     * @param fakeParam - ignorovat
     * @return messageDetails je ArrayList, ktory obsahuje objekty MessageDetail
     */
    @Override
    public ArrayList<MessageDetail> getClientDemands(long businessUserId, int fakeParam) throws RPCException {
        BusinessUser businessUser = this.generalService.find(BusinessUser.class, businessUserId);
        final List<Message> messages = this.messageService.getAllMessages(
                businessUser,
                MessageFilter.MessageFilterBuilder.messageFilter().
                withMessageUserRoleType(MessageUserRoleType.SENDER).
                withMessageContext(MessageContext.NEW_CLIENTS_DEMAND).
                withResultCriteria(ResultCriteria.EMPTY_CRITERIA).build());
        ArrayList<MessageDetail> details = new ArrayList<MessageDetail>();

        for (Message m : messages) {
            ClientDemandMessageDetail md = new ClientDemandMessageDetail();
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
    @Override
    public ArrayList<ClientDemandMessageDetail> getListOfClientDemandMessages(
            long businessUserId, long clientId) throws RPCException {
        ArrayList<ClientDemandMessageDetail> result = new ArrayList();
        BusinessUser businessUser = this.generalService.find(BusinessUser.class, businessUserId);
        Map<Message, Integer> submessageCounts = this.messageService.getListOfClientDemandMessagesAll(businessUser);
        Map<Message, Integer> unreadSubmessageCounts =
                this.messageService.getListOfClientDemandMessagesUnread(businessUser);
        List<UserMessage> userMessages = userMessageService.getUserMessages(
                new ArrayList(submessageCounts.keySet()), businessUser, MessageFilter.EMPTY_FILTER);
        for (UserMessage userMessage : userMessages) {
            ClientDemandMessageDetail detail = ClientDemandMessageDetail.createDetail(userMessage);
            if (submessageCounts.get(userMessage.getMessage()) == null) {
                detail.setMessageCount(0);
            } else {
                detail.setMessageCount(submessageCounts.get(userMessage.getMessage()).intValue());
            }
            if (unreadSubmessageCounts.get(userMessage.getMessage()) == null) {
                detail.setUnreadSubmessages(0);
            } else {
                detail.setUnreadSubmessages(unreadSubmessageCounts.get(userMessage.getMessage()));
            }
        }
        return result;
    }

    /**
     * Message sent by supplier about a query to potential demand.
     * @param messageDetailImpl
     * @return message
     */
    @Override
    public MessageDetail sendQueryToPotentialDemand(MessageDetail messageDetailImpl) throws RPCException {
        try {
            Message m = messageService.newReply(this.messageService.getById(
                    messageDetailImpl.getThreadRootId()),
                    this.generalService.find(User.class, messageDetailImpl.getSenderId()));
            m.setBody(messageDetailImpl.getBody());
            m.setSubject(QUERY_TO_POTENTIAL_DEMAND_SUBJECT);
            // TODO set the id correctly, check it
            MessageDetail messageDetailFromDB = messageConverter.convertToTarget(this.messageService.create(m));
            return messageDetailFromDB;
        } catch (MessageException ex) {
            Logger.getLogger(MessageRPCServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Message sent by user to another user without any link to demand or offer.
     * @param messageDetailImpl
     * @return message
     */
    @Override
    public MessageDetail sendInternalMessage(MessageDetail messageDetailImpl) throws RPCException {
        try {
            Message m = messageService.newReply(this.messageService.getById(
                    messageDetailImpl.getThreadRootId()),
                    this.generalService.find(User.class, messageDetailImpl.getSenderId()));
            m.setBody(messageDetailImpl.getBody());
            m.setSubject(INTERNAL_MESSAGE);
            // TODO set the id correctly, check it
            MessageDetail messageDetailFromDB = messageConverter.convertToTarget(this.messageService.create(m));
            return messageDetailFromDB;
        } catch (MessageException ex) {
            Logger.getLogger(MessageRPCServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Offer sent by supplier to potential demand.
     * TODO replace this into OfferRPCServiceImpl
     * @param offer
     * @return message
     */
    public OfferMessageDetail sendOffer(OfferMessageDetail offer) {
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
//        userMessage.setRead(false);
//        userMessage.setStarred(false);
//        userMessage.setMessage(m);
//        userMessage.setUser(receiver);
//        generalService.save(userMessage);
//        // TODO set children for parent message - check if it is correct
//        parentMessage.getChildren().add(m);
//        parentMessage.setMessageState(MessageState.REPLY_RECEIVED);
//        parentMessage = this.messageService.update(parentMessage);
//
//        return offerDetailPersisted;
        return new OfferMessageDetail();
    }

    @Override
    // TODO call setMessageReadStatus in body
    public ArrayList<MessageDetail> loadSuppliersPotentialDemandConversation(
            long threadId, long userId, long userMessageId) throws RPCException {
        Message threadRoot = messageService.getById(threadId);

        setMessageReadStatus(Arrays.asList(new Long[]{userMessageId}), true);

        User user = this.generalService.find(User.class, userId);
        ArrayList<Message> messages = (ArrayList<Message>) this.messageService.getPotentialDemandConversation(
                threadRoot, user);
        ArrayList<MessageDetail> messageDetailImpls = new ArrayList<MessageDetail>();
        for (Message message : messages) {
            messageDetailImpls.add(messageConverter.convertToTarget(message));
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
            messageDetailImpls.add(messageConverter.convertToTarget(message));
        }
        return messageDetailImpls;
    }

    /**
     * COMMON.
     * Change 'read' status of sent messages to chosen value
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void setMessageReadStatus(List<Long> userMessageIds, boolean isRead) throws RPCException {
        for (Long userMessageId : userMessageIds) {
            UserMessage userMessage = this.generalService.find(UserMessage.class, userMessageId);
            userMessage.setRead(isRead);
            this.userMessageService.update(userMessage);
        }
    }

    /**
     * COMMON.
     * Change 'star' status of sent messages to chosen value
     */
    @Override
    public void setMessageStarStatus(List<Long> userMessageIds, boolean isStarred) throws RPCException {
        for (Long userMessageId : userMessageIds) {
            UserMessage userMessage = this.generalService.find(UserMessage.class, userMessageId);
            userMessage.setStarred(isStarred);
            this.userMessageService.update(userMessage);
        }
    }

    /**
     *
     * TODO - remove this garbage and call {@link UserMessageService#getPotentialDemands)
     *
     * SUPPLIER.
     * Returns messages for PotentialDemandsView's table
     */
    @Override
    public ArrayList<PotentialDemandMessage> getPotentialDemands(long businessUserId) throws RPCException {
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
            PotentialDemandMessage detail = PotentialDemandMessage.createMessageDetail(um);
            detail.setClientRating(ratingService.getAvgRating(um.getMessage().getDemand().getClient()));
            detail.setMessageCount(messageService.getAllDescendantsCount(um.getMessage(), businessUser));
            detail.setUnreadSubmessages(messageService.getUnreadDescendantsCount(um.getMessage(), businessUser));
            potentailDemands.add(detail);
        }
        return potentailDemands;
    }

    //Martin - temporary, to try if it works this way too, if yes && fast too -> this one will be better
    // Works fine - if more data in DB available - perform speed test
    @Override
    public ArrayList<PotentialDemandMessage> getPotentialDemandsBySearch(
            long userId, SearchModuleDataHolder searchDataHolder) throws RPCException {
        // TODO userID vs businessUserID ??
        User user = generalService.find(User.class, userId);

        Search messageSearch = null;
        if (searchDataHolder != null) {
            for (FilterItem item : searchDataHolder.getAttibutes()) {
                messageSearch = new Search(Message.class);
                if (item.getItem().equals("email")) {
                    messageSearch.addFilterIn("sender", generalService.search(
                            this.filter(new Search(User.class), "", item)));
                } else {
                    this.filter(messageSearch, "", item);
                }
            }
        }

        Search messageUserRoleSearch = new Search(MessageUserRole.class);
        messageUserRoleSearch.addFilterEqual("user", user);
        messageUserRoleSearch.addFilterEqual("type", MessageUserRoleType.TO);
        if (searchDataHolder != null) {
            messageUserRoleSearch.addFilterIn("message", generalService.search(messageSearch));
        }
        List<MessageUserRole> messageUsersRole = generalService.search(messageUserRoleSearch);

        List<MessageUserRole> potentialDemandMessages = new ArrayList<MessageUserRole>();
        for (MessageUserRole mur : messageUsersRole) {
            if (mur.getMessageContext().equals(MessageContext.POTENTIAL_SUPPLIERS_DEMAND)) {
                potentialDemandMessages.add(mur);
            }
        }

        List<UserMessage> potentialDemandUserMessages = new ArrayList<UserMessage>();
        for (MessageUserRole mur : potentialDemandMessages) {
            Search potentialDemandUserMessagesSearch = new Search(UserMessage.class);
            potentialDemandUserMessagesSearch.addFilterEqual("user", mur.getUser());
            potentialDemandUserMessagesSearch.addFilterEqual("message", mur.getMessage());
            potentialDemandUserMessages.addAll(generalService.search(potentialDemandUserMessagesSearch));
        }

        // fill list
        ArrayList<PotentialDemandMessage> potentailDemands = new ArrayList<PotentialDemandMessage>();
        for (UserMessage um : potentialDemandUserMessages) {
            PotentialDemandMessage detail = PotentialDemandMessage.createMessageDetail(um);
            detail.setClientRating(ratingService.getAvgRating(um.getMessage().getDemand().getClient()));
            detail.setMessageCount(messageService.getAllDescendantsCount(um.getMessage(), user));
            detail.setUnreadSubmessages(messageService.getUnreadDescendantsCount(um.getMessage(), user));
            potentailDemands.add(detail);
        }
        return potentailDemands;
    }

    /**
     * CLIENT.
     *
     * Vrati zoznam ponukovych sprav.
     *
     * TODO: nacitat zoznam ponukovych sprav tak, aby pri kazdej sprave bolo jasne,
     * ci je oznacena ako precitana alebo
     * nie.
     */
    @Override
    public ArrayList<OfferDemandMessage> getOfferDemands(long businessUserId) throws RPCException {

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

        ArrayList<OfferDemandMessage> offerDemands = new ArrayList<OfferDemandMessage>();
        for (UserMessage m : userMessages) {
            OfferDemandMessage om = OfferDemandMessage.createMessageDetail(m);
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
    public ArrayList<MessageDetail> getClientDemandConversations(long threadRootId) throws RPCException {
        // TODO Vojto
        ArrayList<MessageDetail> childrenList = new ArrayList<MessageDetail>();

        Message root = messageService.getById(threadRootId);
        List<Message> threads = root.getChildren();
        for (Message msg : threads) {
            childrenList.add(messageConverter.convertToTarget(msg));
        }
        return childrenList;
    }

    /**
     * Get all conversations of thread.
     * @param threadRootId
     * @param subRootId
     * @return conversation list
     */
    @Override
    public ArrayList<MessageDetail> getConversationMessages(long threadRootId, long subRootId) throws RPCException {
//        Message root = messageService.getById(threadRootId);
        Message subRoot = messageService.getById(subRootId);
        List<Message> conversation = messageService.getAllDescendants(subRoot);

        ArrayList<MessageDetail> result = new ArrayList<MessageDetail>();
        // add root and subRoot message
//        result.add(MessageDetail.createMessageDetail(root));
        result.add(messageConverter.convertToTarget(subRoot));
        for (Message m : conversation) {
            result.add(messageConverter.convertToTarget(m));
        }
        return result;
    }

    private List<UserMessageDetail> createUserMessageDetailList(Collection<UserMessage> userMessages) {
        List<UserMessageDetail> userMessageDetails = new ArrayList<UserMessageDetail>();
        for (UserMessage userMessage : userMessages) {
            UserMessageDetail demandDetail = UserMessageDetail.createUserMessageDetail(userMessage);
            userMessageDetails.add(demandDetail);
        }
        return userMessageDetails;
    }

    @Override
    public List<UserMessageDetail> getInboxMessages(Long recipientId, SearchModuleDataHolder searchDataHolder)
        throws RPCException {
        return this.getMessages(recipientId, searchDataHolder, Arrays.asList(
                MessageUserRoleType.TO, MessageUserRoleType.CC, MessageUserRoleType.BCC));
    }

    @Override
    public List<UserMessageDetail> getSentMessages(Long senderId, SearchModuleDataHolder searchDataHolder)
        throws RPCException {
        User sender = generalService.find(User.class, senderId);

        /****/// ziskaj vsetky spravy poslane danym uzivatelom
        Search messageSearch = new Search(Message.class);
        messageSearch.addFilterEqual("sender", sender);
        //ak treba, filtruj spravy poslane danym uzivatelom
        if (searchDataHolder != null) {
            for (FilterItem item : searchDataHolder.getAttibutes()) {
                this.filter(messageSearch, "", item);
            }
        }

        /****/
        Map<Long, Message> senderMessages = new HashMap<Long, Message>();
        /****/
        List<Message> senderMessagesTmp = new ArrayList<Message>();
        /****/
        senderMessagesTmp.addAll(generalService.search(messageSearch));
        for (Message msg : senderMessagesTmp) {
            if (!senderMessages.containsKey(msg.getThreadRoot().getId())) {
                senderMessages.put(msg.getThreadRoot().getId(), msg);
            }
        }

        /****///Ziskaj vsetkych prijemcov danych sprav
        List<MessageUserRole> recipients = new ArrayList<MessageUserRole>();
        Search messageUserRoleSearch = new Search(MessageUserRole.class);
        messageUserRoleSearch.addFilterIn("message", generalService.search(messageSearch));
        messageUserRoleSearch.addFilterIn("type", MessageUserRoleType.TO);
        //ak treba, filtruj prijemcov danych sprav
        if (searchDataHolder != null) {
            for (FilterItem item : searchDataHolder.getAttibutes()) {
                messageUserRoleSearch.addFilterIn("user", generalService.search(
                        this.filter(new Search(User.class), "", item)));
            }
        }
        /****/
        recipients.addAll(generalService.search(messageUserRoleSearch));

        todoDeleteOrRefactor();


        //Stacilo by mi aj to zhora, ale musim ziskat este UserMessage, aby som vedel, isRead, isStarred, ...

        /**///Ziskaj UserMessage (read/unread , starred/unstarred)
        List<UserMessage> inboxMessages = new ArrayList<UserMessage>();
        Search userMessagesSearch = new Search(UserMessage.class);
//        for (Message msg : rootMessages) {

        userMessagesSearch.addFilterEqual("user", sender);
        userMessagesSearch.addFilterIn("message", senderMessages.values());
        /**/ inboxMessages.addAll(generalService.search(userMessagesSearch));
//        }

        //Create details
        List<UserMessageDetail> inboxMessagesDetail = new ArrayList<UserMessageDetail>();
//        for (MessageUserRole)
        for (UserMessage userMessage : inboxMessages) {
//            rootMessages.contains(userMessage.getMessage());
//            senderMessages.containsValue(userMessage.getMessage());
//            userMessage.getMessage().equals(this);
            for (MessageUserRole mur : recipients) {
                if (mur.getMessage().equals(userMessage.getMessage())) {
                    try {
                        userMessage.getMessage().setSender(mur.getMessage().getSender());
                    } catch (MessageException ex) {
                        Logger.getLogger(MessageRPCServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            inboxMessagesDetail.add(UserMessageDetail.createUserMessageDetail(userMessage));
        }

        return inboxMessagesDetail;
//        return this.getMessages(senderId, searchDataHolder, Arrays.asList(MessageUserRoleType.SENDER));
    }

    // TODO - check this method
    private void todoDeleteOrRefactor() {
        //        Search recipientMessagesSearch = new Search(MessageUserRole.class);
//        recipientMessagesSearch.addFilterEqual("user", sender);
//        recipientMessagesSearch.addFilterIn("type", roles);
//        if (searchDataHolder != null) {
//            recipientMessagesSearch.addFilterIn("message", generalService.search(messageSearch));
//        }
        //ziskaj prvotne spravy na vypis v tabulke
//        List<Message> rootMessages = new ArrayList<Message>();
//        List<Long> threadIds = new ArrayList<Long>();
//        if (recipients.isEmpty()) {
//            for (Message msg : senderMessages) {
//                if (!threadIds.contains(msg.getThreadRoot().getId())) {
//                    threadIds.add(msg.getThreadRoot().getId());
//                    Search rootMsgSearch = new Search(Message.class);
//                    rootMsgSearch.addFilterEqual("id", msg.getThreadRoot().getId());
//                    rootMsgSearch.addFilterNull("parent");
//                    rootMessages.addAll(generalService.search(messageSearch));
//                }
//            }
//        } else {
//            for (MessageUserRole mur : recipients) {
//                if (!threadIds.contains(mur.getMessage().getThreadRoot().getId())) {
//                    threadIds.add(mur.getMessage().getThreadRoot().getId());
//                    Search rootMsgSearch = new Search(Message.class);
//                    rootMsgSearch.addFilterEqual("id", mur.getMessage().getThreadRoot().getId());
//                    rootMsgSearch.addFilterNull("parent");
//                    rootMessages.addAll(generalService.search(messageSearch));
//                }
//            }
//        }
//        Search firstBornRecipientMessagesSearch = new Search(Message.class);
//        List<Message> firstBornRecipientMessages = new ArrayList<Message>();
//        for (MessageUserRole mur : recipientMessages) {
//            firstBornRecipientMessagesSearch.addFilterEqual("id", mur.getMessage().getId());
//            firstBornRecipientMessages = generalService.search(firstBornRecipientMessagesSearch);
//        }
//        Map<Long, Message> rootRecipientMessages = new TreeMap<Long, Message>();
//        for (MessageUserRole mur : senderMessages) {
//            if (mur.getMessage().getParent() == null) {
//                // nemusi kontorlovat, ved thread_id s parent_id = null je vzdy len jeden
////                if (!rootRecipientMessages.containsKey(mur.getMessage().getThreadRoot().getId())) {
//                rootRecipientMessages.put(mur.getMessage().getThreadRoot().getId(), mur.getMessage());
//            }
//        }
    }

    private List<UserMessageDetail> getMessages(Long recipientId, SearchModuleDataHolder searchDataHolder,
            List<MessageUserRoleType> roles) {
        User recipient = generalService.find(User.class, recipientId);

        Search messageSearch = null;
        if (searchDataHolder != null) {
            messageSearch = new Search(Message.class);
            for (FilterItem item : searchDataHolder.getAttibutes()) {
                if (item.getItem().equals("email")) {
                    messageSearch.addFilterIn("sender", generalService.search(
                            this.filter(new Search(User.class), "", item)));
                } else {
                    this.filter(messageSearch, "", item);
                }
            }
        }

        //Ziskaj vsetky spravy daneho uzivatela, kt bol oznaceny ako adresat alebo odosielatel
        List<MessageUserRole> recipientMessages = new ArrayList<MessageUserRole>();
        Search recipientMessagesSearch = new Search(MessageUserRole.class);
        recipientMessagesSearch.addFilterEqual("user", recipient);
        recipientMessagesSearch.addFilterIn("type", roles);
        if (searchDataHolder != null) {
            recipientMessagesSearch.addFilterIn("message", generalService.search(messageSearch));
        }
        recipientMessages.addAll(generalService.search(recipientMessagesSearch));


//        Search firstBornRecipientMessagesSearch = new Search(Message.class);
//        List<Message> firstBornRecipientMessages = new ArrayList<Message>();
//        for (MessageUserRole mur : recipientMessages) {
//            firstBornRecipientMessagesSearch.addFilterEqual("id", mur.getMessage().getId());
//            firstBornRecipientMessages = generalService.search(firstBornRecipientMessagesSearch);
//        }

        Map<Long, Message> rootRecipientMessages = new TreeMap<Long, Message>();
        for (MessageUserRole mur : recipientMessages) {
            if (mur.getMessage().getParent() == null) {
                // nemusi kontorlovat, ved thread_id s parent_id = null je vzdy len jeden
//                if (!rootRecipientMessages.containsKey(mur.getMessage().getThreadRoot().getId())) {
                rootRecipientMessages.put(mur.getMessage().getThreadRoot().getId(), mur.getMessage());
            }
        }
        //Stacilo by mi aj to zhora, ale musim ziskat este UserMessage, aby som vedel, isRead, isStarred, ...
        List<UserMessage> inboxMessages = new ArrayList<UserMessage>();
        for (Message msg : rootRecipientMessages.values()) {
            Search userMessagesSearch = new Search(UserMessage.class);
            userMessagesSearch.addFilterEqual("user", recipient);
            userMessagesSearch.addFilterEqual("message", msg);
            inboxMessages.addAll(generalService.search(userMessagesSearch));
        }
        List<UserMessageDetail> inboxMessagesDetail = new ArrayList<UserMessageDetail>();
        for (UserMessage userMessage : inboxMessages) {
            inboxMessagesDetail.add(UserMessageDetail.createUserMessageDetail(userMessage));
        }

        return inboxMessagesDetail;
    }

    @Override
    public List<UserMessageDetail> getDeletedMessages(Long userId, SearchModuleDataHolder searchDataHolder)
        throws RPCException {
        Search messageSearch = new Search(Message.class);
        messageSearch.addFilterEqual("messageState", MessageState.DELETED);
        if (searchDataHolder != null) {
            for (FilterItem item : searchDataHolder.getAttibutes()) {
                if (item.getItem().equals("email")) {
                    messageSearch.addFilterIn("sender", generalService.search(
                            this.filter(new Search(User.class), "", item)));
                } else {
                    this.filter(messageSearch, "", item);
                }
            }
        }

        Search userMessagesSearch = new Search(UserMessage.class);
        userMessagesSearch.addFilterEqual("user", generalService.find(User.class, userId));
        if (searchDataHolder != null) {
            userMessagesSearch.addFilterIn("message", generalService.search(messageSearch));
        }
        List<UserMessage> userMessages = generalService.search(userMessagesSearch);
        Map<Long, UserMessage> rootDeletedMessages = new TreeMap<Long, UserMessage>();

        for (UserMessage userMessage : userMessages) {
            if (userMessage.getMessage().getParent() == null) {
                if (!rootDeletedMessages.containsKey(userMessage.getMessage().getThreadRoot().getId())) {
                    rootDeletedMessages.put(userMessage.getMessage().getThreadRoot().getId(), userMessage);
                }
            }
        }
        List<UserMessageDetail> deletedMessagesDetail = new ArrayList<UserMessageDetail>();

        for (UserMessage userMessage : rootDeletedMessages.values()) {
            deletedMessagesDetail.add(UserMessageDetail.createUserMessageDetail(userMessage));
        }

        return deletedMessagesDetail;
    }

    @Override
    public void deleteMessages(List<Long> messagesIds) throws RPCException {
        Search searchMsgs = new Search(Message.class);
        searchMsgs.addFilterIn("id", messagesIds);
        List<Message> msgs = generalService.search(searchMsgs);
        for (Message msg : msgs) {
            Message subRoot = messageService.getById(msg.getId());
            List<Message> conversation = messageService.getAllDescendants(subRoot);
            msg.setMessageState(MessageState.DELETED); //musi byt? neobsahuje to ten getALlDescendatns
            for (Message msg1 : conversation) {
                msg1.setMessageState(MessageState.DELETED);
            }
            generalService.merge(msg);
        }
    }

    private Search filter(Search search, String prefix, FilterItem item) {
        prefix += ".";
        switch (item.getOperation()) {
            case FilterItem.OPERATION_EQUALS:
                search.addFilterEqual(prefix + item.getItem(), item.getValue());
                break;
            case FilterItem.OPERATION_LIKE:
                search.addFilterLike(prefix + item.getItem(), "%" + item.getValue().toString() + "%");
                break;
            case FilterItem.OPERATION_IN:
                search.addFilterIn(prefix + item.getItem(), item.getValue());
                break;
            case FilterItem.OPERATION_FROM:
                search.addFilterGreaterOrEqual(prefix + item.getItem(), item.getValue());
                break;
            case FilterItem.OPERATION_TO:
                search.addFilterLessOrEqual(prefix + item.getItem(), item.getValue());
                break;
            default:
                break;
        }
        return search;
    }
}