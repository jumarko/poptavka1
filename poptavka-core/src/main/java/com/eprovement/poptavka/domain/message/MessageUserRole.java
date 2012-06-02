/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eprovement.poptavka.domain.message;

import com.eprovement.poptavka.domain.common.DomainObject;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.util.orm.OrmConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

/**
 *
 * @author Vojtech Hubr
 *         Date 12.4.11
 * Associates a user to a message and assigns him/her a role
 */
@Entity
public class MessageUserRole extends DomainObject {
    @ManyToOne
    private User user;
    @ManyToOne
    private Message message;

    @Enumerated(value = EnumType.STRING)
    @Column(length = OrmConstants.ENUM_FIELD_LENGTH)
    private MessageUserRoleType type;

    @Enumerated(value = EnumType.ORDINAL)
    @Column(length = OrmConstants.ENUM_SHORTINT_FIELD_LENGTH)
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
        sb.append("MessageUserRole");
        sb.append("{user.email='").append(user.getEmail()).append('\'');
        sb.append("{roleType='").append(type).append('\'');
        sb.append("{message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
