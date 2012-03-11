package cz.poptavka.sample.client.user.demands;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;

@History(type = HistoryConverterType.SIMPLE, name = "demands")
public class DemandModuleHistoryConverter implements HistoryConverter<DemandModuleEventBus> {

    /**
     * To convert token for goToHomeSuppliersModule method
     * @param searchDataHolder
     * @param location
     * @return token string like module/method?param, where param = welcome, potentialDemands, myClients ...
     */
    public String convertToToken(String methodName, SearchModuleDataHolder searchDataHolder, String loadWidget) {
        return loadWidget;
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
    public void convertFromToken(String historyName, String param, DemandModuleEventBus eventBus) {
        eventBus.goToDemandModule(null, param);
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
