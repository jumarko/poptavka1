package com.eprovement.poptavka.client.user.supplierdemands;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.service.demand.UserRPCServiceAsync;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

/**
 * History converter class. Handles history for SupplierDemandsModule.
 *
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.DEFAULT, name = "supplierDemands")
public class SupplierDemandsModuleHistoryConverter implements HistoryConverter<SupplierDemandsModuleEventBus> {

    @Inject
    protected UserRPCServiceAsync service = null;

    public String onGoToSupplierDemandsModule(SearchModuleDataHolder filterm, int loadWidget) {
        return "widget=" + loadWidget;
    }

    @Override
    public void convertFromToken(String historyName, String param, final SupplierDemandsModuleEventBus eventBus) {
        if (Storage.isAppCalledByURL() != null && Storage.isAppCalledByURL()) {
            // The app called by URL thus Storage is going to be populated from Session. If session is empty then
            // initiate login from session object that will load complete user part with required module.
            eventBus.populateStorageByUserDetail();
        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
