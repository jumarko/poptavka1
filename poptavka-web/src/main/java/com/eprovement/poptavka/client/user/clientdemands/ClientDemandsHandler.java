package com.eprovement.poptavka.client.user.clientdemands;

import com.eprovement.poptavka.client.service.demand.ClientDemandsRPCServiceAsync;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.domain.enums.OrderType;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import java.util.Map;

@EventHandler
public class ClientDemandsHandler extends BaseEventHandler<ClientDemandsEventBus> {

    @Inject
    private ClientDemandsRPCServiceAsync clientDemandsService = null;

    //*************************************************************************/
    // Overriden methods of IEventBusData interface.                          */
    //*************************************************************************/
    public void onGetDataCount(final UniversalAsyncGrid grid, SearchModuleDataHolder detail) {
    }

    public void onGetData(int start, int maxResults,
            SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
    }

    //*************************************************************************/
    // Retrieving methods                                                     */
    //*************************************************************************/
}
