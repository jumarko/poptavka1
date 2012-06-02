package com.eprovement.poptavka.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.demand.OfferDemandDetail;
import com.eprovement.poptavka.shared.domain.offer.FullOfferDetail;

public interface OfferRPCServiceAsync {

    void getClientDemands(long clientId, AsyncCallback<ArrayList<OfferDemandDetail>> callback);

    void getDemandOffers(long demandId, long threadRootId, AsyncCallback<ArrayList<FullOfferDetail>> callback);

    void changeOfferState(OfferDetail offerDetail, AsyncCallback<OfferDetail> callback);

    void updateOffer(FullOfferDetail offer, AsyncCallback<FullOfferDetail> asyncCallback);
}
