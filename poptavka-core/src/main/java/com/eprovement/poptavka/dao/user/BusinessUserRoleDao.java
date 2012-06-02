package com.eprovement.poptavka.dao.user;

import com.eprovement.poptavka.dao.GenericDao;
import com.eprovement.poptavka.domain.user.BusinessUserRole;
import com.eprovement.poptavka.service.user.UserSearchCriteria;

import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 13.5.11
 */
public interface BusinessUserRoleDao<BUR extends BusinessUserRole> extends GenericDao<BUR> {

    List<BUR> searchByCriteria(UserSearchCriteria userSarchCritera);
}
