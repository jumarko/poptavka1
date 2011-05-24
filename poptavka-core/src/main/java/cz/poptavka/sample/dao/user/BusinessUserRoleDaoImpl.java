package cz.poptavka.sample.dao.user;

import cz.poptavka.sample.dao.GenericHibernateDao;
import cz.poptavka.sample.domain.user.BusinessUser;
import cz.poptavka.sample.domain.user.BusinessUserData;
import cz.poptavka.sample.domain.user.BusinessUserRole;
import cz.poptavka.sample.service.user.UserSearchCriteria;
import org.hibernate.Criteria;
import org.hibernate.criterion.Example;

import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 14.5.11
 */
public abstract class BusinessUserRoleDaoImpl<BUR extends BusinessUserRole> extends GenericHibernateDao<BUR>
        implements BusinessUserRoleDao<BUR> {

    @Override
    public List<BUR> searchByCriteria(UserSearchCriteria userSearchCritera) {
        // query by example
        // criteria
        final Criteria businessUserCriteria = getHibernateSession().createCriteria(getPersistentClass())
                .createCriteria("businessUser")
                .add(Example.create(
                        new BusinessUser(userSearchCritera.getEmail(), userSearchCritera.getPassword())));

        final Criteria businessUserDataCriteria = businessUserCriteria.createCriteria("businessUserData");
        businessUserDataCriteria.add(Example.create(
                new BusinessUserData.Builder()
                        .personFirstName(userSearchCritera.getName())
                        .personLastName(userSearchCritera.getSurName())
                        .companyName(userSearchCritera.getCompanyName())
                        .build()));
        return businessUserDataCriteria.list();
    }



}
