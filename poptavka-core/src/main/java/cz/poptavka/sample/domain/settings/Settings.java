package cz.poptavka.sample.domain.settings;

import cz.poptavka.sample.domain.common.DomainObject;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 11.4.11
 */
@Entity
public class Settings extends DomainObject {

    @OneToMany
    private List<NotificationItem> notificationItems;

    public List<NotificationItem> getNotificationItems() {
        return notificationItems;
    }

    public void setNotificationItems(List<NotificationItem> notificationItems) {
        this.notificationItems = notificationItems;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Settings");
        sb.append("{notificationItems=").append(notificationItems);
        sb.append('}');
        return sb.toString();
    }
}
