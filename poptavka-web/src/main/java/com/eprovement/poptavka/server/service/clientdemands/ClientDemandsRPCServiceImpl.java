/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.server.service.clientdemands;

import com.eprovement.poptavka.client.service.demand.ClientDemandsRPCService;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.exception.MessageException;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.server.service.demands.DemandsRPCServiceImpl;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.message.MessageService;
import com.eprovement.poptavka.service.usermessage.UserMessageService;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectContestantDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectConversationDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Martin Slavkovsky
 */
@Component(ClientDemandsRPCService.URL)
public class ClientDemandsRPCServiceImpl extends AutoinjectingRemoteService
        implements ClientDemandsRPCService {

    public static final String QUERY_TO_POTENTIAL_DEMAND_SUBJECT = "Dotaz na Vasu zadanu poptavku";
    //Services
    private GeneralService generalService;
    private UserMessageService userMessageService;
    private MessageService messageService;
    //Converters
    private Converter<Demand, FullDemandDetail> demandConverter;
    private Converter<Supplier, FullSupplierDetail> supplierConverter;
    private Converter<Message, MessageDetail> messageConverter;

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

    /**************************************************************************/
    /* Table getter methods                                                   */
    /**************************************************************************/
    //************************* CLIENT - My Demands ***************************/
    /**
     * Get all demand's count that has been created by client.
     * When new demand is created by client, will be involved here.
     * As Client: "All demands created by me."
     *
     * @param clientID - client's ID
     * @param filter - define searching criteria if any
     * @return count
     */
    @Override
    public long getClientProjectsCount(long clientID, SearchModuleDataHolder filter) {
        //TODO Martin - implement when implemented on backend
        return 1L;
    }

    /**
     * Get all demands that has been created by client.
     * When new demand is created by client, will be involved here.
     * As Client: "All demands created by me."
     *
     * @param start
     * @param maxResult
     * @param filter
     * @param orderColumns
     * @return list of demand's detail objects
     */
    @Override
    public List<ClientProjectDetail> getClientProjects(int start, int maxResult,
            SearchModuleDataHolder filter, Map<String, OrderType> orderColumns) {
        //TODO Martin - implement when implemented on backend
        ClientProjectDetail cdmd1 = new ClientProjectDetail();
        cdmd1.setRead(false);
        cdmd1.setDemandId(1L);
        cdmd1.setDemandStatus(DemandStatus.NEW);
        cdmd1.setDemandTitle("Poptavka1");
        cdmd1.setPrice(BigDecimal.valueOf(10000));
        cdmd1.setEndDate(new Date());
        cdmd1.setValidToDate(new Date());

        List<ClientProjectDetail> list = new ArrayList<ClientProjectDetail>();
        list.add(cdmd1);
        return list;
    }

    /**
     * When supplier asks something about a demand of some client.
     * The conversation has more messages of course but I want count of threads.
     * As Client: "Questions made by suppliers to demands made by me." "How many suppliers
     * are asing something about a certain demand."
     *
     * @param clientID - client's ID
     * @param demandID - demand's ID
     * @param filter - define searching criteria if any
     * @return count
     */
    @Override
    public long getClientProjectConversationsCount(long clientID, long demandID, SearchModuleDataHolder filter) {
        //TODO Martin - implement when implemented on backend
        return 1L;
    }

    /**
     * When supplier asks something about a demand of some client.
     * The conversation has more messages of course but I want count of threads. As
     * Client: "Questions made by suppliers to demands made by me."
     * "How many suppliers are asing something about a certain demand."
     *
     * @param clientID - client's
     * @param demandID - demand's
     * @param start
     * @param maxResult
     * @param filter
     * @param orderColumns
     * @return
     */
    @Override
    public List<ClientProjectConversationDetail> getClientProjectConversations(long clientID, long demandID, int start,
            int maxResult, SearchModuleDataHolder filter, Map<String, OrderType> orderColumns) {
        //TODO Martin - implement when implemented on backend
        ClientProjectConversationDetail a = new ClientProjectConversationDetail();
        a.setRead(false);
        a.setUserMessageId(1L);
        a.setSupplierName("Good Data");
        MessageDetail md = new MessageDetail();
        md.setBody("Tak ak date cenu o 10% dole ta to beriem.");
        a.setMessageDetail(md);
        a.setDate(new Date());
        List<ClientProjectConversationDetail> list = new ArrayList<ClientProjectConversationDetail>();
        list.add(a);
        return list;
    }

    //************************* CLIENT - My Offers ****************************/
    /**
     * Get all demands where have been placed an offer by some supplier.
     * When supplier place an offer to client's demand, the demand will be involved here.
     * As Client: "Demands that have already an offer."
     *
     * @param clientID
     * @param filter
     * @return
     */
    @Override
    public long getClientOfferedProjectsCount(long clientID, SearchModuleDataHolder filter) {
        //TODO Martin - implement when implemented on backend
        return 0L;
    }

    /**
     * Get all demands where have been placed an offer by some supplier.
     * When supplier place an offer to client's demand, the demand will be involved here.
     * As Client: "Demands that have already an offer."
     *
     * @param clientID - client's ID
     * @param demandID - demands's ID
     * @param start
     * @param maxResult
     * @param filter
     * @param orderColumns
     * @return
     */
    @Override
    public List<ClientProjectDetail> getClientOfferedProjects(long clientID, long demandID, int start,
            int maxResult, SearchModuleDataHolder filter, Map<String, OrderType> orderColumns) {
        //TODO Martin - implement when implemented on backend
        return new ArrayList<ClientProjectDetail>();
    }

    /**
     * Get all offers of given demand.
     * When supplier place an offer to client's demand, the offer will be involved here.
     * As Client: "How many suppliers placed an offers to a certain demand."
     *
     * @return offers count of given demand
     */
    @Override
    public long getClientProjectContestantsCount(long clientID, long demandID, SearchModuleDataHolder filter) {
        //TODO Martin - implement when implemented on backend
        return 0L;
    }

    /**
     * Get all offers of given demand.
     * When supplier place an offer to client's demand, the offer will be involved here.
     * As Client: "How many suppliers placed an offers to a certain demand."
     *
     * @param clientID
     * @param demandID
     * @param start
     * @param maxResult
     * @param filter
     * @param orderColumns
     * @return
     */
    @Override
    public List<ClientProjectContestantDetail> getClientProjectContestants(long clientID, long demandID, int start,
            int maxResult, SearchModuleDataHolder filter, Map<String, OrderType> orderColumns) {
        //TODO Martin - implement when implemented on backend
        return new ArrayList<ClientProjectContestantDetail>();
    }

    //******************** CLIENT - My Assigned Demands ***********************/
    /**
     * Get all offers that were accepted by client to solve a demand.
     * When client accept an offer, will be involved here.
     * As Client: "All offers that were accepted by me to solve my demand."
     *
     * @param clientID
     * @param filter
     * @return
     */
    @Override
    public long getClientAssignedProjectsCount(long clientID, SearchModuleDataHolder filter) {
        //TODO Martin - implement when implemented on backend
        return 0L;
    }

    /**
     * Get all offers that were accepted by client to solve a demand.
     * When client accept an offer, will be involved here.
     * As Client: "All offers that were accepted by me to solve my demand."
     *
     * @param clientID
     * @param start
     * @param maxResult
     * @param filter
     * @param orderColumns
     * @return
     */
    @Override
    public List<ClientProjectContestantDetail> getClientAssignedProjects(long clientID, int start, int maxResult,
            SearchModuleDataHolder filter, Map<String, OrderType> orderColumns) {
        //TODO Martin - implement when implemented on backend
        return new ArrayList<ClientProjectContestantDetail>();
    }

    /**************************************************************************/
    /* Other getter methods                                                         */
    /**************************************************************************/
    @Override
    public FullDemandDetail getFullDemandDetail(long demandId) {
        return demandConverter.convertToTarget(generalService.find(Demand.class, demandId));
    }

    @Override
    public FullSupplierDetail getFullSupplierDetail(long supplierId) {
        return supplierConverter.convertToTarget(generalService.find(Supplier.class, supplierId));
    }

    @Override
    // TODO call setMessageReadStatus in body
    public ArrayList<MessageDetail> getSuppliersPotentialDemandConversation(
            long threadId, long userId, long userMessageId) throws RPCException {
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
    public void setMessageReadStatus(List<Long> userMessageIds, boolean isRead) throws RPCException {
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
    public void setMessageStarStatus(List<Long> userMessageIds, boolean isStarred) throws RPCException {
        for (Long userMessageId : userMessageIds) {
            UserMessage userMessage = this.generalService.find(UserMessage.class, userMessageId);
            userMessage.setStarred(isStarred);
            this.userMessageService.update(userMessage);
        }
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
    public MessageDetail sendQueryToPotentialDemand(MessageDetail messageDetailImpl) throws RPCException {
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
            Logger.getLogger(DemandsRPCServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
