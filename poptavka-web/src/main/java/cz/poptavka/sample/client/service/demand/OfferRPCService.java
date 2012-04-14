package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.shared.domain.adminModule.OfferDetail;
import cz.poptavka.sample.shared.domain.demand.OfferDemandDetail;
import cz.poptavka.sample.shared.domain.offer.FullOfferDetail;
import cz.poptavka.sample.shared.exceptions.CommonException;

@RemoteServiceRelativePath("service/offers")
public interface OfferRPCService extends RemoteService {

    ArrayList<OfferDemandDetail> getClientDemands(long clientId) throws CommonException;

    ArrayList<FullOfferDetail> getDemandOffers(long demandId, long threadRootId) throws CommonException;

    OfferDetail changeOfferState(OfferDetail offerDetail) throws CommonException;

    FullOfferDetail updateOffer(FullOfferDetail newOffer) throws CommonException;
}
