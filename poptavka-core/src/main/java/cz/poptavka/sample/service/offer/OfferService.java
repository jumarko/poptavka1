/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.service.offer;

import cz.poptavka.sample.dao.offer.OfferDao;
import cz.poptavka.sample.domain.offer.Offer;
import cz.poptavka.sample.domain.offer.OfferState;
import cz.poptavka.sample.service.GenericService;
import java.util.List;

/**
 *
 * @author ivan.vlcek
 */
public interface OfferService extends GenericService<Offer, OfferDao> {

    List<OfferState> getOfferStates();

    OfferState getOfferState(String code);
}
