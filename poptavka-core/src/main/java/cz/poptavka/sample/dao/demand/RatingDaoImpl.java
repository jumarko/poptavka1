/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.dao.demand;

import cz.poptavka.sample.dao.GenericHibernateDao;
import cz.poptavka.sample.domain.demand.Rating;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.domain.user.Supplier;

import java.util.HashMap;

/**
 *
 * @author Vojtech Hubr
 */
public class RatingDaoImpl extends GenericHibernateDao<Rating>
        implements RatingDao {

    /** {@inheritDoc} */
    @Override
    public int getAvgRating(Supplier supplier) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("supplier", supplier);
        Object result = runNamedQueryForSingleResult("getAvgRatingForSupplier",
                queryParams);
        return (int) Math.round((Double) result);
    }

    /** {@inheritDoc} */
    @Override
    public int getAvgRating(Client client) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("client", client);
        Object result = runNamedQueryForSingleResult("getAvgRatingForClient",
                queryParams);
        return (int) Math.round((Double) result);
    }
}
