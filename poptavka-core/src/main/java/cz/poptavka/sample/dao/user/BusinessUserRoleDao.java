package cz.poptavka.sample.dao.user;

import cz.poptavka.sample.dao.GenericDao;
import cz.poptavka.sample.domain.user.BusinessUserRole;
import cz.poptavka.sample.service.user.UserSearchCriteria;

import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 13.5.11
 */
public interface BusinessUserRoleDao<BUR extends BusinessUserRole> extends GenericDao<BUR> {

    List<BUR> searchByCriteria(UserSearchCriteria userSarchCritera);
}
