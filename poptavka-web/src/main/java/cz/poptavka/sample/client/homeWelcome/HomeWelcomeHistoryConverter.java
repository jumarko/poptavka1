package cz.poptavka.sample.client.homeWelcome;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;

/**
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.SIMPLE, name = "homeWelcome")
public class HomeWelcomeHistoryConverter implements HistoryConverter<HomeWelcomeEventBus> {

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    /**
     * To convert token for initHomeWelcomeModule method
     * @param searchDataHolder
     * @param location
     * @return token string like module/method?param, where param = welcome
     */
    public String convertToToken(String methodName, SearchModuleDataHolder filter) {
        if (methodName.equals("goToHomeWelcomeModule")) {
            return "welcome";
        }
        return "";
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
    public void convertFromToken(String methodName, String param, HomeWelcomeEventBus eventBus) {
        if (methodName.equals("goToHomeWelcomeModule")) {
            eventBus.goToHomeWelcomeModule(null);
        }
    }

    @Override
    public boolean isCrawlable() {
        // TODO Auto-generated method stub
        return false;
    }
}
