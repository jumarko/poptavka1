package com.eprovement.poptavka.service.notification;

import com.eprovement.poptavka.domain.enums.NotificationType;
import com.eprovement.poptavka.domain.settings.Notification;

import java.util.List;

public interface NotificationTypeService {
    /**
     * @return all notifications that could be associated with any {@link com.eprovement.poptavka.domain.user.User}
     */
    List<Notification> getNotificationsForUser();

    /**
     * @return all notifications that could be associated with any {@link com.eprovement.poptavka.domain.user.Client}
     */
    List<Notification> getNotificationsForClient();

    /**
     * @return all notifications that could be associated with any {@link com.eprovement.poptavka.domain.user.Supplier}
     */
    List<Notification> getNotificationsForSupplier();

    /**
     * Load notifications of given type.
     * Notifications are fairly static therefore we can cache them for a longer period of time.
     */
    List<Notification> getNotifications(NotificationType notificationType);
}
