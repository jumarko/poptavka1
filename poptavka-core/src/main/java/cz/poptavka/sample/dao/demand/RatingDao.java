/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.dao.demand;

import cz.poptavka.sample.dao.GenericDao;
import cz.poptavka.sample.domain.demand.Rating;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.domain.user.Supplier;

/**
 *
 * @author Excalibur
 */
public interface RatingDao extends GenericDao<Rating> {


     /**
     * Retrieves a supplier rating average, <code>null</code> if no
     * demans of the respective supplier have been rated
     *
     * @retun supplier's average rating
     */
    int getAvgRating(Supplier supplier);

    /**
     * Retrieves a supplier rating average, <code>null</code> if no
     * demans of the respective supplier have been rated
     *
     * @retun supplier's average rating
     */
    int getAvgRating(Client client);

}
