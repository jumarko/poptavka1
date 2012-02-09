/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.server.service.message;

import com.googlecode.genericdao.search.Search;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.service.demand.MessageRPCService;
import cz.poptavka.sample.dao.message.MessageFilter;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.domain.message.MessageContext;
import cz.poptavka.sample.domain.message.MessageState;
import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.domain.message.UserMessageRoleType;
import cz.poptavka.sample.domain.user.BusinessUser;
import cz.poptavka.sample.domain.user.User;
import cz.poptavka.sample.exception.MessageException;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ivan.vlcek
 */
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
        final List<Message> messages = this.messageService.getAllMessages(
                getBusinessUser(businessUserId),
                MessageFilter.MessageFilterBuilder.messageFilter().
                        withUserMessageRoleType(UserMessageRoleType.SENDER).
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
        final ArrayList<ClientDemandMessageDetail> result = new ArrayList();
        BusinessUser businessUser = getBusinessUser(businessUserId);
        Map<Message, Integer> submessageCounts = this.messageService.getListOfClientDemandMessagesAll(businessUser);
        Map<Message, Integer> unreadSubmessageCounts =
                this.messageService.getListOfClientDemandMessagesUnread(businessUser);

        final List<UserMessage> userMessages = userMessageService.getUserMessages(
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
     * Message sent by user to another user without any link to demand or offer.
     * @param messageDetailImpl
     * @return message
     */
    @Override
    public MessageDetail sendInternalMessage(MessageDetail messageDetailImpl) {
        try {
            Message m = messageService.newReply(this.messageService.getById(
                    messageDetailImpl.getThreadRootId()),
                    this.generalService.find(User.class, messageDetailImpl.getSenderId()));
            m.setBody(messageDetailImpl.getBody());
            m.setSubject(INTERNAL_MESSAGE);
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
        final ArrayList<Message> messages = (ArrayList<Message>) this.messageService.getPotentialDemandConversation(
                threadRoot, user);
        final ArrayList<MessageDetail> messageDetailImpls = new ArrayList<MessageDetail>();
        for (Message message : messages) {
            messageDetailImpls.add(MessageDetail.createMessageDetail(message));
        }
        return messageDetailImpls;
    }

    public ArrayList<MessageDetail> loadClientsPotentialOfferConversation(long threadId, long userId) {
        final ArrayList<Message> messages = (ArrayList<Message>) this.messageService.getPotentialOfferConversation(
                messageService.getById(threadId), this.generalService.find(User.class, userId));
        final ArrayList<MessageDetail> messageDetailImpls = new ArrayList<MessageDetail>();
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
    @Transactional(propagation = Propagation.REQUIRED)
    public void setMessageReadStatus(List<Long> userMessageIds, boolean isRead) {
        for (Long userMessageId : userMessageIds) {
            final UserMessage userMessage = this.generalService.find(UserMessage.class, userMessageId);
            userMessage.setRead(isRead);
            this.userMessageService.update(userMessage);
        }
    }

    /**
     * COMMON.
     * Change 'star' status of sent messages to chosen value
     */
    @Override
    public void setMessageStarStatus(List<Long> userMessageIds, boolean isStarred) {
        for (Long userMessageId : userMessageIds) {
            final UserMessage userMessage = this.generalService.find(UserMessage.class, userMessageId);
            userMessage.setStarred(isStarred);
            this.userMessageService.update(userMessage);
        }
    }

    /**
     * SUPPLIER.
     * Returns messages for PotentialDemandsView's table
     */
    @Override
    public ArrayList<PotentialDemandMessage> getPotentialDemands(long businessUserId) {
        final BusinessUser businessUser = getBusinessUser(businessUserId);
        final List<UserMessage> potentialDemands = this.userMessageService.getPotentialDemands(businessUser);

        final ArrayList<PotentialDemandMessage> potentialDemandsDetails = new ArrayList<PotentialDemandMessage>();
        for (UserMessage potentialDemand : potentialDemands) {
            PotentialDemandMessage detail = PotentialDemandMessage.createMessageDetail(potentialDemand);
            detail.setClientRating(ratingService.getAvgRating(potentialDemand.getMessage().getDemand().getClient()));
            detail.setMessageCount(messageService.getAllDescendantsCount(potentialDemand.getMessage(), businessUser));
            detail.setUnreadSubmessages(messageService.getUnreadDescendantsCount(
                    potentialDemand.getMessage(), businessUser));
            potentialDemandsDetails.add(detail);
        }
        return potentialDemandsDetails;
    }


    //TODO Martin - if not used - delete it
//    private Search setPotentialDemandsFilter(SearchModuleDataHolder searchDataHolder, Search search) {
//        if (searchDataHolder.getPotentialDemandMessages().getSender() != null) {
//            search.addFilterLike("sender.email", searchDataHolder.getPotentialDemandMessages().getSender());
//        }
//        if (searchDataHolder.getPotentialDemandMessages().getUrgention() != null) {
//            Calendar calendarDate1 = Calendar.getInstance(); //today
//            Calendar calendarDate2 = Calendar.getInstance();
//            switch (searchDataHolder.getPotentialDemandMessages().getUrgention()) {
//                case 1: //ends in one day => urgent
//                    calendarDate1.add(Calendar.DATE, +1);
//                    search.addFilterLessOrEqual("demand.endDate", calendarDate1.DATE);
//                    break;
//                case 2: //ends in one week, but not tomorrow => less urgent
//                    calendarDate1.add(Calendar.DATE, +1);
//                    calendarDate2.add(Calendar.DATE, +7);
//                    search.addFilterGreaterOrEqual("demand.endDate", calendarDate1.DATE);
//                    search.addFilterLessOrEqual("demand.endDate", calendarDate2.DATE);
//                    break;
//                case 3: //ends in one month, but not next week => normal
//                    calendarDate1.add(Calendar.DATE, +7);
//                    calendarDate2.add(Calendar.MONTH, +1);
//                    search.addFilterGreaterOrEqual("demand.endDate", calendarDate1.DATE);
//                    search.addFilterLessOrEqual("demand.endDate", calendarDate2.DATE);
//                    break;
//                case 4: //ends in more than one month => less normal
//                    calendarDate2.add(Calendar.MONTH, +1);
//                    search.addFilterGreaterOrEqual("demand.endDate", calendarDate2.DATE);
//                    break;
//                default:
//                    ;
//            }
//        }
//        if (searchDataHolder.getPotentialDemandMessages().getCreatedFrom() != null) {
//            search.addFilterGreaterOrEqual("created",
//                    searchDataHolder.getPotentialDemandMessages().getCreatedFrom());
//        }
//        if (searchDataHolder.getPotentialDemandMessages().getCreatedTo() != null) {
//            search.addFilterLessOrEqual("created", searchDataHolder.getPotentialDemandMessages().getCreatedTo());
//        }
//        //Demand
//        if (searchDataHolder.getPotentialDemandMessages().getRatingFrom() != null) {
//            search.addFilterGreaterOrEqual("demand.client.overalRating",
//                    searchDataHolder.getPotentialDemandMessages().getRatingFrom());
//        }
//        if (searchDataHolder.getPotentialDemandMessages().getRatingTo() != null) {
//            search.addFilterLessOrEqual("demand.client.overalRating",
//                    searchDataHolder.getPotentialDemandMessages().getRatingTo());
//        }
//        if (searchDataHolder.getPotentialDemandMessages().getDemandTitle() != null) {
//            search.addFilterLike("demand.title", searchDataHolder.getPotentialDemandMessages().getDemandTitle());
//        }
//        //TODO demand.price alebo offer.price???
//        if (searchDataHolder.getPotentialDemandMessages().getPriceFrom() != null) {
//            search.addFilterGreaterOrEqual("demand.price",
//                    searchDataHolder.getPotentialDemandMessages().getPriceFrom());
//        }
//        if (searchDataHolder.getPotentialDemandMessages().getPriceTo() != null) {
//            search.addFilterLessOrEqual("demand.price", searchDataHolder.getPotentialDemandMessages().getPriceTo());
//        }
//        return search;
//    }
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
        final List<UserMessage> userMessages =
                userMessageService.getOffersUserMessages(getBusinessUser(businessUserId));
        // fill list

        final ArrayList<OfferDemandMessage> offerDemands = new ArrayList<OfferDemandMessage>();
        for (UserMessage userMessage : userMessages) {
            offerDemands.add(OfferDemandMessage.createMessageDetail(userMessage));
        }
        return offerDemands;
    }

    private BusinessUser getBusinessUser(long businessUserId) {
        return this.generalService.find(BusinessUser.class, businessUserId);
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

    /**
     * Get all conversations of thread.
     * @param threadRootId
     * @param subRootId
     * @return conversation list
     */
    @Override
    public ArrayList<MessageDetail> getConversationMessages(long threadRootId, long subRootId) {
//        Message root = messageService.getById(threadRootId);
        Message subRoot = messageService.getById(subRootId);
        List<Message> conversation = messageService.getAllDescendants(subRoot);

        ArrayList<MessageDetail> result = new ArrayList<MessageDetail>();
        // add root and subRoot message
//        result.add(MessageDetail.createMessageDetail(root));
        result.add(MessageDetail.createMessageDetail(subRoot));
        for (Message m : conversation) {
            result.add(MessageDetail.createMessageDetail(m));
        }
        return result;
    }

    private List<UserMessageDetail> createUserMessageDetailList(Collection<UserMessage> userMessages) {
        final List<UserMessageDetail> userMessageDetails = new ArrayList<UserMessageDetail>();
        for (UserMessage userMessage : userMessages) {
            UserMessageDetail demandDetail = UserMessageDetail.createUserMessageDetail(userMessage);
            userMessageDetails.add(demandDetail);
        }
        return userMessageDetails;
    }

    @Override
    public List<UserMessageDetail> getInboxMessages(Long recipientId, SearchModuleDataHolder searchDataHolder) {
        return this.getMessages(recipientId, searchDataHolder, Arrays.asList(
                UserMessageRoleType.TO, UserMessageRoleType.CC, UserMessageRoleType.BCC));
    }

    @Override
    public List<UserMessageDetail> getSentMessages(Long senderId, SearchModuleDataHolder searchDataHolder) {
        User sender = generalService.find(User.class, senderId);

        /****/// ziskaj vsetky spravy poslane danym uzivatelom
        Search messageSearch = new Search(Message.class);
        messageSearch.addFilterEqual("sender", sender);
        //ak treba, filtruj spravy poslane danym uzivatelom
        if (searchDataHolder != null) {
            if (searchDataHolder.getMessagesTab().getSubject() != null) {
                messageSearch.addFilterLike("subject", "%" + searchDataHolder.getMessagesTab().getSubject() + "%");
            }
            if (searchDataHolder.getMessagesTab().getBody() != null) {
                messageSearch.addFilterLike("body", "%" + searchDataHolder.getMessagesTab().getBody() + "%");
            }
        }

        /****/
        final Map<Long, Message> senderMessages = new HashMap<Long, Message>();
        /****/
        final List<Message> senderMessagesTmp = new ArrayList<Message>();
        // add only thread root messages, not every message (many of them maybe only the replies to some other messages)
        senderMessagesTmp.addAll(generalService.search(messageSearch));
        for (Message msg : senderMessagesTmp) {
            if (!senderMessages.containsKey(msg.getThreadRoot().getId())) {
                senderMessages.put(msg.getThreadRoot().getId(), msg);
            }
        }

        /****///Ziskaj vsetkych prijemcov danych sprav
        final Search userMessageSearch = new Search(UserMessage.class);
        final List<UserMessage> recipients = new ArrayList<UserMessage>();
        userMessageSearch.addFilterIn("roleType", UserMessageRoleType.TO);
        //ak treba, filtruj prijemcov danych sprav
        if (searchDataHolder != null) {
            userMessageSearch.addFilterIn("user", generalService.search(
                    new Search(User.class).addFilterLike(
                        "email", "%" + searchDataHolder.getMessagesTab().getSender() + "%")));
        }
        recipients.addAll(generalService.search(userMessageSearch));

        todoDeleteOrRefactor();

        //Create details
        final List<UserMessageDetail> inboxMessagesDetail = new ArrayList<UserMessageDetail>();
//        for (MessageUserRole)
        for (UserMessage recipient : recipients) {
            inboxMessagesDetail.add(UserMessageDetail.createUserMessageDetail(recipient));
        }

        return inboxMessagesDetail;
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
            List<UserMessageRoleType> roles) {
        Validate.notNull(searchDataHolder);
        final User recipient = generalService.find(User.class, recipientId);
        final Search messageSearch = new Search(Message.class);
        if (searchDataHolder.getMessagesTab().getSender() != null) {
            messageSearch.addFilterIn("sender", generalService.search(
                    new Search(User.class).addFilterLike(
                            "email", "%" + searchDataHolder.getMessagesTab().getSender() + "%")));
        }
        if (searchDataHolder.getMessagesTab().getSubject() != null) {
            messageSearch.addFilterLike("subject", "%" + searchDataHolder.getMessagesTab().getSubject() + "%");
        }
        if (searchDataHolder.getMessagesTab().getBody() != null) {
            messageSearch.addFilterLike("body", "%" + searchDataHolder.getMessagesTab().getBody() + "%");
        }

        //Ziskaj vsetky spravy daneho uzivatela, kt bol oznaceny ako adresat alebo odosielatel
        final List<UserMessage> recipientMessages = new ArrayList<UserMessage>();
        Search recipientMessagesSearch = new Search(UserMessage.class);
        recipientMessagesSearch.addFilterEqual("user", recipient);
        recipientMessagesSearch.addFilterIn("type", roles);

        recipientMessagesSearch.addFilterIn("message", generalService.search(messageSearch));

        recipientMessages.addAll(generalService.search(recipientMessagesSearch));

//        Search firstBornRecipientMessagesSearch = new Search(Message.class);
//        List<Message> firstBornRecipientMessages = new ArrayList<Message>();
//        for (MessageUserRole mur : recipientMessages) {
//            firstBornRecipientMessagesSearch.addFilterEqual("id", mur.getMessage().getId());
//            firstBornRecipientMessages = generalService.search(firstBornRecipientMessagesSearch);
//        }

        final List<UserMessageDetail> inboxMessagesDetail = new ArrayList<UserMessageDetail>();
        for (UserMessage userMessage : recipientMessages) {
            if (hasThreadRootMessage(userMessage)) {
                inboxMessagesDetail.add(UserMessageDetail.createUserMessageDetail(userMessage));
            }
        }

        return inboxMessagesDetail;
    }

    private boolean hasThreadRootMessage(UserMessage userMessage) {
        return userMessage.getMessage().getParent() == null;
    }

    @Override
    public List<UserMessageDetail> getDeletedMessages(Long userId, SearchModuleDataHolder searchDataHolder) {
        final Search deletedUserMessageSearch = new Search(UserMessage.class);
        deletedUserMessageSearch.addFilterEqual("user", generalService.find(User.class, userId));
        deletedUserMessageSearch.addFilterEqual("message.messageState", MessageState.DELETED);
        if (searchDataHolder != null) {
            if (searchDataHolder.getMessagesTab().getSender() != null) {
                deletedUserMessageSearch.addFilterIn("message.sender", generalService.search(
                        new Search(User.class).addFilterLike(
                                "message.email", "%" + searchDataHolder.getMessagesTab().getSender() + "%")));
            }
            if (searchDataHolder.getMessagesTab().getSubject() != null) {
                deletedUserMessageSearch.addFilterLike("message.subject",
                        "%" + searchDataHolder.getMessagesTab().getSubject() + "%");
            }
            if (searchDataHolder.getMessagesTab().getBody() != null) {
                deletedUserMessageSearch.addFilterLike("message.body",
                        "%" + searchDataHolder.getMessagesTab().getBody() + "%");
            }
        }
        final List<UserMessage> deletedUserMessages = this.generalService.search(deletedUserMessageSearch);

        // TODO jumar - why TreeMap?
        final Map<Long, UserMessage> rootDeletedMessages = new TreeMap<Long, UserMessage>();

        for (UserMessage userMessage : deletedUserMessages) {
            if (hasThreadRootMessage(userMessage)) {
                if (!rootDeletedMessages.containsKey(userMessage.getMessage().getThreadRoot().getId())) {
                    rootDeletedMessages.put(userMessage.getMessage().getThreadRoot().getId(), userMessage);
                }
            }
        }
        final List<UserMessageDetail> deletedMessagesDetail = new ArrayList<UserMessageDetail>();

        for (UserMessage userMessage : rootDeletedMessages.values()) {
            deletedMessagesDetail.add(UserMessageDetail.createUserMessageDetail(userMessage));
        }

        return deletedMessagesDetail;
    }

    @Override
    public void deleteMessages(List<Long> messagesIds) {
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
}