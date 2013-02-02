/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.dao.offer;

import com.eprovement.poptavka.dao.GenericDao;
import com.eprovement.poptavka.domain.offer.Offer;
import com.eprovement.poptavka.domain.offer.OfferState;

/**
 *
 * @author ivan.vlcek
 */
public interface OfferDao extends GenericDao<Offer> {

    /**
     * Gets a count of supplier's offers of given OfferState.
     * @param supplierId
     * @param offerState - state of offer
     * @return count of supplier's offers
     */
    Long getOffersCountForSupplier(long supplierId, OfferState offerState);

    /**
     * Gets a count of supplier's offers of given OfferStates.
     * @param supplierId
     * @param offerState1 - state of offer
     * @param offerState2 - state of offer
     * @return count of supplier's offers
     */
    Long getOffersCountForSupplier(long supplierId, OfferState offerState1, OfferState offerState2);

}
