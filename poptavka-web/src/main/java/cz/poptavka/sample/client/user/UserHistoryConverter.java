package cz.poptavka.sample.client.user;

import com.google.gwt.core.client.GWT;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

@History(type = HistoryConverterType.NONE)
public class UserHistoryConverter implements HistoryConverter<UserEventBus>  {


    @Override
    public void convertFromToken(String historyName, String param,
            UserEventBus eventBus) {
        GWT.log(" +++++++++ Name: " + historyName + "\nParam: " + param);
        eventBus.atAccount();
//        eventBus.markEventToLoad(historyName);
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
//
//        if (historyName.equals("invokeAdminOffers")) {
//            eventBus.invokeAdminOffers();
//        }

    }

    @Override
    public boolean isCrawlable() {
        return true;
    }

}
