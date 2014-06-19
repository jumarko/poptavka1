package com.eprovement.poptavka.domain.settings;

import com.eprovement.poptavka.domain.common.DomainObject;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 * @author Juraj Martinka
 *         Date: 11.4.11
 */
@Entity
// "SETTINGS" is some special table in H2 database - SETTINGS (NAME, VALUE, ID), therefore we need to use different name
@Table(name = "SettingsT")
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
    @Cascade(value = CascadeType.ALL)
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

    /**
     * Adds given notifications items. Addition checks for duplicity.
     * If no notifications yet set, new list of notification is created.
     * @param notificationItems
     */
    public void addNotificationItems(List<NotificationItem> notificationItems) {
        if (this.notificationItems == null || this.notificationItems.isEmpty()) {
            setNotificationItems(notificationItems);
        }
        for (NotificationItem notificationItem : notificationItems) {
            if (!this.notificationItems.contains(notificationItem)) {
                this.notificationItems.add(notificationItem);
            }
        }
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
