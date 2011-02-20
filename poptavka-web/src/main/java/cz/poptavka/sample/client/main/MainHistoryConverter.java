package cz.poptavka.sample.client.main;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.history.HistoryConverter;

/**
 * Basic History Management.
 *
 * @author Beho
 */
@History(convertParams = false)
public class MainHistoryConverter implements HistoryConverter<MainEventBus> {

    @Override
    public void convertFromToken(String eventType, String param,
            MainEventBus eventBus) {
        //TODO
    }

    @Override
    public boolean isCrawlable() {
        return true;
    }

}
