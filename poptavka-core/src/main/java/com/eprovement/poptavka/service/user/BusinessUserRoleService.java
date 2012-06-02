package com.eprovement.poptavka.service.user;

import com.eprovement.poptavka.dao.user.BusinessUserRoleDao;
import com.eprovement.poptavka.domain.user.BusinessUserRole;
import com.eprovement.poptavka.service.GenericService;

import java.util.List;

/**
 * Common interface for all interfaces which describe operations for BusinessUserRole-s, i.e. operations
 * for {@link com.eprovement.poptavka.domain.user.Client}, {@link com.eprovement.poptavka.domain.user.Supplier},
 * {@link com.eprovement.poptavka.domain.user.Partner}, etc.
 * @author Juraj Martinka
 *         Date: 13.5.11
 */
public interface BusinessUserRoleService<BUR extends BusinessUserRole, BUDao extends BusinessUserRoleDao<BUR>>
        extends GenericService<BUR, BUDao> {
    List<BUR> searchByCriteria(UserSearchCriteria userSarchCritera);


    boolean checkFreeEmail(String email);

}
