package com.eprovement.poptavka.client.root;

import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;
import com.eprovement.poptavka.client.common.session.Storage;
import com.mvp4g.client.annotation.History;

/**
 * History converter class. Handles history for RootModule. Especially login/logout process.
 *
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.DEFAULT, name = "root")
public class RootHistoryConverter implements HistoryConverter<RootEventBus> {

    private static final String LOGIN = "login";
    private static final String LOGOUT = "logout";

    /**
     * To convert token for registerLogEventForHistory method.
     *
     * @param loadModule
     * @return token string like module/method?param
     */
    public String onRegisterLogEventForHistory() {
        if (Storage.getUser() == null) {
            return LOGIN;
        } else {
            return LOGOUT;
        }
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
        if (historyName.equals("registerLogEventForHistory")) {
            //LOGING
            if (Storage.getUser() == null) {
                if (param.equals(LOGIN)) {
                    Storage.setForwardHistory(Storage.FORWARD);
                } else {
                    Storage.setForwardHistory(Storage.BACK);
                }
                //Set pointer to true to detect that login process is going to be invoked by history
                Storage.setLoginDueToHistory(true);
                //Login user and sets account layout
                //Martin - temporary disabled
                //eventBus.login();
                //LOGOUT
            } else {
                if (param.equals(LOGIN)) {
                    Storage.setForwardHistory(Storage.BACK);
                } else {
                    Storage.setForwardHistory(Storage.FORWARD);
                }
                //Set pointer to true to detect that logout process is going to be invoked by history
                Storage.setLogoutDueToHistory(true);
                //Logout user and sets home layout
                eventBus.logout(); //sets Storage.getUser to null
            }
        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
