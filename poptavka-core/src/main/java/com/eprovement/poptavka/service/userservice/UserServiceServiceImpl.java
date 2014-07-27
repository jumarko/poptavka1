/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.service.userservice;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.eprovement.poptavka.dao.userservice.UserServiceDao;
import com.eprovement.poptavka.domain.product.UserService;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.service.GenericServiceImpl;

/**
*
* @author ivan.vlcek
*/
public class UserServiceServiceImpl extends GenericServiceImpl<UserService, UserServiceDao> implements
        UserServiceService {

    public UserServiceServiceImpl(UserServiceDao userServiceDao) {
        setDao(userServiceDao);
    }

    @Override
    @Transactional(readOnly = false)
    public void addCredits(final long userServiceId, final int newCredits) {
        final UserService userService = getDao().findById(userServiceId);
        userService.getBusinessUser().getBusinessUserData().addCredits(newCredits);
        getDao().update(userService);
    }

    @Override
    public List<UserService> getUsersServices(BusinessUser businessUser) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
