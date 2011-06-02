/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.service.offer;

import cz.poptavka.sample.dao.offer.OfferDao;
import cz.poptavka.sample.domain.offer.Offer;
import cz.poptavka.sample.domain.offer.OfferState;
import cz.poptavka.sample.service.GenericServiceImpl;
import cz.poptavka.sample.service.register.RegisterService;
import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import java.util.List;

/**
 *
 * @author ivan.vlcek
 */
public class OfferServiceImpl extends GenericServiceImpl<Offer, OfferDao> implements OfferService {

    private RegisterService registerService;

    public OfferServiceImpl(OfferDao offerDao) {
        setDao(offerDao);
    }

    //---------------------------------- GETTERS AND SETTERS -----------------------------------------------------------
    public void setRegisterService(RegisterService registerService) {
        this.registerService = registerService;
    }

    //----------------------------------  Methods for DemandType-s -----------------------------------------------------
    @Override
    public List<OfferState> getOfferStates() {
        return this.registerService.getAllValues(OfferState.class);
    }

    @Override
    public OfferState getOfferState(String code) {
        Preconditions.checkArgument(StringUtils.isNotBlank(code), "Code for offer state is empty!");
        return this.registerService.getValue(code, OfferState.class);
    }
}
