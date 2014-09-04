/*
 * Copyright (C) 2013, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.service.detail;


import com.eprovement.poptavka.client.service.demand.DetailRPCService;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.enums.CommonAccessRoles;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.offer.Offer;
import com.eprovement.poptavka.domain.offer.OfferState;
import com.eprovement.poptavka.domain.register.Registers;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.converter.FullDemandConverter;
import com.eprovement.poptavka.server.converter.SupplierConverter;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.message.MessageService;
import com.eprovement.poptavka.service.offer.OfferService;
import com.eprovement.poptavka.service.user.ExternalUserNotificator;
import com.eprovement.poptavka.service.usermessage.UserMessageService;
import com.eprovement.poptavka.shared.domain.FullClientDetail;
import com.eprovement.poptavka.shared.domain.FullRatingDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;

/**
 * This RPC handles all requests for Detail module.
 * @author Martin Slavkovsky
 */
@Configurable
public class DetailRPCServiceImpl extends AutoinjectingRemoteService implements DetailRPCService {

    private static final String DEMAND_TITLE_NOTIFICATION_PARAM = "demand.title";
    private static final String DEMAND_DESC_NOTIFICATION_PARAM = "demand.desc";
    private static final String CLIENT_NOTIFICATION_PARAM = "client";
    private static final String SUPPLIER_NOTIFICATION_PARAM = "supplier";
    private GeneralService generalService;
    private MessageService messageService;
    private OfferService offerService;
    private UserMessageService userMessageService;
    private ExternalUserNotificator externalUserNotificator;

    //Converters
    private FullDemandConverter demandConverter;
    private Converter<Client, FullClientDetail> clientConverter;
    private SupplierConverter supplierConverter;
    private Converter<Message, MessageDetail> messageConverter;
    private Converter<UserMessage, MessageDetail> userMessageConverter;

    /**************************************************************************/
    /* Autowired methods                                                      */
    /**************************************************************************/
    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setOfferService(OfferService offerService) {
        this.offerService = offerService;
    }

    @Autowired
    public void setUserMessageService(UserMessageService userMessageService) {
        this.userMessageService = userMessageService;
    }

    @Autowired
    public void setExternalUserNotificator(ExternalUserNotificator externalUserNotificator) {
        this.externalUserNotificator = externalUserNotificator;
    }

    //Converters
    @Autowired
    public void setDemandConverter(
        @Qualifier("fullDemandConverter") FullDemandConverter demandConverter) {
        this.demandConverter = demandConverter;
    }

    @Autowired
    public void setClientConverter(
        @Qualifier("clientConverter") Converter<Client, FullClientDetail> clientConverter) {
        this.clientConverter = clientConverter;
    }

    @Autowired
    public void setSupplierConverter(
        @Qualifier("supplierConverter") SupplierConverter supplierConverter) {
        this.supplierConverter = supplierConverter;
    }

    @Autowired
    public void setMessageConverter(
        @Qualifier("messageConverter") Converter<Message, MessageDetail> messageConverter) {
        this.messageConverter = messageConverter;
    }

    @Autowired
    public void setUserMessageConverter(
        @Qualifier("userMessageConverter") Converter<UserMessage, MessageDetail> userMessageConverter) {
        this.userMessageConverter = userMessageConverter;
    }

    /**************************************************************************/
    /* DevelDetailWrapper widget methods                                      */
    /**************************************************************************/
    @Override
    public FullDemandDetail getFullDemandDetail(long demandId) throws RPCException {
        return demandConverter.convertToTarget(generalService.find(Demand.class, demandId), true);
    }

    @Override
    public FullClientDetail getFullClientDetail(long clientId) throws RPCException {
        return clientConverter.convertToTarget(generalService.find(Client.class, clientId));
    }

    @Override
    public FullSupplierDetail getFullSupplierDetail(long supplierId) throws RPCException {
        return supplierConverter.convertToTarget(generalService.find(Supplier.class, supplierId), true);
    }

    @Override
    public FullRatingDetail getFullRatingDetail(long demandId) throws RPCException {
        final Demand demand = generalService.find(Demand.class, demandId);
        final OfferState offerClosed = offerService.getOfferState(OfferStateType.CLOSED.getValue());
        final OfferState offerCompleted = offerService.getOfferState(OfferStateType.COMPLETED.getValue());
        final Search supplierClosedDemandsSearch = new Search(Offer.class);
        supplierClosedDemandsSearch.addFilterEqual("demand", demand);
        supplierClosedDemandsSearch.addFilterIn("state", offerClosed, offerCompleted);
        supplierClosedDemandsSearch.addFilterNotNull("demand.rating");
        //Demand can have many offers but only one is in closed state.
        final Offer offer = (Offer) generalService.searchUnique(supplierClosedDemandsSearch);

        FullRatingDetail drd = new FullRatingDetail();
        drd.setDemandId(demand.getId());
        drd.setDemandTitle(demand.getTitle());
        drd.setPrice(demand.getPrice());
        drd.setDemandDescription(demand.getDescription());
        drd.setRatingClient(demand.getRating().getClientRating());
        drd.setRatingSupplier(demand.getRating().getSupplierRating());
        drd.setRatingClientMessage(demand.getRating().getClientMessage());
        drd.setRatingSupplierMessage(demand.getRating().getSupplierMessage());
        drd.setClientName(demand.getClient().getBusinessUser().getBusinessUserData().getDisplayName());
        drd.setSupplierName(offer.getSupplier().getBusinessUser().getBusinessUserData().getDisplayName());
        return drd;
    }

    /**
     * Gets all inbox and sent user messages UsereMessage of given user and thread root.
     * Once loaded, all user messages are set as read, see isRead attribute of UserMessage.
     *
     * @param threadRootId is root message id
     * @param loggedUserId can be either supplier's or client's user Id
     * @return list of all messages in this thread
     * @throws RPCException
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public List<MessageDetail> getConversation(long threadRootId, long loggedUserId, long counterPartyUserId)
        throws RPCException, ApplicationSecurityException {
        final List<UserMessage> userMessages = getConversationUserMessages(
            threadRootId, loggedUserId, counterPartyUserId);
        return userMessageConverter.convertToTargetList(userMessages);
    }

    /**
     * Get conversation user messages.
     * @param threadRootId
     * @param loggedUserId
     * @param counterPartyUserId
     * @return list of user messages
     */
    private List<UserMessage> getConversationUserMessages(long threadRootId, long loggedUserId,
        long counterPartyUserId) {
        Message threadRoot = messageService.getById(threadRootId);

        User user = this.generalService.find(User.class, loggedUserId);
        User counterparty = this.generalService.find(User.class, counterPartyUserId);
        return userMessageService.getConversation(user, counterparty, threadRoot);
    }

    /**
     * Update isRead status of all messages for given User.
     *
     * @param userId user whose UserMessages will be udpated
     * @param messages messages to be updated as read
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public void updateUserMessagesReadStatus(long userId, List<MessageDetail> messages) throws RPCException,
        ApplicationSecurityException {
        final Search search = new Search(UserMessage.class);
        search.addFilterIn("message.id", getMessageIds(messages));
        search.addFilterEqual("user.id", userId);
        search.addFilterEqual("isRead", false);
        List<UserMessage> unreadUserMessages = generalService.search(search);
        for (UserMessage userMessage : unreadUserMessages) {
            userMessage.setRead(true);
            userMessageService.update(userMessage);
        }
    }

    /**
     * Get messages ids.
     * @param messages
     * @return list of message ids.
     */
    private List<Long> getMessageIds(List<MessageDetail> messages) {
        List<Long> messageIds = new ArrayList<Long>(messages.size());
        for (MessageDetail message : messages) {
            messageIds.add(message.getMessageId());
        }
        return messageIds;
    }

    /**************************************************************************/
    /* Messages methods                                                       */
    /**************************************************************************/
    /**
     * Message sent by supplier about a query to potential demand.
     * @param questionMessageToSend message containing a user's question
     * @return message
     */
    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public MessageDetail sendQuestionMessage(MessageDetail questionMessageToSend) throws RPCException,
        ApplicationSecurityException {
        final ReplyMessage replyMessage = sendReplyMessage(questionMessageToSend);
        return getMessageDetail(replyMessage);
    }

    /**
     * Send a message with incorporated Offer. This message with offer will be the reply message to latest message
     * from Client / Supplier conversation. From this moment on every new message in further coversation will have to
     * contain this Offer.
     *
     * @param offerMessageToSend message containing and offer made by supplier
     * @return messageDetail created from persisted Message object.
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public MessageDetail sendOfferMessage(OfferMessageDetail offerMessageToSend) throws RPCException,
        ApplicationSecurityException {
        final ReplyMessage replyMessage = sendReplyMessage(offerMessageToSend);
        // TODO LATER ivlcek - create converter for offers

        // update demand entity
        final Demand demand = replyMessage.message.getDemand();
        demand.setStatus(DemandStatus.OFFERED);
        generalService.save(demand);

        // save offer for message
        replyMessage.message.setOffer(createOfferFromMessage(offerMessageToSend, replyMessage.message));
        messageService.update(replyMessage.message);

        // notify external client if applicable - this time the original sender can only be in role CLIENT
        final Supplier supplier = this.generalService.find(Supplier.class, offerMessageToSend.getSupplierId());
        final BusinessUser client = this.generalService.find(BusinessUser.class,
            replyMessage.message.getThreadRoot().getSender().getId());
        externalUserNotificator.send(client, Registers.Notification.EXTERNAL_CLIENT,
            ImmutableMap.of(
                DEMAND_TITLE_NOTIFICATION_PARAM, demand.getTitle(),
                DEMAND_DESC_NOTIFICATION_PARAM, demand.getDescription(),
                CLIENT_NOTIFICATION_PARAM, client.getDisplayName(),
                SUPPLIER_NOTIFICATION_PARAM, supplier.getBusinessUser().getDisplayName()
            )
        );

        return getMessageDetail(replyMessage);
    }

    /**
     * Creates offer from message.
     * @param offerMessageToSend
     * @param message
     * @return created offer
     */
    private Offer createOfferFromMessage(OfferMessageDetail offerMessageToSend, Message message) {
        final Offer offer = new Offer();
        offer.setSupplier(generalService.find(Supplier.class, offerMessageToSend.getSupplierId()));
        offer.setFinishDate(offerMessageToSend.getFinishDate());
        offer.setPrice(offerMessageToSend.getPrice());
        offer.setState(generalService.find(OfferState.class, 2L));
        offer.setDemand(message.getDemand());
        offer.setCreated(new Date());
        return generalService.save(offer);
    }

    /**
     * Sends new reply to the sender of original message.
     * @param replyMessageToSend message envelope containing information about message to be send
     * @return message representing reply which has been sent
     */
    private ReplyMessage sendReplyMessage(MessageDetail replyMessageToSend) {
        final BusinessUser sender = this.generalService.find(BusinessUser.class, replyMessageToSend.getSenderId());
        final Message originalMessage = this.messageService.getById(replyMessageToSend.getParentId());
        final UserMessage replyUserMessage = messageService.newReply(originalMessage, sender);
        final Message replyMessage = replyUserMessage.getMessage();
        replyMessage.setBody(replyMessageToSend.getBody());
        replyMessage.setSubject(replyMessageToSend.getSubject());
        messageService.send(replyMessage);

        return new ReplyMessage(replyMessage, replyUserMessage);
    }

    //TODO refactor and comment
    private static final class ReplyMessage {

        private final Message message;
        private final UserMessage userMessage;

        private ReplyMessage(Message message, UserMessage userMessage) {
            Validate.notNull(message, "message cannot be null!");
            Validate.notNull(userMessage, "userMessage cannot be null!");
            this.message = message;
            this.userMessage = userMessage;
        }
    }

    /**
     * Creates message from given ReplyMessage.
     * @param replyMessage
     * @return message detail
     */
    private MessageDetail getMessageDetail(ReplyMessage replyMessage) {
        final MessageDetail messageDetail = messageConverter.convertToTarget(replyMessage.message);
        messageDetail.setUserMessageId(replyMessage.userMessage.getId());
        messageDetail.setRead(replyMessage.userMessage.isRead());
        messageDetail.setStarred(replyMessage.userMessage.isStarred());
        return messageDetail;
    }

    @Override
    public Boolean substractCredit(long userId, int credits) throws RPCException {
        BusinessUser businessUser = generalService.find(BusinessUser.class, userId);
        if (businessUser.getBusinessUserData().getCurrentCredits() - credits < 0) {
            return false;
        } else {
            businessUser.getBusinessUserData().substractCredits(credits);
            generalService.save(businessUser);
            return true;
        }
    }
}
