package cz.poptavka.sample.client.user;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

@History(type = HistoryConverterType.NONE)
public class UserHistoryConverter implements HistoryConverter<UserEventBus> {

    @Override
    public void convertFromToken(String historyName, String param,
            UserEventBus eventBus) {
        if (historyName.equals("atAccount")) {
            eventBus.atAccount();
        }
    }

    @Override
    public boolean isCrawlable() {
        return true;
    }

}
