/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.service.userservice;

import static org.apache.commons.lang.Validate.notNull;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

import com.eprovement.poptavka.dao.userservice.UserServiceDao;
import com.eprovement.poptavka.domain.enums.ServiceType;
import com.eprovement.poptavka.domain.product.UserService;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.GenericServiceImpl;
import com.googlecode.genericdao.search.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
*
* @author ivan.vlcek
*/
public class UserServiceServiceImpl extends GenericServiceImpl<UserService, UserServiceDao> implements
        UserServiceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceServiceImpl.class);
    private GeneralService generalService;

    public UserServiceServiceImpl(UserServiceDao userServiceDao, GeneralService generalService) {
        setDao(userServiceDao);
        this.generalService = generalService;
    }

    @Override
    @Transactional
    public UserService create(UserService entity) {
        super.create(entity); //To change body of generated methods, choose Tools | Templates.
        getDao().setUniqueOrderNumber(entity.getId()); //set unique order number
        refresh(entity); //refresh to load order number to returning entity
        return entity;
    }

    @Override
    @Transactional(readOnly = false)
    public void addCredits(final long userServiceId, final int newCredits) {
        final UserService userService = getDao().findById(userServiceId);
        userService.getBusinessUser().getBusinessUserData().addCredits(newCredits);
        getDao().update(userService);
    }

    @Override
    public UserService getUserServiceByOrderNumber(final long orderNumber) {
        return getDao().getUserServiceByOrderNumber(orderNumber);
    }

    @Override
    public List<UserService> getUsersServices(BusinessUser businessUser) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserService> getPromotionUserServices(final BusinessUser businessUser) {
        notNull(businessUser, "businessUser cannot be null");
        final Search freeUserServiceSearch = new Search(UserService.class);
        freeUserServiceSearch.addFilterEqual("businessUser", businessUser);
        freeUserServiceSearch.addFilterEqual("service.serviceType", ServiceType.PROMOTION);
        return this.generalService.search(freeUserServiceSearch);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasUserHadPromoService(final BusinessUser businessUser, final String serviceCode) {
        notNull(businessUser, "businessUser cannot be null");
        final Search freeUserServiceSearch = new Search(UserService.class);
        freeUserServiceSearch.addFilterEqual("businessUser", businessUser);
        freeUserServiceSearch.addFilterEqual("service.code", serviceCode);
        return !(this.generalService.search(freeUserServiceSearch)).isEmpty();
    }
}
