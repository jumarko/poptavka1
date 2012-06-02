/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.dao.settings;

import com.eprovement.poptavka.dao.GenericDao;
import com.eprovement.poptavka.domain.settings.NotificationItem;
import com.eprovement.poptavka.domain.user.User;
import java.util.List;

/**
 *
 * @author ivan.vlcek
 */
public interface NotificationItemDao extends GenericDao<NotificationItem> {

    /**
     * Get all enabled notifications items associated with particular <code>user</code>
     *
     * @param user
     * @return all notificationItems of given user
     * @see User, NotificationItem, Notification
     */
    List<NotificationItem> getUsersEnabledNotification(User user);
}
