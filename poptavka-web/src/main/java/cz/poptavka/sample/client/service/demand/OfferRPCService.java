/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import cz.poptavka.sample.shared.domain.OfferDemandDetail;
import java.util.ArrayList;

/**
 *
 * @author ivan.vlcek
 */
@RemoteServiceRelativePath("service/offers")
public interface OfferRPCService extends RemoteService {

    ArrayList<OfferDemandDetail> getClientDemands(long clientId);
}
