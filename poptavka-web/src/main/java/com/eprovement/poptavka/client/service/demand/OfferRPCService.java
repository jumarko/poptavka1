package com.eprovement.poptavka.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.demand.OfferDemandDetail;
import com.eprovement.poptavka.shared.domain.offer.FullOfferDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;

@RemoteServiceRelativePath(OfferRPCService.URL)
public interface OfferRPCService extends RemoteService {

    String URL = "service/offers";

    ArrayList<OfferDemandDetail> getClientDemands(long clientId) throws RPCException;

    ArrayList<FullOfferDetail> getDemandOffers(long demandId, long threadRootId) throws RPCException;

    OfferDetail changeOfferState(OfferDetail offerDetail) throws RPCException;

    FullOfferDetail updateOffer(FullOfferDetail newOffer) throws RPCException;
}
