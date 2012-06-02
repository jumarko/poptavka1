package com.eprovement.poptavka.service.demand;

import com.eprovement.poptavka.dao.demand.RatingDao;
import com.eprovement.poptavka.domain.demand.Rating;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.service.GenericServiceImpl;
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
