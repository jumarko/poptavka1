package com.eprovement.poptavka.domain.settings;

import com.eprovement.poptavka.domain.enums.NotificationType;
import com.eprovement.poptavka.domain.register.Register;
import com.eprovement.poptavka.util.orm.OrmConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Notification object represents one type of notifications being sent to the users.
 * @see NotificationItem for concrete settings of notifications for users
 */
@Entity
public class Notification extends Register {

    /** Human readable name of notification. Used as subject for notification message. */
    private String name;

    /** Short description of purpose of this notification. */
    private String description;

    @Enumerated(value = EnumType.STRING)
    @Column(length = OrmConstants.ENUM_FIELD_LENGTH)
    private NotificationType notificationType;

    /**
     * Base template for generating notification messages. Message templates can consist of simple text
     * and template variables using syntax {myVariable}. Variables can be expanded at runtime.
     * @see org.springframework.web.util.UriTemplate for inspiration and basic ways how to expand variables.
     */
    @Column(length = OrmConstants.TEXT_LENGTH)
    private String messageTemplate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }

    public void setMessageTemplate(String messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Notification");
        sb.append("{name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", notificationType='").append(notificationType).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
