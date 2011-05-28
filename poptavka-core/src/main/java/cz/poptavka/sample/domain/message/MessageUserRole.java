/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.domain.message;

import cz.poptavka.sample.domain.common.DomainObject;
import cz.poptavka.sample.domain.user.User;
import cz.poptavka.sample.util.orm.Constants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

/**
 *
 * @author Vojtech Hubr
 *         Date 12.4.11
 * Associates a user to a message and assigns him/her a role
 */
@Entity
@NamedQuery(name = "getUserMessageThreads",
        query = "select messageUserRole.message "
                + " from MessageUserRole messageUserRole"
                + " where messageUserRole.user = :user")
public class MessageUserRole extends DomainObject {
    @ManyToOne
    private User user;
    @ManyToOne
    private Message message;

    @Enumerated(value = EnumType.STRING)
    @Column(length = Constants.ENUM_FIELD_LENGTH)
    private MessageUserRoleType type;

    @Enumerated(value = EnumType.ORDINAL)
    @Column(length = Constants.ENUM_SHORTINT_FIELD_LENGTH)
    private MessageContext messageContext;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public MessageUserRoleType getType() {
        return type;
    }

    public void setType(MessageUserRoleType type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MessageContext getMessageContext() {
        return messageContext;
    }

    public void setMessageContext(MessageContext messageContext) {
        this.messageContext = messageContext;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("MessageserRole");
        sb.append("{user.email='").append(user.getEmail()).append('\'');
        sb.append("{roleType='").append(type).append('\'');
        sb.append("{message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
