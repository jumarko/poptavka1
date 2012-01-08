/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.server.service.message;

import com.googlecode.genericdao.search.Search;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.exception.MessageException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import java.util.Calendar;
import java.util.Collection;

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
    private RatingService ratingService;

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
            long businessUserId, long clientId) {
        ArrayList<ClientDemandMessageDetail> result = new ArrayList();
        BusinessUser businessUser = this.generalService.find(BusinessUser.class, businessUserId);
        Map<Message, Integer> submessageCounts = this.messageService
                .getListOfClientDemandMessagesAll(businessUser);
        Map<Message, Integer> unreadSubmessageCounts = this.messageService
                .getListOfClientDemandMessagesUnread(businessUser);
        List<UserMessage> userMessages = userMessageService.getUserMessages(
                new ArrayList(submessageCounts.keySet()), businessUser, MessageFilter.EMPTY_FILTER);
        for (UserMessage userMessage : userMessages) {
            ClientDemandMessageDetail detail = ClientDemandMessageDetail.createDetail(userMessage);
            detail.setMessageCount(submessageCounts.get(userMessage.getMessage()));
            detail.setUnreadSubmessages(unreadSubmessageCounts.get(userMessage.getMessage()));
        }
        return result;
    }

    /**
     * Message sent by supplier about a query to potential demand.
     * @param messageDetailImpl
     * @return message
     */
    @Override
    public MessageDetail sendQueryToPotentialDemand(MessageDetail messageDetailImpl) {
        try {
            Message m = messageService.newReply(this.messageService.getById(
                    messageDetailImpl.getThreadRootId()),
                    this.generalService.find(User.class, messageDetailImpl.getSenderId()));
            m.setBody(messageDetailImpl.getBody());
            m.setSubject(QUERY_TO_POTENTIAL_DEMAND_SUBJECT);
            // TODO set the id correctly, check it
            MessageDetail messageDetailFromDB = MessageDetail.createMessageDetail(this.messageService.create(m));
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
            long threadId, long userId, long userMessageId) {
        Message threadRoot = messageService.getById(threadId);

        setMessageReadStatus(Arrays.asList(new Long[]{userMessageId}), true);

        User user = this.generalService.find(User.class, userId);
        ArrayList<Message> messages = (ArrayList<Message>) this.messageService.getPotentialDemandConversation(
                threadRoot, user);
        ArrayList<MessageDetail> messageDetailImpls = new ArrayList<MessageDetail>();
        for (Message message : messages) {
            messageDetailImpls.add(MessageDetail.createMessageDetail(message));
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
            messageDetailImpls.add(MessageDetail.createMessageDetail(message));
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
            userMessage.setRead(isRead);
            this.generalService.save(userMessage);
        }
    }

    /**
     * COMMON.
     * Change 'star' status of sent messages to chosen value
     */
    @Override
    public void setMessageStarStatus(List<Long> userMessageIds, boolean isRead) {
        for (Long userMessageId : userMessageIds) {
            UserMessage userMessage = this.generalService.find(UserMessage.class, userMessageId);
            userMessage.setStarred(isRead);
            this.generalService.save(userMessage);
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
            PotentialDemandMessage detail = PotentialDemandMessage.createMessageDetail(um);
            detail.setClientRating(ratingService.getAvgRating(um.getMessage().getDemand().getClient()));
            detail.setMessageCount(messageService.getAllDescendantsCount(um.getMessage(), businessUser));
            detail.setUnreadSubmessages(messageService.getUnreadDescendantsCount(um.getMessage(), businessUser));
            potentailDemands.add(detail);
        }
        return potentailDemands;
    }

    //Martin - temporary, to try if it works this way too, if yes && fast too -> this one will be better
    @Override
    public ArrayList<PotentialDemandMessage> getPotentialDemandsBySearch(
            long businessUserId, SearchModuleDataHolder searchDataHolder) {
        BusinessUser businessUser = this.generalService.find(BusinessUser.class, businessUserId);

        User user = generalService.find(User.class, businessUserId);

        Search searchMsgUser = new Search(MessageUserRole.class);
        searchMsgUser.addFilterEqual("user", user);
        searchMsgUser.addFilterEqual("messageContext", MessageContext.POTENTIAL_SUPPLIERS_DEMAND);

        Search searchMsgs = new Search(Message.class);
        searchMsgs.addFilterIn("roles", generalService.search(searchMsgUser));
        if (searchDataHolder != null) {
            searchMsgs = this.setPotentialDemandsFilter(searchDataHolder, searchMsgs);
        } else {
            searchMsgs.addFilterEqual("sender", user); // ?? to iste ako User vyssie?
        }

        Search searchUserMsgs = new Search(UserMessage.class);
        searchUserMsgs.addFilterEqual("user", user);
        searchUserMsgs.addFilterEqual("message", (Collection) Arrays.asList(generalService.search(searchMsgs)));
        if (searchDataHolder != null) {
            if (searchDataHolder.getPotentialDemandMessages().getIsStared() != null) {
                searchUserMsgs.addFilterEqual("isStarred",
                        searchDataHolder.getPotentialDemandMessages().getIsStared());
            }
        }

        final List<UserMessage> userMessages = generalService.search(searchUserMsgs);

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

    private Search setPotentialDemandsFilter(SearchModuleDataHolder searchDataHolder, Search search) {
        if (searchDataHolder.getPotentialDemandMessages().getSender() != null) {
            search.addFilterLike("sender.email", searchDataHolder.getPotentialDemandMessages().getSender());
        }
        if (searchDataHolder.getPotentialDemandMessages().getUrgention() != null) {
            Calendar calendarDate1 = Calendar.getInstance(); //today
            Calendar calendarDate2 = Calendar.getInstance();
            switch (searchDataHolder.getPotentialDemandMessages().getUrgention()) {
                case 1: //ends in one day => urgent
                    calendarDate1.add(Calendar.DATE, +1);
                    search.addFilterLessOrEqual("demand.endDate", calendarDate1.DATE);
                    break;
                case 2: //ends in one week, but not tomorrow => less urgent
                    calendarDate1.add(Calendar.DATE, +1);
                    calendarDate2.add(Calendar.DATE, +7);
                    search.addFilterGreaterOrEqual("demand.endDate", calendarDate1.DATE);
                    search.addFilterLessOrEqual("demand.endDate", calendarDate2.DATE);
                    break;
                case 3: //ends in one month, but not next week => normal
                    calendarDate1.add(Calendar.DATE, +7);
                    calendarDate2.add(Calendar.MONTH, +1);
                    search.addFilterGreaterOrEqual("demand.endDate", calendarDate1.DATE);
                    search.addFilterLessOrEqual("demand.endDate", calendarDate2.DATE);
                    break;
                case 4: //ends in more than one month => less normal
                    calendarDate2.add(Calendar.MONTH, +1);
                    search.addFilterGreaterOrEqual("demand.endDate", calendarDate2.DATE);
                    break;
                default:
                    ;
            }
        }
        if (searchDataHolder.getPotentialDemandMessages().getCreatedFrom() != null) {
            search.addFilterGreaterOrEqual("created",
                    searchDataHolder.getPotentialDemandMessages().getCreatedFrom());
        }
        if (searchDataHolder.getPotentialDemandMessages().getCreatedTo() != null) {
            search.addFilterLessOrEqual("created", searchDataHolder.getPotentialDemandMessages().getCreatedTo());
        }
        //Demand
        if (searchDataHolder.getPotentialDemandMessages().getRatingFrom() != null) {
            search.addFilterGreaterOrEqual("demand.client.overalRating",
                    searchDataHolder.getPotentialDemandMessages().getRatingFrom());
        }
        if (searchDataHolder.getPotentialDemandMessages().getRatingTo() != null) {
            search.addFilterLessOrEqual("demand.client.overalRating",
                    searchDataHolder.getPotentialDemandMessages().getRatingTo());
        }
        if (searchDataHolder.getPotentialDemandMessages().getDemandTitle() != null) {
            search.addFilterLike("demand.title", searchDataHolder.getPotentialDemandMessages().getDemandTitle());
        }
        //TODO demand.price alebo offer.price???
        if (searchDataHolder.getPotentialDemandMessages().getPriceFrom() != null) {
            search.addFilterGreaterOrEqual("demand.price",
                    searchDataHolder.getPotentialDemandMessages().getPriceFrom());
        }
        if (searchDataHolder.getPotentialDemandMessages().getPriceTo() != null) {
            search.addFilterLessOrEqual("demand.price", searchDataHolder.getPotentialDemandMessages().getPriceTo());
        }
        return search;
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
    public ArrayList<OfferDemandMessage> getOfferDemands(long businessUserId) {

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
    public ArrayList<MessageDetail> getClientDemandConversations(long threadRootId) {
        // TODO Vojto
        ArrayList<MessageDetail> childrenList = new ArrayList<MessageDetail>();

        Message root = messageService.getById(threadRootId);
        List<Message> threads = root.getChildren();
        for (Message msg : threads) {
            childrenList.add(MessageDetail.createMessageDetail(msg));
        }
        return childrenList;
    }

    public ArrayList<MessageDetail> getConversationMessages(long threadRootId, long subRootId) {
        Message root = messageService.getById(threadRootId);
        Message subRoot = messageService.getById(subRootId);
        List<Message> conversation = messageService.getAllDescendants(subRoot);

        ArrayList<MessageDetail> result = new ArrayList<MessageDetail>();
        // add root and subRoot message
        result.add(MessageDetail.createMessageDetail(root));
        result.add(MessageDetail.createMessageDetail(subRoot));
        for (Message m : conversation) {
            result.add(MessageDetail.createMessageDetail(m));
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

    public List<UserMessageDetail> getInboxMessages(Long recipientId) {
        Search search = new Search(MessageUserRole.class);
        search.addFilterIn("type", Arrays.asList(
                MessageUserRoleType.TO, MessageUserRoleType.CC, MessageUserRoleType.BCC));
        search.addFilterEqual("user", generalService.find(User.class, recipientId));
        search.setDistinct(true);
        search.addSort("message.sent", true);
        List<MessageUserRole> messageUserRoles = generalService.search(search);
        List<UserMessageDetail> messageDetails = new ArrayList<UserMessageDetail>();
        for (MessageUserRole messageUserRole : messageUserRoles) {
            Search searchUserMessage = new Search(UserMessage.class);
            searchUserMessage.addFilterEqual("user", generalService.find(User.class,
                    messageUserRole.getUser().getId()));
            List<UserMessage> users = generalService.search(searchUserMessage);
            for (UserMessage userMessage : users) {
                PotentialDemandMessage detail = PotentialDemandMessage.createMessageDetail(userMessage);
                detail.setMessageCount(messageService.getAllDescendantsCount(
                        userMessage.getMessage(), userMessage.getUser()));
                detail.setUnreadSubmessages(messageService.getUnreadDescendantsCount(
                        userMessage.getMessage(), userMessage.getUser()));
                messageDetails.add(UserMessageDetail.createUserMessageDetail(userMessage));
            }
        }
        return messageDetails;
    }

    public List<UserMessageDetail> getSentMessages(Long senderId) {
        Search search = new Search(Message.class);
        search.addFilterEqual("sender", generalService.find(User.class, senderId));
        search.addSort("sent", true);
        List<Message> messages = generalService.search(search);
        List<UserMessageDetail> messageDetails = new ArrayList<UserMessageDetail>();
        for (Message message : messages) {
            messageDetails.add(UserMessageDetail.createUserMessageDetail(
                    generalService.find(UserMessage.class, message.getId())));
        }
        return messageDetails;
    }

    public List<UserMessageDetail> getDeletedMessages(Long userId) {
        Search search = new Search(UserMessage.class);
        search.addFilterEqual("user", generalService.find(User.class, userId));
        search.addFilterEqual("message.messageState", MessageState.DELETED);
        //TODO Martin - created alebo lastModified? Ak reply tak sa zmeni lastModified?
        search.addSort("created", true);
        return this.createUserMessageDetailList(generalService.search(search));
    }
}
