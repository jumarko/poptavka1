/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.eprovement.poptavka.client.main.common.search.SearchModuleDataHolder;
import com.eprovement.poptavka.domain.common.OrderType;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Praso
 */
@RemoteServiceRelativePath(HomeDemandsRPCService.URL)
public interface HomeDemandsRPCService extends RemoteService {

    String URL = "service/homedemands";

    long getDemandsCount(SearchModuleDataHolder holder) throws RPCException;

    List<FullDemandDetail> getDemands(int start, int count,
            SearchModuleDataHolder holder, Map<String, OrderType> orderColumns) throws RPCException;
}
