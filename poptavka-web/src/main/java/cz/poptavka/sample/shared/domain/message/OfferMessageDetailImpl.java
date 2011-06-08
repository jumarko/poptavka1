package cz.poptavka.sample.shared.domain.message;

import cz.poptavka.sample.shared.domain.type.MessageType;
import cz.poptavka.sample.shared.domain.type.OfferStateType;

public class OfferMessageDetailImpl extends PotentialDemandMessageImpl implements OfferMessageDetail {

    private long supplierId;
    private long offerId;
    private String supplierName;
    private OfferStateType offerState;

    @Override
    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    @Override
    public long getSupplierId() {
        return supplierId;
    }

    @Override
    public void setOfferId(long offerId) {
        this.offerId = offerId;
    }

    @Override
    public long getOfferId() {
        return offerId;
    }

    @Override
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @Override
    public String getSupplierName() {
        return supplierName;
    }

    @Override
    public void setOfferState(OfferStateType offerState) {
        this.offerState = offerState;
    }

    @Override
    public String getOfferState() {
        return offerState.getValue();
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.OFFER;
    }

}
