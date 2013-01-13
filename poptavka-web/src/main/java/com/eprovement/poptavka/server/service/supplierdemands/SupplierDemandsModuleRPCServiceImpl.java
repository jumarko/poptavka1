/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.server.service.supplierdemands;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.service.demand.SupplierDemandsModuleRPCService;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.enums.CommonAccessRoles;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.offer.Offer;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.exception.MessageException;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.security.PoptavkaUserAuthentication;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.demand.RatingService;
import com.eprovement.poptavka.service.message.MessageService;
import com.eprovement.poptavka.service.offer.OfferService;
import com.eprovement.poptavka.service.usermessage.UserMessageService;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.PotentialDemandMessage;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.domain.offer.SupplierOffersDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.domain.supplierdemands.SupplierPotentialDemandDetail;
import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.googlecode.genericdao.search.Search;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class SupplierDemandsModuleRPCServiceImpl extends AutoinjectingRemoteService
        implements SupplierDemandsModuleRPCService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SupplierDemandsModuleRPCServiceImpl.class);

    //Services
    private GeneralService generalService;
    private UserMessageService userMessageService;
    private MessageService messageService;
    private RatingService ratingService;
    private OfferService offerService;
    //Converters
    private Converter<Demand, FullDemandDetail> demandConverter;
    private Converter<Supplier, FullSupplierDetail> supplierConverter;
    private Converter<Message, MessageDetail> messageConverter;
    private Converter<UserMessage, PotentialDemandMessage> potentialDemandMessageConverter;
    private Converter<Search, SearchDefinition> searchConverter;

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
    public void setRatingService(RatingService ratingService) {
        this.ratingService = ratingService;
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
    public long getSupplierPotentialDemandsCount(long supplierID,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        //TODO Martin - implement search definition when implemented on backend
        final BusinessUser businessUser = generalService.find(BusinessUser.class, supplierID);
        final Search potentialDemandsCountSearch = searchConverter.convertToSource(searchDefinition);
        return userMessageService.getPotentialDemandsCount(businessUser, potentialDemandsCountSearch);
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
    public List<SupplierPotentialDemandDetail> getSupplierPotentialDemands(long supplierID,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        final BusinessUser businessUser = generalService.find(BusinessUser.class, supplierID);
        final Search potentialDemandsSearch = searchConverter.convertToSource(searchDefinition);
        final List<UserMessage> userMessages = userMessageService.getPotentialDemands(
                businessUser, potentialDemandsSearch);
        // TODO RELEASE ivlcek - refactor with detail converter
        ArrayList<SupplierPotentialDemandDetail> supplierPotentialDemands =
                new ArrayList<SupplierPotentialDemandDetail>();
        for (UserMessage um : userMessages) {
            SupplierPotentialDemandDetail detail = new SupplierPotentialDemandDetail();
            // Client part
            detail.setClientId(um.getMessage().getDemand().getClient().getId());
            detail.setClientName(
                    um.getMessage().getDemand().getClient().getBusinessUser().getBusinessUserData().getDisplayName());
            // Message part
            detail.setMessageId(um.getMessage().getId());
            detail.setThreadRootId(um.getMessage().getThreadRoot().getId());
            detail.setSenderId(um.getMessage().getSender().getId());
            detail.setMessageSent(um.getMessage().getSent());
            // UserMessage part
            detail.setUserMessageId(um.getId());
            detail.setIsStarred(um.isStarred());
            detail.setMessageCount(messageService.getAllDescendantsCount(um.getMessage(), businessUser));
            detail.setUnreadMessageCount(messageService.getUnreadDescendantsCount(um.getMessage(), businessUser));
            // Demand part
            detail.setDemandId(um.getMessage().getDemand().getId());
            detail.setValidTo(um.getMessage().getDemand().getValidTo());
            detail.setEndDate(um.getMessage().getDemand().getEndDate());
            detail.setTitle(um.getMessage().getDemand().getTitle());
            detail.setPrice(um.getMessage().getDemand().getPrice().toPlainString());

            supplierPotentialDemands.add(detail);
        }

        return supplierPotentialDemands;
    }

    //************************ SUPPLIER - My Offers ***************************/
    /**
     * Get offers sent by supplier.
     * When supplier sends an offer, it will be involved here.
     * As Supplier: "Offers I sent"
     *
     * @param supplierID
     * @param filter
     * @return
     */
    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public long getSupplierOffersCount(long supplierID,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        //TODO Martin - implement when implemented on backend
        Supplier supplier = generalService.find(Supplier.class, supplierID);
        Search supplierOffersSearch = new Search(Offer.class);
        supplierOffersSearch.addFilterEqual("supplier.id", supplierID);
        return generalService.search(supplierOffersSearch).size();
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
    public List<SupplierOffersDetail> getSupplierOffers(long supplierID,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        Supplier supplier = generalService.find(Supplier.class, supplierID);
        Search supplierOffersSearch = new Search(Offer.class);
        supplierOffersSearch.addFilterEqual("supplier.id", supplierID);
        // TODO RELEASE ivlcek - load offerState by CODE value
        supplierOffersSearch.addFilterEqual("state.id", 2);
        List<Offer> offers = generalService.search(supplierOffersSearch);
        List<SupplierOffersDetail> listSod = new ArrayList<SupplierOffersDetail>();

        for (Offer offer : offers) {
            SupplierOffersDetail sod = new SupplierOffersDetail();

            // TODO RELASE ivlcek - refactor and create converter, set Rating
            sod.setSupplierId(offer.getSupplier().getId());
            sod.setClientName(offer.getDemand().getClient().getBusinessUser().getBusinessUserData().getDisplayName());
            sod.setClientId(offer.getDemand().getClient().getId());
            sod.setDemandId(offer.getDemand().getId());
            sod.setPrice(offer.getPrice().toPlainString());
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

//            sod.setMessageCount(messageService.getAllDescendantsCount(
//                    latestUserMessage.getMessage(), offer.getSupplier().getBusinessUser()));
            // TODO RELEASE ivlcek - remove unread message count. We will get this attribute value
            // from latestUserMessage object
            sod.setUnreadMessageCount(-1);
            sod.setThreadRootId(latestUserMessage.getMessage().getThreadRoot().getId());
            sod.setSupplierUserId(offer.getSupplier().getBusinessUser().getId());
            listSod.add(sod);
        }
        LOGGER.debug("getSupplierOffers() result:" + listSod.size());
        return listSod;
    }

    //******************* SUPPLIER - My Assigned Demands **********************/
    /**
     * Get supplier's offers that have been accepted.
     * When client accept an supplier's offer, the offer will be implemented here.
     * As Supplier: "Offers that I 'won'."
     *
     * @param supplierID
     * @param filter
     * @return
     */
    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public long getSupplierAssignedDemandsCount(long supplierID,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        //TODO Martin - implement when implemented on backend
        return 1L;
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
        Search supplierOffersSearch = new Search(Offer.class);
        supplierOffersSearch.addFilterEqual("supplier.id", supplierID);
        // TODO RELEASE ivlcek - load offerState by CODE value or ake a select
        supplierOffersSearch.addFilterEqual("state.id", 1);
        supplierOffersSearch.addFilterEqual("state.id", 4);
        List<Offer> offers = generalService.search(supplierOffersSearch);
        List<SupplierOffersDetail> listSod = new ArrayList<SupplierOffersDetail>();

        for (Offer offer : offers) {
            SupplierOffersDetail sod = new SupplierOffersDetail();

            // TODO RELEASE ivlcek - refactor and create converter, set Rating
            sod.setSupplierId(offer.getSupplier().getId());
            sod.setClientName(offer.getDemand().getClient().getBusinessUser().getBusinessUserData().getDisplayName());
            sod.setClientId(offer.getDemand().getClient().getId());
            sod.setDemandId(offer.getDemand().getId());
            sod.setPrice(offer.getPrice().toPlainString());
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

//            sod.setMessageCount(messageService.getAllDescendantsCount(
//                    latestUserMessage.getMessage(), offer.getSupplier().getBusinessUser()));
            // TODO RELEASE ivlcek - remove unread message count. We will get this attribute value from
            // latestUserMessage object
            sod.setUnreadMessageCount(-1);
            sod.setThreadRootId(latestUserMessage.getMessage().getThreadRoot().getId());
            sod.setSupplierUserId(offer.getSupplier().getBusinessUser().getId());
            LOGGER.trace("SupplierOfferDetail=" + sod.toString());
            listSod.add(sod);
        }
        LOGGER.debug("getSupplierOffers() result:" + listSod.size());
        return listSod;
    }

    /**************************************************************************/
    /* Other getter methods                                                         */
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

    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    // TODO call setMessageReadStatus in body
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
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
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
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public void setMessageStarStatus(List<Long> userMessageIds, boolean isStarred) throws RPCException,
            ApplicationSecurityException {
        for (Long userMessageId : userMessageIds) {
            UserMessage userMessage = this.generalService.find(UserMessage.class, userMessageId);
            userMessage.setStarred(isStarred);
            this.userMessageService.update(userMessage);
        }
    }

    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public MessageDetail finishOffer(long offerId, long userMessageId, long userId) throws
            RPCException, ApplicationSecurityException {
        Offer offer = (Offer) generalService.find(Offer.class, offerId);
        offer.setState(offerService.getOfferState(OfferStateType.DELIVERED.getValue()));
        generalService.merge(offer);

        // TODO RELEASE ivlcek - update the onAddResponseMessage in DetailsWrapperPresenter
        // so that we can retrieve latest userMessageId as parameter.

        try {
            UserMessage latestUserMessage = userMessageService.getById(userMessageId);
            Message message = messageService.newReply(latestUserMessage.getMessage(),
                    this.generalService.find(User.class, userId));
            // TODO RELEASE ivlcek - load text from resources
            message.setBody("Demand has been delivered by supplier. Supplier asked for official acceptance.");
            messageService.send(message);
            return messageConverter.convertToTarget(message);
        } catch (MessageException ex) {
            java.util.logging.Logger.getLogger(SupplierDemandsModuleRPCServiceImpl.class.getName()).log(
                    Level.SEVERE, null, ex);
            return null;
        }
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
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public UnreadMessagesDetail updateUnreadMessagesCount() throws RPCException, ApplicationSecurityException {
        Long userId = ((PoptavkaUserAuthentication) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        // TODO Vojto - get number of unread messages. UserId is provided from Authentication obejct see above

        UnreadMessagesDetail unreadMessagesDetail = new UnreadMessagesDetail();
        unreadMessagesDetail.setUnreadMessagesCount(21);
        return unreadMessagesDetail;
    }

    /**************************************************************************/
    /* Get Detail object for selecting in selection models                    */
    /**************************************************************************/
    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public SupplierPotentialDemandDetail getSupplierDemand(long supplierDemandID)
        throws RPCException, ApplicationSecurityException {
        //TODO Ivan
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
        //TODO Martin - implement when implemented on backend
        long supplierID = Storage.getSupplierId();
        Supplier supplier = generalService.find(Supplier.class, supplierID);
        Search supplierOffersSearch = new Search(Offer.class);
        supplierOffersSearch.addFilterEqual("supplier.id", supplierID);
        // TODO RELEASE ivlcek - load offerState by CODE value
        supplierOffersSearch.addFilterEqual("state.id", 1);
        supplierOffersSearch.addFilterEqual("demand.id", assignedDemandID);
        Offer offer = (Offer) generalService.searchUnique(supplierOffersSearch);

        SupplierOffersDetail sod = new SupplierOffersDetail();

        // TODO RELEASE ivlcek - refactor and create converter, set Rating
        sod.setSupplierId(offer.getSupplier().getId());
        sod.setClientName(offer.getDemand().getClient().getBusinessUser().getBusinessUserData().getDisplayName());
        sod.setClientId(offer.getDemand().getClient().getId());
        sod.setDemandId(offer.getDemand().getId());
        sod.setPrice(offer.getPrice().toPlainString());
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

//            sod.setMessageCount(messageService.getAllDescendantsCount(
//                    latestUserMessage.getMessage(), offer.getSupplier().getBusinessUser()));
        // TODO RELEASE ivlcek - remove unread message count. We will get this attribute value from
        // latestUserMessage object
        sod.setUnreadMessageCount(-1);
        sod.setThreadRootId(latestUserMessage.getMessage().getThreadRoot().getId());
        sod.setSupplierUserId(offer.getSupplier().getBusinessUser().getId());
        return sod;
    }
}
