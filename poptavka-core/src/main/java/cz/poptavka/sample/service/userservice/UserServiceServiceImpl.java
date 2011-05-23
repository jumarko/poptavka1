/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.service.userservice;

import cz.poptavka.sample.dao.userservice.UserServiceDao;
import cz.poptavka.sample.domain.product.UserService;
import cz.poptavka.sample.domain.user.BusinessUser;
import cz.poptavka.sample.service.GenericServiceImpl;
import java.util.List;

/**
 *
 * @author ivan.vlcek
 */
public class UserServiceServiceImpl extends GenericServiceImpl<UserService, UserServiceDao>
        implements UserServiceService {

    @Override
    public List<UserService> getUsersServices(BusinessUser businessUser) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
