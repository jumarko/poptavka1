package com.eprovement.poptavka.service.user;

import com.eprovement.poptavka.dao.user.BusinessUserRoleDao;
import com.eprovement.poptavka.domain.enums.Status;
import com.eprovement.poptavka.domain.enums.Verification;
import com.eprovement.poptavka.domain.product.Service;
import com.eprovement.poptavka.domain.product.UserService;
import com.eprovement.poptavka.domain.register.Registers;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.BusinessUserRole;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.GenericServiceImpl;
import com.eprovement.poptavka.service.register.RegisterService;
import com.google.common.base.Preconditions;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

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

    private final GeneralService generalService;
    private final RegisterService registerService;
    private UserVerificationService userVerificationService;

    public BusinessUserRoleServiceImpl(GeneralService generalService, RegisterService registerService,
            UserVerificationService userVerificationService) {
        Preconditions.checkNotNull(generalService);
        Preconditions.checkNotNull(registerService);
        Preconditions.checkNotNull(userVerificationService);

        this.generalService = generalService;
        this.registerService = registerService;
        this.userVerificationService = userVerificationService;
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
     * @param businessUserRole
     * @return
     */
    @Override
    @Transactional
    public BUR create(BUR businessUserRole) {
        Preconditions.checkNotNull(businessUserRole, "Null client cannot be created.");

        LOGGER.info("action=create_new_business_user_role status=start businuessUser={}",
                businessUserRole.getBusinessUser());
        // set common stuff when creating new business user
        final UserService classicClient = new UserService();
        classicClient.setUser(businessUserRole.getBusinessUser());
        classicClient.setService(this.registerService.getValue(Registers.Service.CLASSIC, Service.class));
        classicClient.setStatus(Status.INACTIVE);
        if (businessUserRole.getBusinessUser().getUserServices() == null) {
            // no services have been assigned to the business user, it is safe to set completely new list
            businessUserRole.getBusinessUser().setUserServices(Arrays.asList(classicClient));
        } else {
            // some services can be assigned to the user, instead of set new list of services, only add classic client
            businessUserRole.getBusinessUser().getUserServices().add(classicClient);
        }

        businessUserRole.getBusinessUser().getBusinessUserRoles().add(businessUserRole);
        businessUserRole.setVerification(Verification.UNVERIFIED);

        createBusinessUserIfNotExist(businessUserRole);
        final BUR createdBusinessUserRole = super.create(businessUserRole);

        LOGGER.info("action=create_new_business_user_role status=finish businuessUser={}",
                businessUserRole.getBusinessUser());
        return createdBusinessUserRole;
    }


    /**
     * Checks whether given <code>businessUser</code> has role specified by <code>userRoleClass</code>.
     *
     * @param businessUser  user which should be checked, can be null -> in that case, false is returned immediately.
     * @param userRoleClass
     * @return true  if given user has specified role, false otherwise.
     */
    public static boolean isUserAtRole(BusinessUser businessUser,
            final Class<? extends BusinessUserRole> userRoleClass) {
        if (businessUser == null) {
            return false;
        }
        List<BusinessUserRole> businessUserRoles = businessUser.getBusinessUserRoles();
        return CollectionUtils.exists(businessUserRoles, new Predicate() {

            @Override
            public boolean evaluate(Object object) {
                return object.getClass().equals(userRoleClass);
            }
        });
    }


    /**
     * Checks if client with {@code email} already exists.
     *
     * @param email
     * @return true if no client with given {@code email} has been already registered, false otherwise
     */
    @Override
    public boolean checkFreeEmail(String email) {
        Validate.notEmpty(email, "Empty email does not mail sense)");
        final Search freeMailCheck = new Search(User.class);
        freeMailCheck.addFilterEqual("email", email);
        final boolean isFree = getGeneralService().count(freeMailCheck) == 0;

        return isFree;
    }


    protected GeneralService getGeneralService() {
        return generalService;
    }

    protected RegisterService getRegisterService() {
        return registerService;
    }

    //---------------------------------------------- HELPER METHODS ---------------------------------------------------
    private void createBusinessUserIfNotExist(BUR businessUserRole) {
        if (isNewBusinessUser(businessUserRole)) {
            final BusinessUser savedBusinessUserEntity = generalService.save(businessUserRole.getBusinessUser());
            businessUserRole.setBusinessUser(savedBusinessUserEntity);

            // TODO RELEASE: move out of this class to the SupplierRPCService and ClientRPCService ??
            userVerificationService.sendNewActivationCodeAsync(savedBusinessUserEntity);
        }
    }

    private boolean isNewBusinessUser(BUR businessUserRole) {
        return businessUserRole.getBusinessUser().getId() == null;
    }
}
