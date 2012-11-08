package com.eprovement.poptavka.client.user.settings;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;
import com.eprovement.poptavka.client.common.session.Storage;

/**
 * History converter class. Handles history for HomeSuppliersModule.
 * This class works different than others. Token is not created for navigation method, because
 * whole widget works different.
 *
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.DEFAULT, name = "settings")
public class SettingsHistoryConverter implements HistoryConverter<SettingsEventBus> {

    /**
     * Creates token(URL) for goToSettingsModule method.
     *
     * @param categoryDetail - processing category
     * @return token string like module/method?param
     */
    public String onGoToSettingsModule() {
        return "";
    }

    /**
     * Called either when browser action <b>back</b> or <b>forward</b> is evocated,
     * or by clicking on <b>hyperlink</b> with set token.
     *
     * @param methodName - name of the called method
     * @param param - string behind '?' in url (module/method?param).
     *                URL creates onGoToSettingsModule method in SettingsModuleHistoryConverter class.
     * @param eventBus - SettingsModuleEventBus
     */
    @Override
    public void convertFromToken(String methodName, String param, SettingsEventBus eventBus) {
        if (Storage.isAppCalledByURL() != null && Storage.isAppCalledByURL()) {
            // login from session method
            eventBus.loginFromSession();
        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
