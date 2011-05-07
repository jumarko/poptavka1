/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.domain.mail;

import cz.poptavka.sample.domain.common.DomainObject;
import cz.poptavka.sample.domain.user.User;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 *
  * @author Vojtech Hubr
 *         Date 12.4.11
 * Stores message attributes for a given user
 */
@Entity
public class UserMessage extends DomainObject {
    private boolean isRead;
    private boolean isStarred;
    @ManyToOne
    private Message message;
    @ManyToOne
    private User user;

    public boolean isIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public boolean isIsStarred() {
        return isStarred;
    }

    public void setIsStarred(boolean isStarred) {
        this.isStarred = isStarred;
    }

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ContactMessage");
        sb.append("{user='").append(user.getEmail()).append('\'');
        sb.append("{message='").append(message).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
