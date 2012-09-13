package com.eprovement.poptavka.client.user.supplierdemands;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;


/**
 * History converter class. Handles history for SupplierDemandsModule.
 *
 * @author slavkovsky.martin
 */

@History(type = HistoryConverterType.NONE)
public class SupplierDemandsModuleHistoryConverter implements HistoryConverter<SupplierDemandsModuleEventBus> {

    @Override
    public void convertFromToken(String historyName, String param, SupplierDemandsModuleEventBus eventBus) {
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }

}
