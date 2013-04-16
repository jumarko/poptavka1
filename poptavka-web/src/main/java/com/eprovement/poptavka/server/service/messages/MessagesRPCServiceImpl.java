/*
 * This RPC service serves all methods from MessagesModule
 */
package com.eprovement.poptavka.server.service.messages;

import com.eprovement.poptavka.server.converter.UserMessageConverter;
import com.googlecode.genericdao.search.Search;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.service.demand.MessagesRPCService;
import com.eprovement.poptavka.domain.enums.CommonAccessRoles;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.enums.MessageState;
import com.eprovement.poptavka.domain.message.MessageUserRole;
import com.eprovement.poptavka.domain.enums.MessageUserRoleType;
import com.eprovement.poptavka.domain.message.UserMessage;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.server.converter.MessageConverter;
import com.eprovement.poptavka.server.security.PoptavkaUserAuthentication;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.message.MessageService;
import com.eprovement.poptavka.service.usermessage.UserMessageService;
import com.eprovement.poptavka.shared.search.FilterItem;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.domain.message.UserMessageDetail;
import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.googlecode.genericdao.search.Field;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Praso
 */
@Configurable
public class MessagesRPCServiceImpl extends AutoinjectingRemoteService implements MessagesRPCService {

    public static final String INTERNAL_MESSAGE = "Interna sprava";
    private GeneralService generalService;
    private MessageService messageService;
    private UserMessageService userMessageService;
    private MessageConverter messageConverter;
    private UserMessageConverter userMessageConverter;

    @Autowired
    public void setUserMessageService(UserMessageService userMessageService) {
        this.userMessageService = userMessageService;
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
    public void setMessageConverter(@Qualifier("messageConverter") MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    @Autowired
    public void setUserMessageConverter(@Qualifier("userMessageConverter") UserMessageConverter userMessageConverter) {
        this.userMessageConverter = userMessageConverter;
    }

    /**
     * Message sent by user to another user without any link to demand or offer.
     * @param messageDetailImpl
     * @return message
     */
    @Override
    public MessageDetail sendInternalMessage(MessageDetail messageDetailImpl) throws RPCException {
        Message m = messageService.newReply(this.messageService.getById(
                messageDetailImpl.getThreadRootId()),
                this.generalService.find(User.class, messageDetailImpl.getSenderId()));
        m.setBody(messageDetailImpl.getBody());
        m.setSubject(INTERNAL_MESSAGE);
        // TODO LATER - for message module
        MessageDetail messageDetailFromDB = messageConverter.convertToTarget(this.messageService.create(m));
        return messageDetailFromDB;
    }

    @Override
    public void deleteMessages(List<Long> messagesIds) throws RPCException {
        Search searchMsgs = new Search(Message.class);
        searchMsgs.addFilterIn("id", messagesIds);
        List<Message> msgs = generalService.search(searchMsgs);
        for (Message msg : msgs) {
            Message subRoot = messageService.getById(msg.getId());
            List<Message> conversation = messageService.getAllDescendants(subRoot);
            msg.setMessageState(MessageState.DELETED); //musi byt? neobsahuje to ten getALlDescendatns
            for (Message msg1 : conversation) {
                msg1.setMessageState(MessageState.DELETED);
            }
            generalService.merge(msg);
        }
    }

    /** Inbox messages.                                                      **/
    //--------------------------------------------------------------------------
    @Override
    public Integer getInboxMessagesCount(Long recipientId, SearchDefinition searchDefinition)
        throws RPCException {
        User recipient = generalService.find(User.class, recipientId);
        Search messagesSearch = new Search(UserMessage.class);
        messagesSearch.addFilterEqual("user", recipient);
        messagesSearch.addFilterNull("message.demand");
        messagesSearch.addField("id", Field.OP_COUNT);
        messagesSearch.setResultMode(Search.RESULT_SINGLE);
        return ((Long) generalService.searchUnique(messagesSearch)).intValue();
    }

    @Override
    public ArrayList<MessageDetail> getInboxMessages(Long recipientId, SearchDefinition searchDefinition)
        throws RPCException {
        User recipient = generalService.find(User.class, recipientId);
        Search messagesSearch = new Search(UserMessage.class);
        messagesSearch.addFilterEqual("user", recipient);
        messagesSearch.addFilterNull("message.demand");
        return userMessageConverter.convertToTargetList(generalService.search(messagesSearch));
    }

    //--------------------------------------------------------------------------
    @Override
    public List<UserMessageDetail> getSentMessages(Long senderId, SearchModuleDataHolder searchDataHolder)
        throws RPCException {
        User sender = generalService.find(User.class, senderId);

        /****/// ziskaj vsetky spravy poslane danym uzivatelom
        Search messageSearch = new Search(Message.class);
        messageSearch.addFilterEqual("sender", sender);
        //ak treba, filtruj spravy poslane danym uzivatelom
        if (searchDataHolder != null) {
            for (FilterItem item : searchDataHolder.getAttributes()) {
                this.filter(messageSearch, "", item);
            }
        }

        /****/
        Map<Long, Message> senderMessages = new HashMap<Long, Message>();
        /****/
        List<Message> senderMessagesTmp = new ArrayList<Message>();
        /****/
        senderMessagesTmp.addAll(generalService.search(messageSearch));
        for (Message msg : senderMessagesTmp) {
            if (!senderMessages.containsKey(msg.getThreadRoot().getId())) {
                senderMessages.put(msg.getThreadRoot().getId(), msg);
            }
        }

        /****///Ziskaj vsetkych prijemcov danych sprav
        List<MessageUserRole> recipients = new ArrayList<MessageUserRole>();
        Search messageUserRoleSearch = new Search(MessageUserRole.class);
        messageUserRoleSearch.addFilterIn("message", generalService.search(messageSearch));
        messageUserRoleSearch.addFilterIn("type", MessageUserRoleType.TO);
        //ak treba, filtruj prijemcov danych sprav
        if (searchDataHolder != null) {
            for (FilterItem item : searchDataHolder.getAttributes()) {
                if (item.getItem().equals("email")) {
                    messageUserRoleSearch.addFilterIn("user", generalService.search(
                            this.filter(new Search(User.class), "", item)));
                } else {
                    this.filter(messageSearch, "", item);
                }
            }
        }
        /****/
        recipients.addAll(generalService.search(messageUserRoleSearch));


        //Stacilo by mi aj to zhora, ale musim ziskat este UserMessage, aby som vedel, isRead, isStarred, ...

        /**///Ziskaj UserMessage (read/unread , starred/unstarred)
        List<UserMessage> inboxMessages = new ArrayList<UserMessage>();
        Search userMessagesSearch = new Search(UserMessage.class);
//        for (Message msg : rootMessages) {

        userMessagesSearch.addFilterEqual("user", sender);
        userMessagesSearch.addFilterIn("message", senderMessages.values());
        /**/ inboxMessages.addAll(generalService.search(userMessagesSearch));
//        }

        //Create details
        List<UserMessageDetail> inboxMessagesDetail = new ArrayList<UserMessageDetail>();
//        for (MessageUserRole)
        for (UserMessage userMessage : inboxMessages) {
//            rootMessages.contains(userMessage.getMessage());
//            senderMessages.containsValue(userMessage.getMessage());
//            userMessage.getMessage().equals(this);
            for (MessageUserRole mur : recipients) {
                if (mur.getMessage().equals(userMessage.getMessage())) {
                    userMessage.getMessage().setSender(mur.getMessage().getSender());
                }
            }
//            modify to return MessageDetail
//            inboxMessagesDetail.add(userMessageConverter.convertToTarget(userMessage));
        }

        return inboxMessagesDetail;
//        return this.getMessages(senderId, searchDataHolder, Arrays.asList(MessageUserRoleType.SENDER));
    }

    /**
     * Get all conversations of thread.
     * @param threadRootId
     * @param subRootId
     * @return conversation list
     */
    @Override
    public ArrayList<MessageDetail> getConversationMessages(long threadRootId,
            long subRootId) throws RPCException {
//        Message root = messageService.getById(threadRootId);
        Message subRoot = messageService.getById(subRootId);
        List<Message> conversation = messageService.getAllDescendants(subRoot);

        ArrayList<MessageDetail> result = new ArrayList<MessageDetail>();
        // add root and subRoot message
//        result.add(MessageDetail.createMessageDetail(root));
        result.add(messageConverter.convertToTarget(subRoot));
        for (Message m : conversation) {
            result.add(messageConverter.convertToTarget(m));
        }
        return result;
    }

    /**
     * COMMON.
     * Change 'read' status of sent messages to chosen value
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void setMessageReadStatus(List<Long> userMessageIds,
            boolean isRead) throws RPCException {
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
    public void setMessageStarStatus(List<Long> userMessageIds,
            boolean isStarred) throws RPCException {
        for (Long userMessageId : userMessageIds) {
            UserMessage userMessage = this.generalService.find(UserMessage.class, userMessageId);
            userMessage.setStarred(isStarred);
            this.userMessageService.update(userMessage);
        }
    }

    @Override
    public List<UserMessageDetail> getDeletedMessages(Long userId, SearchModuleDataHolder searchDataHolder)
        throws RPCException {
        Search messageSearch = new Search(Message.class);
        messageSearch.addFilterEqual("messageState", MessageState.DELETED);
        if (searchDataHolder != null) {
            for (FilterItem item : searchDataHolder.getAttributes()) {
                if (item.getItem().equals("email")) {
                    messageSearch.addFilterIn("sender", generalService.search(
                            this.filter(new Search(User.class), "", item)));
                } else {
                    this.filter(messageSearch, "", item);
                }
            }
        }

        Search userMessagesSearch = new Search(UserMessage.class);
        userMessagesSearch.addFilterEqual("user", generalService.find(User.class, userId));
        if (searchDataHolder != null) {
            userMessagesSearch.addFilterIn("message", generalService.search(messageSearch));
        }
        List<UserMessage> userMessages = generalService.search(userMessagesSearch);
        Map<Long, UserMessage> rootDeletedMessages = new TreeMap<Long, UserMessage>();

        for (UserMessage userMessage : userMessages) {
            if (userMessage.getMessage().getParent() == null) {
                if (!rootDeletedMessages.containsKey(userMessage.getMessage().getThreadRoot().getId())) {
                    rootDeletedMessages.put(userMessage.getMessage().getThreadRoot().getId(), userMessage);
                }
            }
        }
        List<UserMessageDetail> deletedMessagesDetail = new ArrayList<UserMessageDetail>();

        for (UserMessage userMessage : rootDeletedMessages.values()) {
//            modify to return MessageDetail
//            deletedMessagesDetail.add(userMessageConverter.convertToTarget(userMessage));
        }

        return deletedMessagesDetail;
    }

    private List<UserMessageDetail> getMessages(Long recipientId, SearchModuleDataHolder searchDataHolder,
            List<MessageUserRoleType> roles) {
        User recipient = generalService.find(User.class, recipientId);

        Search messageSearch = null;
        if (searchDataHolder != null) {
            messageSearch = new Search(Message.class);
            for (FilterItem item : searchDataHolder.getAttributes()) {
                if (item.getItem().equals("email")) {
                    messageSearch.addFilterIn("sender", generalService.search(
                            this.filter(new Search(User.class), "", item)));
                } else {
                    this.filter(messageSearch, "", item);
                }
            }

            //Ziskaj vsetky spravy daneho uzivatela, kt bol oznaceny ako adresat alebo odosielatel
            List<MessageUserRole> recipientMessages = new ArrayList<MessageUserRole>();
            Search recipientMessagesSearch = new Search(MessageUserRole.class);
            recipientMessagesSearch.addFilterEqual("user", recipient);
            recipientMessagesSearch.addFilterIn("type", roles);
            if (searchDataHolder != null) {
                recipientMessagesSearch.addFilterIn("message", generalService.search(messageSearch));
            }
            recipientMessages.addAll(generalService.search(recipientMessagesSearch));


//        Search firstBornRecipientMessagesSearch = new Search(Message.class);
//        List<Message> firstBornRecipientMessages = new ArrayList<Message>();
//        for (MessageUserRole mur : recipientMessages) {
//            firstBornRecipientMessagesSearch.addFilterEqual("id", mur.getMessage().getId());
//            firstBornRecipientMessages = generalService.search(firstBornRecipientMessagesSearch);
//        }

            Map<Long, Message> rootRecipientMessages = new TreeMap<Long, Message>();
            for (MessageUserRole mur : recipientMessages) {
                if (mur.getMessage().getParent() == null) {
                    // nemusi kontorlovat, ved thread_id s parent_id = null je vzdy len jeden
//                if (!rootRecipientMessages.containsKey(mur.getMessage().getThreadRoot().getId())) {
                    rootRecipientMessages.put(mur.getMessage().getThreadRoot().getId(), mur.getMessage());
                }
            }
            //Stacilo by mi aj to zhora, ale musim ziskat este UserMessage, aby som vedel, isRead, isStarred, ...
            List<UserMessage> inboxMessages = new ArrayList<UserMessage>();
            for (Message msg : rootRecipientMessages.values()) {
                Search userMessagesSearch = new Search(UserMessage.class);
                userMessagesSearch.addFilterEqual("user", recipient);
                userMessagesSearch.addFilterEqual("message", msg);
                inboxMessages.addAll(generalService.search(userMessagesSearch));
            }
            List<UserMessageDetail> inboxMessagesDetail = new ArrayList<UserMessageDetail>();
            for (UserMessage userMessage : inboxMessages) {
//                modify to return MessageDetail
//                inboxMessagesDetail.add(userMessageConverter.convertToTarget(userMessage));
            }
        }
        return null;
    }

    private Search filter(Search search, String prefix, FilterItem item) {
        prefix += ".";
        switch (item.getOperation()) {
            case OPERATION_EQUALS:
                search.addFilterEqual(prefix + item.getItem(), item.getValue());
                break;
            case OPERATION_LIKE:
                search.addFilterLike(prefix + item.getItem(), "%" + item.getValue().toString() + "%");
                break;
            case OPERATION_IN:
                search.addFilterIn(prefix + item.getItem(), item.getValue());
                break;
            case OPERATION_FROM:
                search.addFilterGreaterOrEqual(prefix + item.getItem(), item.getValue());
                break;
            case OPERATION_TO:
                search.addFilterLessOrEqual(prefix + item.getItem(), item.getValue());
                break;
            default:
                break;
        }
        return search;
    }

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
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
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
}
