package com.eprovement.poptavka.client.user.clientdemands;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;


/**
 * History converter class. Handles history for ClientDemandsModule.
 *
 * @author slavkovsky.martin
 */

@History(type = HistoryConverterType.NONE)
public class ClientDemandsHistoryConverter implements HistoryConverter<ClientDemandsEventBus> {

    @Override
    public void convertFromToken(String historyName, String param, ClientDemandsEventBus eventBus) {
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }

}
