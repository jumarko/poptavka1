package com.eprovement.poptavka.client.homedemands;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

/**
 * History converter class. Handles history for HomeDemandsModule.
 *
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.NONE, name = "homeDemands")
public class HomeDemandsHistoryConverter implements HistoryConverter<HomeDemandsEventBus> {

    /**
     * Called either when browser action <b>back</b> or <b>forward</b> is evocated,
     * or by clicking on <b>hyperlink</b> with set token.
     *
     * @param methodName - name of the called method
     * @param param - string behind '?' in url (module/method?param).
     *                URL creates goToHomeDemandsModule method in HomeDemandsHistoryConverter class.
     * @param eventBus - HomeDemandsEventBus
     */
    @Override
    public void convertFromToken(String methodName, String param, HomeDemandsEventBus eventBus) {
        if (Storage.getUser() == null) {
            eventBus.menuStyleChange(Constants.HOME_DEMANDS_MODULE);
        } else {
            eventBus.userMenuStyleChange(Constants.USER_DEMANDS_MODULE);
        }
        if (Storage.isAppCalledByURL()) {
            eventBus.goToHomeDemandsModule(null, Constants.HOME_DEMANDS_BY_DEFAULT);
        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
