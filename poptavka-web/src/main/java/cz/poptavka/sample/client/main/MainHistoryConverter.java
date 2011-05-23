package cz.poptavka.sample.client.main;

import java.util.logging.Logger;

import com.google.gwt.user.client.Timer;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

import cz.poptavka.sample.client.main.login.LoginPopupPresenter;

/**
 * Basic History Management.
 *
 * @author Beho
 */

@History(type = HistoryConverterType.NONE)
public class MainHistoryConverter implements HistoryConverter<MainEventBus> {

    private static final Logger LOGGER = Logger.getLogger(MainHistoryConverter.class.getName());

    @Override
    public void convertFromToken(String historyName, String param,
            MainEventBus eventBus) {
        LOGGER.fine(historyName);
        if (historyName.equals("login")) {
            final LoginPopupPresenter presenter = eventBus.addHandler(LoginPopupPresenter.class);
            Timer timer = new Timer() {
                @Override
                public void run() {
                    presenter.onLogin();
                }
            };

        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }





}
