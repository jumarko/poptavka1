package com.eprovement.poptavka.client.root;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;
import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.client.main.login.LoginPopupPresenter;

/**
 * Musime pouzit history aj na methody atHome a atAccount, ktore maju na starosti
 * nastavovanie layoutov (menu, login link)
 */
/**
 * History converter class. Handles history for RootModule.
 *
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.DEFAULT, name = "root")
public class RootHistoryConverter implements HistoryConverter<RootEventBus> {

    /**
     * Kedze sme vlozili dalsi medziclanok medzi volanie modulov. Vytvara sa nam novy
     * token ktory ma za ulohu odchitit volanie metod atHome a atAccount aby sme vedeli
     * aky layout zobrazit, ale neloaduje ziaden dalsi modul, teda musim volat dodatocne
     * back & forward akcie. Pre jednoznacne rozlisenie sluzi nasledujuca pomocna metoda.
     */
    /**
     * To convert token for atHome method.
     *
     * @param loadModule
     * @return token string like module/method?param
     */
    public String onAtHome() {
        return "";
    }
    /**
     * To convert token for atAccount method.
     *
     * @param loadModule
     * @return token string like module/method?param
     */
    public String onAtAccount() {
        return "";
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
        if (historyName.equals("atAccount")) {
            if (Storage.getActionLoginAccountHistory().equals("back")) {
                eventBus.setHistoryStoredForNextOne(false);
                eventBus.atHome();
                eventBus.getHistory().back();
                Storage.setActionLoginAccountHistory("forward");
            } else {
                LoginPopupPresenter login = eventBus.addHandler(LoginPopupPresenter.class);
                login.onLogin();
            }

        } else { //    equals("atHome")) {
            if (Storage.getActionLoginHomeHistory().equals("back")) {
                LoginPopupPresenter login = eventBus.addHandler(LoginPopupPresenter.class);
                login.onLogin();
            } else {
                eventBus.setHistoryStoredForNextOne(false);
                eventBus.atHome();
                eventBus.getHistory().forward();
                Storage.setActionLoginHomeHistory("back");
            }
        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
