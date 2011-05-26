package cz.poptavka.sample.client.user;

import java.util.logging.Logger;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

import cz.poptavka.sample.shared.domain.UserDetail;

@History(type = HistoryConverterType.DEFAULT)
public class UserHistoryConverter implements HistoryConverter<UserEventBus>  {

    private UserDetail user;


    private static final Logger LOGGER = Logger
            .getLogger(UserHistoryConverter.class.getName());

    public String onAtAccount() {
        return "";
    }

    // TODO is this the best aproach?

    public String onInvokeNewDemand(Long id) {
        return id + "";
    }

    public String onInvokeMyDemands() {
        return "";
    }

    public String onInvokeOffers() {
        return "";
    }

    public String onInvokeMyDemandsOperator() {
        return "";
    }

    public String onInvokeAdministration() {
        return "";
    }

    @Override
    public void convertFromToken(String historyName, String param,
            UserEventBus eventBus) {
        LOGGER.fine("Name: " + historyName + "\nParam: " + param);
        eventBus.atAccount();
        if (historyName.equals("invokeMyDemands")) {
            eventBus.invokeMyDemands();
        }
        if (historyName.equals("invokeOffers")) {
//            eventBus.invokeOffers();
        }
        if (historyName.equals("invokeNewDemand")) {
            eventBus.invokeNewDemand(Long.valueOf(param));
        }
        if (historyName.equals("invokeMyDemandsOperator")) {
            eventBus.invokeMyDemandsOperator();
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
