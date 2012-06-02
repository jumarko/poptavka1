/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.dao.userservice;

import com.eprovement.poptavka.dao.GenericDao;
import com.eprovement.poptavka.domain.product.UserService;
import com.eprovement.poptavka.domain.user.BusinessUser;
import java.util.List;

/**
 *
 * @author ivan.vlcek
 */
public interface UserServiceDao extends GenericDao<UserService> {

    /**
     * Get all services that were ever associated with particular <code>businessUser</code>
     *
     * @param businessUser
     * @return all userServices of given businessUser
     * @see UserService
     */
    List<UserService> getUsersServices(BusinessUser businessUser);

}
