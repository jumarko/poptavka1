/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;

/**
 *
 * @author Praso
 */
@RemoteServiceRelativePath(HomeDemandsRPCService.URL)
public interface HomeDemandsRPCService extends RemoteService {

    String URL = "service/homedemands";

    CategoryDetail getCategory(long categoryID) throws RPCException;

    FullDemandDetail getDemand(long demandID) throws RPCException;

    long getDemandsCount(SearchDefinition searchDefinition) throws RPCException;

    List<FullDemandDetail> getDemands(SearchDefinition searchDefinition) throws RPCException;
}
