package com.eprovement.poptavka.service.notification;

import com.eprovement.poptavka.domain.enums.NotificationType;
import com.eprovement.poptavka.domain.settings.Notification;
import com.eprovement.poptavka.service.GeneralService;
import com.googlecode.ehcache.annotations.Cacheable;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang.Validate;

import java.util.ArrayList;
import java.util.List;

public class NotificationTypeServiceImpl implements NotificationTypeService {

    private final GeneralService generalService;

    public NotificationTypeServiceImpl(GeneralService generalService) {
        Validate.notNull(generalService, "generalService cannot be null!");
        this.generalService = generalService;
    }

    /**
     * @return all notifications that could be associated with any {@link com.eprovement.poptavka.domain.user.User}
     */
    @Override
    public List<Notification> getNotificationsForUser() {
        return getNotifications(NotificationType.USER);
    }


    /**
     * @return all notifications that could be associated with any {@link com.eprovement.poptavka.domain.user.Client}
     */
    @Override
    public List<Notification> getNotificationsForClient() {
        final List<Notification> notifications = new ArrayList<>();
        notifications.addAll(getNotifications(NotificationType.USER));
        notifications.addAll(getNotifications(NotificationType.CLIENT));
        return notifications;
    }


    /**
     * @return all notifications that could be associated with any {@link com.eprovement.poptavka.domain.user.Supplier}
     */
    @Override
    public List<Notification> getNotificationsForSupplier() {
        final List<Notification> notifications = new ArrayList<>();
        notifications.addAll(getNotifications(NotificationType.USER));
        notifications.addAll(getNotifications(NotificationType.SUPPLIER));
        return notifications;
    }

    //--------------------------------------------------- HELPER METHODS -----------------------------------------------

    /**
     * Load notifications of given type.
     * Notifications are fairly static therefore we can cache them for a longer period of time.
     */
    @Cacheable(cacheName = "cache5h")
    private List<Notification> getNotifications(NotificationType notificationType) {
        final Search notificationSearch = new Search(Notification.class);
        notificationSearch.addFilterEqual("notificationType", notificationType);
        return  generalService.search(notificationSearch);
    }
}
