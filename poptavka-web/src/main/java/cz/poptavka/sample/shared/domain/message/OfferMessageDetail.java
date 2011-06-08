package cz.poptavka.sample.shared.domain.message;

import cz.poptavka.sample.shared.domain.type.OfferStateType;

public interface OfferMessageDetail extends PotentialDemandMessage {

    void setSupplierId(long supplierId);
    long getSupplierId();

    void setOfferId(long offerId);
    long getOfferId();

    void setSupplierName(String supplierName);
    String getSupplierName();

    void setOfferState(OfferStateType offerState);
    String getOfferState();

}
