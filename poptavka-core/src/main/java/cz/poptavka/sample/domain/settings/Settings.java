package cz.poptavka.sample.domain.settings;

import cz.poptavka.sample.domain.common.DomainObject;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 11.4.11
 */
@Entity
public class Settings extends DomainObject {

    /**
     * General preferences assigned (usually user's preferences).
     * These can be used storing preferences in simple way as a key-value.
     * @see Preference
     */
    @OneToMany
    @JoinColumn(name = "settings_id")
    private List<Preference> preferences;

    @OneToMany
    @JoinColumn(name = "settings_id")
    private List<NotificationItem> notificationItems;


    public List<Preference> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<Preference> preferences) {
        this.preferences = preferences;
    }

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
