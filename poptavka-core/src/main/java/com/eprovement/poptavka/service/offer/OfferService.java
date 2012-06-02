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
}
