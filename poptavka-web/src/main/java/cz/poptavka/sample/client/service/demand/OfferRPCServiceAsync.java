package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.shared.domain.OfferDemandDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;

public interface OfferRPCServiceAsync {

    void getClientDemands(long clientId, AsyncCallback<ArrayList<OfferDemandDetail>> callback);

    void getDemandOffers(long demandId, AsyncCallback<ArrayList<OfferDetail>> callback);

}
