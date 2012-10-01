package com.eprovement.poptavka.client.user.supplierdemands;

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
//        eventBus.goToSupplierDemandsModule(null, Integer.valueOf(param.split("=")[1]));
        // TODO ivlcek - for now we forward user always to welcome widget where all notifications are summarized
        eventBus.goToSupplierDemandsModule(null, 0);
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
