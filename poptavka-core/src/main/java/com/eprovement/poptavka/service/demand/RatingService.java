/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eprovement.poptavka.service.demand;

import com.eprovement.poptavka.dao.demand.RatingDao;
import com.eprovement.poptavka.domain.demand.Rating;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.service.GenericService;


/**
 * @author Vojtech Hubr
 */
public interface RatingService extends GenericService<Rating, RatingDao> {


    /**
     * Retrieves a supplier rating average, <code>null</code> if no
     * demans of the respective supplier have been rated
     *
     * @retun client's average rating, <code>null</code> if the client
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
