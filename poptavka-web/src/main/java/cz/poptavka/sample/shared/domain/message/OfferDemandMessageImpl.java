package cz.poptavka.sample.shared.domain.message;

import cz.poptavka.sample.domain.message.UserMessage;

public class OfferDemandMessageImpl extends PotentialDemandMessageImpl implements OfferDemandMessage {

    /**
     *
     */
    private static final long serialVersionUID = 2349565802701324033L;
    private int offerCount;
    private int maxOfferCount;
    private String demandTitle;

    public static OfferDemandMessage createMessageDetail(UserMessage message) {
        OfferDemandMessage detail = new OfferDemandMessageImpl();
        detail = (OfferDemandMessage) PotentialDemandMessageImpl.fillMessageDetail(detail, message);
        detail.setDemandTitle(message.getMessage().getDemand().getTitle());
        detail.setOfferCount(message.getMessage().getDemand().getOffers().size());
        detail.setMaxOfferCount(message.getMessage().getDemand().getMaxSuppliers());
        return detail;
    }

    @Override
    public void setOfferCount(int offerCount) {
        this.offerCount = offerCount;
    }

    @Override
    public int getOfferCount() {
        return offerCount;
    }

    @Override
    public void setMaxOfferCount(int maxOfferCount) {
        this.maxOfferCount = maxOfferCount;
    }

    @Override
    public int getMaxOfferCount() {
        return maxOfferCount;
    }

    @Override
    public String toString() {
        return super.toString()
            + "Client Offer Demand Message:"
            + "\n offerCount=" + offerCount
            + "\n maxOfferCount=" + maxOfferCount;
    }

    @Override
    public void setDemandTitle(String demandTitle) {
        this.demandTitle = demandTitle;
    }

    @Override
    public String getDemandTitle() {
        return demandTitle;
    }

}
