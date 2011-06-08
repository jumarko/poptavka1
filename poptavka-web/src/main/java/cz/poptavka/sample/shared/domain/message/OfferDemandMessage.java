package cz.poptavka.sample.shared.domain.message;

public interface OfferDemandMessage extends PotentialDemandMessage {

    void setOfferCount(int offerCount);
    int getOfferCount();

    void setMaxOfferCount(int maxOfferCount);
    int getMaxOfferCount();
}
