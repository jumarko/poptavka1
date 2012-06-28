package com.eprovement.poptavka.domain.settings;

import com.eprovement.poptavka.domain.enums.NotificationType;
import com.eprovement.poptavka.domain.register.Register;
import com.eprovement.poptavka.util.orm.OrmConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * @author Juraj Martinka
 *         Date: 11.4.11
 */
@Entity
public class Notification extends Register {

    private String name;

    private String description;

    @Enumerated(value = EnumType.STRING)
    @Column(length = OrmConstants.ENUM_FIELD_LENGTH)
    private NotificationType notificationType;

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
