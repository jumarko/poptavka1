package cz.poptavka.sample.dao.user;

import cz.poptavka.sample.dao.GenericHibernateDao;
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
        final Criteria personCriteria = getHibernateSession().createCriteria(getPersistentClass())
                .createCriteria("businessUser")
                .createCriteria("businessUserData");
        personCriteria.add(Example.create(
                new BusinessUserData.Builder()
                        .personFirstName(userSearchCritera.getName())
                        .personLastName(userSearchCritera.getSurName())
                        .companyName(userSearchCritera.getCompanyName())
                        .build()));
        return personCriteria.list();
    }



}
