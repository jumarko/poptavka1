package cz.poptavka.sample.client.home.creation;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;
import cz.poptavka.sample.client.main.Constants;
import cz.poptavka.sample.client.main.Storage;

/**
 * History converter class. Handles history of demand creation module.
 *
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.DEFAULT, name = "demandCreation")
public class DemandCreationHistoryConverter implements HistoryConverter<DemandCreationEventBus> {

    /**
     * Creates token(URL) for goToCreateDemandModule method.
     *
     * @return token string like module/method?param, where param = home / user
     */
    public String onGoToCreateDemandModule() {
        /* Martin - Nemusi to byt, pretoze v convertFromToken to neberiem vobec do uvahy.
                   Ale pre testovacie ucely ale vhodne. Potom moze odstranit */
        if (Storage.getUser() == null) {
            return "location=home";
        } else {
            return "location=user";
        }
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
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
