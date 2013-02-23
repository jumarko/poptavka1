package com.eprovement.poptavka.shared.domain.message;

import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.Date;


public class OfferMessageDetail extends DemandMessageDetail implements IsSerializable {
    private long supplierId;
    private long offerId;
    private String supplierName;
    private OfferStateType offerState;
    private Date offerFinishDate;


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

    /**
     * @return the offerFinishDate
     */
    public Date getOfferFinishDate() {
        return offerFinishDate;
    }

    /**
     * @param offerFinishDate the offerFinishDate to set
     */
    public void setOfferFinishDate(Date offerFinishDate) {
        this.offerFinishDate = offerFinishDate;
    }
}
