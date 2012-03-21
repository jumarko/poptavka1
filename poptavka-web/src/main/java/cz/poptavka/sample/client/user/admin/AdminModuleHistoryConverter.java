package cz.poptavka.sample.client.user.admin;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;

@History(type = HistoryConverterType.SIMPLE, name = "administration")
public class AdminModuleHistoryConverter implements HistoryConverter<AdminModuleEventBus> {

    /**
     * To convert token for goToHomeSuppliersModule method
     * @param searchDataHolder
     * @param location
     * @return token string like module/method?param, where param = initAdminAccess, ....
     */
    public String convertToToken(String methodName, SearchModuleDataHolder searchDataHolder, int loadWidget) {
        return Integer.toString(loadWidget);
    }

    /**
     * Called when browser action <b>back</b> or <b>forward</b> is evocated.
     * Or by clicking on <b>hyperlink</b> with set token.
     *
     * @param methodName - name of the called method
     * @param param - string behind '?' in url (module/method?param). Url generates convertToToken method.
     * @param eventBus
     */
    @Override
    public void convertFromToken(String historyName, String param, AdminModuleEventBus eventBus) {
        eventBus.goToAdminModule(null, Integer.valueOf(param));
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
