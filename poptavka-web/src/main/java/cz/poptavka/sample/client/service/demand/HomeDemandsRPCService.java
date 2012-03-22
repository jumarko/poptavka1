/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Praso
 */
@RemoteServiceRelativePath("service/homedemands")
public interface HomeDemandsRPCService extends RemoteService {

    long filterDemandsCount(SearchModuleDataHolder holder);

    List<FullDemandDetail> filterDemands(int start, int count,
            SearchModuleDataHolder holder, Map<String, OrderType> orderColumns);
}
