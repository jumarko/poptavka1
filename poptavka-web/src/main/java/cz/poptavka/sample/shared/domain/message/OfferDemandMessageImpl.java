package cz.poptavka.sample.shared.domain.message;

import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.service.demand.DemandService;
import org.springframework.beans.factory.annotation.Autowired;

public class OfferDemandMessageImpl extends PotentialDemandMessageImpl implements OfferDemandMessage {

    /**
     *
     */
    private static final long serialVersionUID = 2349565802701324033L;
    private int offerCount;
    private int maxOfferCount;

    private static DemandService demandService;

    @Autowired
    public void setDemandService(DemandService demandService) {
        OfferDemandMessageImpl.demandService = demandService;
    }

    public static OfferDemandMessageImpl createMessageDetail(UserMessage message) {
        OfferDemandMessageImpl detail = new OfferDemandMessageImpl();
        detail = (OfferDemandMessageImpl) MessageDetailImpl.fillMessageDetail(detail, message.getMessage());
        detail.setOfferCount((int) demandService.getOfferCount(message.getMessage().getDemand()));
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

}
