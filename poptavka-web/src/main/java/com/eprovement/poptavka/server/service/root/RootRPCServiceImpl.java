/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.server.service.root;

import com.eprovement.poptavka.client.service.demand.RootRPCService;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.offer.Offer;
import com.eprovement.poptavka.domain.offer.OfferState;
import com.eprovement.poptavka.domain.product.Service;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.exception.ExpiredActivationCodeException;
import com.eprovement.poptavka.exception.IncorrectActivationCodeException;
import com.eprovement.poptavka.exception.UserNotExistException;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.message.MessageService;
import com.eprovement.poptavka.service.user.ClientService;
import com.eprovement.poptavka.service.user.UserVerificationService;
import com.eprovement.poptavka.service.usermessage.UserMessageService;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.FullClientDetail;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import com.eprovement.poptavka.shared.domain.root.UserActivationResult;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Martin Slavkovsky
 */
@Configurable
public class RootRPCServiceImpl extends AutoinjectingRemoteService
        implements RootRPCService {

    private ClientService clientService;
    private GeneralService generalService;
    private MessageService messageService;
    private UserMessageService userMessageService;
    private UserVerificationService userVerificationService;
    //Converters
    private Converter<BusinessUser, BusinessUserDetail> businessUserConverter;
    private Converter<Demand, FullDemandDetail> demandConverter;
    private Converter<Client, FullClientDetail> clientConverter;
    private Converter<Supplier, FullSupplierDetail> supplierConverter;
    private Converter<Message, MessageDetail> messageConverter;
    private Converter<UserMessage, MessageDetail> userMessageConverter;
    private Converter<Service, ServiceDetail> serviceConverter;

    /**************************************************************************/
    /* Autowired methods                                                      */
    /**************************************************************************/

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
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
    public void setUserVerificationService(UserVerificationService userVerificationService) {
        this.userVerificationService = userVerificationService;
    }

    //Converters
    @Autowired
    public void setBusinessUserConverter(
            @Qualifier("businessUserConverter") Converter<BusinessUser, BusinessUserDetail> businessUserConverter) {
        this.businessUserConverter = businessUserConverter;
    }

    @Autowired
    public void setDemandConverter(
            @Qualifier("fullDemandConverter") Converter<Demand, FullDemandDetail> demandConverter) {
        this.demandConverter = demandConverter;
    }

    @Autowired
    public void setClientConverter(
            @Qualifier("clientConverter") Converter<Client, FullClientDetail> clientConverter) {
        this.clientConverter = clientConverter;
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
    public void setUserMessageConverter(
            @Qualifier("userMessageConverter") Converter<UserMessage, MessageDetail> userMessageConverter) {
        this.userMessageConverter = userMessageConverter;
    }

    @Autowired
    public void setServiceConverter(
            @Qualifier("serviceConverter") Converter<Service, ServiceDetail> serviceConverter) {
        this.serviceConverter = serviceConverter;
    }


    /**************************************************************************/
    /* User methods                                                           */
    /**************************************************************************/
    @Override
    public BusinessUserDetail getUserById(Long userId) throws RPCException {
        //Find vs. SearchUnique ?? - find - ak neviem ci existuje
        //                         - SearchUnique - ak urcite ze existuje
        return businessUserConverter.convertToTarget(generalService.find(BusinessUser.class, userId));
    }

    @Override
    public BusinessUserDetail getBusinessUserByEmail(String email) throws RPCException {
        return businessUserConverter.convertToTarget(findUserByEmail(email));
    }

    /**************************************************************************/
    /* DevelDetailWrapper widget methods                                      */
    /**************************************************************************/
    @Override
    public FullDemandDetail getFullDemandDetail(long demandId) throws RPCException {
        return demandConverter.convertToTarget(generalService.find(Demand.class, demandId));
    }

    @Override
    public FullClientDetail getFullClientDetail(long clientId) throws RPCException {
        return clientConverter.convertToTarget(generalService.find(Client.class, clientId));
    }

    @Override
    public FullSupplierDetail getFullSupplierDetail(long supplierId) throws RPCException {
        return supplierConverter.convertToTarget(generalService.find(Supplier.class, supplierId));
    }

    /**
     * Gets all inbox and sent user messages UsereMessage of given user and thread root.
     * Once loaded, all user messages are set as read, see isRead attribute of UserMessage.
     *
     * @param threadId is root message id
     * @param userId can be either supplier's or client's user Id
     * @return list of all messages in this thread
     * @throws RPCException
     */
    @Override
    // TODO RELEASE ivlcek - secure this method and other methods in rootRPCService
    public List<MessageDetail> getConversation(long threadId, long userId) throws RPCException {
        final List<UserMessage> userMessages = getConversationUserMessages(threadId, userId);
        // set all user messages as read
        for (UserMessage userMessage : userMessages) {
            userMessage.setRead(true);
            userMessageService.update(userMessage);
        }
        return userMessageConverter.convertToTargetList(userMessages);
    }

    private List<UserMessage> getConversationUserMessages(long threadId, long userId) {
        Message threadRoot = messageService.getById(threadId);

        User user = this.generalService.find(User.class, userId);
        final Search searchDefinition = new Search(UserMessage.class);
        searchDefinition.addSort("message.created", true);
        return this.messageService
                .getConversationUserMessages(threadRoot, user, searchDefinition);
    }

    /**************************************************************************/
    /* Messages methods                                                       */
    /**************************************************************************/
    /**
     * Change 'read' status of sent messages to chosen value.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void setMessageReadStatus(List<Long> userMessageIds, boolean isRead) throws RPCException {
        for (Long userMessageId : userMessageIds) {
            UserMessage userMessage = this.generalService.find(UserMessage.class, userMessageId);
            userMessage.setRead(isRead);
            this.userMessageService.update(userMessage);
        }
    }

    /**
     * Message sent by supplier about a query to potential demand.
     * @param questionMessageToSend
     * @return message
     */
    @Override
    public MessageDetail sendQuestionMessage(MessageDetail questionMessageToSend) throws RPCException {
        Message message = messageService.newReply(this.messageService.getById(
                questionMessageToSend.getParentId()),
                this.generalService.find(User.class, questionMessageToSend.getSenderId()));
        message.setBody(questionMessageToSend.getBody());
        message.setSubject(questionMessageToSend.getSubject());
        messageService.send(message);
//            Don't undersand create method here?
//            MessageDetail messageDetailFromDB = messageConverter.convertToTarget(this.messageService.create(message));
//            Isn't creating detail object enough?
//            MessageDetail messageDetailFromDB = messageConverter.convertToTarget(message);
        return messageConverter.convertToTarget(message);
    }

    @Override
    public MessageDetail sendOfferMessage(OfferMessageDetail offerMessageToSend) throws RPCException {
        Message message = messageService.newReply(this.messageService.getById(
                offerMessageToSend.getParentId()),
                this.generalService.find(User.class, offerMessageToSend.getSenderId()));
        message.setBody(offerMessageToSend.getBody());
        // TODO RELEASE ivlcek - create converter for offer
        // update demand entity
        Demand demand = message.getDemand();
        demand.setStatus(DemandStatus.OFFERED);
        generalService.save(demand);
        Offer offer = new Offer();
        offer.setSupplier(generalService.find(Supplier.class, offerMessageToSend.getSupplierId()));
        offer.setFinishDate(offerMessageToSend.getOfferFinishDate());
        offer.setPrice(offerMessageToSend.getPrice());
        offer.setState(generalService.find(OfferState.class, 2L));
        offer.setDemand(message.getDemand());
        offer.setCreated(new Date());
        Offer offerFromDB = generalService.save(offer);
        message.setOffer(offerFromDB);
        // TODO RELEASE ivlcek - shall I save message here or shall I let send() method do it?
        messageService.send(message);
        return messageConverter.convertToTarget(message);
    }

    /**************************************************************************/
    /* Activation methods                                                     */
    /**************************************************************************/
    @Override
    public UserActivationResult activateUser(BusinessUserDetail user, String activationCode) throws RPCException {
        try {
            userVerificationService.activateUser(findUserByEmail(user.getEmail()),
                    StringUtils.trimToEmpty(activationCode));
        } catch (UserNotExistException unee) {
            return UserActivationResult.ERROR_UNKNOWN_USER;
        } catch (IncorrectActivationCodeException iace) {
            return UserActivationResult.ERROR_INCORRECT_ACTIVATION_CODE;
        } catch (ExpiredActivationCodeException eace) {
            return UserActivationResult.ERROR_EXPIRED_ACTIVATION_CODE;
        }

        return UserActivationResult.OK;
    }

    @Override
    public boolean sendActivationCodeAgain(BusinessUserDetail user) throws RPCException {
        // we must search business user by email because detail object doesn't have to proper ID already assigned.
        // TODO LATER : move this to the common place
        userVerificationService.sendNewActivationCode(findUserByEmail(user.getEmail()));
        // since activation mail has been sent in synchronous fashion everything should be ok
        return true;
    }

    private BusinessUser findUserByEmail(String email) {
        final Search search = new Search(BusinessUser.class);
        search.addFilterEqual("email", email);
        return (BusinessUser) generalService.searchUnique(search);
    }

    /**************************************************************************/
    /* Supplier Service methods                                               */
    /**************************************************************************/
    @Override
    public ArrayList<ServiceDetail> getSupplierServices() throws RPCException {
        List<Service> services = this.generalService.findAll(Service.class);
        if (services != null) {
            System.out.println("Services count: " + services.size());
        } else {
            System.out.println("NNULLLLLLLL");
        }
        return serviceConverter.convertToTargetList(services);
    }

    /**************************************************************************/
    /* Registration user methods - Account info                               */
    /**************************************************************************/
    @Override
    public boolean checkFreeEmail(String email) throws RPCException {
        return clientService.checkFreeEmail(email);
    }
}
