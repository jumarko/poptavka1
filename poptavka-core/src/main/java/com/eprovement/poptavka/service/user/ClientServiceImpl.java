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
import java.util.Arrays;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Excalibur
 * @author Juraj Martinka
 */
public class ClientServiceImpl extends BusinessUserRoleServiceImpl<Client, ClientDao> implements ClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final NotificationUtils notificationUtils;
    private final List<AccessRole> clientAccessRoles;

    public ClientServiceImpl(GeneralService generalService, RegisterService registerService,
            BusinessUserVerificationService userVerificationService) {
        super(generalService, registerService, userVerificationService);
        this.notificationUtils = new NotificationUtils(registerService);
        this.clientAccessRoles = Arrays.asList(
                getRegisterService().getValue(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE, AccessRole.class),
                getRegisterService().getValue(CommonAccessRoles.USER_ACCESS_ROLE_CODE, AccessRole.class));

    }

    @Cacheable(cacheName = "cache5min")
    public Client getCachedClientById(long id) {
        return searchById(id);
    }

    @Override
    public Client create(Client businessUserRole) {
        /** Set service for new client **/

        LOGGER.info("action=create_client status=start client={}", businessUserRole);

        // TODO ivlcek - nastavit datum vytvorenia UserService aby sme mohli
        // objednannu service zrusit ak client neaktivuje svoj ucet do 14 dni
        // budeme to ziskavat cez AUD entitu alebo novy atribut. AUD entitu pre
        // UserService nemame a je urcite nutna

        /** Notifications for new client. **/
        createDefaultNotifications(businessUserRole);
        createDefaultAccessRole(businessUserRole);

        /** TODO ivlcek - email activation. **/

        final Client createdClient = super.create(businessUserRole);
        LOGGER.info("action=create_client status=finish client={}", createdClient);
        return createdClient;
    }

    private void createDefaultAccessRole(Client businessUserRole) {
        Validate.notNull(businessUserRole);
        Preconditions.checkNotNull(businessUserRole.getBusinessUser(), "Client.businessUser must not be null!");
        if (CollectionUtils.isEmpty(businessUserRole.getBusinessUser().getAccessRoles())) {
            LOGGER.info("action=client_create_default_access_roles client={} roles={}",
                    businessUserRole, clientAccessRoles);
            businessUserRole.getBusinessUser().setAccessRoles(clientAccessRoles);
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

        LOGGER.info("action=client_create_default_notifications client={} notifications={}",
                businessUserRole, notificationItems);
        businessUserRole.getBusinessUser().getSettings().setNotificationItems(notificationItems);
    }

}
