package com.eprovement.poptavka.domain.user;

import com.eprovement.poptavka.domain.common.DomainObject;
import com.eprovement.poptavka.domain.settings.Notification;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;

/**
 * Domain class representing user's notifications.
 * Typically used for tracking email notifications for external users.
 */
@Entity
public class UserNotification extends DomainObject {

    /** Notified user. */
    @NotNull
    @ManyToOne(optional = false)
    private User user;

    /** notification date. */
    @NotNull
    @Temporal(value = TemporalType.TIMESTAMP)
    @Past
    private Date sent = new Date();

    /**
     * Associated notification.
     * From this field we can derived the type of verification and other aspects (e.g. message template)
     * @see Notification
     */
    @NotNull
    @ManyToOne(optional = false)
    private Notification notification;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date date) {
        this.sent = date;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("user", user)
                .append("sent", sent)
                .append("notification", notification)
                .toString();
    }
}
