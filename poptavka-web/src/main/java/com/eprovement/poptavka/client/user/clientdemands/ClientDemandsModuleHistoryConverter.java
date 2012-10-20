package com.eprovement.poptavka.client.user.clientdemands;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.service.demand.UserRPCServiceAsync;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

/**
 * History converter class. Handles history for ClientDemandsModule.
 *
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.DEFAULT, name = "clientDemands")
public class ClientDemandsModuleHistoryConverter implements HistoryConverter<ClientDemandsModuleEventBus> {

    @Inject
    protected UserRPCServiceAsync service = null;

    public String onGoToClientDemandsModule(SearchModuleDataHolder filterm, int loadWidget) {
        return "widget=" + loadWidget;
    }

    @Override
    public void convertFromToken(String historyName, String param, final ClientDemandsModuleEventBus eventBus) {
        if (Storage.isAppCalledByURL() != null && Storage.isAppCalledByURL()) {
            // login from session method
            eventBus.populateStorageByUserDetail();
        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
