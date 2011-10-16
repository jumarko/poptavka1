package cz.poptavka.sample.service.demand;

import cz.poptavka.sample.dao.demand.RatingDao;
import cz.poptavka.sample.domain.demand.Rating;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.service.GenericServiceImpl;

/**
 *
 * @author Vojtech Hubr
 */
public class RatingServiceImpl extends GenericServiceImpl<Rating, RatingDao>
        implements RatingService {

    public RatingServiceImpl(RatingDao ratingDao) {
        setDao(ratingDao);
    }

    /** {@inheritDoc} */
    @Override
    public int getAvgRating(Supplier supplier) {
        return this.getDao().getAvgRating(supplier);
    }

    /** {@inheritDoc} */
    @Override
    public int getAvgRating(Client client) {
        return this.getDao().getAvgRating(client);
    }

}
