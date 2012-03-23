package cz.poptavka.sample.client.root;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

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
    private String action = "back";

    /**
     * To convert token for atHome method.
     *
     * @param loadModule
     * @return token string like module/method?param
     */
    public String onAtHome(int loadModule) {
        return Integer.toString(loadModule);
    }

    /**
     * To convert token for atAccount method.
     *
     * @param loadModule
     * @return token string like module/method?param
     */
    public String onAtAccount(int loadModule) {
        return Integer.toString(loadModule);
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
        if (historyName.equals("atHome")) {
            if (action.equals("back")) {
                action = "forward";
                eventBus.setHistoryStoredForNextOne(false);
                eventBus.getHistory().back();
            } else {
                action = "back";
                eventBus.setHistoryStoredForNextOne(false);
                eventBus.getHistory().forward();
            }
            eventBus.atAccount(Integer.valueOf(param));
        }
        if (historyName.equals("atAccount")) {
            if (action.equals("back")) {
                action = "forward";
                eventBus.setHistoryStoredForNextOne(false);
                eventBus.getHistory().back();
            } else {
                action = "back";
                eventBus.setHistoryStoredForNextOne(false);
                eventBus.getHistory().forward();
            }
            eventBus.atHome(Integer.valueOf(param));
        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
