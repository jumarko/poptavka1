/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.domain.message;

import com.eprovement.poptavka.domain.common.DomainObject;
import com.eprovement.poptavka.domain.enums.MessageContext;
import com.eprovement.poptavka.domain.enums.MessageUserRoleType;
import com.eprovement.poptavka.domain.user.User;

import com.eprovement.poptavka.util.orm.OrmConstants;
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

        @NamedQuery(name = "getConversationUserMessages",
                query = " select userMessage"
                        + " from UserMessage userMessage"
                        + " where "
                        // either message itself is thread root or it has given thread root
                        + " (userMessage.message = :threadRoot OR userMessage.message.threadRoot = :threadRoot)"
                        + "   and userMessage.user = :supplier"),

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
                        + " and userMessage.message = :message"),
        @NamedQuery(name = "getInbox",
                query = "select userMessage"
                        + " from UserMessage userMessage"
                        + " inner join userMessage.message.roles role\n"
                        + "where userMessage.user = :user"
                        + " and role.type = 'TO'"
                        + " and role.user = :user"),
        @NamedQuery(name = "getSentItems",
                query = "select userMessage"
                        + " from UserMessage userMessage\n"
                        + "where userMessage.user = :user"
                        + " and userMessage.message.sender = :user"),
        @NamedQuery(name = "getPotentialDemands",
                query = "select userMessage"
                        + " from UserMessage userMessage "
                        + "where userMessage.user = :supplier"
                        + " and userMessage.message.demand is not NULL"
                        + " and userMessage.message.parent is NULL"
                        + " and userMessage.message.demand.status"
                        + " = 'ACTIVE'"),
        @NamedQuery(name = "getPotentialDemandsCount",
                query = "select count(*)"
                        + " from UserMessage userMessage "
                        + "where userMessage.user = :supplier"
                        + " and userMessage.message.demand is not NULL"
                        + " and userMessage.message.parent is NULL"
                        + " and userMessage.message.demand.status"
                        + " = 'ACTIVE'"),
        @NamedQuery(name = "getSupplierConversationsWithoutOffer",
                query = "select latestUserMessage, count(subUserMessage.id)\n"
                        + "from UserMessage as subUserMessage right join\n"
                        + " subUserMessage.message.threadRoot as rootMessage,"
                        + "UserMessage as latestUserMessage\n"
                        + "where latestUserMessage.message.threadRoot = rootMessage"
                        + " and latestUserMessage.user = :user"
                        + " and subUserMessage.user = :user"
                        + " and rootMessage.demand is not null"
                        + " and latestUserMessage.message.firstBorn is null"
                        + " and latestUserMessage.message.nextSibling is null"
                        + " and latestUserMessage.message.offer is null\n"
                        + "group by latestUserMessage.id"),
        @NamedQuery(name = "getSupplierConversationsWithOffer",
                query = "select latestUserMessage, count(subUserMessage.id)\n"
                        + "from UserMessage as subUserMessage right join\n"
                        + " subUserMessage.message.threadRoot as rootMessage,"
                        + "UserMessage as latestUserMessage\n"
                        + "where latestUserMessage.message.threadRoot = rootMessage"
                        + " and latestUserMessage.user = :user"
                        + " and subUserMessage.user = :user"
                        + " and rootMessage.demand is not null"
                        + " and latestUserMessage.message.firstBorn is null"
                        + " and latestUserMessage.message.nextSibling is null"
                        + " and latestUserMessage.message.offer is not null"
                        + " and latestUserMessage.message.offer.state = :pendingState\n"
                        + "group by latestUserMessage.id"),
        @NamedQuery(name = "getSupplierConversationsWithOfferCount",
                query = "select count(latestUserMessage.id)\n"
                        + "from UserMessage as latestUserMessage\n"
                        + "where latestUserMessage.user = :user"
                        + " and latestUserMessage.message.demand is not null"
                        + " and latestUserMessage.message.firstBorn is null"
                        + " and latestUserMessage.message.nextSibling is null"
                        + " and latestUserMessage.message.offer is not null\n"),
        @NamedQuery(name = "getSupplierConversationsWithoutOfferCount",
                query = "select count(latestUserMessage.id)\n"
                        + "from UserMessage as latestUserMessage\n"
                        + "where latestUserMessage.user = :user"
                        + " and latestUserMessage.message.demand is not null"
                        + " and latestUserMessage.message.firstBorn is null"
                        + " and latestUserMessage.message.nextSibling is null"
                        + " and latestUserMessage.message.offer is null\n"),
        @NamedQuery(name = "getSupplierConversationsWithAcceptedOffer",
                query = "select latestUserMessage, count(subUserMessage.id)\n"
                        + "from UserMessage as subUserMessage right join\n"
                        + " subUserMessage.message.threadRoot as rootMessage,"
                        + "UserMessage as latestUserMessage\n"
                        + "where latestUserMessage.message.threadRoot = rootMessage"
                        + " and latestUserMessage.user = :user"
                        + " and subUserMessage.user = :user"
                        + " and rootMessage.demand is not null"
                        + " and rootMessage.demand.status = :pendingCompletionStatus"
                        + " and latestUserMessage.message.firstBorn is null"
                        + " and latestUserMessage.message.nextSibling is null"
                        + " and latestUserMessage.message.offer is not null\n"
                        + " and (latestUserMessage.message.offer.state = :statusAccepted"
                        + " or latestUserMessage.message.offer.state = :statusCompleted)\n"
                        + "group by latestUserMessage.id"),
        @NamedQuery(name = "getSupplierConversationsWithClosedDemands",
                query = "select latestUserMessage, count(subUserMessage.id)\n"
                        + "from UserMessage as subUserMessage right join\n"
                        + " subUserMessage.message.threadRoot as rootMessage,"
                        + "UserMessage as latestUserMessage\n"
                        + "where latestUserMessage.message.threadRoot = rootMessage"
                        + " and latestUserMessage.user = :user"
                        + " and subUserMessage.user = :user"
                        + " and rootMessage.demand is not null"
                        + " and rootMessage.demand.status = :closedStatus"
                        + " and latestUserMessage.message.firstBorn is null"
                        + " and latestUserMessage.message.nextSibling is null\n"
                        + "group by latestUserMessage.id"),
            @NamedQuery(name = "getClientConversationsWithoutOffer",
                query = "select latestUserMessage, supplier,"
                        + " count(subUserMessage.id)\n"
                        + "from UserMessage as subUserMessage right join\n"
                        + " subUserMessage.message.threadRoot as rootMessage,"
                        + " UserMessage as latestUserMessage"
                        + " left join latestUserMessage.message.roles toRole,"
                        + " User as supplier\n"
                        + "where latestUserMessage.message.threadRoot = rootMessage"
                        + " and rootMessage.demand is not null"
                        + " and rootMessage = :root"
                        + " and latestUserMessage.user = :user"
                        + " and latestUserMessage.message.firstBorn is null"
                        + " and latestUserMessage.message.nextSibling is null"
                        + " and latestUserMessage.message.offer is null"
                        + " and ((latestUserMessage.message.sender = :user"
                        + " and toRole.user = supplier"
                        + " and toRole.type = 'TO')"
                        + " or (latestUserMessage.message.sender != :user"
                        + " and latestUserMessage.message.sender = supplier))"
                        + " and subUserMessage.user = supplier"
                        + "\n"
                        + "group by latestUserMessage.id, supplier.id"),
            @NamedQuery(name = "getClientConversationsWithoutOfferCount",
                query = "select count(latestUserMessage.id)\n"
                        + "from UserMessage as latestUserMessage inner join\n"
                        + " latestUserMessage.message.threadRoot as rootMessage\n"
                        + "where rootMessage.demand is not null"
                        + " and latestUserMessage.user = :user"
                        + " and latestUserMessage.message.firstBorn is null"
                        + " and latestUserMessage.message.nextSibling is null"
                        + " and latestUserMessage.message.offer is null"),
            @NamedQuery(name = "getClientConversationsWithOfferCount",
                query = "select count(latestUserMessage.id)\n"
                        + "from UserMessage as latestUserMessage inner join\n"
                        + " latestUserMessage.message.threadRoot as rootMessage\n"
                        + "where rootMessage.demand is not null"
                        + " and latestUserMessage.user = :user"
                        + " and latestUserMessage.message.demand = :demand"
                        + " and latestUserMessage.message.firstBorn is null"
                        + " and latestUserMessage.message.nextSibling is null"
                        + " and latestUserMessage.message.offer is not null")
}
)
public class UserMessage extends DomainObject {
    @ManyToOne
    private Message message;
    @ManyToOne
    private User user;

    @Enumerated(value = EnumType.STRING)
    @Column(length = OrmConstants.ENUM_FIELD_LENGTH)
    private MessageUserRoleType roleType;

    @Enumerated(value = EnumType.ORDINAL)
    @Column(length = OrmConstants.ENUM_SHORTINT_FIELD_LENGTH)
    private MessageContext messageContext;

    /** Column cannot be named "read" because that is MySQL reserved key word */
    // workaround - see http://stackoverflow.com/questions/8667965/found-bit-expected-boolean-after-hibernate-4-upgrade
    @Column(columnDefinition = "BIT")
    private boolean isRead;
    // workaround - see http://stackoverflow.com/questions/8667965/found-bit-expected-boolean-after-hibernate-4-upgrade
    @Column(columnDefinition = "BIT")
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

    public MessageUserRoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(MessageUserRoleType roleType) {
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
