package cz.poptavka.sample.client.user;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

import cz.poptavka.sample.shared.domain.UserDetail;

@History(type = HistoryConverterType.NONE)
public class UserHistoryConverter implements HistoryConverter<UserEventBus> {

    @Override
    public void convertFromToken(String historyName, String param,
            UserEventBus eventBus) {
        eventBus.atAccount(new UserDetail());
        if (historyName.equals("invokeMyDemands")) {
            eventBus.invokeMyDemands();
        }
        if (historyName.equals("invokeOffers")) {
            eventBus.invokeOffers();
        }
        if (historyName.equals("invokeNewDemand")) {
//            eventBus.invokeNewDemand();
        }
        if (historyName.equals("invokeMyDemandsOperator")) {
//          eventBus.invokeNewDemand();
        }
        if (historyName.equals("invokeAdministration")) {
            eventBus.invokeAdministration();
        }
    }

    @Override
    public boolean isCrawlable() {
        return true;
    }
}
