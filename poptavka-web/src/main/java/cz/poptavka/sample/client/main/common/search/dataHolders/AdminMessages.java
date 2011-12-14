package cz.poptavka.sample.client.main.common.search.dataHolders;

import java.io.Serializable;
import java.util.Date;

/** ADMINMESSAGES **/
public class AdminMessages implements Serializable {

    private Long messageIdFrom = null;
    private Long messageIdTo = null;
    private Long demandIdFrom = null;
    private Long demandIdTo = null;
    private Long parentIdFrom = null;
    private Long parentIdTo = null;
    private Long senderIdFrom = null;
    private Long senderIdTo = null;
    private Long receiverIdFrom = null;
    private Long receiverIdTo = null;
    private String subject = null;
    private String state = null;
    private String type = null;
    private Date createdFrom = null;
    private Date createdTo = null;
    private Date sentFrom = null;
    private Date sentTo = null;
    private String body = null;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Long getDemandIdFrom() {
        return demandIdFrom;
    }

    public void setDemandIdFrom(Long demandIdFrom) {
        this.demandIdFrom = demandIdFrom;
    }

    public Long getDemandIdTo() {
        return demandIdTo;
    }

    public void setDemandIdTo(Long demandIdTo) {
        this.demandIdTo = demandIdTo;
    }

    public Long getMessageIdFrom() {
        return messageIdFrom;
    }

    public void setMessageIdFrom(Long messageIdFrom) {
        this.messageIdFrom = messageIdFrom;
    }

    public Long getMessageIdTo() {
        return messageIdTo;
    }

    public void setMessageIdTo(Long messageIdTo) {
        this.messageIdTo = messageIdTo;
    }

    public Long getParentIdFrom() {
        return parentIdFrom;
    }

    public void setParentIdFrom(Long parentIdFrom) {
        this.parentIdFrom = parentIdFrom;
    }

    public Long getParentIdTo() {
        return parentIdTo;
    }

    public void setParentIdTo(Long parentIdTo) {
        this.parentIdTo = parentIdTo;
    }

    public Long getReceiverIdFrom() {
        return receiverIdFrom;
    }

    public void setReceiverIdFrom(Long receiverIdFrom) {
        this.receiverIdFrom = receiverIdFrom;
    }

    public Long getReceiverIdTo() {
        return receiverIdTo;
    }

    public void setReceiverIdTo(Long receiverIdTo) {
        this.receiverIdTo = receiverIdTo;
    }

    public Long getSenderIdFrom() {
        return senderIdFrom;
    }

    public void setSenderIdFrom(Long senderIdFrom) {
        this.senderIdFrom = senderIdFrom;
    }

    public Long getSenderIdTo() {
        return senderIdTo;
    }

    public void setSenderIdTo(Long senderIdTo) {
        this.senderIdTo = senderIdTo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreatedFrom() {
        return createdFrom;
    }

    public void setCreatedFrom(Date createdFrom) {
        this.createdFrom = createdFrom;
    }

    public Date getCreatedTo() {
        return createdTo;
    }

    public void setCreatedTo(Date createdTo) {
        this.createdTo = createdTo;
    }

    public Date getSentFrom() {
        return sentFrom;
    }

    public void setSentFrom(Date sentFrom) {
        this.sentFrom = sentFrom;
    }

    public Date getSentTo() {
        return sentTo;
    }

    public void setSentTo(Date sentTo) {
        this.sentTo = sentTo;
    }
}
