/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.service.message.settings;

import cz.poptavka.sample.dao.settings.NotificationItemDao;
import cz.poptavka.sample.domain.settings.NotificationItem;
import cz.poptavka.sample.domain.user.User;
import cz.poptavka.sample.service.GenericService;
import java.util.List;

/**
 *
 * @author ivan.vlcek
 */
public interface NotificationItemService extends GenericService<NotificationItem, NotificationItemDao> {

    /**
     * Get all enabled notifications items associated with particular <code>user</code>
     *
     * @param user
     * @return all notificationItems of given user
     * @see User, NotificationItem, Notification
     */
    List<NotificationItem> getUsersEnabledNotification(User user);
}
