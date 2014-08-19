/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.service.userservice;

import com.eprovement.poptavka.dao.userservice.UserServiceDao;
import com.eprovement.poptavka.domain.product.UserService;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.service.GenericService;
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

    /**
     * Adds credits to user's service.
     *
     * @param userServiceId the user's service ID
     * @param newCredits credits to be added
     */
    void addCredits(long userServiceId, int newCredits);

    /**
     * Checks whether <code>BusinessUser</code> already had promo service. For example, we don't
     * want to give FREE service two times for the same <code>BusinessUser</code>.
     *
     * @param businessUser the services of which to be checked
     * @param serviceCode the service code to be checked
     * @return true if <code>BusinessUser</code> already had this promo service
     */
    boolean hasUserHadPromoService(final BusinessUser businessUser, final String serviceCode);

    /**
     * Returns a list of PROMOTION i.e FREE <code>UserService</code>-s for given BusinessUser.
     * @param businessUser
     * @return list of user services with type promotion
     */
    List<UserService> getPromotionUserServices(final BusinessUser businessUser);
    UserService getUserServiceByOrderNumber(long orderNumber);
}
