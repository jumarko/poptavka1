package cz.poptavka.sample.service.user;

import cz.poptavka.sample.dao.user.BusinessUserRoleDao;
import cz.poptavka.sample.domain.user.BusinessUserRole;
import cz.poptavka.sample.service.GenericService;

import java.util.List;

/**
 * Common interface for all interfaces which describe operations for BusinessUserRole-s, i.e. operations
 * for {@link cz.poptavka.sample.domain.user.Client}, {@link cz.poptavka.sample.domain.user.Supplier},
 * {@link cz.poptavka.sample.domain.user.Partner}, etc.
 * @author Juraj Martinka
 *         Date: 13.5.11
 */
public interface BusinessUserRoleService<BUR extends BusinessUserRole, BUDao extends BusinessUserRoleDao<BUR>>
        extends GenericService<BUR, BUDao> {
    List<BUR> searchByCriteria(UserSearchCriteria userSarchCritera);
}
