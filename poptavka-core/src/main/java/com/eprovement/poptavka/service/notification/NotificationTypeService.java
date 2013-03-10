/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.service.notification;

import com.eprovement.poptavka.domain.settings.Notification;
import java.util.List;

public interface NotificationTypeService {
    /**
     * @return all notifications that could be associated with any {@link User}
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
}
