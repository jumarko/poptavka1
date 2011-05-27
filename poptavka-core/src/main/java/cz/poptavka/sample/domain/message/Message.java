/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.domain.message;

import cz.poptavka.sample.domain.common.DomainObject;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.offer.Offer;
import cz.poptavka.sample.domain.user.Problem;
import cz.poptavka.sample.domain.user.User;
import cz.poptavka.sample.util.orm.Constants;
import cz.poptavka.sample.util.strings.ToStringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 *
 * @author Vojtech Hubr
 *         Date 12.4.11
 *
 */

@Entity
public class Message extends DomainObject {

    /* the first message in the thread, i.e. the original question */
    @ManyToOne
    private Message threadRoot;

    /* immediate parent of this message - to what this message is a reply */
    @ManyToOne(fetch = FetchType.LAZY)
    private Message parent;

    /** All direct responses to this message, i.e. all messages for which this message is a parent. */
    @OneToMany(mappedBy = "parent")
    private List<Message> children;

    /* the first child of this message - the first reply to trhis message */
    @ManyToOne
    private Message firstBorn;

    /* the next reply to this message's parent */
    @OneToOne
    @JoinColumn
    private Message nextSibling;

    @Column(length = 127)
    private String subject;

    @Lob
    private String body;

    @ManyToOne
    private User sender;

    @Enumerated(value = EnumType.STRING)
    @Column(length = Constants.ENUM_FIELD_LENGTH)
    private MessageState messageState;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date created;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date lastModified;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date sent;

    @OneToMany(mappedBy = "message")
    @Cascade(value = CascadeType.ALL)
    private List<MessageUserRole> roles;

    /* a demand to which this message pertains */
    @ManyToOne
    private Demand demand;

    /* an offer to which this message pertains */
    @ManyToOne
    private Offer offer;

    /* a problem to which this message pertains */
    @ManyToOne
    private Problem problem;


    //---------------------------------- GETTERS AND SETTERS -----------------------------------------------------------

    public boolean isThreadRoot() {
        return parent == null;
    }

    public Message getThreadRoot() {
        return threadRoot;
    }

    public void setThreadRoot(Message threadRoot) {
        this.threadRoot = threadRoot;
    }

    public Message getParent() {
        return parent;
    }

    public void setParent(Message parent) {
        this.parent = parent;
    }

    public List<Message> getChildren() {
        return children;
    }

    public void setChildren(List<Message> children) {
        this.children = children;
    }

    public Message getFirstBorn() {
        return firstBorn;
    }

    public void setFirstBorn(Message firstBorn) {
        this.firstBorn = firstBorn;
    }

    public Message getNextSibling() {
        return nextSibling;
    }

    public void setNextSibling(Message nextSibling) {
        this.nextSibling = nextSibling;
    }


    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public MessageState getMessageState() {
        return messageState;
    }

    public void setMessageState(MessageState messageState) {
        this.messageState = messageState;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Demand getDemand() {
        return demand;
    }

    public void setDemand(Demand demand) {
        this.demand = demand;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public List<MessageUserRole> getRoles() {
        return roles;
    }

    public void setRoles(List<MessageUserRole> roles) {
        this.roles = roles;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Message");
        sb.append("{threadRoot.id=").append(ToStringUtils.printId(threadRoot));
        sb.append(", parent.id=").append(ToStringUtils.printId(parent));
        sb.append(", firstBorn.id=").append(ToStringUtils.printId(firstBorn));
        sb.append(", nextSibling.id=").append(ToStringUtils.printId(nextSibling));
        sb.append("{subject='").append(subject).append('\'');
        sb.append("{sender.email='").append(sender != null ? sender.getEmail() : "").append('\'');
        sb.append("{messageState='").append(messageState).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
