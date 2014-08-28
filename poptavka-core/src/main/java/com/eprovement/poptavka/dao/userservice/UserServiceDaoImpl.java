/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.dao.userservice;

import com.eprovement.poptavka.dao.GenericHibernateDao;
import com.eprovement.poptavka.domain.product.UserService;
import com.eprovement.poptavka.domain.user.BusinessUser;

import java.util.List;

import org.hibernate.criterion.Restrictions;

/**
 *
 * @author ivan.vlcek
 */
public class UserServiceDaoImpl extends GenericHibernateDao<UserService> implements UserServiceDao {

    /**
     * Get all services that were ever associated with particular <code>businessUser</code>
     *
     * @param businessUser
     * @return all userServices of given businessUser
     * @see UserService
     */
    @Override
    public List<UserService> getUsersServices(BusinessUser businessUser) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public UserService getUserServiceByOrderNumber(Long orderNumber) {
        return (UserService) getHibernateSession().createCriteria(UserService.class)
                .add(Restrictions.eq("orderNumber", orderNumber))
                .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUniqueOrderNumber(long userServiceId) {
        em().createNativeQuery("UPDATE UserService as us1 \n"
            + " INNER JOIN (SELECT IFNULL(MAX(orderNumber), 0) + 1 max FROM UserService) as us3 \n"
            + " SET us1.orderNumber = us3.max \n"
            + " WHERE us1.id = :id")
            .setParameter("id", userServiceId).
            executeUpdate();
    }
}
