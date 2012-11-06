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
import com.eprovement.poptavka.domain.offer.OfferState;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.exception.MessageException;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.security.PoptavkaUserAuthentication;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.server.util.SearchUtils;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.message.MessageService;
import com.eprovement.poptavka.service.offer.OfferService;
import com.eprovement.poptavka.service.user.ClientService;
import com.eprovement.poptavka.service.user.UserSearchCriteria;
import com.eprovement.poptavka.service.usermessage.UserMessageService;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandConversationDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.domain.offer.FullOfferDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.util.search.Searcher;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
    private GeneralService generalService;
    private OfferService offerService;
    private UserMessageService userMessageService;
    private MessageService messageService;
    //Converters
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

    /**************************************************************************/
    /* Table getter methods                                                   */
    /**************************************************************************/
    //************************* CLIENT - My Demands ***************************/
    /**
     * Get all demand's count that has been created by client.
     * When new demand is created by client, will be involved here.
     * As Client: "All demands created by me."
     *
     * @param userId id of user represented by client. Note that userId and userId are different
     *               If userId represents some different user than client, exception will be thrown
     * @param filter - define searching criteria if any
     * @return count
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public long getClientDemandsCount(long userId,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        //TODO Martin - implement when implemented on backend
        return 1L;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     *     PERFORMANCE: This implementation can be slow if client has large amount of demands because it loads
     *     all his demands from DB into the memory and perform filtering afterwards.
     *     It this is the issue then consider reimplementation of this method.
     * </p>
     *
     * @param userId id of user represented by client. Note that userId and userId are different
     *               If userId represents some different user than client, exception will be thrown
     * @return list of demand's detail objects
     * @throws ApplicationSecurityException if user is not authorized to call this method
     * @throws IllegalArgumentException if given user does not exist or it represents user other than client
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public List<ClientDemandDetail> getClientDemands(long userId, SearchDefinition searchDefinition)
        throws RPCException, ApplicationSecurityException, IllegalArgumentException {
        final Client client = findClient(userId);
        final Search clientDemandsSearch = searchConverter.convertToSource(searchDefinition);
        clientDemandsSearch.setSearchClass(Demand.class);
        clientDemandsSearch.addFilterEqual("status", DemandStatus.NEW);
        final List<Demand> clientDemands = Searcher.searchCollection(client.getDemands(), clientDemandsSearch);
        return clientDemandConverter.convertToTargetList(clientDemands);
    }

    /**
     * When supplier asks something about a demand of some client.
     * The conversation has more messages of course but I want count of threads.
     * As Client: "Questions made by suppliers to demands made by me." "How many suppliers
     * are asing something about a certain demand."
     *
     * @param userId id of user represented by client. Note that userId and userId are different
     *               If userId represents some different user than client, exception will be thrown
     * @param demandID - demand's ID
     * @param filter - define searching criteria if any
     * @return count
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public long getClientDemandConversationsCount(long userId, long demandID,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        //TODO Martin - implement when implemented on backend
        return 1L;
    }

    /**
     * When supplier asks something about a demand of some client.
     * The conversation has more messages of course but I want count of threads. As
     * Client: "Questions made by suppliers to demands made by me."
     * "How many suppliers are asing something about a certain demand."
     *
     * @param userId id of user represented by client. Note that userId and userId are different
     *               If userId represents some different user than client, exception will be thrown
     * @param demandID - demand's
     * @param start
     * @param maxResult
     * @param filter
     * @param orderColumns
     * @return
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public List<ClientDemandConversationDetail> getClientDemandConversations(long userId, long demandID,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        //TODO Martin - implement when implemented on backend
        ClientDemandConversationDetail a = new ClientDemandConversationDetail();
        a.setRead(false);
        a.setUserMessageId(1L);
        a.setSupplierName("Good Data");
        MessageDetail md = new MessageDetail();
        md.setBody("Tak ak date cenu o 10% dole ta to beriem.");
        a.setMessageDetail(md);
        a.setDate(new Date());
        List<ClientDemandConversationDetail> list = new ArrayList<ClientDemandConversationDetail>();
        list.add(a);
        return list;
    }

    //************************* CLIENT - My Offers ****************************/
    /**
     * Get all demands where have been placed an offer by some supplier.
     * When supplier place an offer to client's demand, the demand will be involved here.
     * As Client: "Demands that have already an offer."
     *
     * @param userId id of user represented by client. Note that userId and userId are different
     *               If userId represents some different user than client, exception will be thrown
     * @param filter
     * @return
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public long getClientOfferedDemandsCount(long userId,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        return 1L;
    }

    /**
     * Get all demands where have been placed an offer by some supplier.
     * When supplier place an offer to client's demand, the demand will be involved here.
     * As Client: "Demands that have already an offer."
     *
     * @param userId id of user represented by client. Note that userId and userId are different
     *               If userId represents some different user than client, exception will be thrown
     * @param demandID - demands's ID
     * @param start
     * @param maxResult
     * @param filter
     * @param orderColumns
     * @return
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public List<ClientDemandDetail> getClientOfferedDemands(long userId, long demandID,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        List<ClientDemandDetail> list = new ArrayList<ClientDemandDetail>();
        ClientDemandDetail c = new ClientDemandDetail();
        c.setDemandId(1L);
        c.setDemandTitle("demand title");
        c.setPrice(BigDecimal.valueOf(10200));
        c.setEndDate(new Date());
        c.setValidToDate(new Date());
        list.add(c);
        return list;
    }

    /**
     * Get all offers of given demand.
     * When supplier place an offer to client's demand, the offer will be involved here.
     * As Client: "How many suppliers placed an offers to a certain demand."
     *
     * @param userId id of user represented by client. Note that userId and userId are different
     *               If userId represents some different user than client, exception will be thrown
     * @return offers count of given demand
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public long getClientOfferedDemandOffersCount(long userId, long demandID,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        Search backendSearch = getSearchForgetClientOfferedDemandOffers(searchDefinition, userId, demandID);

        return generalService.count(backendSearch);
    }

    /**
     * Get all offers of given demand.
     * When supplier place an offer to client's demand, the offer will be involved here.
     * As Client: "How many suppliers placed an offers to a certain demand."
     *
     * @param userId id of user represented by client. Note that userId and userId are different
     *               If userId represents some different user than client, exception will be thrown
     * @param demandID
     * @param searchDefinition search filter, ordering, ...
     * @return
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public List<FullOfferDetail> getClientOfferedDemandOffers(long userId, long demandID,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        Search backendSearch = getSearchForgetClientOfferedDemandOffers(searchDefinition, userId, demandID);
        List<Message> messages = generalService.search(backendSearch);
        System.out.println(messages);
        return fullOfferConverter.convertToTargetList(messages);
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
     * Get all offers that were accepted by client to solve a demand.
     * When client accept an offer, will be involved here.
     * As Client: "All offers that were accepted by me to solve my demand."
     *
     * @param userId id of user represented by client. Note that userId and userId are different
     *               If userId represents some different user than client, exception will be thrown
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
     * Get all offers that were accepted by client to solve a demand.
     * When client accept an offer, will be involved here.
     * As Client: "All offers that were accepted by me to solve my demand."
     *
     * @param userId id of user represented by client. Note that userId and userId are different
     *               If userId represents some different user than client, exception will be thrown
     * @param searchDefinition search filters, ordering
     * @return
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public List<FullOfferDetail> getClientAssignedDemands(long userId,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        //TODO Martin - implement when implemented on backend
        FullOfferDetail detail = new FullOfferDetail();
        detail.getOfferDetail().setDemandId(1L);
        detail.getOfferDetail().setState(OfferStateType.ACCEPTED);
        detail.getOfferDetail().setClientName("Martin Slavkovsky");
        detail.getOfferDetail().setSupplierName("Good Data");
        detail.getOfferDetail().setDemandTitle("Poptavka 1234");
        detail.getOfferDetail().setRating(90);
        detail.getOfferDetail().setPrice(10000);
        detail.getOfferDetail().setFinishDate(new Date());
        detail.getOfferDetail().setCreatedDate(new Date());
        List<FullOfferDetail> list = new ArrayList<FullOfferDetail>();
        list.add(detail);
        return list;
    }

    /**************************************************************************/
    /* Other getter methods                                                         */
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
     * COMMON.
     * Change 'read' status of sent messages to chosen value
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
     * COMMON.
     * Change 'star' status of sent messages to chosen value
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
     * When demand is finished (when supplier delivered what client asked), client can finally close demand.
     * At the end of whole process.
     *
     * @param demandDetail
     * @throws RPCException
     * @throws ApplicationSecurityException W
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public void closeDemand(FullDemandDetail demandDetail) throws RPCException, ApplicationSecurityException {
        //TODO Juraj - skontrolovat
        Demand demand = demandConverter.convertToSource(demandDetail);
        demand.setStatus(DemandStatus.CLOSED);
        generalService.merge(demand);
        //Asi nech zmizne aj zo SupplierAssignedDemands
    }

    /**
     * Accept offer. When some offer is accepted, others are automatically declined.
     *
     * @param fullOfferDetail
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public void acceptOffer(FullOfferDetail fullOfferDetail) throws RPCException, ApplicationSecurityException {
        //TODO Juraj
        //Accept given offer (offerDetail.getOfferDetail())
        //Decline all other offers of given demand (offerDetail.getDemandDetail())
    }

    /**
     * Decline offer. When client is not satisfied with and offer, he can decline it. It doesn't influence
     * other offers of that demand.
     *
     * @param offerDetail
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public void declineOffer(OfferDetail offerDetail) throws RPCException, ApplicationSecurityException {
        //TODO Juraj
        //Decline given offer
    }

    /**************************************************************************/
    /* Messages methods                                                       */
    /**************************************************************************/
    /**
     * Message sent by supplier about a query to potential demand.
     * @param messageDetailImpl
     * @return message
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public MessageDetail sendQueryToPotentialDemand(MessageDetail messageDetailImpl) throws RPCException,
            ApplicationSecurityException {
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
            Logger.getLogger(ClientDemandsModuleRPCServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public OfferDetail changeOfferState(OfferDetail offerDetail) throws RPCException, ApplicationSecurityException {
        Offer offer = this.generalService.find(Offer.class, offerDetail.getId());

        OfferState offerState = offerService.getOfferState(offerDetail.getState().getValue());
        offer.setState(offerState);
        offer = (Offer) this.generalService.save(offer);
        offerDetail.setState(offer.getState().getType());
        return offerDetail;
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

    /**
     * This method will update number of unread messages of logged user.
     * Since this RPC class requires access of authenticated user (see security-web.xml) this method will be called
     * only when PoptavkaUserAuthentication object exist in SecurityContextHolder and we can retrieve userId.
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
    public ClientDemandDetail getClientDemand(long clientDemandID) {
        return clientDemandConverter.convertToTarget(generalService.find(Demand.class, clientDemandID));
    }

    @Override
    public ClientDemandConversationDetail getClientDemandConversation(long clientDemandConversationID) {
        //Neviem ako a z coho to zickat este, mozno nakoniec to nemusi byt ani ClientDemandConversationDetail();
        return new ClientDemandConversationDetail();
    }

    @Override
    public ClientDemandDetail getClientOfferedDemand(long clientDemandID) {
        //staci takto? alebo treba nejak rozlisovat demand a offeredDemand
        return clientDemandConverter.convertToTarget(generalService.find(Demand.class, clientDemandID));
    }

    @Override
    public FullOfferDetail getClientOfferedDemandOffer(long clientOfferedDemandOfferID) {
        return new FullOfferDetail();
    }

    @Override
    public FullOfferDetail getClientAssignedDemand(long offerID) {
        return fullOfferConverter.convertToTarget(generalService.find(Message.class, offerID));
    }
}
