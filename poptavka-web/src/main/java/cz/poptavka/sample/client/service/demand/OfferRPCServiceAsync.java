package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.demand.OfferDemandDetail;
import cz.poptavka.sample.shared.domain.offer.FullOfferDetail;

public interface OfferRPCServiceAsync {

    void getClientDemands(long clientId, AsyncCallback<ArrayList<OfferDemandDetail>> callback);

    void getDemandOffers(long demandId, long threadRootId, AsyncCallback<ArrayList<OfferDetail>> callback);

    void changeOfferState(OfferDetail offerDetail, AsyncCallback<OfferDetail> callback);

    void updateOffer(FullOfferDetail offer, AsyncCallback<FullOfferDetail> asyncCallback);
}
