package com.eprovement.poptavka.service.user;

import com.eprovement.poptavka.dao.user.BusinessUserRoleDao;
import com.eprovement.poptavka.domain.common.Origin;
import com.eprovement.poptavka.domain.enums.Period;
import com.eprovement.poptavka.domain.enums.Status;
import com.eprovement.poptavka.domain.enums.Verification;
import com.eprovement.poptavka.domain.product.Service;
import com.eprovement.poptavka.domain.product.UserService;
import com.eprovement.poptavka.domain.register.Registers;
import com.eprovement.poptavka.domain.settings.Notification;
import com.eprovement.poptavka.domain.settings.NotificationItem;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.BusinessUserRole;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.domain.user.rights.AccessRole;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.GenericServiceImpl;
import com.eprovement.poptavka.service.notification.NotificationTypeService;
import com.eprovement.poptavka.service.register.RegisterService;
import com.eprovement.poptavka.util.notification.NotificationUtils;
import com.google.common.base.Preconditions;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.collections.CollectionUtils;
import static org.apache.commons.lang.Validate.notNull;
import static org.apache.commons.lang.Validate.notEmpty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Common ancestor for all implementations of service methods for {@link BusinessUserRole}-s.
 * <p/>
 * Provides customized implementations of common methods, such as
 * {@link #create(com.eprovement.poptavka.domain.user.BusinessUserRole)} which is (in normal case)
 * directly used from {@link com.eprovement.poptavka.service.GeneralServiceImpl}, but it is necessary to modify it
 * for {@link BusinessUserRole}-s -> see {@link #create(com.eprovement.poptavka.domain.user.BusinessUserRole)}
 * <p/>
 * <p>Examples of child implementations:
 *
 * @author Juraj Martinka
 *         Date: 14.5.11
 * @see ClientServiceImpl
 * @see SupplierServiceImpl
 */
public abstract class BusinessUserRoleServiceImpl<BUR extends BusinessUserRole, BURDao extends BusinessUserRoleDao<BUR>>
    extends GenericServiceImpl<BUR, BURDao>
    implements BusinessUserRoleService<BUR, BURDao> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessUserRoleServiceImpl.class);

    protected final GeneralService generalService;
    private final RegisterService registerService;
    private final NotificationUtils notificationUtils;
    protected final NotificationTypeService notificationTypeService;
    private final Class<? extends BusinessUserRole> businessUserRoleClass;

    private UserVerificationService userVerificationService;

    public BusinessUserRoleServiceImpl(Class<? extends BusinessUserRole> businessUserRoleClass,
        GeneralService generalService, RegisterService registerService,
        UserVerificationService userVerificationService, NotificationTypeService notificationTypeService) {
        notNull(businessUserRoleClass, "businessUserRoleClass cannot be null");
        notNull(generalService, "generalService cannot be null");
        notNull(registerService, "registerService cannot be null");
        notNull(userVerificationService, "userVerificationService cannot be null!");
        notNull(notificationTypeService, "notificationTypeService cannot be null!");

        this.businessUserRoleClass = businessUserRoleClass;
        this.generalService = generalService;
        this.registerService = registerService;
        this.userVerificationService = userVerificationService;
        this.notificationUtils = new NotificationUtils(registerService);
        this.notificationTypeService = notificationTypeService;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<BUR> searchByCriteria(UserSearchCriteria userSarchCritera) {
        return getDao().searchByCriteria(userSarchCritera);
    }

    /**
     * Create s new instance of this business user role - concrete instance type is as specified by generic type
     * BUR.
     * <p/>
     * Each business user role (e.g. client, supplier or partner) is connected to the particular business user.
     * Therefore instance of {@link BusinessUser} must be create before {@link BusinessUserRole} itself.
     * If new {@link BusinessUserRole} should be assigned to the existing {@link BusinessUser} then that
     * {@link BusinessUser} must be explicitly set to <code>businessUserRole</code> object.
     *
     * @param businessUserRole business user role to be created
     * @return business user role which has been created
     */
    @Override
    @Transactional
    public BUR create(BUR businessUserRole) {
        Preconditions.checkNotNull(businessUserRole, "Null client cannot be created.");

        LOGGER.info("action=create_new_business_user_role status=start businuessUser={}",
            businessUserRole.getBusinessUser());

        createDefaultNotifications(businessUserRole, false);
        createDefaultAccessRole(businessUserRole);

        // set default service if no service provided from frontend when creating new business user
        if (businessUserRole.getBusinessUser().getUserServices() == null
            || businessUserRole.getBusinessUser().getUserServices().isEmpty()) {
            final UserService classicClient = new UserService();
            classicClient.setBusinessUser(businessUserRole.getBusinessUser());
            classicClient.setService(this.registerService.getValue(Registers.Service.CLASSIC, Service.class));
            classicClient.setStatus(Status.INACTIVE);
            // no services have been assigned to the business user, it is safe to set completely new list
            businessUserRole.getBusinessUser().setUserServices(Arrays.asList(classicClient));
        }

        businessUserRole.getBusinessUser().getBusinessUserRoles().add(businessUserRole);

        createBusinessUserIfNotExist(businessUserRole);
        final BUR createdBusinessUserRole = super.create(businessUserRole);

        LOGGER.info("action=create_new_business_user_role status=finish businuessUser={}",
            businessUserRole.getBusinessUser());
        return createdBusinessUserRole;
    }

    /**
     * Checks if client with {@code email} already exists.
     *
     * @param email email to be checked
     * @return true if no client with given {@code email} has been already registered, false otherwise
     */
    @Override
    public boolean checkFreeEmail(String email) {
        notEmpty(email, "Empty email does not make sense)");
        final Search freeMailCheck = new Search(User.class);
        freeMailCheck.addFilterEqual("email", email);
        return getGeneralService().count(freeMailCheck) == 0;
    }

    /**
     * Finds business user by his email.
     *
     * @param email user's email
     * @return business user by specified email or null if no such user exists
     */
    @Override
    public BUR getByEmail(String email) {
        notEmpty(email, "Empty email does not make sense)");
        final Search getByEmail = new Search(businessUserRoleClass);
        getByEmail.addFilterEqual("businessUser.email", email);
        final List<BUR> users = getGeneralService().search(getByEmail);

        if (CollectionUtils.isEmpty(users)) {
            return null;
        }

        if (users.size() > 1) {
            throw new IllegalStateException("Multiple users with the same email=" + email + " exist! Cannot happen!");
        }

        return users.get(0);
    }

    protected GeneralService getGeneralService() {
        return generalService;
    }

    protected RegisterService getRegisterService() {
        return registerService;
    }

    /**
     * @return all access roles for concrete business user role
     */
    protected abstract List<AccessRole> getDefaultAccessRoles();

    /**
     * @return all notifications for concrete business user role
     * @see com.eprovement.poptavka.service.notification.NotificationTypeService
     */
    protected abstract List<Notification> getNotificationsWithDefaultPeriod();

    protected abstract Map<Notification, Period> getNotificationsWithCustomPeriod();

    /**
     * Loads welcome notification from DB that has the same code as {@code expectedWelcomeNotification}.
     * @param expectedWelcomeNotification represents notification which we want to load from DB, identified by code
     * @return notification from DB corresponding to the {@code expectedWelcomeNotification} (they have the same code).
     */
    protected final Notification getWelcomeNotification(Registers.Notification expectedWelcomeNotification) {
        final Notification notification
            = getRegisterService().getValue(expectedWelcomeNotification.getCode(), Notification.class);
        notNull(notification, "no notification corresponding to the " + expectedWelcomeNotification + " exists!");
        return notification;
    }

    //---------------------------------------------- HELPER METHODS ---------------------------------------------------
    private void createBusinessUserIfNotExist(BUR businessUserRole) {
        if (isNewBusinessUser(businessUserRole)) {
            final BusinessUser savedBusinessUserEntity = generalService.save(businessUserRole.getBusinessUser());
            businessUserRole.setBusinessUser(savedBusinessUserEntity);
            if (!savedBusinessUserEntity.isUserFromExternalSystem()) {
                // do not send activation email to users which has been automatically registered from external systems
                userVerificationService.sendNewActivationCodeAsync(savedBusinessUserEntity);
            } else {
                LOGGER.info("action=send_activation_email status=skip reason=external_user user={} ", businessUserRole);
            }
        }
    }

    private boolean isNewBusinessUser(BUR businessUserRole) {
        return businessUserRole.getBusinessUser().getId() == null;
    }

    /**
     * Creates default notifications both with default and custom period.
     * <b>Note</b>
     * Notice ignoreOrigin param. Since we want to manage this functionality at one place
     * we need some kind of mechanism to set non-external notifications to external user.
     * E.g. when external user first loggs in, he needs non-external notifications to be set,
     * but we want to hold the information about his origin (he is valuated as external).
     * @param businessUserRole
     * @param ignoreOrigin true to create notifications no matter the origin
     */
    public void createDefaultNotifications(BusinessUserRole businessUserRole, boolean ignoreOrigin) {
        final List<NotificationItem> notificationItems = new ArrayList<>();
        if (!ignoreOrigin && businessUserRole.getBusinessUser().isUserFromExternalSystem()) {
            // external users have only one type of notification since we do not want to send them other emails
            // until they get an offer and register themselves at our system as regular users
            notificationItems.add(notificationUtils.createNotificationItemWithDefaultPeriod(
                Registers.Notification.NEW_MESSAGE.getCode(), true));
        } else {
            for (Notification notification : getNotificationsWithDefaultPeriod()) {
                notificationItems.add(
                    notificationUtils.createNotificationItemWithDefaultPeriod(notification.getCode(), true));
            }
            for (Map.Entry<Notification, Period> customNotification : getNotificationsWithCustomPeriod().entrySet()) {
                notNull(customNotification, "notification should not be null");
                notificationItems.add(notificationUtils.createNotificationItem(customNotification.getKey().getCode(),
                    customNotification.getValue(), true));
            }
        }

        LOGGER.info("action=businessUserRole_create_default_notifications businessUserRole={} notifications={}",
            businessUserRole, notificationItems);

        businessUserRole.getBusinessUser().getSettings().addNotificationItems(notificationItems);
    }

    private void createDefaultAccessRole(BusinessUserRole businessUserRole) {
        notNull(businessUserRole);
        notNull(businessUserRole.getBusinessUser(), "Supplier.businessUser must not be null!");
        if (CollectionUtils.isEmpty(businessUserRole.getBusinessUser().getAccessRoles())) {
            final List<AccessRole> defaultAccessRoles = getDefaultAccessRoles();
            LOGGER.info("action=create_default_access_roles business_user_role={} roles={}",
                businessUserRole, defaultAccessRoles);
            businessUserRole.getBusinessUser().setAccessRoles(defaultAccessRoles);
        }
    }

    /**
     * Changes origin of given businessUserRole to given origin.
     * Notifications are accordingly updated as well.
     * @param businessUserRoleId defines user's to be updated
     * @param originId defines new origin
     */
    public void changeOrigin(long businessUserRoleId, long originId) {
        final BusinessUserRole user = generalService.find(BusinessUserRole.class, businessUserRoleId);
        final Origin origin = generalService.find(Origin.class, originId);

        user.getBusinessUser().setOrigin(origin);
        user.getBusinessUser().setVerification(Verification.UNVERIFIED);
        user.getBusinessUser().getSettings().getNotificationItems().clear();
        createDefaultNotifications(user, false);
        generalService.merge(user);
    }
}
