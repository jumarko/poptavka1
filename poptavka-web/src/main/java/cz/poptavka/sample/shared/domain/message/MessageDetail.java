/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.shared.domain.message;

import java.io.Serializable;
import java.util.Date;

import com.google.gwt.core.client.GWT;

import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.shared.domain.type.MessageType;

/**
 *
 * @author ivan.vlcek
 */
public class MessageDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -928374659233195109L;
    private long messageId;
    private long threadRootId;
    private long parentId;
//    private long firstBornId;
//    private long nexSiblingId;
    private long demandId;
    private String subject;
    private String body;
    private String messageState;
    private String messageType;
    private Date created;
    private Date sent;
    private long senderId;
    private long receiverId;

    public MessageDetail() {
    }

    public MessageDetail(MessageDetail detail) {
        this.updateWholeMessage(detail);
    }

    public static MessageDetail createMessageDetail(Message message) {
        return fillMessageDetail(new MessageDetail(), message);
    }

    public static MessageDetail fillMessageDetail(MessageDetail detail, Message message) {
        detail.setMessageId(message.getId());
        detail.setBody(message.getBody());
        detail.setCreated(message.getCreated());
        detail.setDemandId(message.getDemand().getId());
//        m.setFirstBornId(serialVersionUID);
        detail.setMessageState(message.getMessageState().name());
        detail.setMessageType(MessageType.CONVERSATION.name());
//        m.setNexSiblingId(serialVersionUID);
        detail.setParentId(message.getParent() == null ? -1 : message.getParent().getId());
        GWT.log(detail.getParentId() + " messageSetter");
//        m.setReceiverId();
        detail.setSenderId(message.getSender().getId());
        detail.setSent(message.getSent());
        detail.setSubject(message.getSubject());
        detail.setThreadRootId(message.getThreadRoot().getId());
        return detail;
    }

    //---------------------------- GETTERS AND SETTERS --------------------
    public void updateWholeMessage(MessageDetail detail) {
        messageId = detail.getMessageId();
        threadRootId = detail.getThreadRootId();
        parentId = detail.getParentId();
        demandId = detail.getDemandId();
        subject = detail.getSubject();
        body = detail.getBody();
        messageState = detail.getMessageState();
        messageType = detail.getMessageType();
        created = detail.getCreated();
        sent = detail.getSent();
        senderId = detail.getSenderId();
        receiverId = detail.getReceiverId();
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
    public long getDemandId() {
        return demandId;
    }

    public void setDemandId(long demandId) {
        this.demandId = demandId;
    }

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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MessageID: " + messageId);
        sb.append("\nThreadID: " + threadRootId);
        sb.append("\nParentID: " + parentId);
//        sb.append("\nFirstBornIdID: " + firstBornId);
//        sb.append("\nNexSiblingID: " + nexSiblingId);
        sb.append("\ndemandID: " + demandId);
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
