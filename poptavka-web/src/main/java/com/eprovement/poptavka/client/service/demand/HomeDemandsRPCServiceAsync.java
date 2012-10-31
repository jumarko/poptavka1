/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

/**
 *
 * @author Praso
 */
public interface HomeDemandsRPCServiceAsync {

    void getCategory(long categoryID, AsyncCallback<CategoryDetail> callback);

    void getDemand(long demandID, AsyncCallback<FullDemandDetail> callback);

    void getDemands(SearchDefinition searchDefinition, AsyncCallback<List<FullDemandDetail>> callback);

    void getDemandsCount(SearchDefinition searchDefinition, AsyncCallback<Long> callback);
}
