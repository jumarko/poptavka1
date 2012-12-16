/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.server.service.supplierdemands;

import com.eprovement.poptavka.client.service.demand.SupplierDemandsModuleRPCService;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.enums.CommonAccessRoles;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.security.PoptavkaUserAuthentication;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.demand.RatingService;
import com.eprovement.poptavka.service.message.MessageService;
import com.eprovement.poptavka.service.usermessage.UserMessageService;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.PotentialDemandMessage;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.domain.message.UserMessageDetail;
import com.eprovement.poptavka.shared.domain.offer.FullOfferDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.domain.supplierdemands.SupplierPotentialDemandDetail;
import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.googlecode.genericdao.search.Search;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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

    //Services
    private GeneralService generalService;
    private UserMessageService userMessageService;
    private MessageService messageService;
    private RatingService ratingService;
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
        // TODO ivlcek - refactor with detail converter
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
            detail.setRead(um.isRead());
            detail.setStarred(um.isStarred());
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

        // TODO Martin - return supplierPotentialDemands instead of FakeData
        return supplierPotentialDemands;
//        return getFakeData();
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
        return 1L;
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
    public List<FullOfferDetail> getSupplierOffers(long supplierID,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        //TODO Martin - implement when implemented on backend
        return getFakeData();
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
    public List<FullOfferDetail> getSupplierAssignedDemands(long supplierID,
            SearchDefinition searchDefinition) throws RPCException, ApplicationSecurityException {
        //TODO Martin - implement when implemented on backend
        return getFakeData();
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
    public void finishOffer(long id) throws RPCException, ApplicationSecurityException {
        //TODO Juraj - finish offer
        //set OfferState to finished
        //send message to client that offer was finished
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
    public FullOfferDetail getSupplierOffer(long supplierDemandID)
        throws RPCException, ApplicationSecurityException {
        return getFakeItemById(supplierDemandID);
    }

    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public FullOfferDetail getSupplierAssignedDemand(long assignedDemandID)
        throws RPCException, ApplicationSecurityException {
        return getFakeItemById(assignedDemandID);
    }

    /**************************************************************************/
    /* Fake data                                                              */
    /**************************************************************************/
    private List<FullOfferDetail> getFakeData() throws RPCException {
        FullOfferDetail detail = new FullOfferDetail();
        detail.getOfferDetail().setSupplierId(1L);
        detail.getOfferDetail().setDemandId(1L);
        detail.getOfferDetail().setState(OfferStateType.ACCEPTED);
        detail.getOfferDetail().setClientName("Martin Slavkovsky");
        detail.getOfferDetail().setSupplierName("Good Data");
        detail.getOfferDetail().setDemandTitle("Poptavka 1234");
        detail.getOfferDetail().setRating(90);
        detail.getOfferDetail().setPrice(10000);
        detail.getOfferDetail().setFinishDate(new Date());
        detail.getOfferDetail().setCreatedDate(new Date());

        FullDemandDetail demand = new FullDemandDetail();
        demand.setClientId(1L);
        demand.setDemandId(1L);
        demand.setTitle("Poptavka 1234");
        demand.setPrice("21342");
        demand.setValidToDate(new Date());
        demand.setCreated(new Date());
        demand.setEndDate(new Date());
        detail.setDemandDetail(demand);

        UserMessageDetail umd = new UserMessageDetail();
        MessageDetail md = new MessageDetail();
        md.setMessageId(1L);
        md.setThreadRootId(1L);
        md.setSenderId(1L);
        md.setSent(new Date());
        umd.setMessageDetail(md);
        umd.setMessageCount(10);
        umd.setUnreadMessageCount(10);
        detail.setUserMessageDetail(umd);

        List<FullOfferDetail> list = new ArrayList<FullOfferDetail>();
        list.add(detail);
        return list;
    }

    private FullOfferDetail getFakeItemById(long itemId) {
        FullOfferDetail detail = new FullOfferDetail();
        detail.getOfferDetail().setSupplierId(1L);
        detail.getOfferDetail().setDemandId(1L);
        detail.getOfferDetail().setState(OfferStateType.ACCEPTED);
        detail.getOfferDetail().setClientName("Martin Slavkovsky");
        detail.getOfferDetail().setSupplierName("Good Data");
        detail.getOfferDetail().setDemandTitle("Poptavka 1234");
        detail.getOfferDetail().setRating(90);
        detail.getOfferDetail().setPrice(10000);
        detail.getOfferDetail().setFinishDate(new Date());
        detail.getOfferDetail().setCreatedDate(new Date());

        FullDemandDetail demand = new FullDemandDetail();
        demand.setClientId(1L);
        demand.setDemandId(1L);
        demand.setTitle("Poptavka 1234");
        demand.setPrice("21342");
        demand.setValidToDate(new Date());
        demand.setCreated(new Date());
        demand.setEndDate(new Date());
        detail.setDemandDetail(demand);

        UserMessageDetail umd = new UserMessageDetail();
        MessageDetail md = new MessageDetail();
        md.setMessageId(1L);
        md.setThreadRootId(1L);
        md.setSenderId(1L);
        md.setSent(new Date());
        umd.setMessageDetail(md);
        umd.setMessageCount(10);
        umd.setUnreadMessageCount(10);
        detail.setUserMessageDetail(umd);

        return detail;
    }
}
