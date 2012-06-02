/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.service.userservice;

import com.eprovement.poptavka.dao.userservice.UserServiceDao;
import com.eprovement.poptavka.domain.product.UserService;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.service.GenericServiceImpl;
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
