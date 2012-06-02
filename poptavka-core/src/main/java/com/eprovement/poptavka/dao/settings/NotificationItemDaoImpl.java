/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.dao.settings;

import com.eprovement.poptavka.dao.GenericHibernateDao;
import com.eprovement.poptavka.domain.settings.NotificationItem;
import com.eprovement.poptavka.domain.user.User;
import java.util.List;

/**
 *
 * @author ivan.vlcek
 */
public class NotificationItemDaoImpl extends GenericHibernateDao<NotificationItem>
        implements NotificationItemDao {

    /**
     * Get all enabled notifications items associated with particular <code>user</code>
     *
     * @param user
     * @return all notificationItems of given user
     * @see User, NotificationItem, Notification
     */
    @Override
    public List<NotificationItem> getUsersEnabledNotification(User user) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
