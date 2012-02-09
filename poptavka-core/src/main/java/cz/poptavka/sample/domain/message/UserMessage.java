/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.domain.message;

import cz.poptavka.sample.domain.common.DomainObject;
import cz.poptavka.sample.domain.user.User;

import cz.poptavka.sample.util.orm.OrmConstants;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Stores message attributes for a given user.
 *
  * @author Vojtech Hubr
 *         Date 12.4.11
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "getUserMessageThreads",
                query = "select userMessage.message "
                        + " from UserMessage userMessage"
                        + " where userMessage.user = :user"),

        @NamedQuery(name = "getPotentialDemandConversation",
                query = " select userMessage.message"
                        + " from UserMessage userMessage"
                        + " where "
                        // either message itself is thread root or it has given thread root
                        + " (userMessage.message = :threadRoot OR userMessage.message.threadRoot = :threadRoot)"
                        + "   and userMessage.user = :supplier and userMessage.message.offer is null"),

        @NamedQuery(name = "getPotentialOfferConversation",
                query = " select userMessage.message"
                        + " from UserMessage userMessage"
                        + " where "
                        // either message itself is thread root or it has given thread root
                        + " (userMessage.message = :threadRoot OR userMessage.message.threadRoot = :threadRoot)"
                        + "   and userMessage.user = :supplier and userMessage.message.offer is not null"),
        @NamedQuery(name = "getUserMesage",
                query = " select userMessage"
                        + " from UserMessage userMessage\n"
                        + "where userMessage.user = :user"
                        + " and userMessage.message = :message") }
)
// TODO jumar - add another roles types or new attribute representing old approach to UserMessage
// if there is a necessity to store UserMessage to allow admin find all unprocessed demands, then
// add new boolean flag (or enum or even first class object for representation of Message state - processed, unprocess,
// review by admin, etc.)
public class UserMessage extends DomainObject {
    @ManyToOne
    private Message message;
    @ManyToOne
    private User user;

    @Enumerated(value = EnumType.STRING)
    @Column(length = OrmConstants.ENUM_FIELD_LENGTH)
    private UserMessageRoleType roleType;

    @Enumerated(value = EnumType.ORDINAL)
    @Column(length = OrmConstants.ENUM_SHORTINT_FIELD_LENGTH)
    private MessageContext messageContext;

    /** Column cannot be named "read" because that is MySQL reserved key word */
    private boolean isRead;
    private boolean starred;


    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserMessageRoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(UserMessageRoleType roleType) {
        this.roleType = roleType;
    }

    public MessageContext getMessageContext() {
        return messageContext;
    }

    public void setMessageContext(MessageContext messageContext) {
        this.messageContext = messageContext;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        this.isRead = read;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ContactMessage");
        sb.append("{user.email='").append(user.getEmail()).append('\'');
        sb.append("{message='").append(message).append('\'');
        sb.append("{roleType='").append(roleType).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
