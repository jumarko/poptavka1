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
    long getOffersCountForSupplier(long supplierId, OfferState offerState);
}
