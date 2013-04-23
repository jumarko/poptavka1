/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.server.service.supplierdemands;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.service.demand.SupplierDemandsModuleRPCService;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.demand.Rating;
import com.eprovement.poptavka.domain.enums.CommonAccessRoles;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.offer.Offer;
import com.eprovement.poptavka.domain.offer.OfferState;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.security.PoptavkaUserAuthentication;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.message.MessageService;
import com.eprovement.poptavka.service.offer.OfferService;
import com.eprovement.poptavka.service.usermessage.UserMessageService;
import com.eprovement.poptavka.shared.domain.DemandRatingsDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.PotentialDemandMessage;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.domain.offer.SupplierOffersDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.domain.supplierdemands.SupplierDashboardDetail;
import com.eprovement.poptavka.shared.domain.supplierdemands.SupplierPotentialDemandDetail;
import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SortPair;
import com.eprovement.poptavka.util.search.Searcher;
import com.googlecode.genericdao.search.Field;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.Sort;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Martin Slavkovsky
 */
@Configurable
public class SupplierDemandsModuleRPCServiceImpl extends AutoinjectingRemoteService
        implements SupplierDemandsModuleRPCService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SupplierDemandsModuleRPCServiceImpl.class);
    //Services
    private GeneralService generalService;
    private UserMessageService userMessageService;
    private MessageService messageService;
    private OfferService offerService;
    //Converters
    private Converter<Demand, FullDemandDetail> demandConverter;
    private Converter<Supplier, FullSupplierDetail> supplierConverter;
    private Converter<Message, MessageDetail> messageConverter;
    private Converter<UserMessage, PotentialDemandMessage> potentialDemandMessageConverter;
    private Converter<Search, SearchDefinition> searchConverter;
    private Converter<Sort, SortPair> sortConverter;

    /**************************************************************************/
    /* Autowired methods                                                      */
    /**************************************************************************/
    //Services
    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    @Autowired
    public void setUserMessageService(UserMessageService userMessageService) {
        this.userMessageService = userMessageService;
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setOfferService(OfferService offerService) {
        this.offerService = offerService;
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
    public void setPotentialDemandMessageConverter(
            @Qualifier("potentialDemandMessageConverter") Converter<UserMessage, PotentialDemandMessage>
                    potentialDemandMessageConverter) {
        this.potentialDemandMessageConverter = potentialDemandMessageConverter;
    }

    @Autowired
    public void setSearchConverter(
            @Qualifier("searchConverter") Converter<Search, SearchDefinition> searchConverter) {
        this.searchConverter = searchConverter;
    }

    @Autowired
    public void setSortConverter(
            @Qualifier("sortConverter") Converter<Sort, SortPair> sortConverter) {
        this.sortConverter = sortConverter;
    }

    //************************ SUPPLIER - My Demands **************************/
    /**
     * Get demands of categories that I am interested in.
     * When a demand is created it is assigned to certain categories.
     * Than the system sends demands to suppliers what have registered those categories and are
     * interested in receiving this kind of demand.
     * As Supplier: "All potential demands that I am interested in."
     *
     * @param supplierID
     * @param filter
     * @return
     */
    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public int getSupplierPotentialDemandsCount(long userId,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        //TODO RELEASE vojto - implement SearchDefinition
        final BusinessUser businessUser = generalService.find(BusinessUser.class, userId);
        final Search potentialDemandsCountSearch = searchConverter.convertToSource(searchDefinition);
        potentialDemandsCountSearch.setSearchClass(Demand.class);
        return (int) userMessageService.getPotentialDemandsCount(businessUser, potentialDemandsCountSearch);
    }

    /**
     * Get demands of categories that I am interested in.
     * When a demand is created it is assigned to certain categories. Than the system sends
     * demands to suppliers what have registered those categories and are
     * interested in receiving this kind of demand.
     * As Supplier: "All potential demands that I am interested in."
     *
     * @param supplierID
     * @param start
     * @param maxResult
     * @param filter
     * @param orderColumns
     * @return
     */
    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public List<SupplierPotentialDemandDetail> getSupplierPotentialDemands(long userId, long supplierId,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        final User user = (User) generalService.find(User.class, userId);
        final Map<UserMessage, Integer> latestUserMessagesWithCount =
                userMessageService.getSupplierConversationsWithoutOffer(user);

        ArrayList<SupplierPotentialDemandDetail> supplierPotentialDemands =
                new ArrayList<SupplierPotentialDemandDetail>();

        List<UserMessage> sortedList = setSort(
                new Search(UserMessage.class), "message.demand.",
                searchDefinition, latestUserMessagesWithCount.keySet());

        // TODO LATER ivlcek - refactor with detail converter
        for (UserMessage um : sortedList) {
            SupplierPotentialDemandDetail detail = new SupplierPotentialDemandDetail();
            // Client part
            detail.setClientId(um.getMessage().getDemand().getClient().getId());
            detail.setSenderId(um.getMessage().getThreadRoot().getSender().getId());
            detail.setDisplayName(
                    um.getMessage().getDemand().getClient().getBusinessUser().getBusinessUserData().getDisplayName());
            detail.setRating(um.getMessage().getDemand().getClient().getOveralRating());
            // Supplier part
            detail.setSupplierId(supplierId);
            // Message part
            detail.setMessageId(um.getMessage().getId());
            detail.setThreadRootId(um.getMessage().getThreadRoot().getId());
            detail.setMessageSent(um.getMessage().getSent());
            // UserMessage part
            detail.setUserMessageId(um.getId());
            detail.setIsStarred(um.isStarred());
            detail.setMessageCount(latestUserMessagesWithCount.get(um));
            detail.setIsRead(um.isRead());
            // Demand part
            detail.setDemandId(um.getMessage().getDemand().getId());
            detail.setValidTo(um.getMessage().getDemand().getValidTo());
            detail.setEndDate(um.getMessage().getDemand().getEndDate());
            detail.setTitle(um.getMessage().getDemand().getTitle());
            detail.setPrice(um.getMessage().getDemand().getPrice());

            supplierPotentialDemands.add(detail);
        }
        return supplierPotentialDemands;
    }

    //************************ SUPPLIER - My Offers ***************************/
    /**
     * Gets count of supplier's offers in state PENDING.
     * @param supplierId
     * @return count of PENDING offers
     */
    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public int getSupplierOffersCount(long supplierID,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        //TODO RELEASE vojto- implement SearchDefinition
        return offerService.getPendingOffersCountForSupplier(supplierID).intValue();
    }

    /**
     * Get offers sent by supplier.
     * When supplier sends an offer, it will be involved here.
     * As Supplier: "Offers I sent"
     *
     * @param supplierID
     * @param start
     * @param maxResult
     * @param filter
     * @param orderColumns
     * @return
     */
    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public List<SupplierOffersDetail> getSupplierOffers(long supplierID, long userId,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        List<SupplierOffersDetail> listSod = new ArrayList<SupplierOffersDetail>();
        OfferState pendingState = offerService.getOfferState(OfferStateType.PENDING.getValue());

        final User user = (User) generalService.find(User.class, userId);
        final Map<UserMessage, Integer> latestUserMessagesWithCount =
                userMessageService.getSupplierConversationsWithOffer(user, pendingState);

        List<UserMessage> sortedList = setSort(
                new Search(UserMessage.class), "message.demand.",
                searchDefinition, latestUserMessagesWithCount.keySet());

        // TODO LATER ivlcek - refactor with detail converter
        for (UserMessage latestUserMessage : sortedList) {
            Offer offer = latestUserMessage.getMessage().getOffer();
            SupplierOffersDetail sod = new SupplierOffersDetail();

            // TODO LATER ivlcek - refactor and create converter
            // supplier part
            sod.setSupplierId(supplierID);
            sod.setRating(offer.getDemand().getClient().getOveralRating());
            // client part
            // Client name can be displayed because it contrains only contact person name
            sod.setDisplayName(offer.getDemand().getClient().getBusinessUser().getBusinessUserData().getDisplayName());
            sod.setClientId(offer.getDemand().getClient().getId());
            sod.setSenderId(latestUserMessage.getMessage().getThreadRoot().getSender().getId());
            // demand part
            sod.setDemandId(offer.getDemand().getId());
            sod.setTitle(offer.getDemand().getTitle());
            // offer part
            sod.setPrice(offer.getPrice());
            sod.setDeliveryDate(offer.getFinishDate());
            sod.setOfferId(offer.getId());
            // Message part
            sod.setReceivedDate(offer.getCreated());
            sod.setThreadRootId(latestUserMessage.getMessage().getThreadRoot().getId());
            // set UserMessage attributes
            sod.setUserMessageId(latestUserMessage.getId());
            sod.setMessageCount(latestUserMessagesWithCount.get(latestUserMessage));
            sod.setIsRead(latestUserMessage.isRead());
            sod.setIsStarred(latestUserMessage.isStarred());

            listSod.add(sod);
        }
        return listSod;
    }

    //******************* SUPPLIER - My Assigned Demands **********************/
    /**
     * Gets count of supplier's offers in state ACCEPTED.
     * @param supplierId
     * @return count of ACCEPTED offers
     */
    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public int getSupplierAssignedDemandsCount(long supplierID,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        //TODO RELEASE vojto- implement SearchDefinition
        return offerService.getAcceptedOffersCountForSupplier(supplierID).intValue();
    }

    /**
     * Get supplier's offers that have been accepted.
     * When client accept an supplier's offer, the offer will be implemented here.
     * As Supplier: "Offers that I 'won'."
     *
     * @param supplierID
     * @param start
     * @param maxResult
     * @param filter
     * @param orderColumns
     * @return
     */
    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public List<SupplierOffersDetail> getSupplierAssignedDemands(long supplierID,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        Supplier supplier = generalService.find(Supplier.class, supplierID);

        OfferState offerAccepted = offerService.getOfferState(OfferStateType.ACCEPTED.getValue());
        OfferState offerCompleted = offerService.getOfferState(OfferStateType.COMPLETED.getValue());
        Map<UserMessage, Integer> latestUserMessages =
                userMessageService.getSupplierConversationsWithAcceptedOffer(
                supplier.getBusinessUser(), offerAccepted, offerCompleted);

        List<UserMessage> sortedList = setSort(
                new Search(UserMessage.class), "message.demand.", searchDefinition, latestUserMessages.keySet());
        List<SupplierOffersDetail> listSod = new ArrayList<SupplierOffersDetail>();

        for (UserMessage latestUserMessage : sortedList) {
            Offer offer = latestUserMessage.getMessage().getOffer();
            SupplierOffersDetail sod = new SupplierOffersDetail();

            // TODO LATER ivlcek - refactor and create converter
            // supplier part
            sod.setSupplierId(supplierID);
            sod.setRating(offer.getDemand().getClient().getOveralRating());
            // client part
            sod.setDisplayName(offer.getDemand().getClient().getBusinessUser().getBusinessUserData().getDisplayName());
            sod.setClientId(offer.getDemand().getClient().getId());
            sod.setSenderId(latestUserMessage.getMessage().getThreadRoot().getSender().getId());
            // demand part
            sod.setDemandId(offer.getDemand().getId());
            sod.setTitle(offer.getDemand().getTitle());
            // offer part
            sod.setPrice(offer.getPrice());
            sod.setDeliveryDate(offer.getFinishDate());
            sod.setOfferId(offer.getId());
            // Message part
            sod.setReceivedDate(offer.getCreated());
            sod.setThreadRootId(latestUserMessage.getMessage().getThreadRoot().getId());
            // set UserMessage attributes
            sod.setUserMessageId(latestUserMessage.getId());
            sod.setMessageCount(latestUserMessages.get(latestUserMessage));
            sod.setIsRead(latestUserMessage.isRead());
            sod.setIsStarred(latestUserMessage.isStarred());

            listSod.add(sod);
        }
        return listSod;
    }

    //******************* SUPPLIER - My Closed Demands **********************/
    /**
     * Get supplier's closed demands count.
     * When supplier's work has been accepted, demand changed his status to Closed.
     *
     * @param supplierID
     * @param filter
     * @return
     */
    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public int getSupplierClosedDemandsCount(long supplierID,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        final Supplier supplier = generalService.find(Supplier.class, supplierID);
        final OfferState offerClosed = offerService.getOfferState(OfferStateType.CLOSED.getValue());
        final Search supplierClosedDemandsSearch = searchConverter.convertToSource(searchDefinition);
        supplierClosedDemandsSearch.setSearchClass(Offer.class);
        supplierClosedDemandsSearch.addFilterEqual("supplier", supplier);
        supplierClosedDemandsSearch.addFilterEqual("state", offerClosed);
        supplierClosedDemandsSearch.addFilterEqual("demand.status", DemandStatus.CLOSED);
        supplierClosedDemandsSearch.addField("id", Field.OP_COUNT);
        supplierClosedDemandsSearch.setResultMode(Search.RESULT_SINGLE);
        return ((Long) generalService.searchUnique(supplierClosedDemandsSearch)).intValue();
    }

    /**
     * Get supplier's closed demands.
     * When supplier's work has been accepted, demand changed his status to Closed.
     *
     * @param supplierID
     * @param filter
     * @return
     */
    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public List<SupplierOffersDetail> getSupplierClosedDemands(long supplierID,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        Supplier supplier = generalService.find(Supplier.class, supplierID);
        OfferState offerClosed = offerService.getOfferState(OfferStateType.CLOSED.getValue());

        Map<UserMessage, Integer> latestUserMessages =
                userMessageService.getSupplierConversationsWithClosedDemands(supplier.getBusinessUser(), offerClosed);

        List<UserMessage> sortedList = setSort(
                new Search(UserMessage.class), "message.demand.", searchDefinition, latestUserMessages.keySet());
        List<SupplierOffersDetail> listSod = new ArrayList<SupplierOffersDetail>();

        for (UserMessage latestUserMessage : sortedList) {
            Offer offer = latestUserMessage.getMessage().getOffer();
            SupplierOffersDetail sod = new SupplierOffersDetail();

            // TODO LATER ivlcek - refactor and create converter
            // supplier part
            sod.setSupplierId(supplierID);
            sod.setRating(offer.getDemand().getClient().getOveralRating());
            // client part
            sod.setDisplayName(offer.getDemand().getClient().getBusinessUser().getBusinessUserData().getDisplayName());
            sod.setClientId(offer.getDemand().getClient().getId());
            sod.setSenderId(latestUserMessage.getMessage().getThreadRoot().getSender().getId());
            // demand part
            sod.setDemandId(offer.getDemand().getId());
            sod.setTitle(offer.getDemand().getTitle());
            // offer part
            sod.setPrice(offer.getPrice());
            sod.setDeliveryDate(offer.getFinishDate());
            sod.setOfferId(offer.getId());
            // Message part
            sod.setReceivedDate(offer.getCreated());
            sod.setThreadRootId(latestUserMessage.getMessage().getThreadRoot().getId());
            // set UserMessage attributes
            sod.setUserMessageId(latestUserMessage.getId());
            sod.setMessageCount(latestUserMessages.get(latestUserMessage));
            sod.setIsRead(latestUserMessage.isRead());
            sod.setIsStarred(latestUserMessage.isStarred());

            listSod.add(sod);
        }
        return listSod;
    }

    //******************** SUPPLIER - My Ratings ******************************/
    /**
     * Get ratings of my closed demands.
     *
     * @param userId user's id
     * @param filter
     * @return
     */
    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public int getSupplierRatingsCount(long supplierID,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        final Supplier supplier = generalService.find(Supplier.class, supplierID);
        final OfferState offerClosed = offerService.getOfferState(OfferStateType.CLOSED.getValue());
        final OfferState offerCompleted = offerService.getOfferState(OfferStateType.COMPLETED.getValue());
        final Search supplierClosedDemandsSearch = searchConverter.convertToSource(searchDefinition);
        supplierClosedDemandsSearch.setSearchClass(Offer.class);
        supplierClosedDemandsSearch.addFilterEqual("supplier", supplier);
        supplierClosedDemandsSearch.addFilterIn("state", offerClosed, offerCompleted);
        supplierClosedDemandsSearch.addFilterNotNull("demand.rating");
        supplierClosedDemandsSearch.addField("id", Field.OP_COUNT);
        supplierClosedDemandsSearch.setResultMode(Search.RESULT_SINGLE);
        return ((Long) generalService.searchUnique(supplierClosedDemandsSearch)).intValue();
    }

    /**
     * Get ratings of my all closed demands.
     *
     * @param userId user's id
     * @param searchDefinition
     * @return
     */
    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public List<DemandRatingsDetail> getSupplierRatings(long supplierID,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {

        final Supplier supplier = generalService.find(Supplier.class, supplierID);
        final OfferState offerClosed = offerService.getOfferState(OfferStateType.CLOSED.getValue());
        final OfferState offerCompleted = offerService.getOfferState(OfferStateType.COMPLETED.getValue());
        final Search supplierClosedDemandsSearch = searchConverter.convertToSource(searchDefinition);
        supplierClosedDemandsSearch.setSearchClass(Offer.class);
        supplierClosedDemandsSearch.addFilterEqual("supplier", supplier);
        supplierClosedDemandsSearch.addFilterIn("state", offerClosed, offerCompleted);
        supplierClosedDemandsSearch.addFilterNotNull("demand.rating");
        if (searchDefinition != null && searchDefinition.getSortOrder() != null) {
            supplierClosedDemandsSearch.addSorts(convertSortList(searchDefinition.getSortOrder(), "demand."));
        }
        List<Offer> offersWithRating = generalService.search(supplierClosedDemandsSearch);

        ArrayList<DemandRatingsDetail> ratings = new ArrayList<DemandRatingsDetail>();

        for (Offer offer : offersWithRating) {
            DemandRatingsDetail drd = new DemandRatingsDetail();
            Demand demand = offer.getDemand();
            drd.setDemandId(demand.getId());
            drd.setDemandTitle(demand.getTitle());
            drd.setDemandPrice(demand.getPrice());
            drd.setDemandDescription(demand.getDescription());
            drd.setRatingClient(demand.getRating().getClientRating());
            drd.setRatingSupplier(demand.getRating().getSupplierRating());
            drd.setRatingClientMessage(demand.getRating().getClientMessage());
            drd.setRatingSupplierMessage(demand.getRating().getSupplierMessage());
            ratings.add(drd);
        }
        return ratings;
    }

    /**************************************************************************/
    /* Other getter methods                                                   */
    /**************************************************************************/
    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public FullDemandDetail getFullDemandDetail(long demandId) throws RPCException, ApplicationSecurityException {
        return demandConverter.convertToTarget(generalService.find(Demand.class, demandId));
    }

    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public FullSupplierDetail getFullSupplierDetail(long supplierId) throws RPCException, ApplicationSecurityException {
        return supplierConverter.convertToTarget(generalService.find(Supplier.class, supplierId));
    }

    /**************************************************************************/
    /* Setter methods                                                         */
    /**************************************************************************/
    /**
     * This method will update number of unread messages of logged user.
     * Since this RPC class requires access of authenticated user (see security-web.xml) this method will be called
     * only when PoptavkaUserAuthentication object exist in SecurityContextHolder and we can retrieve userId.
     *
     * @return UnreadMessagesDetail with number of unread messages and other info to be displayed after users logs in
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public UnreadMessagesDetail updateUnreadMessagesCount() throws RPCException, ApplicationSecurityException {
        Long userId = ((PoptavkaUserAuthentication) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        Search unreadMessagesSearch = new Search(UserMessage.class);
        unreadMessagesSearch.addFilterNotNull("message.demand");
        unreadMessagesSearch.addFilterEqual("isRead", false);
        unreadMessagesSearch.addFilterEqual("user.id", userId.longValue());
        unreadMessagesSearch.addField("id", Field.OP_COUNT);
        unreadMessagesSearch.setResultMode(Search.RESULT_SINGLE);
        UnreadMessagesDetail unreadMessagesDetail = new UnreadMessagesDetail();
        unreadMessagesDetail.setUnreadMessagesCount((
                (Long) generalService.searchUnique(unreadMessagesSearch)).intValue());
        Search unreadSystemMessagesSearch = new Search(UserMessage.class);
        unreadSystemMessagesSearch.addFilterNull("message.demand");
        unreadSystemMessagesSearch.addFilterEqual("isRead", false);
        unreadSystemMessagesSearch.addFilterEqual("user.id", userId.longValue());
        unreadSystemMessagesSearch.addField("id", Field.OP_COUNT);
        unreadSystemMessagesSearch.setResultMode(Search.RESULT_SINGLE);
        unreadMessagesDetail.setUnreadSystemMessageCount((
                (Long) generalService.searchUnique(unreadSystemMessagesSearch)).intValue());
        return unreadMessagesDetail;
    }

    /**************************************************************************/
    /* Get Detail object for selecting in selection models                    */
    /**************************************************************************/
    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public SupplierPotentialDemandDetail getSupplierDemand(long supplierDemandID)
        throws RPCException, ApplicationSecurityException {
        //TODO RELEASE ivlcek: check workflow of this method and test UI
        return new SupplierPotentialDemandDetail();
    }

    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public SupplierOffersDetail getSupplierOffer(long supplierDemandID)
        throws RPCException, ApplicationSecurityException {
        return new SupplierOffersDetail();
    }

    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public SupplierOffersDetail getSupplierAssignedDemand(long assignedDemandID) throws RPCException,
            ApplicationSecurityException {
        long supplierID = Storage.getSupplierId();
        Supplier supplier = generalService.find(Supplier.class, supplierID);
        OfferState offerAccepted = offerService.getOfferState(OfferStateType.ACCEPTED.getValue());
        Search supplierOffersSearch = new Search(Offer.class);
        supplierOffersSearch.addFilterEqual("supplier.id", supplierID);
        supplierOffersSearch.addFilterEqual("state", offerAccepted);
        supplierOffersSearch.addFilterEqual("demand.id", assignedDemandID);
        Offer offer = (Offer) generalService.searchUnique(supplierOffersSearch);

        SupplierOffersDetail sod = new SupplierOffersDetail();

        // TODO LATER ivlcek - refactor and create converter. Finish if we need history link for this
        sod.setSupplierId(offer.getSupplier().getId());
        sod.setDisplayName(offer.getDemand().getClient().getBusinessUser().getBusinessUserData().getDisplayName());
        sod.setClientId(offer.getDemand().getClient().getId());
        sod.setDemandId(offer.getDemand().getId());
        sod.setPrice(offer.getPrice());
        sod.setDeliveryDate(offer.getFinishDate());
        sod.setOfferId(offer.getId());
        sod.setReceivedDate(offer.getCreated());
        sod.setRating(offer.getSupplier().getOveralRating());

        // load latest userMessage in conversation
        Search conversationMessagesSearch = new Search(UserMessage.class);
        conversationMessagesSearch.addFilterEqual("message.demand.id", offer.getDemand().getId());
        conversationMessagesSearch.addFilterEqual("user.id", offer.getSupplier().getBusinessUser().getId());
        conversationMessagesSearch.addSortDesc("id", false);
        List<UserMessage> conversationMessages = (generalService.search(conversationMessagesSearch));
        UserMessage latestUserMessage = conversationMessages.get(0);
        sod.setMessageCount(conversationMessages.size());
        sod.setIsRead(latestUserMessage.isRead());
        sod.setUserMessageId(latestUserMessage.getId());
        // latestUserMessage object
        sod.setThreadRootId(latestUserMessage.getMessage().getThreadRoot().getId());
        return sod;
    }

    @Override
    /**
     * Suppier enters a new feedback for Client with respect to given demand.
     *
     * @param demandID of Demand to which this feedback is connected
     * @param clientRating integer number that will be assigned to client
     * @param clientMessage comment that will be assigned to client
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public void finishOfferAndEnterFeedbackForClient(final long demandID, final long offerID,
            final Integer supplierRating, final String supplierMessage)
        throws RPCException, ApplicationSecurityException {
        finishOffer(offerID);
        enterFeedbackForClient(demandID, supplierRating, supplierMessage);
    }

    /**
     * Load all data to construct SupplierDashboardDetail. Data such as number of unread messages for particular
     * sections will be retrieved.
     *
     * @param userId of Supplier for which dashboard object will be created
     * @param supplierId of this Supplier
     * @return supplierDashboardDetail
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public SupplierDashboardDetail getSupplierDashboardDetail(long userId, long supplierId) throws RPCException,
            ApplicationSecurityException {
        User user = generalService.find(User.class, userId);
        Supplier supplier = generalService.find(Supplier.class, supplierId);
        SupplierDashboardDetail cdd = new SupplierDashboardDetail();
        cdd.setUserId(userId);
        // my PotentialDemands unread messages
        final Search search1 = new Search(UserMessage.class);
        search1.addFilterEqual("user", user);
        search1.addFilterEqual("isRead", false);
        search1.addFilterNotNull("message.demand");
        search1.addFilterNotEqual("message.threadRoot.sender", user);
        search1.addFilterIn("message.demand.status", DemandStatus.ACTIVE, DemandStatus.OFFERED);
        search1.addFilterNull("message.offer");
        search1.addField("id", Field.OP_COUNT);
        search1.setResultMode(Search.RESULT_SINGLE);
        cdd.setUnreadMessagesPotentialDemandsCount(((Long) generalService.searchUnique(search1)).intValue());
        // my Offered Demands unread messages
        OfferState offerPending = offerService.getOfferState(OfferStateType.PENDING.getValue());
        final Search search2 = new Search(UserMessage.class);
        search2.addFilterEqual("user", user);
        search2.addFilterEqual("isRead", false);
        search2.addFilterNotNull("message.demand");
        search2.addFilterNotNull("message.offer");
        search2.addFilterEqual("message.offer.supplier", supplier);
        search2.addFilterEqual("message.offer.state", offerPending);
        search2.addField("id", Field.OP_COUNT);
        search2.setResultMode(Search.RESULT_SINGLE);
        cdd.setUnreadMessagesOfferedDemandsCount(((Long) generalService.searchUnique(search2)).intValue());
        // my Assigned Demands unread messages
        OfferState offerAccepted = offerService.getOfferState(OfferStateType.ACCEPTED.getValue());
        OfferState offerCompleted = offerService.getOfferState(OfferStateType.COMPLETED.getValue());
        final Search search3 = new Search(UserMessage.class);
        search3.addFilterEqual("user", user);
        search3.addFilterEqual("isRead", false);
        search3.addFilterNotNull("message.demand");
        search3.addFilterNotNull("message.offer");
        search3.addFilterEqual("message.offer.supplier", supplier);
        search3.addFilterIn("message.offer.state", offerAccepted, offerCompleted);
        search3.addField("id", Field.OP_COUNT);
        search3.setResultMode(Search.RESULT_SINGLE);
        cdd.setUnreadMessagesAssignedDemandsCount(((Long) generalService.searchUnique(search3)).intValue());
        // my Closed Demands unread messages
        OfferState offerClosed = offerService.getOfferState(OfferStateType.CLOSED.getValue());
        final Search search4 = new Search(UserMessage.class);
        search4.addFilterEqual("user", user);
        search4.addFilterEqual("isRead", false);
        search4.addFilterNotNull("message.demand");
        search4.addFilterNotNull("message.offer");
        search4.addFilterEqual("message.offer.supplier", supplier);
        search4.addFilterEqual("message.offer.state", offerClosed);
        search4.addField("id", Field.OP_COUNT);
        search4.setResultMode(Search.RESULT_SINGLE);
        cdd.setUnreadMessagesClosedDemandsCount(((Long) generalService.searchUnique(search4)).intValue());

        return cdd;
    }

    private void finishOffer(long offerId) throws RPCException, ApplicationSecurityException {
        Offer offer = (Offer) generalService.find(Offer.class, offerId);
        Demand demand = offer.getDemand();
        demand.setStatus(DemandStatus.PENDINGCOMPLETION);
        generalService.merge(demand);
        offer.setState(offerService.getOfferState(OfferStateType.COMPLETED.getValue()));
        generalService.save(offer);
    }

    private void enterFeedbackForClient(final long demandID, final Integer clientRating, final String clientMessage)
        throws RPCException, ApplicationSecurityException {
        final Demand demand = generalService.find(Demand.class, demandID);
        Rating rating;
        if (demand.getRating() == null) {
            rating = new Rating();
        } else {
            rating = demand.getRating();
        }
        rating.setClientRating(clientRating);
        rating.setClientMessage(clientMessage);
        generalService.save(rating);
        demand.setRating(rating);
        generalService.save(demand);
        recalculateClientOveralRating(demand.getClient());
    }

    /**
     * Recalculate overall <code>Client</code> rating from all ratings posted by <code>Supplier</code>-s.
     *
     * @param client whose overalRating will be recalculated.
     */
    private void recalculateClientOveralRating(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Passed Client object is null");
        }
        final Search demandsWithRatingSearch = new Search(Demand.class);
        demandsWithRatingSearch.addFilterIn("status", DemandStatus.CLOSED, DemandStatus.PENDINGCOMPLETION);
        demandsWithRatingSearch.addFilterEqual("client", client);
        demandsWithRatingSearch.addFilterNotNull("rating");
        List<Demand> demandsWithRating = generalService.search(demandsWithRatingSearch);

        int numberOfRatings = 0;
        int ratingSum = 0;
        for (Demand demand : demandsWithRating) {
            if (demand.getRating().getClientRating() != null) {
                ratingSum = ratingSum + demand.getRating().getClientRating().intValue();
                numberOfRatings++;
            }
        }
        Double ratingScore = (double) ratingSum / numberOfRatings;
        client.setOveralRating(Integer.valueOf(ratingScore.intValue()));
        generalService.save(client);
    }

    private List<UserMessage> setSort(
            Search search, String pathToAttributes, SearchDefinition searchDefinition, Set set) {
        List<UserMessage> sortedList = new ArrayList<UserMessage>(set);
        if (searchDefinition != null && searchDefinition.getSortOrder() != null) {
            search.addSorts(convertSortList(searchDefinition.getSortOrder(), pathToAttributes));
            Searcher.sortList(search, sortedList);
        }
        return sortedList;
    }

    private Sort[] convertSortList(ArrayList<SortPair> sortPairs, String pathToAttributes) {
        List<Sort> sorts = new ArrayList<Sort>();
        for (SortPair pair : sortPairs) {
            pair.setPathToAttributes(pathToAttributes);
            sorts.add(sortConverter.convertToSource(pair));
        }
        return sorts.toArray(new Sort[sorts.size()]);
    }
}
