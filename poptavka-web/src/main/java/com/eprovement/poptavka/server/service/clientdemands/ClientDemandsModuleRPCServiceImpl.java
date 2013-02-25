/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.server.service.clientdemands;

import com.eprovement.poptavka.client.service.demand.ClientDemandsModuleRPCService;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.demand.Rating;
import com.eprovement.poptavka.domain.enums.CommonAccessRoles;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.eprovement.poptavka.domain.message.ClientConversation;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.offer.Offer;
import com.eprovement.poptavka.domain.offer.OfferState;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.User;
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
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.ChangeDetail;
import com.eprovement.poptavka.shared.domain.DemandRatingsDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
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
import com.googlecode.genericdao.search.Field;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
    private Converter<Category, CategoryDetail> categoryConverter;
    private Converter<Locality, LocalityDetail> localityConverter;
    private Converter<Demand, FullDemandDetail> demandConverter;
    private Converter<Supplier, FullSupplierDetail> supplierConverter;
    private Converter<Message, MessageDetail> messageConverter;
    private Converter<Search, SearchDefinition> searchConverter;
    private Converter<Demand, ClientDemandDetail> clientDemandConverter;
    private Converter<Message, FullOfferDetail> fullOfferConverter;

    /**************************************************************************/
    /* Autowired methods                                                      */
    /**************************************************************************/
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
    public void setCategoryConverter(
            @Qualifier("categoryConverter") Converter<Category, CategoryDetail> categoryConverter) {
        this.categoryConverter = categoryConverter;
    }

    @Autowired
    public void setLocalityConverter(
            @Qualifier("localityConverter") Converter<Locality, LocalityDetail> localityConverter) {
        this.localityConverter = localityConverter;
    }

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

    /**************************************************************************/
    /* Table getter methods                                                   */
    /**************************************************************************/
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
    public int getClientDemandsCount(long userId,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        final Client client = findClient(userId);
        final Search clientDemandsSearch = searchConverter.convertToSource(searchDefinition);
        clientDemandsSearch.setSearchClass(Demand.class);
        ArrayList<DemandStatus> demandStatuses = new ArrayList<DemandStatus>();
        demandStatuses.add(DemandStatus.ACTIVE);
        demandStatuses.add(DemandStatus.NEW);
        demandStatuses.add(DemandStatus.INVALID);
        demandStatuses.add(DemandStatus.INACTIVE);
        demandStatuses.add(DemandStatus.OFFERED);
        clientDemandsSearch.addFilterIn("status", demandStatuses);
        clientDemandsSearch.addFilterEqual("client", client);
        clientDemandsSearch.addField("id", Field.OP_COUNT);
        clientDemandsSearch.setResultMode(Search.RESULT_SINGLE);
        return ((Long) generalService.searchUnique(clientDemandsSearch)).intValue();
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
        demandStatuses.add(DemandStatus.OFFERED);
        clientDemandsSearch.addFilterIn("status", demandStatuses);
        final List<Demand> clientDemands = Searcher.searchCollection(client.getDemands(), clientDemandsSearch);
        ArrayList<ClientDemandDetail> cdds = clientDemandConverter.convertToTargetList(clientDemands);

        // TODO RELEASE ivlcek, vojto - replace by new SQL that will load demandIds,
        // latest userMessageId and number of total submessages. Vojto is working on this
        Iterator it = messageService.getListOfClientDemandMessagesUnread(generalService.find(
                User.class, userId)).entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry) it.next();
            // key is message and val is number
            Long demandId = (Long) pairs.getKey();
            for (ClientDemandDetail cdd : cdds) {
                if (cdd.getDemandId() == demandId) {
                    cdd.setUnreadSubmessagesCount(((Integer) pairs.getValue()).intValue());
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
    public int getClientDemandConversationsCount(final long userId, final long demandID,
            final SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        final User user = generalService.find(User.class, userId);
        return userMessageService.getClientConversationsWithoutOfferCount(user);
    }

    /**
     * Gets a list of all client's conversation related to their demands where no offer has been made
     *
     * @param userId logged client user Id
     * @param demandID demand with question responses from suppliers
     * @param searchDefinition defines a search criteria for pager etc.
     * @return list of latest user messages from suppliers with total count of submessages
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public List<ClientDemandConversationDetail> getClientDemandConversations(final long userId, final long demandID,
            final SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        final User user = generalService.find(User.class, userId);
        final Message root = messageService.getThreadRootMessage(generalService.find(Demand.class, demandID));
        final Map<UserMessage, ClientConversation> latestSupplierUserMessagesWithUnreadSub =
                userMessageService.getClientConversationsWithoutOffer(user, root);
        final List<ClientDemandConversationDetail> list = new ArrayList<ClientDemandConversationDetail>();

        for (Map.Entry<UserMessage, ClientConversation> userMessageEntry
                : latestSupplierUserMessagesWithUnreadSub.entrySet()) {

            final UserMessage userMessage = userMessageEntry.getKey();

            // TODO RELEASE ivlcek - make detail object converter and create search definition
            final ClientDemandConversationDetail cdcd = new ClientDemandConversationDetail();
            // set UserMessage attributes
            cdcd.setUserMessageId(userMessage.getId());
            cdcd.setIsStarred(userMessage.isStarred());
            cdcd.setIsRead(userMessage.isRead());
            cdcd.setMessageCount(userMessageEntry.getValue().getMessageCount());
            // set Message attributes
            cdcd.setMessageId(userMessage.getMessage().getId());
            cdcd.setThreadRootId(userMessage.getMessage().getThreadRoot().getId());
            cdcd.setMessageBody(userMessage.getMessage().getBody());
            cdcd.setMessageSent(userMessage.getMessage().getSent());
            // set Demand attributes
            cdcd.setDemandId(demandID);
            cdcd.setTitle(userMessage.getMessage().getDemand().getTitle());
            // set Supplier attributes
            // TODO RELEASE ivlcek - if latest user message is from Client then this line doesn't work with client id
            final Supplier supplier = findSupplier(userMessageEntry.getValue().getSupplier().getId());
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
     * As soon as first offer has been submed by some supplier we change the state of this demand to OFFERED.
     *
     * @param userId id of user represented by client. Note that userId and userId are different If userId represents
     * some different user than client, exception will be thrown
     * @param filter
     * @return
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public int getClientOfferedDemandsCount(long userId,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        final Client client = findClient(userId);
        final Search clientDemandsSearch = searchConverter.convertToSource(searchDefinition);
        clientDemandsSearch.setSearchClass(Demand.class);
        clientDemandsSearch.addFilterIn("status", DemandStatus.OFFERED);
        clientDemandsSearch.addFilterEqual("client", client);
        clientDemandsSearch.addField("id", Field.OP_COUNT);
        clientDemandsSearch.setResultMode(Search.RESULT_SINGLE);
        return ((Long) generalService.searchUnique(clientDemandsSearch)).intValue();
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
        final Client client = findClient(userId);
        final Search clientDemandsSearch = searchConverter.convertToSource(searchDefinition);
        clientDemandsSearch.setSearchClass(Demand.class);
        clientDemandsSearch.addFilterIn("status", DemandStatus.OFFERED);
        clientDemandsSearch.addFilterEqual("client", client);
        List<Demand> clientDemands = generalService.search(clientDemandsSearch);

        ArrayList<ClientDemandDetail> cdds = clientDemandConverter.convertToTargetList(clientDemands);

        // load a map of unread messages for all client demands with offer, then iterate this map and
        // match with clientDemands so that unread submessages count will be set
        for (Map.Entry<Long, Integer> userMessageEntry : messageService.getListOfClientDemandMessagesWithOfferUnreadSub(
                generalService.find(User.class, userId)).entrySet()) {
            userMessageEntry.getKey();
            userMessageEntry.getValue();
            // key is demandId and valuse is number of unread submessages
            for (ClientDemandDetail cdd : cdds) {
                if (cdd.getDemandId() == userMessageEntry.getKey()) {
                    cdd.setUnreadSubmessagesCount(((Integer) userMessageEntry.getValue()).intValue());
                    break;
                }
            }
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
    public int getClientOfferedDemandOffersCount(final long userId, final long demandID,
            final SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        // TODO RELEASE ivlcek - incorporate searchDefinition for this method and refactor with new sql
        final User user = generalService.find(User.class, userId);
        final Demand demand = generalService.find(Demand.class, demandID);
        return userMessageService.getClientConversationsWithOfferCount(user, demand);
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
        OfferState offerPending = offerService.getOfferState(OfferStateType.PENDING.getValue());

        List<ClientOfferedDemandOffersDetail> listCodod = new ArrayList<ClientOfferedDemandOffersDetail>();

        Map<Long, Integer> latestSupplierUserMessagesWithUnreadSub =
                messageService.getLatestSupplierUserMessagesWithOfferForDemand(user, root, offerPending);
        for (Map.Entry<Long, Integer> userMessageEntry : latestSupplierUserMessagesWithUnreadSub.entrySet()) {
            UserMessage userMessage = (UserMessage) generalService.find(UserMessage.class, userMessageEntry.getKey());
            Offer offer = userMessage.getMessage().getOffer();
            // TODO ivlcek - refactor and create converter
            ClientOfferedDemandOffersDetail codod = new ClientOfferedDemandOffersDetail();
            // set UserMessage attributes
            codod.setIsRead(userMessage.isRead());
            codod.setIsStarred(userMessage.isStarred());
            codod.setMessageCount(userMessageEntry.getValue());
            codod.setThreadRootId(userMessage.getMessage().getThreadRoot().getId());
            codod.setUserMessageId(userMessage.getId());
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
            // set demand attributes
            codod.setTitle(offer.getDemand().getTitle());

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
    public int getClientAssignedDemandsCount(long userId,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        final Client client = findClient(userId);
        final Search clientDemandsSearch = searchConverter.convertToSource(searchDefinition);
        clientDemandsSearch.setSearchClass(Demand.class);
        clientDemandsSearch.addFilterIn("status", DemandStatus.ASSIGNED,
                DemandStatus.PENDINGCOMPLETION);
        clientDemandsSearch.addFilterEqual("client", client);
        clientDemandsSearch.addField("id", Field.OP_COUNT);
        clientDemandsSearch.setResultMode(Search.RESULT_SINGLE);
        return ((Long) generalService.searchUnique(clientDemandsSearch)).intValue();
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
    public List<ClientOfferedDemandOffersDetail> getClientAssignedDemands(long userId,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        User user = generalService.find(User.class, userId);
        OfferState offerAccepted = offerService.getOfferState(OfferStateType.ACCEPTED.getValue());
        OfferState offerCompleted = offerService.getOfferState(OfferStateType.COMPLETED.getValue());

        List<ClientOfferedDemandOffersDetail> listCodod = new ArrayList<ClientOfferedDemandOffersDetail>();

        Map<UserMessage, Integer> latestSupplierUserMessagesWithUnreadSub =
                userMessageService.getSupplierConversationsWithAcceptedOffer(user, offerAccepted, offerCompleted);
        for (Map.Entry<UserMessage, Integer> userMessageEntry : latestSupplierUserMessagesWithUnreadSub.entrySet()) {
            UserMessage userMessage = userMessageEntry.getKey();
            Offer offer = userMessage.getMessage().getOffer();
            // TODO RELEASE ivlcek - create converter
            ClientOfferedDemandOffersDetail codod = new ClientOfferedDemandOffersDetail();
            // set UserMessage attributes
            codod.setIsRead(userMessage.isRead());
            codod.setIsStarred(userMessage.isStarred());
            codod.setUserMessageId(userMessage.getId());
            codod.setMessageCount(userMessageEntry.getValue());
            codod.setThreadRootId(userMessage.getMessage().getThreadRoot().getId());
            // set Supplier attributes
            codod.setSupplierId(offer.getSupplier().getId());
            codod.setSupplierName(offer.getSupplier().getBusinessUser().getBusinessUserData().getDisplayName());
            codod.setRating(offer.getSupplier().getOveralRating());
            codod.setSupplierUserId(offer.getSupplier().getBusinessUser().getId());
            // set Offer attributes
            codod.setOfferId(offer.getId());
            codod.setPrice(offer.getPrice().toPlainString());
            codod.setDeliveryDate(offer.getFinishDate());
            codod.setReceivedDate(offer.getCreated());
            // set Demand attributes
            codod.setDemandId(offer.getDemand().getId());
            codod.setTitle(offer.getDemand().getTitle());

            listCodod.add(codod);
        }
        return listCodod;
    }

    //******************** CLIENT - My Closed Demands *************************/
    /**
     * Get all closed demands.
     * When client accept supplier's work, demand is closed and stored to demand's history.
     *
     * @param userId user's id
     * @param filter
     * @return count of closed demands
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public int getClientClosedDemandsCount(long userId,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        final Client client = findClient(userId);
        final Search clientDemandsSearch = searchConverter.convertToSource(searchDefinition);
        clientDemandsSearch.setSearchClass(Demand.class);
        clientDemandsSearch.addFilterEqual("status", DemandStatus.CLOSED);
        clientDemandsSearch.addFilterEqual("client", client);
        clientDemandsSearch.addField("id",  Field.OP_COUNT);
        clientDemandsSearch.setResultMode(Search.RESULT_SINGLE);
        return ((Long) generalService.searchUnique(clientDemandsSearch)).intValue();
    }

    /**
     * Get all closed demands.
     * When client accept supplier's work, demand is closed and stored to demand's history.
     *
     * @param userId user's id
     * @param searchDefinition
     * @return
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public List<ClientOfferedDemandOffersDetail> getClientClosedDemands(long userId,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        User user = generalService.find(User.class, userId);

        List<ClientOfferedDemandOffersDetail> listCodod = new ArrayList<ClientOfferedDemandOffersDetail>();

        Map<UserMessage, Integer> latestSupplierUserMessagesWithUnreadSub =
                userMessageService.getSupplierConversationsWithClosedDemands(user);
        for (Map.Entry<UserMessage, Integer> userMessageEntry : latestSupplierUserMessagesWithUnreadSub.entrySet()) {
            UserMessage userMessage = userMessageEntry.getKey();
            Offer offer = userMessage.getMessage().getOffer();
            // TODO RELEASE ivlcek - create converter
            ClientOfferedDemandOffersDetail codod = new ClientOfferedDemandOffersDetail();
            // set UserMessage attributes
            codod.setIsRead(userMessage.isRead());
            codod.setIsStarred(userMessage.isStarred());
            codod.setUserMessageId(userMessage.getId());
            codod.setMessageCount(userMessageEntry.getValue());
            codod.setThreadRootId(userMessage.getMessage().getThreadRoot().getId());
            // set Supplier attributes
            codod.setSupplierId(offer.getSupplier().getId());
            codod.setSupplierName(offer.getSupplier().getBusinessUser().getBusinessUserData().getDisplayName());
            codod.setRating(offer.getSupplier().getOveralRating());
            codod.setSupplierUserId(offer.getSupplier().getBusinessUser().getId());
            // set Offer attributes
            codod.setOfferId(offer.getId());
            codod.setPrice(offer.getPrice().toPlainString());
            codod.setDeliveryDate(offer.getFinishDate());
            codod.setReceivedDate(offer.getCreated());
            // set Demand attributes
            codod.setDemandId(offer.getDemand().getId());
            codod.setTitle(offer.getDemand().getTitle());

            listCodod.add(codod);
        }
        return listCodod;
    }

    //******************** CLIENT - My Ratings ********************************/
    /**
     * Get ratings of my closed demands.
     *
     * @param userId user's id
     * @param filter
     * @return
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public int getClientRatingsCount(long userId,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        return 0;
    }

    /**
     * Get ratings of my all closed demands.
     *
     * @param userId user's id
     * @param searchDefinition
     * @return
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public List<DemandRatingsDetail> getClientRatings(long userId,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        return new ArrayList<DemandRatingsDetail>();
    }

    /**************************************************************************/
    /* Other getter methods                                                   */
    /**************************************************************************/
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

    /**************************************************************************/
    /* Setter methods                                                         */
    /**************************************************************************/
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
     * Accept selected offer and decline other offers and change demand state to ASSIGNED.
     *
     * @param offerId to be accepted
     * @param latestUserMessageId for which a new automatic reply message will be sent
     * @return void
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public void acceptOffer(final long offerId, final long latestUserMessageId, String acceptOfferMesage)
        throws RPCException, ApplicationSecurityException {
        Offer offer = (Offer) this.generalService.find(Offer.class, offerId);

        // set offer as accepted
        offer.setState((OfferState) offerService.getOfferState(
                OfferStateType.ACCEPTED.getValue()));
        generalService.save(offer);

        // load other offers and set them as declined
        Demand demand = offer.getDemand();
        for (Offer declinedOffer : demand.getOffers()) {
            declinedOffer.setState(offerService.getOfferState(OfferStateType.ACCEPTED.getValue()));
            generalService.save(declinedOffer);
        }
        demand.setStatus(DemandStatus.ASSIGNED);
        generalService.save(demand);

        messageService.sendGeneratedMessage(
                latestUserMessageId, demand.getClient().getBusinessUser(), acceptOfferMesage);
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
        Message m = messageService.newReply(this.messageService.getById(
                messageDetailImpl.getParentId()),
                this.generalService.find(User.class, messageDetailImpl.getSenderId()));
        m.setBody(messageDetailImpl.getBody());
        m.setSubject(messageDetailImpl.getSubject());
        // TODO set the id correctly, check it
        MessageDetail messageDetailFromDB = messageConverter.convertToTarget(this.messageService.create(m));
        return messageDetailFromDB;
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

    /**************************************************************************/
    /* Get Detail object for selecting in selection models                    */
    /**************************************************************************/
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public ClientDemandDetail getClientDemand(long clientDemandID) throws RPCException, ApplicationSecurityException {
        return clientDemandConverter.convertToTarget(generalService.find(Demand.class, clientDemandID));
    }

    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public ClientDemandConversationDetail getClientDemandConversation(long clientDemandConversationID) throws
            RPCException, ApplicationSecurityException {
        //TODO Martin - finnish
        //- Neviem ako a z coho to zickat este, mozno nakoniec to nemusi byt ani ClientDemandConversationDetail();
        return new ClientDemandConversationDetail();
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
        //TODO LATER - if history in Client Demands will be supported again
        return new ClientOfferedDemandOffersDetail();
    }

    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public ClientOfferedDemandOffersDetail getClientAssignedDemand(long assignedDemandID) throws
            RPCException, ApplicationSecurityException {
        //TODO LATER - if history in Client Demands will be supported again
        return new ClientOfferedDemandOffersDetail();
    }

    /**************************************************************************/
    /* CRUD operation of demand                                               */
    /**************************************************************************/
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public Boolean updateDemand(long demandId, ArrayList<ChangeDetail> changes) throws
            RPCException, ApplicationSecurityException {
        Demand demand = generalService.find(Demand.class, demandId);
        updateDemandFields(demand, changes);
        generalService.save(demand);

        return true;
    }

    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public Boolean deleteDemand(long demandId) throws RPCException, ApplicationSecurityException {
        Demand demand = generalService.find(Demand.class, demandId);
        //TODO RELEASE - remove object or set status to removed ???
        //If set status - New status DELETED is needed
        //demand.setStatus(DemandStatus.DELETED);
        //if delete object - removal cases foregin key constrain violation
        //both generalService and demandService
        //return generalService.remove(demand);
        //return demandService.remove(demand);
        return true;
    }

    private Demand updateDemandFields(Demand demand, ArrayList<ChangeDetail> changes) {
        for (ChangeDetail change : changes) {
            switch ((FullDemandDetail.DemandField) change.getField()) {
                case TITLE:
                    demand.setTitle((String) change.getValue());
                    break;
                case DESCRIPTION:
                    demand.setDescription((String) change.getValue());
                    break;
                case PRICE:
                    demand.setPrice((BigDecimal) change.getValue());
                    break;
                case END_DATE:
                    demand.setEndDate((Date) change.getValue());
                    break;
                case VALID_TO_DATE:
                    demand.setValidTo((Date) change.getValue());
                    break;
                case MAX_OFFERS:
                    demand.setMaxSuppliers((Integer) change.getValue());
                    break;
                case MIN_RATING:
                    demand.setMinRating(Integer.parseInt((String) change.getValue()));
                    break;
                case DEMAND_STATUS:
                    demand.setStatus(DemandStatus.valueOf((String) change.getValue()));
                    break;
                case CREATED:
                    demand.setCreatedDate((Date) change.getValue());
                    break;
                case CATEGORIES:
                    demand.setCategories(categoryConverter.convertToSourceList(
                            (ArrayList<CategoryDetail>) change.getValue()));
                    break;
                case LOCALITIES:
                    demand.setLocalities(localityConverter.convertToSourceList(
                            (ArrayList<LocalityDetail>) change.getValue()));
                    break;
                case EXCLUDE_SUPPLIER:
                    demand.setExcludedSuppliers(supplierConverter.convertToSourceList(
                            (ArrayList<FullSupplierDetail>) change.getValue()));
                    break;
                default:
                    break;
            }
        }
        return demand;
    }

    @Override
    /**
     * Client enters a new feedback for Supplier with respect to given demand.
     *
     * @param demandID of Demand to which this feedback is connected
     * @param supplierRating integer number that will be assigned to supplier
     * @param supplierMessage comment that will be assigned to supplier
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public void closeDemandAndEnterFeedbackForSupplier(final long demandID, final Integer supplierRating,
        final String supplierMessage)
        throws RPCException, ApplicationSecurityException {
        final Demand demand = generalService.find(Demand.class, demandID);
        //close demand by updating its status to closed
        demand.setStatus(DemandStatus.CLOSED);
        //set demand's rating for supplier
        Rating rating;
        if (demand.getRating() == null) {
            rating = new Rating();
        } else {
            rating = demand.getRating();
        }
        rating.setSupplierRating(supplierRating);
        rating.setSupplierMessage(supplierMessage);
        generalService.save(rating);
        demand.setRating(rating);
        generalService.save(demand);
    }
}
