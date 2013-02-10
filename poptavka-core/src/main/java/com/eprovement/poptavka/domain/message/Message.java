/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eprovement.poptavka.domain.message;

import com.eprovement.poptavka.domain.common.DomainObject;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.enums.MessageState;
import com.eprovement.poptavka.domain.offer.Offer;
import com.eprovement.poptavka.domain.user.Problem;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.exception.MessageException;
import com.eprovement.poptavka.util.orm.OrmConstants;
import com.eprovement.poptavka.util.strings.ToStringUtils;
import java.util.ArrayList;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 *
 * @author Vojtech Hubr
 *         Date 12.4.11
 *
 */

@Entity
@NamedQueries({
        @NamedQuery(name = "getListOfClientDemandMessages",
                query = "from Message message\n"
                        + "where parent is null"
                        + " and message.demand is not null"
                        + " and message.sender = :sender\n"),

        @NamedQuery(name = "getListOfClientDemandMessagesSub",
                query = "select message.id, count(subUserMessage.id)\n"
                        + "from UserMessage as subUserMessage right join"
                        + " subUserMessage.message as subMessage"
                        + " right join subMessage.threadRoot as message\n"
                        + "where subUserMessage.user = :user"
                        + " and message.demand is not null"
                        + " and message.sender = :sender\n"
                        + "group by message.id"),
        @NamedQuery(name = "getLatestSupplierUserMessagesWithoutOfferForDemand",
                query = "select max(subUserMessage.id), count(subUserMessage.id)\n"
                        + "from UserMessage as subUserMessage right join"
                        + " subUserMessage.message as subMessage"
                        + " right join subMessage.threadRoot as message\n"
                        + "where subUserMessage.user = :user"
                        + " and message.threadRoot = :threadRoot"
                        + " and message.sender = :user"
                        + " and subMessage.offer is null\n"
                        + "group by subMessage.sender.id"),
        @NamedQuery(name = "getLatestSupplierUserMessagesWithOfferForDemand",
                query = "select latestUserMessage.id, count(subUserMessage.id)\n"
                        + "from UserMessage as subUserMessage right join\n"
                        + " subUserMessage.message.threadRoot as rootMessage,"
                        + "UserMessage as latestUserMessage\n"
                        + "where latestUserMessage.message.threadRoot = rootMessage"
                        + " and latestUserMessage.user = :user"
                        + " and subUserMessage.user = :user"
                        + " and rootMessage = :threadRoot"
                        + " and latestUserMessage.message.firstBorn is null"
                        + " and latestUserMessage.message.nextSibling is null"
                        + " and latestUserMessage.message.offer is not null"
                        + " and latestUserMessage.message.offer.state = :pendingState\n"
                        + "group by latestUserMessage.id"),
        @NamedQuery(name = "getListOfClientDemandMessagesUnreadSub",
                query = "select message.demand.id, count(subUserMessage.id)\n"
                        + "from UserMessage as subUserMessage right join"
                        + " subUserMessage.message as subMessage"
                        + " right join subMessage.threadRoot as message\n"
                        + "where subUserMessage.isRead = false"
                        + " and subUserMessage.user = :user"
                        + " and message.demand is not null"
                        + " and subMessage.offer is null"
                        + " and message.sender = :sender\n"
                        + "group by message.id"),
        @NamedQuery(name = "getListOfClientDemandMessagesWithOfferUnreadSub",
                query = "select message.demand.id, count(subUserMessage.id)\n"
                        + "from UserMessage as subUserMessage right join"
                        + " subUserMessage.message as subMessage"
                        + " right join subMessage.threadRoot as message\n"
                        + "where subUserMessage.isRead = false"
                        + " and subUserMessage.user = :user"
                        + " and message.demand is not null"
                        + " and subMessage.offer is not null"
                        + " and message.sender = :sender\n"
                        + "group by message.id"),
        @NamedQuery(name = "getLastChild",
                query = "select message\n"
                        + "from Message as message\n"
                        + "where message.parent = :parent"
                        + " and message.sent is not null\n"
                        + "order by message.sent desc\n"
                        + "limit 1")
}
)

/* @NamedQueries({
        @NamedQuery(name = "getListOfClientDemandMessages",
                query = "select m.id, count(um.id)\n"
                        + "from UserMessage as um inner join"
                        + " um.message as m\n"
                        + "where um.read = false"
                        + " and um.user = :user\n"
                        + "group by m.id\n"
                        + "having m.sender = :sender"
                        ) }
) */


public class Message extends DomainObject {

    /* the first message in the thread, i.e. the original question */
    @ManyToOne
    private Message threadRoot;

    /* immediate parent of this message - to what this message is a reply */
    @ManyToOne(fetch = FetchType.LAZY)
    private Message parent;

    /** All direct responses to this message, i.e. all messages for which this message is a parent. */
    @OneToMany(mappedBy = "parent")
    private List<Message> children = new ArrayList<Message>();

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
    @Column(length = OrmConstants.ENUM_FIELD_LENGTH)
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

    public void setBody(String body) throws MessageException {
        checkModPermissions();
        this.body = body;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) throws MessageException {
        checkModPermissions();
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

    public void setCreated(Date created) throws MessageException {
        checkModPermissions();
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
    // ----------------------- HELPER METHODS -------------
    private void checkModPermissions() throws MessageException {
        if (getMessageState() != MessageState.COMPOSED) {
            throw new MessageException("Message can be modified only in"
                    + " COMPOSED state ehile now it's in " + getMessageState()
                    + " state.");
        }
    }
}
