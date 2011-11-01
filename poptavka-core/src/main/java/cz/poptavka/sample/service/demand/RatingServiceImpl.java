package cz.poptavka.sample.service.demand;

import cz.poptavka.sample.dao.demand.RatingDao;
import cz.poptavka.sample.domain.demand.Rating;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.service.GenericServiceImpl;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Vojtech Hubr
 */
@Transactional
public class RatingServiceImpl extends GenericServiceImpl<Rating, RatingDao>
        implements RatingService {

    public RatingServiceImpl(RatingDao ratingDao) {
        setDao(ratingDao);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Integer getAvgRating(Supplier supplier) {
        if (supplier == null) {
            throw new IllegalArgumentException("Supplier mustn't be null.");
        }
        return this.getDao().getAvgRating(supplier);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Integer getAvgRating(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Client mustn't be null.");
        }
        return this.getDao().getAvgRating(client);
    }

}
