/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eprovement.poptavka.client.main.common.search.SearchModuleDataHolder;
import com.eprovement.poptavka.domain.common.OrderType;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import java.util.Map;
import java.util.List;

/**
 *
 * @author Praso
 */
public interface HomeDemandsRPCServiceAsync {

    void filterDemands(int start, int count, SearchModuleDataHolder holder, Map<String, OrderType> orderColumns,
            AsyncCallback<List<FullDemandDetail>> callback);

    void filterDemandsCount(SearchModuleDataHolder holder, AsyncCallback<Long> callback);
}
