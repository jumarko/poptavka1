/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.LesserDemandDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;

/**
 *
 * @author Ivan Vlcek
 */
@RemoteServiceRelativePath(HomeDemandsRPCService.URL)
public interface HomeDemandsRPCService extends RemoteService {

    String URL = "service/homedemands";

    ICatLocDetail getCategory(long categoryID) throws RPCException;

    FullDemandDetail getDemand(long demandID) throws RPCException;

    Integer getDemandsCount(SearchDefinition searchDefinition) throws RPCException;

    List<LesserDemandDetail> getDemands(SearchDefinition searchDefinition) throws RPCException;
}
