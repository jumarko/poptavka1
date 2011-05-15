package cz.poptavka.sample.service.user;

import com.google.common.base.Preconditions;
import cz.poptavka.sample.dao.user.BusinessUserRoleDao;
import cz.poptavka.sample.domain.user.BusinessUser;
import cz.poptavka.sample.domain.user.BusinessUserRole;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.GenericServiceImpl;
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
