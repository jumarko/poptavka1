/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.server.service.clientdemands;

import com.eprovement.poptavka.client.service.demand.ClientDemandsModuleRPCService;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.enums.CommonAccessRoles;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.offer.Offer;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.exception.MessageException;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.security.PoptavkaUserAuthentication;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.server.util.SearchUtils;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.demand.DemandService;
import com.eprovement.poptavka.service.message.MessageService;
import com.eprovement.poptavka.service.offer.OfferService;
import com.eprovement.poptavka.service.user.ClientService;
import com.eprovement.poptavka.service.user.SupplierService;
import com.eprovement.poptavka.service.user.UserSearchCriteria;
import com.eprovement.poptavka.service.usermessage.UserMessageService;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandConversationDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.domain.offer.ClientOfferedDemandOffersDetail;
import com.eprovement.poptavka.shared.domain.offer.FullOfferDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.util.search.Searcher;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Martin Slavkovsky
 */
@Configurable
public class ClientDemandsModuleRPCServiceImpl extends AutoinjectingRemoteService
        implements ClientDemandsModuleRPCService {

    public static final String QUERY_TO_POTENTIAL_DEMAND_SUBJECT = "Dotaz na Vasu zadanu poptavku";
    //Services
    private ClientService clientService;
    private SupplierService supplierService;
    private GeneralService generalService;
    private OfferService offerService;
    private UserMessageService userMessageService;
    private MessageService messageService;
    private DemandService demandService;
    //Converters
    private Converter<Demand, FullDemandDetail> demandConverter;
    private Converter<Supplier, FullSupplierDetail> supplierConverter;
    private Converter<Message, MessageDetail> messageConverter;
    private Converter<Search, SearchDefinition> searchConverter;
    private Converter<Demand, ClientDemandDetail> clientDemandConverter;
    private Converter<Message, FullOfferDetail> fullOfferConverter;

    /**
     * ***********************************************************************
     */
    /*
     * Autowired methods
     */
    /**
     * ***********************************************************************
     */
    //Services
    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Autowired
    public void setSupplierService(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
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

    //Converters
    @Autowired
    public void setDemandConverter(
            @Qualifier("fullDemandConverter") Converter<Demand, FullDemandDetail> demandConverter) {
        this.demandConverter = demandConverter;
    }

    @Autowired
    public void setSupplierConverter(
            @Qualifier("supplierConverter") Converter<Supplier, FullSupplierDetail> supplierConverter) {
        this.supplierConverter = supplierConverter;
    }

    @Autowired
    public void setMessageConverter(
            @Qualifier("messageConverter") Converter<Message, MessageDetail> messageConverter) {
        this.messageConverter = messageConverter;
    }

    @Autowired
    public void setSearchConverter(
            @Qualifier("searchConverter") Converter<Search, SearchDefinition> searchConverter) {
        this.searchConverter = searchConverter;
    }

    @Autowired
    public void setClientDemandConverter(
            @Qualifier("clientDemandConverter") Converter<Demand, ClientDemandDetail> clientDemandConverter) {
        this.clientDemandConverter = clientDemandConverter;
    }

    @Autowired
    public void setFullOfferConverter(
            @Qualifier("fullOfferConverter") Converter<Message, FullOfferDetail> fullOfferConverter) {
        this.fullOfferConverter = fullOfferConverter;
    }

    /**
     * ***********************************************************************
     */
    /*
     * Table getter methods
     */
    /**
     * ***********************************************************************
     */
    //************************* CLIENT - My Demands ***************************/
    /**
     * Get all demand's count that has been created by client. When new demand is created by client, will be involved
     * here. As Client: "All demands created by me."
     *
     * @param userId id of user represented by client. Note that userId and userId are different If userId represents
     * some different user than client, exception will be thrown
     * @param filter - define searching criteria if any
     * @return count
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public long getClientDemandsCount(long userId,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        final Client client = findClient(userId);
        final Search clientDemandsSearch = searchConverter.convertToSource(searchDefinition);
        clientDemandsSearch.setSearchClass(Demand.class);
        clientDemandsSearch.addFilterEqual("status", DemandStatus.ACTIVE);
        ArrayList<DemandStatus> demandStatuses = new ArrayList<DemandStatus>();
        demandStatuses.add(DemandStatus.ACTIVE);
        demandStatuses.add(DemandStatus.NEW);
        demandStatuses.add(DemandStatus.INVALID);
        demandStatuses.add(DemandStatus.INACTIVE);
        clientDemandsSearch.addFilterIn("status", demandStatuses);
        clientDemandsSearch.addFilterEqual("status", DemandStatus.ACTIVE);
        return Searcher.searchCollection(client.getDemands(), clientDemandsSearch).size();
    }

    /**
     * {@inheritDoc}
     *
     * <p> PERFORMANCE: This implementation can be slow if client has large amount of demands because it loads all his
     * demands from DB into the memory and perform filtering afterwards. It this is the issue then consider
     * reimplementation of this method. </p>
     *
     * @param userId id of user represented by client. Note that userId and userId are different If userId represents
     * some different user than client, exception will be thrown
     * @return list of demand's detail objects
     * @throws ApplicationSecurityException if user is not authorized to call this method
     * @throws IllegalArgumentException if given user does not exist or it represents user other than client
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public List<ClientDemandDetail> getClientDemands(long userId, SearchDefinition searchDefinition) throws
            RPCException, ApplicationSecurityException, IllegalArgumentException {
        final Client client = findClient(userId);
        final Search clientDemandsSearch = searchConverter.convertToSource(searchDefinition);
        clientDemandsSearch.setSearchClass(Demand.class);
        ArrayList<DemandStatus> demandStatuses = new ArrayList<DemandStatus>();
        demandStatuses.add(DemandStatus.ACTIVE);
        demandStatuses.add(DemandStatus.NEW);
        demandStatuses.add(DemandStatus.INVALID);
        demandStatuses.add(DemandStatus.INACTIVE);
        clientDemandsSearch.addFilterIn("status", demandStatuses);
        final List<Demand> clientDemands = Searcher.searchCollection(client.getDemands(), clientDemandsSearch);
        ArrayList<ClientDemandDetail> cdds = clientDemandConverter.convertToTargetList(clientDemands);

        Iterator it = messageService.getListOfClientDemandMessagesUnread(generalService.find(
                User.class, userId)).entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            // key is message and val is number
            Long messageId = (Long) pairs.getKey();
            for (ClientDemandDetail cdd : cdds) {
                if (cdd.getDemandId() == messageId) {
                    cdd.setUnreadMessageCount(((Integer) pairs.getValue()).intValue());
                    break;
                }
            }
            it.remove();
        }
        return cdds;
    }

    /**
     * When supplier asks something about a demand of some client. The conversation has more messages of course but I
     * want count of threads. As Client: "Questions made by suppliers to demands made by me." "How many suppliers are
     * asing something about a certain demand."
     *
     * @param userId id of user represented by client. Note that userId and userId are different If userId represents
     * some different user than client, exception will be thrown
     * @param demandID - demand's ID
     * @param filter - define searching criteria if any
     * @return count
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public long getClientDemandConversationsCount(long userId, long demandID,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        //TODO Martin/Ivlcek - change long return type to int
        Message root = messageService.getThreadRootMessage(generalService.find(Demand.class, demandID));
        return root.getChildren().size();
    }

    /**
     * Get a list of latest user messages from suppliers who sent a question response to given demand.
     *
     * @param userId logged client user Id
     * @param demandID demand with question responses from suppliers
     * @param searchDefinition defines a search criteria for pager etc.
     * @return list of latest user messages from suppliers with total count of submessages
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public List<ClientDemandConversationDetail> getClientDemandConversations(long userId, long demandID,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        User user = generalService.find(User.class, userId);
        Message root = messageService.getThreadRootMessage(generalService.find(Demand.class, demandID));
        List<ClientDemandConversationDetail> list = new ArrayList<ClientDemandConversationDetail>();

        Map<Long, Integer> latestSupplierUserMessagesWithUnreadSub =
                messageService.getLatestSupplierUserMessagesWithoutOfferForDemnd(user, root);

        for (Long userMessageIdKey : latestSupplierUserMessagesWithUnreadSub.keySet()) {
            UserMessage userMessage = (UserMessage) generalService.find(UserMessage.class, userMessageIdKey);
            // TODO ivlcek - make detail object converter
            // TODO ivlcek - implement searchDefinition
            ClientDemandConversationDetail cdcd = new ClientDemandConversationDetail();
            // set UserMessage attributes
            cdcd.setUserMessageId(userMessage.getId());
            cdcd.setIsStarred(userMessage.isStarred());
            cdcd.setIsRead(userMessage.isRead());
            cdcd.setMessageCount(latestSupplierUserMessagesWithUnreadSub.get(userMessageIdKey));
            // set Message attributes
            cdcd.setMessageId(userMessage.getMessage().getId());
            cdcd.setThreadRootId(userMessage.getMessage().getThreadRoot().getId());
            cdcd.setMessageBody(userMessage.getMessage().getBody());
            cdcd.setMessageSent(userMessage.getMessage().getSent());
            // set Demand attributes
            cdcd.setDemandId(demandID);
            // set Supplier attributes
            Supplier supplier = findSupplier(userMessage.getMessage().getSender().getId());
            cdcd.setSupplierId(supplier.getId());
            cdcd.setSupplierName(supplier.getBusinessUser().getBusinessUserData().getDisplayName());
            cdcd.setSenderId(supplier.getBusinessUser().getId());

            list.add(cdcd);
        }
        return list;
    }

    //************************* CLIENT - My Offers ****************************/
    /**
     * Get all demands where have been placed an offer by some supplier. When supplier place an offer to client's
     * demand, the demand will be involved here. As Client: "Demands that have already an offer."
     *
     * @param userId id of user represented by client. Note that userId and userId are different If userId represents
     * some different user than client, exception will be thrown
     * @param filter
     * @return
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public long getClientOfferedDemandsCount(long userId,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        return demandService.getClientDemandsWithOfferCount(findClient(userId));
    }

    /**
     * Get all demands where have been placed an offer by some supplier. When supplier place an offer to client's
     * demand, the demand will be involved here. As Client: "Demands that have already an offer."
     *
     * @param userId id of user represented by client. Note that userId and userId are different If userId represents
     * some different user than client, exception will be thrown
     * @param demandID - demands's ID
     * @param start
     * @param maxResult
     * @param filter
     * @param orderColumns
     * @return
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public List<ClientDemandDetail> getClientOfferedDemands(long userId,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        // load list of client demands with offer
        List<Demand> clientDemands = demandService.getClientDemandsWithOffer(findClient(userId));
        ArrayList<ClientDemandDetail> cdds = clientDemandConverter.convertToTargetList(clientDemands);

        // load a map of unread messages for all client demands with offer, then iterate this map and
        // match with clientDemands
        Iterator it = messageService.getListOfClientDemandMessagesWithOfferUnreadSub(
                generalService.find(User.class, userId)).entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            // key is messageId and val is number of unread submessages
            Long messageId = (Long) pairs.getKey();
            for (ClientDemandDetail cdd : cdds) {
                if (cdd.getDemandId() == messageId) {
                    cdd.setUnreadMessageCount(((Integer) pairs.getValue()).intValue());
                    break;
                }
            }
            it.remove();
        }
        return cdds;
    }

    /**
     * Get all offers of given demand. When supplier place an offer to client's demand, the offer will be involved here.
     * As Client: "How many suppliers placed an offers to a certain demand."
     *
     * @param userId id of user represented by client. Note that userId and userId are different If userId represents
     * some different user than client, exception will be thrown
     * @return offers count of given demand
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public long getClientOfferedDemandOffersCount(long userId, long demandID,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        // TODO ivlcek - incorporate searchDefinition for this method
        Demand demand = generalService.find(Demand.class, demandID);
        return demand.getOffers().size();
    }

    /**
     * Get a list of latest user messages from suppliers who sent a offer response to given demand.
     *
     * @param userId logged client user Id
     * @param demandID demand with offer responses from suppliers
     * @param threadRootId thread root message that represents demand message made by client
     * @param searchDefinition defines a search criteria for pager etc.
     * @return list of latest user messages from suppliers with total count of submessages
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public List<ClientOfferedDemandOffersDetail> getClientOfferedDemandOffers(long userId, long demandID,
            long threadRootId, SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        User user = generalService.find(User.class, userId);
        Message root = messageService.getThreadRootMessage(generalService.find(Demand.class, demandID));
        List<ClientOfferedDemandOffersDetail> listCodod = new ArrayList<ClientOfferedDemandOffersDetail>();

        Map<Long, Integer> latestSupplierUserMessagesWithUnreadSub =
                messageService.getLatestSupplierUserMessagesWithOfferForDemnd(user, root);
        for (Long userMessageIdKey : latestSupplierUserMessagesWithUnreadSub.keySet()) {
            UserMessage userMessage = (UserMessage) generalService.find(UserMessage.class, userMessageIdKey);
            Offer offer = userMessage.getMessage().getOffer();
            // TODO ivlcek - refactor and create converter
            ClientOfferedDemandOffersDetail codod = new ClientOfferedDemandOffersDetail();
            // set UserMessage attributes
            codod.setIsRead(userMessage.isRead());
            codod.setIsStarred(userMessage.isStarred());
            codod.setMessageCount(latestSupplierUserMessagesWithUnreadSub.get(userMessageIdKey));
            codod.setThreadRootId(userMessage.getMessage().getThreadRoot().getId());
            // set Supplier attributes
            codod.setSupplierId(offer.getSupplier().getId());
            codod.setSupplierName(offer.getSupplier().getBusinessUser().getBusinessUserData().getDisplayName());
            codod.setRating(offer.getSupplier().getOveralRating());
            codod.setSupplierUserId(offer.getSupplier().getBusinessUser().getId());
            // set Offer attributes
            codod.setOfferId(offer.getId());
            codod.setDemandId(offer.getDemand().getId());
            codod.setPrice(offer.getPrice().toPlainString());
            codod.setDeliveryDate(offer.getFinishDate());
            codod.setReceivedDate(offer.getCreated());

            listCodod.add(codod);
        }
        return listCodod;
    }

    private Search getSearchForgetClientOfferedDemandOffers(SearchDefinition searchDefinition,
            long userId, long demandId) {
        List<String> searchAttributes = new ArrayList<String>();
        searchAttributes.add("message.demand.title");
        searchAttributes.add("message.offer.supplier.businessUser.businessUserData.companyName");
        searchAttributes.add("message.offer.supplier.businessUser.businessUserData.personFirstName");
        searchAttributes.add("message.offer.supplier.businessUser.businessUserData.personLastName");
        Search backendSearch = SearchUtils.toBackendSearch(UserMessage.class,
                searchDefinition, searchAttributes, "message.offer.suppplier.");
        backendSearch.addFilter(new Filter("user.id",
                userId));
        backendSearch.addFilter(new Filter("message.threadRoot.sender.id",
                userId));
        backendSearch.addFilter(new Filter("message.parent.parent", null, Filter.OP_NULL));
        backendSearch.addFilter(new Filter("message.parent", null, Filter.OP_NOT_NULL));
        backendSearch.addFilter(new Filter("message.offer", null, Filter.OP_NOT_NULL));

        backendSearch.addFilter(new Filter("message.demand.id", demandId));
        return backendSearch;
    }

    //******************** CLIENT - My Assigned Demands ***********************/
    /**
     * Get all offers that were accepted by client to solve a demand. When client accept an offer, will be involved
     * here. As Client: "All offers that were accepted by me to solve my demand."
     *
     * @param userId id of user represented by client. Note that userId and userId are different If userId represents
     * some different user than client, exception will be thrown
     * @param filter
     * @return
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public long getClientAssignedDemandsCount(long userId,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        //TODO Martin - implement when implemented on backend
        return 1L;
    }

    /**
     * Get all offers that were accepted by client to solve a demand. When client accept an offer, will be involved
     * here. As Client: "All offers that were accepted by me to solve my demand."
     *
     * @param userId id of user represented by client. Note that userId and userId are different If userId represents
     * some different user than client, exception will be thrown
     * @param searchDefinition search filters, ordering
     * @return
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public List<ClientDemandDetail> getClientAssignedDemands(long userId,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        //TODO Martin - implement when implemented on backend

        final Client client = findClient(userId);
        final Search clientDemandsSearch = searchConverter.convertToSource(searchDefinition);
        clientDemandsSearch.setSearchClass(Demand.class);
        ArrayList<DemandStatus> demandStatuses = new ArrayList<DemandStatus>();
        demandStatuses.add(DemandStatus.ACTIVE);
        demandStatuses.add(DemandStatus.NEW);
        demandStatuses.add(DemandStatus.INVALID);
        demandStatuses.add(DemandStatus.INACTIVE);
        clientDemandsSearch.addFilterIn("status", demandStatuses);
        final List<Demand> clientDemands = Searcher.searchCollection(client.getDemands(), clientDemandsSearch);
        ArrayList<ClientDemandDetail> cdds = clientDemandConverter.convertToTargetList(clientDemands);

        Iterator it = messageService.getListOfClientDemandMessagesUnread(generalService.find(
                User.class, userId)).entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            // key is message and val is number
            Long messageId = (Long) pairs.getKey();
            for (ClientDemandDetail cdd : cdds) {
                if (cdd.getDemandId() == messageId) {
                    cdd.setUnreadMessageCount(((Integer) pairs.getValue()).intValue());
                    break;
                }
            }
            it.remove();
        }
        return cdds;
    }

    /**
     * ***********************************************************************
     */
    /*
     * Other getter methods
     */
    /**
     * ***********************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public FullDemandDetail getFullDemandDetail(long demandId) throws RPCException, ApplicationSecurityException {
        return demandConverter.convertToTarget(generalService.find(Demand.class, demandId));
    }

    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public FullSupplierDetail getFullSupplierDetail(long supplierId) throws RPCException, ApplicationSecurityException {
        return supplierConverter.convertToTarget(generalService.find(Supplier.class, supplierId));
    }

    @Override
    // TODO is this RPC service the correct place for this and following methods ?!
    // TODO call setMessageReadStatus in body
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public ArrayList<MessageDetail> getSuppliersPotentialDemandConversation(
            long threadId, long userId, long userMessageId) throws RPCException, ApplicationSecurityException {
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

    /**
     * ***********************************************************************
     */
    /*
     * Setter methods
     */
    /**
     * ***********************************************************************
     */
    /**
     * COMMON. Change 'read' status of sent messages to chosen value
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public void setMessageReadStatus(List<Long> userMessageIds, boolean isRead) throws RPCException,
            ApplicationSecurityException {
        for (Long userMessageId : userMessageIds) {
            UserMessage userMessage = this.generalService.find(UserMessage.class, userMessageId);
            userMessage.setRead(isRead);
            this.userMessageService.update(userMessage);
        }
    }

    /**
     * COMMON. Change 'star' status of sent messages to chosen value
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public void setMessageStarStatus(List<Long> userMessageIds, boolean isStarred) throws RPCException,
            ApplicationSecurityException {
        for (Long userMessageId : userMessageIds) {
            UserMessage userMessage = this.generalService.find(UserMessage.class, userMessageId);
            userMessage.setStarred(isStarred);
            this.userMessageService.update(userMessage);
        }
    }

    /**
     * When demand is finished (when supplier delivered what client asked), client can finally close demand. At the end
     * of whole process.
     *
     * @param demandDetail
     * @throws RPCException
     * @throws ApplicationSecurityException W
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public void closeDemand(long id) throws RPCException, ApplicationSecurityException {
        //TODO Juraj - skontrolovat
        Demand demand = generalService.find(Demand.class, id);
        demand.setStatus(DemandStatus.CLOSED);
        generalService.merge(demand);
        //Asi nech zmizne aj zo SupplierAssignedDemands
    }

    /**
     * Accept selected offer and decline other offers and change demand state to ASSIGNED.
     *
     * @param offerId to be accepted
     * @return
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public void acceptOffer(long offerId) throws RPCException, ApplicationSecurityException {
        Offer offer = this.generalService.find(Offer.class, offerId);

        // set offer as accepted
        offer.setState(offerService.getOfferState(OfferStateType.ACCEPTED.getValue()));
        generalService.merge(offer);

        // load other offers and set them as declined
        Demand demand = offer.getDemand();
        for (Offer declinedOffer : demand.getOffers()) {
            declinedOffer.setState(offerService.getOfferState(OfferStateType.ACCEPTED.getValue()));
            generalService.merge(declinedOffer);
        }
        demand.setStatus(DemandStatus.ASSIGNED);
        generalService.merge(demand);
    }

    /**
     * Message sent by supplier about a query to potential demand.
     *
     * @param messageDetailImpl
     * @return message
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public MessageDetail sendQueryToPotentialDemand(MessageDetail messageDetailImpl) throws RPCException,
            ApplicationSecurityException {
        try {
            Message m = messageService.newReply(this.messageService.getById(
                    messageDetailImpl.getParentId()),
                    this.generalService.find(User.class, messageDetailImpl.getSenderId()));
            m.setBody(messageDetailImpl.getBody());
            m.setSubject(messageDetailImpl.getSubject());
            // TODO set the id correctly, check it
            MessageDetail messageDetailFromDB = messageConverter.convertToTarget(this.messageService.create(m));
            return messageDetailFromDB;
        } catch (MessageException ex) {
            Logger.getLogger(ClientDemandsModuleRPCServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    //--------------------------------------------------- HELPER METHODS -----------------------------------------------
    private Client findClient(long userId) {
        final User user = generalService.find(User.class, userId);
        if (user == null) {
            throw new IllegalArgumentException("User with id=" + userId + " does not exist!");
        }
        if (StringUtils.isEmpty(user.getEmail())) {
            throw new IllegalStateException("User has no email. Invalid state of explication! Each user MUST HAVE"
                    + " a valid email address!");
        }
        final List<Client> clients = this.clientService.searchByCriteria(
                UserSearchCriteria.Builder.userSearchCriteria().withEmail(user.getEmail()).build());
        if (CollectionUtils.isEmpty(clients)) {
            throw new IllegalArgumentException("No client with email=" + user.getEmail() + " has been found!");
        }

        if (clients.size() > 1) {
            throw new IllegalArgumentException("One client  with email=" + user.getEmail() + " expected, but found"
                    + clients.size());
        }

        return clients.get(0);
    }

    private Supplier findSupplier(long userId) {
        final User user = generalService.find(User.class, userId);
        if (user == null) {
            throw new IllegalArgumentException("User with id=" + userId + " does not exist!");
        }
        if (StringUtils.isEmpty(user.getEmail())) {
            throw new IllegalStateException("User has no email. Invalid state of explication! Each user MUST HAVE"
                    + " a valid email address!");
        }
        final List<Supplier> suppliers = this.supplierService.searchByCriteria(
                UserSearchCriteria.Builder.userSearchCriteria().withEmail(user.getEmail()).build());
        if (CollectionUtils.isEmpty(suppliers)) {
            throw new IllegalArgumentException("No supplier with email=" + user.getEmail() + " has been found!");
        }

        if (suppliers.size() > 1) {
            throw new IllegalArgumentException("One supplier with email=" + user.getEmail() + " expected, but found"
                    + suppliers.size());
        }

        return suppliers.get(0);
    }

    /**
     * This method will update number of unread messages of logged user. Since this RPC class requires access of
     * authenticated user (see security-web.xml) this method will be called only when PoptavkaUserAuthentication object
     * exist in SecurityContextHolder and we can retrieve userId.
     *
     * TODO Vojto - call DB servise to retrieve the number of unread messages for given userId
     *
     * @return UnreadMessagesDetail with number of unread messages and other info to be displayed after users logs in
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public UnreadMessagesDetail updateUnreadMessagesCount() throws RPCException, ApplicationSecurityException {
        Long userId = ((PoptavkaUserAuthentication) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        // TODO Vojto - get number of unread messages. UserId is provided from Authentication obejct see above
        UnreadMessagesDetail unreadMessagesDetail = new UnreadMessagesDetail();
        unreadMessagesDetail.setUnreadMessagesCount(99);
        return unreadMessagesDetail;
    }

    /**
     * ***********************************************************************
     */
    /*
     * Get Detail object for selecting in selection models
     */
    /**
     * ***********************************************************************
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public ClientDemandDetail getClientDemand(long clientDemandID) throws RPCException, ApplicationSecurityException {
        return clientDemandConverter.convertToTarget(generalService.find(Demand.class, clientDemandID));
    }

    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public ClientDemandConversationDetail getClientDemandConversation(long clientDemandConversationID) throws
            RPCException, ApplicationSecurityException {
        //Neviem ako a z coho to zickat este, mozno nakoniec to nemusi byt ani ClientDemandConversationDetail();
        if (clientDemandConversationID == 1L) {
            ClientDemandConversationDetail a1 = new ClientDemandConversationDetail();
            a1.setUserMessageId(1L);
            a1.setSupplierId(1L);
            a1.setSupplierName("Good Data");
            a1.setMessageBody("Tak ak date cenu o 10% dole ta to beriem.");
            a1.setMessageSent(new Date());
            return a1;
        } else if (clientDemandConversationID == 2L) {
            ClientDemandConversationDetail a2 = new ClientDemandConversationDetail();
            a2.setUserMessageId(2L);
            a2.setSupplierId(2L);
            a2.setSupplierName("Eprovement");
            a2.setMessageBody("Chcem chcem chcem!!!");
            a2.setMessageSent(new Date());
            return a2;
        } else if (clientDemandConversationID == 3L) {
            ClientDemandConversationDetail a3 = new ClientDemandConversationDetail();
            a3.setUserMessageId(3L);
            a3.setSupplierId(3L);
            a3.setSupplierName("CoraGeo");
            a3.setMessageBody("To nic lepsie nemate?");
            a3.setMessageSent(new Date());
            return a3;
        } else {
            return new ClientDemandConversationDetail();
        }
    }

    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public ClientDemandDetail getClientOfferedDemand(long clientDemandID) throws RPCException,
            ApplicationSecurityException {
        //TODO staci takto? alebo treba nejak rozlisovat demand a offeredDemand
        return clientDemandConverter.convertToTarget(generalService.find(Demand.class, clientDemandID));
    }

    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public ClientOfferedDemandOffersDetail getClientOfferedDemandOffer(long clientOfferedDemandOfferID) throws
            RPCException, ApplicationSecurityException {
        return new ClientOfferedDemandOffersDetail();
    }

    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public FullOfferDetail getClientAssignedDemand(long assignedDemandID) throws
            RPCException, ApplicationSecurityException {
        if (assignedDemandID == 1L) {
            FullOfferDetail d1 = new FullOfferDetail();
            d1.getOfferDetail().setDemandId(1L);
            d1.getOfferDetail().setState(OfferStateType.ACCEPTED);
            d1.getOfferDetail().setClientName("Martin Slavkovsky");
            d1.getOfferDetail().setSupplierName("Good Data");
            d1.getOfferDetail().setDemandTitle("Poptavka 1234");
            d1.getOfferDetail().setRating(90);
            d1.getOfferDetail().setPrice(10000);
            d1.getOfferDetail().setFinishDate(new Date());
            d1.getOfferDetail().setCreatedDate(new Date());
            MessageDetail md1 = new MessageDetail();
            md1.setUserMessageId(1L);
            d1.setMessageDetail(md1);
            return d1;
        } else if (assignedDemandID == 2L) {
            FullOfferDetail d2 = new FullOfferDetail();
            d2.getOfferDetail().setDemandId(2L);
            d2.getOfferDetail().setState(OfferStateType.ACCEPTED);
            d2.getOfferDetail().setClientName("Ivan Vlcek");
            d2.getOfferDetail().setSupplierName("CoraGeo");
            d2.getOfferDetail().setDemandTitle("Poptavka na GIS");
            d2.getOfferDetail().setRating(90);
            d2.getOfferDetail().setPrice(10000);
            d2.getOfferDetail().setFinishDate(new Date());
            d2.getOfferDetail().setCreatedDate(new Date());
            MessageDetail md2 = new MessageDetail();
            md2.setUserMessageId(2L);
            return d2;
        } else {
            return new FullOfferDetail();
        }
    }
}
