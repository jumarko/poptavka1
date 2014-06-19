package com.eprovement.poptavka.domain.settings;

import com.eprovement.poptavka.domain.common.DomainObject;
import com.eprovement.poptavka.domain.enums.Period;
import com.eprovement.poptavka.util.orm.OrmConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

/**
 * Notification item represents user's notification setting.
 * It contains {@code period} and {@code notification} which means: "I want to get notification per {@code period}
 * about events represented by {@code notification}".
 *
 * <p>
 *     Custom equals is implemented to ensure that no two same notification settings are set for user.
 *     See {@link com.eprovement.poptavka.service.user.BusinessUserRoleServiceImpl}.
 * </p>
 * @author Juraj Martinka
 *         Date: 11.4.11
 */
@Entity
public class NotificationItem extends DomainObject {

    @Enumerated(value = EnumType.STRING)
    @Column(length = OrmConstants.ENUM_FIELD_LENGTH)
    private Period period;

    @ManyToOne
    private Notification notification;


    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("NotificationItem");
        sb.append("{enabled=").append(isEnabled());
        sb.append(", period=").append(period);
        sb.append(", notification=").append(notification);
        sb.append('}');
        return sb.toString();
    }

    /**
     * CUstom equals method because we need to ensure that each notification setting is saved only once per user.
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final NotificationItem that = (NotificationItem) o;

        if (notification != null ? !notification.equals(that.notification) : that.notification != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (notification != null ? notification.hashCode() : 0);
        return result;
    }
}
