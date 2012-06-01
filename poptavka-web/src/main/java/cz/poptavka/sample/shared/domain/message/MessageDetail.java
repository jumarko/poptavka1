/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.shared.domain.message;

import cz.poptavka.sample.exception.MessageException;
import java.io.Serializable;
import java.util.Date;

import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.domain.message.MessageState;
import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.shared.domain.type.MessageType;

/**
 *
 * @author ivan.vlcek
 */
public class MessageDetail implements Serializable {

    private static final String HTML_UNREAD_START = "<strong>";
    private static final String HTML_UNREAD_END = "</strong>";
    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -928374659233195109L;
    private long messageId;
    private long threadRootId;
    private long parentId;
//    private long firstBornId;
//    private long nexSiblingId;
    private String subject;
    private String body;
    private String messageState;
    private String messageType;
    private Date created;
    private Date sent;
    private long senderId;
    private long receiverId;
    private long userMessageId;
    private boolean read;
    private boolean starred;
    private int messageCount;
    private int unreadSubmessages;

    public MessageDetail() {
    }

    public MessageDetail(MessageDetail detail) {
        this.updateWholeMessage(detail);
    }

    public static MessageDetail fillMessageDetail(MessageDetail detail, Message message) {
        detail.setMessageId(message.getId());
        detail.setParentId(message.getParent() == null ? -1 : message.getParent().getId());
        detail.setSenderId(message.getSender() == null ? -1 : message.getSender().getId());
        detail.setThreadRootId(message.getThreadRoot() == null ? -1 : message.getThreadRoot().getId());
        detail.setBody(message.getBody());
        detail.setCreated(message.getCreated());
        //still get annoying nullPE at PotentialDemandMessage
        //so that's the reason for this check    -Beho. 29.11.11
        if (message.getSent() == null) {
            detail.setSent(message.getCreated());
        } else {
            detail.setSent(message.getSent());
        }

//        m.setFirstBornId(serialVersionUID);
        if (message.getMessageState() != null) {
            detail.setMessageState(message.getMessageState().name());
        }
        detail.setMessageType(MessageType.CONVERSATION.name());
//        m.setNexSiblingId(serialVersionUID);
//        m.setReceiverId();

        detail.setSent(message.getSent());
        detail.setSubject(message.getSubject());

        return detail;
    }

    public static MessageDetail fillMessageDetail(MessageDetail detail,
            UserMessage userMessage) {
        fillMessageDetail(detail, userMessage.getMessage());
        detail.setRead(userMessage.isRead());
        detail.setStarred(userMessage.isStarred());
        detail.setMessageCount(detail.getMessageCount());
        detail.setUserMessageId(userMessage.getId());
        detail.setUnreadSubmessages(detail.getUnreadSubmessages());
        return detail;
    }

    //---------------------------- GETTERS AND SETTERS --------------------
    public void updateWholeMessage(MessageDetail detail) {
        messageId = detail.getMessageId();
        threadRootId = detail.getThreadRootId();
        parentId = detail.getParentId();
        subject = detail.getSubject();
        body = detail.getBody();
        messageState = detail.getMessageState();
        messageType = detail.getMessageType();
        created = detail.getCreated();
        sent = detail.getSent();
        senderId = detail.getSenderId();
        receiverId = detail.getReceiverId();
        read = detail.isRead();
        starred = detail.isStarred();
        messageCount = detail.getMessageCount();
        unreadSubmessages = detail.getUnreadSubmessages();
        userMessageId = detail.getUserMessageId();
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long id) {
        this.messageId = id;
    }

    /**
     * Return the root message representing one communication thread. Still the same, child messages inherit this
     * id.
     *
     * @return the threadRootId
     */
    public long getThreadRootId() {
        return threadRootId;
    }

    /**
     * @param threadRootId the threadRootId to set
     */
    public void setThreadRootId(long threadRootId) {
        this.threadRootId = threadRootId;
    }

    /**
     * For Root Message messageId equals threadId.
     *
     * @return the parentId
     */
    public long getParentId() {
        return parentId;
    }

    /**
     * For Root Message messageId equals threadId.
     *
     * @param parentId the parentId to set
     */
    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

//    TODO for praso: are these attributes/methods needed? They are never used, yet.
//    /**
//     * @return the firstBornId
//     */
//    public long getFirstBornId() {
//        return firstBornId;
//    }
//
//    /**
//     * @param firstBornId the firstBornId to set
//     */
//    public void setFirstBornId(long firstBornId) {
//        this.firstBornId = firstBornId;
//    }
//
//    /**
//     * @return the nexSiblingId
//     */
//    public long getNexSiblingId() {
//        return nexSiblingId;
//    }
//
//    /**
//     * @param nexSiblingId the nexSiblingId to set
//     */
//    public void setNexSiblingId(long nexSiblingId) {
//        this.nexSiblingId = nexSiblingId;
//    }
    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return the messageState
     */
    public String getMessageState() {
        return messageState;
    }

    /**
     * @param messageState the messageState to set
     */
    public void setMessageState(String messageState) {
        this.messageState = messageState;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    /**
     * @return the created
     */
    public Date getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    // TODO for praso: what's the difference between sent and created date? Is it needed
    /**
     * @return the sent
     */
    public Date getSent() {
        return sent;
    }

    // TODO for praso: what's the difference between sent and created date? Is it needed
    /**
     * @param sent the sent to set
     */
    public void setSent(Date sent) {
        this.sent = sent;
    }

    /**
     * @return the senderId
     */
    public long getSenderId() {
        return senderId;
    }

    /**
     * @param senderId the senderId to set
     */
    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    /**
     * @return the receiverId
     */
    public long getReceiverId() {
        return receiverId;
    }

    /**
     * @param receiverId the receiverId to set
     */
    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public int getUnreadSubmessages() {
        return unreadSubmessages;
    }

    public void setUnreadSubmessages(int unreadSubmessages) {
        this.unreadSubmessages = unreadSubmessages;
    }

    public long getUserMessageId() {
        return userMessageId;
    }

    public void setUserMessageId(long userMessageId) {
        this.userMessageId = userMessageId;
    }

    public String getFormattedMessageCount() {
        return "(" + getMessageCount() + "/"
                + getUnreadSubmessages() + ")";
    }

    public static String displayHtml(String trustedHtml, boolean isRead) {
        if (isRead) {
            return trustedHtml;
        } else {
            return HTML_UNREAD_START + trustedHtml + HTML_UNREAD_END;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MessageID: " + messageId);
        sb.append("\nThreadID: " + threadRootId);
        sb.append("\nParentID: " + parentId);
//        sb.append("\nFirstBornIdID: " + firstBornId);
//        sb.append("\nNexSiblingID: " + nexSiblingId);
        sb.append("\nSubject: " + subject);
        sb.append("\nBody: " + body);
        sb.append("\nMessageState: " + messageState);
        sb.append("\nDate-Created: " + (created == null ? "null" : created.toString()));
        sb.append("\nDate-sent: " + (sent == null ? "null" : sent.toString()));
        sb.append("\nSenderID: " + senderId);
        sb.append("\nReceiverID: " + receiverId);
        return sb.toString();
    }
    //    public MessageType getMessageType() {
    //        return MessageType.CONVERSATION;
    //    }
}
