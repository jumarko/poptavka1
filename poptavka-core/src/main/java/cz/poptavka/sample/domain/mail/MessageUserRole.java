/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.domain.mail;

import cz.poptavka.sample.domain.common.DomainObject;
import cz.poptavka.sample.domain.user.User;
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
    private RoleType type;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public RoleType getType() {
        return type;
    }

    public void setType(RoleType type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("MessageserRole");
        sb.append("{user='").append(user.getLogin()).append('\'');
        sb.append("{type='").append(type).append('\'');
        sb.append("{message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
