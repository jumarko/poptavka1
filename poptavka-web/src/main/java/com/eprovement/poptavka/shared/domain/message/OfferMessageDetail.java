package com.eprovement.poptavka.shared.domain.message;

import com.eprovement.poptavka.domain.enums.OfferStateType;
import java.io.Serializable;


public class OfferMessageDetail extends DemandMessageDetail
        implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 6942849643003699484L;
    private long supplierId;
    private long offerId;
    private String supplierName;
    private OfferStateType offerState;


    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setOfferId(long offerId) {
        this.offerId = offerId;
    }

    public long getOfferId() {
        return offerId;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setOfferState(OfferStateType offerState) {
        this.offerState = offerState;
    }

    public String getOfferState() {
        return offerState.getValue();
    }
}
