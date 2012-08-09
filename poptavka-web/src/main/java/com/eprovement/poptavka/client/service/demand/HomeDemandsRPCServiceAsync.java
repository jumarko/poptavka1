/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

/**
 *
 * @author Praso
 */
public interface HomeDemandsRPCServiceAsync {

    void getDemands(SearchDefinition searchDefinition, AsyncCallback<List<FullDemandDetail>> callback);

    void getDemandsCount(SearchModuleDataHolder holder, AsyncCallback<Long> callback);
}
