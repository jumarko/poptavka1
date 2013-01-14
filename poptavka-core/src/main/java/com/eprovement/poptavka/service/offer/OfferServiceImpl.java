/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.service.offer;

import com.eprovement.poptavka.dao.offer.OfferDao;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.eprovement.poptavka.domain.offer.Offer;
import com.eprovement.poptavka.domain.offer.OfferState;
import com.eprovement.poptavka.service.GenericServiceImpl;
import com.eprovement.poptavka.service.register.RegisterService;
import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

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

    //----------------------------------  Methods for OfferState-s -----------------------------------------------------
    @Override
    @Transactional(readOnly = true)
    public List<OfferState> getOfferStates() {
        return this.registerService.getAllValues(OfferState.class);
    }

    @Override
    @Transactional(readOnly = true)
    public OfferState getOfferState(String code) {
        Preconditions.checkArgument(StringUtils.isNotBlank(code), "Code for offer state is empty!");
        return this.registerService.getValue(code, OfferState.class);
    }

    //----------------------------------  Methods for count-s -----------------------------------------------------
    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public long getPendingOffersCountForSupplier(long supplierId) {
        return this.getDao().getOffersCountForSupplier(
                supplierId, this.getOfferState(OfferStateType.PENDING.getValue()));

    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public long getAcceptedOffersCountForSupplier(long supplierId) {
        return this.getDao().getOffersCountForSupplier(supplierId,
                this.getOfferState(OfferStateType.ACCEPTED.getValue()),
                this.getOfferState(OfferStateType.DELIVERED.getValue()));
    }
}
