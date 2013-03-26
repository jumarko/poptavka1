package com.eprovement.poptavka.client.home.createDemand;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;

/**
 * History converter class. Handles history of demand creation module.
 *
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.DEFAULT, name = "demandCreation")
public class DemandCreationHistoryConverter implements HistoryConverter<DemandCreationEventBus> {

    private static final String HOME = "home";
    private static final String USER = "user";

    public String onGoToCreateDemandModule() {
        return Storage.getUser() == null ? HOME : USER;
    }
    /**
     * Called either when browser action <b>back</b> or <b>forward</b> is evocated
     * or by clicking on <b>hyperlink</b> with set token.
     *
     * @param methodName - name of the called method
     * @param param - string behind '?' in url (module/method?param).
     *                URL creates onGoToCreateDemandModule method in DemandCreationHistoryConverter class.
     * @param eventBus - DemandCreationEventBus
     */
    @Override
    public void convertFromToken(String methodName, String param, DemandCreationEventBus eventBus) {
        if (param.equals(HOME)) {
            eventBus.goToCreateDemandModule();
            eventBus.menuStyleChange(Constants.CREATE_DEMAND);
        } else {
            eventBus.setHistoryStoredForNextOne(false);
            eventBus.loginFromSession(Constants.CREATE_DEMAND);
            eventBus.userMenuStyleChange(Constants.CREATE_DEMAND);
        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
