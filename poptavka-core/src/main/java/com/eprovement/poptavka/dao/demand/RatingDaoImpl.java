/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eprovement.poptavka.dao.demand;

import com.eprovement.poptavka.dao.GenericHibernateDao;
import com.eprovement.poptavka.domain.demand.Rating;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;

import java.util.HashMap;

/**
 *
 * @author Vojtech Hubr
 */
public class RatingDaoImpl extends GenericHibernateDao<Rating>
        implements RatingDao {

    /** {@inheritDoc} */
    @Override
    public Integer getAvgRating(Supplier supplier) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("supplier", supplier);
        Object result = runNamedQueryForSingleResult("getAvgRatingForSupplier",
                queryParams);
        if (result == null) {
            return null;
        } else {
            return (int) Math.round((Double) result);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Integer getAvgRating(Client client) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("client", client);
        Object result = runNamedQueryForSingleResult("getAvgRatingForClient",
                queryParams);
        if (result == null) {
            return null;
        } else {
            return (int) Math.round((Double) result);
        }
    }
}
