package cz.poptavka.sample.client.main;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

/**
 * Basic History Management.
 *
 * @author Beho
 */
//mvp4g 1.3.0
@History(type = HistoryConverterType.NONE)

//mvp4g 1.2.0
//@History(convertParams = false)
public class MainHistoryConverter implements HistoryConverter<MainEventBus> {

    @Override
    public void convertFromToken(String eventType, String param, MainEventBus eventBus) {
        //TODO
    }

    @Override
    public boolean isCrawlable() {
        return true;
    }

}
