package cz.poptavka.sample.service.user;

import com.google.common.base.Preconditions;
import cz.poptavka.sample.dao.user.BusinessUserRoleDao;
import cz.poptavka.sample.domain.user.BusinessUser;
import cz.poptavka.sample.domain.user.BusinessUserRole;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.GenericServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Common ancestor for all implementations of service methods for {@link BusinessUserRole}-s.
 * <p>
 *     Provides customized implementations of common methods, such as
 *     {@link #create(cz.poptavka.sample.domain.user.BusinessUserRole)} which is (in normal case)
 *     directly used from {@link cz.poptavka.sample.service.GeneralServiceImpl}, but it is necessary to modify it
 *     for {@link BusinessUserRole}-s -> see {@link #create(cz.poptavka.sample.domain.user.BusinessUserRole)}
 *
 * <p>Examples of child implementations:
 *     @see ClientServiceImpl
 *     @see SupplierServiceImpl
 * @author Juraj Martinka
 *         Date: 14.5.11
 */
public abstract class BusinessUserRoleServiceImpl<BUR extends BusinessUserRole, BURDao extends BusinessUserRoleDao<BUR>>
        extends GenericServiceImpl<BUR, BURDao>
        implements BusinessUserRoleService<BUR, BURDao> {

    private GeneralService generalService;

    public BusinessUserRoleServiceImpl(GeneralService generalService) {
        this.generalService = generalService;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<BUR> searchByCriteria(UserSearchCriteria userSarchCritera) {
        return getDao().searchByCriteria(userSarchCritera);
    }

    /**
     * Create s new instance of this business user role - concrete instance type is as specified by generic type
     * BUR.
     * <p>
     *     Each business user role (e.g. client, supplier or partner) is connected to the particular business user.
     *     Therefore instance of {@link BusinessUser} must be create before {@link BusinessUserRole} itself.
     *     If new {@link BusinessUserRole} should be assigned to the existing {@link BusinessUser} then that
     *     {@link BusinessUser} must be explicitly set to <code>businessUserRole</code> object.
     *
     * @param businessUserRole
     * @return
     */
    @Override
    @Transactional
    public BUR create(BUR businessUserRole) {
        Preconditions.checkArgument(businessUserRole != null, "Null client cannot be created.");
        createBusinessUserIfNotExist(businessUserRole);
        return super.create(businessUserRole);
    }

    /**
     * Checks whether given <code>businessUser</code> has role specified by <code>userRoleClass</code>.
     *
     * @param businessUser user which should be checked, can be null -> in that case, false is returned immediately.
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

    //---------------------------------------------- HELPER METHODS ---------------------------------------------------
    private void createBusinessUserIfNotExist(BUR businessUserRole) {
        if (isNewBusinessUser(businessUserRole)) {
            final BusinessUser savedBusinessUserEntity = generalService.save(businessUserRole.getBusinessUser());
            businessUserRole.setBusinessUser(savedBusinessUserEntity);
        }
    }

    private boolean isNewBusinessUser(BUR businessUserRole) {
        return businessUserRole.getBusinessUser().getId() == null;
    }
}
