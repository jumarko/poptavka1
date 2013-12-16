package com.eprovement.poptavka.service.user;

import com.eprovement.poptavka.domain.enums.CommonAccessRoles;
import com.eprovement.poptavka.domain.settings.Notification;
import com.eprovement.poptavka.domain.user.rights.AccessRole;
import com.eprovement.poptavka.service.notification.NotificationTypeService;
import com.eprovement.poptavka.dao.user.ClientDao;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.register.RegisterService;
import java.util.Arrays;
import java.util.List;

import com.googlecode.ehcache.annotations.Cacheable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Juraj Martinka
 */
public class ClientServiceImpl extends BusinessUserRoleServiceImpl<Client, ClientDao> implements ClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final List<AccessRole> clientAccessRoles;

    public ClientServiceImpl(GeneralService generalService, RegisterService registerService,
                             UserVerificationService userVerificationService,
                             NotificationTypeService notificationTypeService) {
        super(generalService, registerService, userVerificationService, notificationTypeService);
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

        // TODO LATER ivlcek - nastavit datum vytvorenia UserService aby sme mohli
        // objednannu service zrusit ak client neaktivuje svoj ucet do 14 dni
        // budeme to ziskavat cez AUD entitu alebo novy atribut. AUD entitu pre
        // UserService nemame a je urcite nutna


        final Client createdClient = super.create(businessUserRole);
        LOGGER.info("action=create_client status=finish client={}", createdClient);
        return createdClient;
    }

    @Override
    protected List<AccessRole> getDefaultAccessRoles() {
        return clientAccessRoles;
    }

    @Override
    protected List<Notification> getNotifications() {
        return notificationTypeService.getNotificationsForClient();
    }

}
