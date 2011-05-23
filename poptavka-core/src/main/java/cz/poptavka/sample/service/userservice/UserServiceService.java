/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.service.userservice;

import cz.poptavka.sample.dao.userservice.UserServiceDao;
import cz.poptavka.sample.domain.product.UserService;
import cz.poptavka.sample.domain.user.BusinessUser;
import cz.poptavka.sample.service.GenericService;
import java.util.List;
/**
 *
 * @author ivan.vlcek
 */
public interface UserServiceService extends GenericService<UserService, UserServiceDao> {

    /**
     * Get all services that were ever associated with particular <code>businessUser</code>
     *
     * @param businessUser
     * @return all userServices of given businessUser
     * @see UserService
     */
    List<UserService> getUsersServices(BusinessUser businessUser);
}
