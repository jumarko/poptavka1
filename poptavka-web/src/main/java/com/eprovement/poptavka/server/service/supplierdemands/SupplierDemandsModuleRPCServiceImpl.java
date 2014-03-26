/*
 * Copyright (C) 2012, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.service.supplierdemands;

import com.eprovement.poptavka.client.service.demand.SupplierDemandsModuleRPCService;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.demand.Rating;
import com.eprovement.poptavka.domain.enums.CommonAccessRoles;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.offer.Offer;
import com.eprovement.poptavka.domain.offer.OfferState;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.converter.SearchConverter;
import com.eprovement.poptavka.server.security.PoptavkaUserAuthentication;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.offer.OfferService;
import com.eprovement.poptavka.service.usermessage.UserMessageService;
import com.eprovement.poptavka.shared.domain.RatingDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.domain.offer.SupplierOffersDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.domain.supplierdemands.SupplierDashboardDetail;
import com.eprovement.poptavka.shared.domain.supplierdemands.SupplierPotentialDemandDetail;
import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.googlecode.genericdao.search.Field;
import com.googlecode.genericdao.search.Search;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * This RPC handles all requests from SupplierDemands module.
 * @author Martin Slavkovsky
 */
@Configurable
public class SupplierDemandsModuleRPCServiceImpl extends AutoinjectingRemoteService
        implements SupplierDemandsModuleRPCService {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    //Services
    private GeneralService generalService;
    private UserMessageService userMessageService;
    private OfferService offerService;
    //Converters
    private Converter<Demand, FullDemandDetail> demandConverter;
    private Converter<Supplier, FullSupplierDetail> supplierConverter;
    private SearchConverter searchConverter;

    /**************************************************************************/
    /* Autowire services and converters                                       */
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
    public void setSearchConverter(@Qualifier("searchConverter") SearchConverter searchConverter) {
        this.searchConverter = searchConverter;
    }

    /**************************************************************************/
    /* Potential Demands                                                      */
    /**************************************************************************/
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
    public int getSupplierPotentialDemandsCount(long userId, SearchDefinition searchDefinition)
        throws RPCException, ApplicationSecurityException {

        final BusinessUser businessUser = generalService.find(BusinessUser.class, userId);
        final Search potentialDemandsCountSearch =
                searchConverter.convertToSourceForCount(Demand.class, searchDefinition);
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
    public List<SupplierPotentialDemandDetail> getSupplierPotentialDemands(long userId,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {

        final BusinessUser businessUser = generalService.find(BusinessUser.class, userId);
        final Search search = searchConverter.convertToSource(UserMessage.class, searchDefinition);
        final Map<UserMessage, Integer> latestUserMessagesWithCount =
                userMessageService.getSupplierConversationsWithoutOffer(businessUser, search);

        ArrayList<SupplierPotentialDemandDetail> supplierPotentialDemands =
                new ArrayList<SupplierPotentialDemandDetail>();

        // TODO LATER ivlcek - refactor with detail converter
        for (UserMessage um : latestUserMessagesWithCount.keySet()) {
            SupplierPotentialDemandDetail detail = new SupplierPotentialDemandDetail();
            // Client part
            detail.setUserId(um.getMessage().getDemand().getClient().getId());
            detail.setSenderId(um.getMessage().getThreadRoot().getSender().getId());
            detail.setOveralRating(um.getMessage().getDemand().getClient().getOveralRating());
            // Message part
            detail.setThreadRootId(um.getMessage().getThreadRoot().getId());
            // UserMessage part
            detail.setUserMessageId(um.getId());
            detail.setStarred(um.isStarred());
            detail.setMessagesCount(latestUserMessagesWithCount.get(um));
            detail.setRead(um.isRead());
            // Demand part
            detail.setDemandId(um.getMessage().getDemand().getId());
            detail.setValidTo(um.getMessage().getDemand().getValidTo());
            detail.setDemandTitle(um.getMessage().getDemand().getTitle());
            detail.setPrice(um.getMessage().getDemand().getPrice());

            supplierPotentialDemands.add(detail);
        }
        return supplierPotentialDemands;
    }

    /**************************************************************************/
    /*  Offers                                                                */
    /**************************************************************************/
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
     * @param start
     * @param maxResult
     * @param filter
     * @param orderColumns
     * @return
     */
    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public List<SupplierOffersDetail> getSupplierOffers(long userId, SearchDefinition searchDefinition)
        throws RPCException, ApplicationSecurityException {

        final BusinessUser businessUser = generalService.find(BusinessUser.class, userId);
        final Map<UserMessage, Integer> latestUserMessagesWithCount =
                userMessageService.getSupplierConversationsWithOffer(businessUser);

        List<SupplierOffersDetail> listSod = new ArrayList<SupplierOffersDetail>();

        // TODO LATER ivlcek - refactor with detail converter
        for (UserMessage latestUserMessage : latestUserMessagesWithCount.keySet()) {
            Offer offer = latestUserMessage.getMessage().getOffer();
            SupplierOffersDetail sod = new SupplierOffersDetail();

            // TODO LATER ivlcek - refactor and create converter
            // client part
            sod.setUserId(offer.getDemand().getClient().getId());
            sod.setOveralRating(offer.getDemand().getClient().getOveralRating());
            // Client name can be displayed because it contrains only contact person name
            sod.setDisplayName(offer.getDemand().getClient().getBusinessUser().getBusinessUserData().getDisplayName());
            sod.setSenderId(latestUserMessage.getMessage().getThreadRoot().getSender().getId());
            // demand part
            sod.setDemandId(offer.getDemand().getId());
            sod.setDemandTitle(offer.getDemand().getTitle());
            // offer part
            sod.setPrice(offer.getPrice());
            sod.setFinishDate(offer.getFinishDate());
            sod.setOfferId(offer.getId());
            // Message part
            sod.setReceivedDate(offer.getCreated());
            sod.setThreadRootId(latestUserMessage.getMessage().getThreadRoot().getId());
            // set UserMessage attributes
            sod.setUserMessageId(latestUserMessage.getId());
            sod.setMessagesCount(latestUserMessagesWithCount.get(latestUserMessage));
            sod.setRead(latestUserMessage.isRead());
            sod.setStarred(latestUserMessage.isStarred());

            listSod.add(sod);
        }
        return listSod;
    }

    /**************************************************************************/
    /*  Assigned Demands                                                      */
    /**************************************************************************/
    /**
     * Gets count of supplier's offers in state ACCEPTED.
     * @param supplierId
     * @return count of ACCEPTED offers
     */
    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public int getSupplierAssignedDemandsCount(long supplierID, SearchDefinition searchDefinition)
        throws RPCException, ApplicationSecurityException {
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
    public List<SupplierOffersDetail> getSupplierAssignedDemands(long supplierID, SearchDefinition searchDefinition)
        throws RPCException, ApplicationSecurityException {

        final Supplier supplier = generalService.find(Supplier.class, supplierID);
        final Search search = searchConverter.convertToSource(UserMessage.class, searchDefinition);
        final Map<UserMessage, Integer> latestUserMessages =
                userMessageService.getSupplierConversationsWithAcceptedOffer(supplier.getBusinessUser(),  search);

        List<SupplierOffersDetail> listSod = new ArrayList<SupplierOffersDetail>();

        for (UserMessage latestUserMessage : latestUserMessages.keySet()) {
            Offer offer = latestUserMessage.getMessage().getOffer();
            SupplierOffersDetail sod = new SupplierOffersDetail();

            // TODO LATER ivlcek - refactor and create converter
            // client part
            sod.setUserId(offer.getDemand().getClient().getId());
            sod.setOveralRating(offer.getDemand().getClient().getOveralRating());
            sod.setDisplayName(offer.getDemand().getClient().getBusinessUser().getBusinessUserData().getDisplayName());
            sod.setSenderId(latestUserMessage.getMessage().getThreadRoot().getSender().getId());
            // demand part
            sod.setDemandId(offer.getDemand().getId());
            sod.setDemandTitle(offer.getDemand().getTitle());
            // offer part
            sod.setPrice(offer.getPrice());
            sod.setFinishDate(offer.getFinishDate());
            sod.setOfferId(offer.getId());
            // Message part
            sod.setReceivedDate(offer.getCreated());
            sod.setThreadRootId(latestUserMessage.getMessage().getThreadRoot().getId());
            // set UserMessage attributes
            sod.setUserMessageId(latestUserMessage.getId());
            sod.setMessagesCount(latestUserMessages.get(latestUserMessage));
            sod.setRead(latestUserMessage.isRead());
            sod.setStarred(latestUserMessage.isStarred());

            listSod.add(sod);
        }
        return listSod;
    }

    /**************************************************************************/
    /*  Closed Demands                                                        */
    /**************************************************************************/
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
    public int getSupplierClosedDemandsCount(long supplierID, SearchDefinition searchDefinition)
        throws RPCException, ApplicationSecurityException {

        final Supplier supplier = generalService.find(Supplier.class, supplierID);
        final OfferState offerClosed = offerService.getOfferState(OfferStateType.CLOSED.getValue());
        final Search supplierClosedDemandsSearch =
                searchConverter.convertToSourceForCount(Offer.class, searchDefinition);
        supplierClosedDemandsSearch.addFilterEqual("supplier", supplier);
        supplierClosedDemandsSearch.addFilterEqual("state", offerClosed);
        supplierClosedDemandsSearch.addFilterEqual("demand.status", DemandStatus.CLOSED);
        return generalService.count(supplierClosedDemandsSearch);
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
    public List<SupplierOffersDetail> getSupplierClosedDemands(long supplierID, SearchDefinition searchDefinition)
        throws RPCException, ApplicationSecurityException {

        final Supplier supplier = generalService.find(Supplier.class, supplierID);
        final Search search = searchConverter.convertToSource(UserMessage.class, searchDefinition);
        final Map<UserMessage, Integer> latestUserMessages =
                userMessageService.getSupplierConversationsWithClosedDemands(supplier.getBusinessUser(), search);

        List<SupplierOffersDetail> listSod = new ArrayList<SupplierOffersDetail>();

        for (UserMessage latestUserMessage : latestUserMessages.keySet()) {
            Offer offer = latestUserMessage.getMessage().getOffer();
            SupplierOffersDetail sod = new SupplierOffersDetail();

            // TODO LATER ivlcek - refactor and create converter
            // supplier part
            sod.setUserId(offer.getDemand().getClient().getId());
            sod.setOveralRating(offer.getDemand().getClient().getOveralRating());
            // client part
            sod.setDisplayName(offer.getDemand().getClient().getBusinessUser().getBusinessUserData().getDisplayName());
            sod.setSenderId(latestUserMessage.getMessage().getThreadRoot().getSender().getId());
            // demand part
            sod.setDemandId(offer.getDemand().getId());
            sod.setDemandTitle(offer.getDemand().getTitle());
            // offer part
            sod.setPrice(offer.getPrice());
            sod.setFinishDate(offer.getFinishDate());
            sod.setOfferId(offer.getId());
            // Message part
            sod.setReceivedDate(offer.getCreated());
            sod.setThreadRootId(latestUserMessage.getMessage().getThreadRoot().getId());
            // set UserMessage attributes
            sod.setUserMessageId(latestUserMessage.getId());
            sod.setMessagesCount(latestUserMessages.get(latestUserMessage));
            sod.setRead(latestUserMessage.isRead());
            sod.setStarred(latestUserMessage.isStarred());

            listSod.add(sod);
        }
        return listSod;
    }

    /**************************************************************************/
    /*  Ratings                                                               */
    /**************************************************************************/
    /**
     * Get ratings of my closed demands.
     *
     * @param userId user's id
     * @param filter
     * @return
     */
    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public int getSupplierRatingsCount(long supplierID, SearchDefinition searchDefinition)
        throws RPCException, ApplicationSecurityException {

        final Supplier supplier = generalService.find(Supplier.class, supplierID);
        final OfferState offerClosed = offerService.getOfferState(OfferStateType.CLOSED.getValue());
        final OfferState offerCompleted = offerService.getOfferState(OfferStateType.COMPLETED.getValue());
        final Search supplierClosedDemandsSearch =
                searchConverter.convertToSourceForCount(Offer.class, searchDefinition);
        supplierClosedDemandsSearch.addFilterEqual("supplier", supplier);
        supplierClosedDemandsSearch.addFilterIn("state", offerClosed, offerCompleted);
        supplierClosedDemandsSearch.addFilterNotNull("demand.rating");
        return generalService.count(supplierClosedDemandsSearch);
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
    public List<RatingDetail> getSupplierRatings(long supplierID, SearchDefinition searchDefinition)
        throws RPCException, ApplicationSecurityException {

        final Supplier supplier = generalService.find(Supplier.class, supplierID);
        final OfferState offerClosed = offerService.getOfferState(OfferStateType.CLOSED.getValue());
        final OfferState offerCompleted = offerService.getOfferState(OfferStateType.COMPLETED.getValue());
        final Search supplierClosedDemandsSearch = searchConverter.convertToSource(Offer.class, searchDefinition);
        supplierClosedDemandsSearch.addFilterEqual("supplier", supplier);
        supplierClosedDemandsSearch.addFilterIn("state", offerClosed, offerCompleted);
        supplierClosedDemandsSearch.addFilterNotNull("demand.rating");
        final List<Offer> offersWithRating = generalService.search(supplierClosedDemandsSearch);

        ArrayList<RatingDetail> ratings = new ArrayList<RatingDetail>();

        for (Offer offer : offersWithRating) {
            RatingDetail drd = new RatingDetail();
            Demand demand = offer.getDemand();
            drd.setDemandId(demand.getId());
            drd.setDemandTitle(demand.getTitle());
            drd.setPrice(demand.getPrice());
            ratings.add(drd);
        }
        return ratings;
    }

    /**************************************************************************/
    /* Other getter methods                                                   */
    /**************************************************************************/
    /**
     * Get full demand detail.
     * @param demandId
     * @return full demand detail
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public FullDemandDetail getFullDemandDetail(long demandId) throws RPCException, ApplicationSecurityException {
        return demandConverter.convertToTarget(generalService.find(Demand.class, demandId));
    }

    /**
     * Get full supplier detail.
     * @param supplierId
     * @return get full supplier detail
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
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

    /**
     * Suppier enters a new feedback for Client with respect to given demand.
     *
     * @param demandID of Demand to which this feedback is connected
     * @param clientRating integer number that will be assigned to client
     * @param clientMessage comment that will be assigned to client
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
    @Override
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

    /**
     * Finnish offer.
     * @param offerId
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
    private void finishOffer(long offerId) throws RPCException, ApplicationSecurityException {
        Offer offer = (Offer) generalService.find(Offer.class, offerId);
        Demand demand = offer.getDemand();
        demand.setStatus(DemandStatus.PENDINGCOMPLETION);
        generalService.merge(demand);
        offer.setState(offerService.getOfferState(OfferStateType.COMPLETED.getValue()));
        generalService.save(offer);
    }

    /**
     * Ender feedback for client.
     * @param demandID
     * @param clientRating
     * @param clientMessage
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
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
}
