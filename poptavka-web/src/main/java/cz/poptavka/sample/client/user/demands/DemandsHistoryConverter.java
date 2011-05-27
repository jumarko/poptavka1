package cz.poptavka.sample.client.user.demands;

import java.util.logging.Logger;

import com.google.gwt.user.client.Cookies;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

import cz.poptavka.sample.client.user.UserEventBus;

@History(type = HistoryConverterType.NONE)
public class DemandsHistoryConverter implements HistoryConverter<UserEventBus> {


    private static final Logger LOGGER = Logger
            .getLogger(DemandsHistoryConverter.class.getName());

    @Override
    public void convertFromToken(String historyName, String param,
            UserEventBus eventBus) {
        LOGGER.fine("History Name: " + historyName + " || Param: " + param);
        String cookie = Cookies.getCookie("user-presenter");
        LOGGER.fine("Cookie value: " + cookie);
        if (cookie.equals("loaded")) {
            eventBus.dispatch(historyName);
        } else {
            eventBus.atAccount();
            eventBus.markEventToLoad(historyName);
        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }

}
