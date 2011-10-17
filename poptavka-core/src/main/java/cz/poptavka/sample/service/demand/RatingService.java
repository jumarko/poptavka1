/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.service.demand;

import cz.poptavka.sample.dao.demand.RatingDao;
import cz.poptavka.sample.domain.demand.Rating;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.service.GenericService;


/**
 * @author Vojtech Hubr
 */
public interface RatingService extends GenericService<Rating, RatingDao> {


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
