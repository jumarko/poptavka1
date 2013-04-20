package com.eprovement.poptavka.dao.user;

import com.eprovement.poptavka.dao.GenericHibernateDao;
import com.eprovement.poptavka.domain.user.BusinessUserRole;
import com.eprovement.poptavka.service.user.UserSearchCriteria;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 14.5.11
 */
public abstract class BusinessUserRoleDaoImpl<BUR extends BusinessUserRole> extends GenericHibernateDao<BUR>
        implements BusinessUserRoleDao<BUR> {

    @Override
    public List<BUR> searchByCriteria(UserSearchCriteria userSearchCritera) {
        final Criteria businessUserCriteria = getHibernateSession().createCriteria(getPersistentClass())
                .createCriteria("businessUser");
        addRestrictionIfValueNotNull(businessUserCriteria, "email", userSearchCritera.getEmail());

        final Criteria businessUserDataCriteria = businessUserCriteria.createCriteria("businessUserData");
        addRestrictionIfValueNotNull(businessUserDataCriteria, "personFirstName", userSearchCritera.getName());
        addRestrictionIfValueNotNull(businessUserDataCriteria, "personLastName", userSearchCritera.getSurName());
        addRestrictionIfValueNotNull(businessUserDataCriteria, "companyName", userSearchCritera.getCompanyName());

        return businessUserDataCriteria.list();
    }

    /**
     * Applies given restriction that {@code property} has to have given {@code value} but only if
     * {@code value} is different from null. This way we can allow user to omit some properties if he does not want
     * to use them for filtering.
     */
    private void addRestrictionIfValueNotNull(Criteria businessUserCriteria, String property, String value) {
        if (value != null) {
            businessUserCriteria.add(Restrictions.eq(property, value));
        }
    }
}
