package com.eprovement.poptavka.client.root;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

/**
 * History converter class. Handles history for RootModule. Especially login/logout process.
 *
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.NONE, name = "root")
public class RootHistoryConverter implements HistoryConverter<RootEventBus> {

    public String onCreateCustomToken(String param) {
        return param;
    }

    /**
     * Called either when browser action <b>back</b> or <b>forward</b> is evocated,
     * or by clicking on <b>hyperlink</b> with set token.
     *
     * @param methodName - name of the called method
     * @param param - string behind '?' in url (module/method?param).
     *                URL creates onAtHome & onAtAccount method in RootHistoryConverter class.
     * @param eventBus - RootEventBus
     */
    @Override
    public void convertFromToken(String historyName, String param, RootEventBus eventBus) {
        int i = 0;
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
