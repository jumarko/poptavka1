package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.shared.domain.OfferDemandDetail;

@RemoteServiceRelativePath("service/offers")
public interface OfferRPCService extends RemoteService {

    ArrayList<OfferDemandDetail> getClientDemands(long clientId);


}
