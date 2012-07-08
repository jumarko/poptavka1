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
public class SupplierDemandsHistoryConverter implements HistoryConverter<SupplierDemandsEventBus> {

    @Override
    public void convertFromToken(String historyName, String param, SupplierDemandsEventBus eventBus) {
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }

}
