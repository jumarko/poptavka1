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

    public String onRegisterTabToken(int tab) {
        return "tab=" + (tab + 1);
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
        if (Storage.getUser() == null) {
            eventBus.menuStyleChange(Constants.HOME_DEMAND_CREATION_MODULE);
        } else {
            eventBus.userMenuStyleChange(Constants.USER_DEMANDS_MODULE);
        }
        if (Storage.isAppCalledByURL() != null && Storage.isAppCalledByURL()) {
            eventBus.goToCreateDemandModule();
        } else {
            //if tab2 -> tab1 -> logout
            int tab = Integer.parseInt(param.split("=")[1]);
            //case when logged at tab 2 and back performed
            //--> logout and select first tab again
            if (tab == 1) {
                eventBus.logout(Constants.CREATE_DEMAND);
                //case when logged -> back -> forward
                //---> login and select second tab again
            } else if (tab == 2 && Storage.getUser() == null) {
                eventBus.login(Constants.CREATE_DEMAND);
                //otherwise only select wanted tab
            } else {
                eventBus.goToCreateDemandModuleByHistory(tab - 1);
            }
        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
