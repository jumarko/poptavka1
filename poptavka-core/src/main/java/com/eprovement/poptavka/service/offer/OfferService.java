/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.service.offer;

import com.eprovement.poptavka.dao.offer.OfferDao;
import com.eprovement.poptavka.domain.offer.Offer;
import com.eprovement.poptavka.domain.offer.OfferState;
import com.eprovement.poptavka.service.GenericService;
import java.util.List;

/**
 *
 * @author ivan.vlcek
 */
public interface OfferService extends GenericService<Offer, OfferDao> {

    List<OfferState> getOfferStates();

    OfferState getOfferState(String code);

    /**
     * Gets count of supplier's offers in state PENDING.
     * @param supplierId
     * @return count of PENDING offers
     */
    long getPendingOffersCountForSupplier(long supplierId);

    /**
     * Gets count of supplier's offers in state ACCEPTED.
     * @param supplierId
     * @return count of ACCEPTED offers
     */
    long getAcceptedOffersCountForSupplier(long supplierId);
}
