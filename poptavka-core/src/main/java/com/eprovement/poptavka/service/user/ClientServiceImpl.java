/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eprovement.poptavka.service.user;

import com.eprovement.poptavka.domain.enums.CommonAccessRoles;
import com.eprovement.poptavka.domain.user.rights.AccessRole;
import com.google.common.base.Preconditions;
import com.googlecode.ehcache.annotations.Cacheable;
import com.eprovement.poptavka.dao.user.ClientDao;
import com.eprovement.poptavka.domain.register.Registers;
import com.eprovement.poptavka.domain.settings.NotificationItem;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.register.RegisterService;
import com.eprovement.poptavka.util.notification.NotificationUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;

/**
 * @author Excalibur
 * @author Juraj Martinka
 */
public class ClientServiceImpl extends BusinessUserRoleServiceImpl<Client, ClientDao> implements ClientService {

    private final NotificationUtils notificationUtils;

    public ClientServiceImpl(GeneralService generalService, RegisterService registerService,
            BusinessUserVerificationService userVerificationService) {
        super(generalService, registerService, userVerificationService);
        this.notificationUtils = new NotificationUtils(registerService);
    }

    @Cacheable(cacheName = "cache5min")
    public Client getCachedClientById(long id) {
        return searchById(id);
    }

    @Override
    public Client create(Client businessUserRole) {
        /** Set service for new client **/

        // TODO ivlcek - nastavit datum vytvorenia UserService aby sme mohli
        // objednannu service zrusit ak client neaktivuje svoj ucet do 14 dni
        // budeme to ziskavat cez AUD entitu alebo novy atribut. AUD entitu pre
        // UserService nemame a je urcite nutna

        /** Notifications for new client. **/
        createDefaultNotifications(businessUserRole);
        createDefaultAccessRole(businessUserRole);

        /** TODO ivlcek - email activation. **/

        return super.create(businessUserRole);
    }

    private void createDefaultAccessRole(Client businessUserRole) {
        Validate.notNull(businessUserRole);
        Preconditions.checkNotNull(businessUserRole.getBusinessUser(), "Client.businessUser must not be null!");
        if (CollectionUtils.isEmpty(businessUserRole.getBusinessUser().getAccessRoles())) {
            businessUserRole.getBusinessUser().setAccessRoles(Collections.singletonList(
                    getRegisterService().getValue(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE, AccessRole.class)));
        }

    }

    private void createDefaultNotifications(Client businessUserRole) {
        final List<NotificationItem> notificationItems = new ArrayList<NotificationItem>();
        notificationItems.add(
                this.notificationUtils.createInstantNotificationItem(Registers.Notification.CLIENT_NEW_MESSAGE, true));
        notificationItems.add(
                this.notificationUtils.createInstantNotificationItem(Registers.Notification.CLIENT_NEW_OPERATOR, true));
        notificationItems.add(
                this.notificationUtils.createInstantNotificationItem(Registers.Notification.CLIENT_NEW_INFO, false));
        notificationItems.add(
                this.notificationUtils.createInstantNotificationItem(Registers.Notification.CLIENT_NEW_OFFER, false));
        notificationItems.add(
                this.notificationUtils.createInstantNotificationItem(
                        Registers.Notification.CLIENT_DEMAND_STATUS_CHANGED, false));

        businessUserRole.getBusinessUser().getSettings().setNotificationItems(notificationItems);
    }

}
