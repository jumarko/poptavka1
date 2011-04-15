/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.domain.mail;

import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.offer.Offer;
import cz.poptavka.sample.domain.user.Problem;
import cz.poptavka.sample.domain.user.User;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Vojtech Hubr
 *         Date 12.4.11
 *
 */

@Entity
public class Message extends MessageTreeItem {
    @Column(length = 127)
    private String subject;
    @Lob
    private String body;
    @ManyToOne
    private User sender;
    @Enumerated(value = EnumType.STRING)
    private MessageState messageState;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date created;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date lastModified;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date sent;

    @OneToMany
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
        sb.append("{subject='").append(subject).append('\'');
        sb.append("{sender='").append(sender.getLogin()).append('\'');
        sb.append("{messageState='").append(messageState).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
