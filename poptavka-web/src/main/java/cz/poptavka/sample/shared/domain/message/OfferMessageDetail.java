package cz.poptavka.sample.shared.domain.message;

import java.io.Serializable;

import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.shared.domain.type.OfferStateType;

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

    public static OfferMessageDetail createMessageDetail(
            UserMessage userMessage) {
        return fillMessageDetail(new OfferMessageDetail(), userMessage);
    }

    public static OfferMessageDetail fillMessageDetail(OfferMessageDetail detail,
                    UserMessage userMessage) {
        DemandMessageDetail.fillMessageDetail(detail, userMessage);
        return detail;
    }

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
