package cz.poptavka.sample.client.user;

import java.util.logging.Logger;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

@History(type = HistoryConverterType.NONE)
public class UserHistoryConverter implements HistoryConverter<UserEventBus>  {

    private static final Logger LOGGER = Logger
            .getLogger(UserHistoryConverter.class.getName());

    @Override
    public void convertFromToken(String historyName, String param,
            UserEventBus eventBus) {
        LOGGER.fine("Name: " + historyName + "\nParam: " + param);
        eventBus.getUser();
        eventBus.markEventToLoad(historyName);
//        if (historyName.equals("invokeMyDemands")) {
//            eventBus.invokeMyDemands();
//        }
//        if (historyName.equals("invokeOffers")) {
//            eventBus.invokeOffers();
//        }
//        if (historyName.equals("invokeNewDemand")) {
//            eventBus.invokeNewDemand();
//        }
//        if (historyName.equals("invokeMyDemandsOperator")) {
//            eventBus.invokeMyDemandsOperator();
//        }
//        if (historyName.equals("invokeAdministration")) {
//            eventBus.invokeAdministration();
//        }
    }

    @Override
    public boolean isCrawlable() {
        return true;
    }

}
