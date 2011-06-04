package cz.poptavka.sample.client.user.admin;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

import cz.poptavka.sample.client.user.UserEventBus;

@History(type = HistoryConverterType.NONE)
public class AdminHistoryConverter implements HistoryConverter<UserEventBus> {

    @Override
    public void convertFromToken(String historyName, String param,
            UserEventBus eventBus) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isCrawlable() {
        // TODO Auto-generated method stub
        return false;
    }

}
