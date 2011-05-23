/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.dao.userservice;

import cz.poptavka.sample.dao.GenericHibernateDao;
import cz.poptavka.sample.domain.product.UserService;
import cz.poptavka.sample.domain.user.BusinessUser;
import java.util.List;

/**
 *
 * @author ivan.vlcek
 */
public class UserServiceDaoImpl extends GenericHibernateDao<UserService> implements UserServiceDao {

    @Override
    public List<UserService> getUsersServices(BusinessUser businessUser) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
