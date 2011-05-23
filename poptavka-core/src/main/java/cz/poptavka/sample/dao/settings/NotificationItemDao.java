/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.dao.settings;

import cz.poptavka.sample.dao.GenericDao;
import cz.poptavka.sample.domain.settings.NotificationItem;
import cz.poptavka.sample.domain.user.User;
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
