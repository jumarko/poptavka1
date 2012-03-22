/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.AsyncCallback;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
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
