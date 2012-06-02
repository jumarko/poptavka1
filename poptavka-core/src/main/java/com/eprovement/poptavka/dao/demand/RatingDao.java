/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eprovement.poptavka.dao.demand;

import com.eprovement.poptavka.dao.GenericDao;
import com.eprovement.poptavka.domain.demand.Rating;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;

/**
 *
 * @author Excalibur
 */
public interface RatingDao extends GenericDao<Rating> {


     /**
     * Retrieves a supplier rating average, <code>null</code> if no
     * demans of the respective supplier have been rated
     *
     * @retun supplier's average rating, <code>null</code> if the client
     * hasn't been rated yet
     */
    Integer getAvgRating(Supplier supplier);

    /**
     * Retrieves a supplier rating average, <code>null</code> if no
     * demans of the respective supplier have been rated
     *
     * @retun supplier's average rating, <code>null</code> if the supplier
     * hasn't been rated yet
     */
    Integer getAvgRating(Client client);

}
