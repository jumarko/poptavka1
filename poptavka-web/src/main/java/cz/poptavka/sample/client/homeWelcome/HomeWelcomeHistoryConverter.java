package cz.poptavka.sample.client.homeWelcome;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;

/**
 * History converter class. Handles history for HomeWelcomeModule
 *
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.DEFAULT, name = "homeWelcome")
public class HomeWelcomeHistoryConverter implements HistoryConverter<HomeWelcomeEventBus> {

    /**
     * Creates token(URL) for goToHomeWelcomeModule method.
     *
     * @param searchDataHolder
     * @return token string like module/method?param, where param = welcome
     */
    public String onGoToHomeWelcomeModule(SearchModuleDataHolder filter) {
        return "welcome";
    }

    /**
     * Called when browser action <b>back</b> or <b>forward</b> is evocated.
     * Or by clicking on <b>hyperlink</b> with set token.
     *
     * @param methodName - name of the called method
     * @param param - string behind '?' in url (module/method?param).
     *                URL creates method onGoToHomeWelcomeModule in HomeWelcomeHistoryConverter class.
     * @param eventBus - HomeWelcomeEventBus
     */
    @Override
    public void convertFromToken(String methodName, String param, HomeWelcomeEventBus eventBus) {
        eventBus.menuStyleChange(0);
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
