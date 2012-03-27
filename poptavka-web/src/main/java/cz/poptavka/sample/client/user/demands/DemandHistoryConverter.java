package cz.poptavka.sample.client.user.demands;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;
import cz.poptavka.sample.client.main.Constants;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;

/**
 * History converter class. Handles history for DemandsModule.
 *
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.DEFAULT, name = "demands")
public class DemandHistoryConverter implements HistoryConverter<DemandEventBus> {

    private static final String DEMANDS_CLIENT_MY_DEMANDS_TEXT = "demandsClientMyDemands";
    private static final String DEMANDS_CLIENT_OFFERS_TEXT = "demandsClientOffers";
    private static final String DEMANDS_CLIENT_ASSIGNED_DEMANDS_TEXT = "demandsClientAssignedDemands";
    private static final String DEMANDS_SUPPLIER_MY_DEMANDS_TEXT = "demandsSupplierMyDemands";
    private static final String DEMANDS_SUPPLIER_OFFERS_TEXT = "demandsSupplierOffers";
    private static final String DEMANDS_SUPPLIER_ASSIGNED_DEMANDS_TEXT = "demandsSupplierAssignedDemands";
    private static final String DEMANDS_NONE_TEXT = "demandsWelcome";

    /**
     * Created token(URL) for goToDemandModule method.
     *
     * @param searchDataHolder - Provided by search module. Holds data to filter.
     * @param loadWidget - Constant from class Constants. Tells which view to load.
     * @return token string like module/method?param, where param = demandsClientMyDemands, ....
     */
    public String onGoToDemandModule(SearchModuleDataHolder filter, int loadWidget) {
        switch (loadWidget) {
            case Constants.DEMANDS_CLIENT_MY_DEMANDS:
                return DEMANDS_CLIENT_MY_DEMANDS_TEXT;
            case Constants.DEMANDS_CLIENT_OFFERS:
                return DEMANDS_CLIENT_OFFERS_TEXT;
            case Constants.DEMANDS_CLIENT_ASSIGNED_DEMANDS:
                return DEMANDS_CLIENT_ASSIGNED_DEMANDS_TEXT;
            case Constants.DEMANDS_SUPPLIER_MY_DEMANDS:
                return DEMANDS_SUPPLIER_MY_DEMANDS_TEXT;
            case Constants.DEMANDS_SUPPLIER_OFFERS:
                return DEMANDS_SUPPLIER_OFFERS_TEXT;
            case Constants.DEMANDS_SUPPLIER_ASSIGNED_DEMANDS:
                return DEMANDS_SUPPLIER_ASSIGNED_DEMANDS_TEXT;
            default:
                return DEMANDS_NONE_TEXT;
        }
    }

    /**
     * Called either when browser action <b>back</b> or <b>forward</b> is evocated,
     * or by clicking on <b>hyperlink</b> with set token.
     *
     * @param methodName - name of the called method
     * @param param - string behind '?' in url (module/method?param).
     *                URL creates onGoToDemandModule method in DemandModuleHistoryConverter class.
     * @param eventBus - DemandModuleEventBus
     */
    @Override
    public void convertFromToken(String historyName, String param, DemandEventBus eventBus) {
        if (historyName.equals("goToDemandModule")) {
            //Musim naspat previest retazce na konstanty
            if (param.equals(DEMANDS_CLIENT_MY_DEMANDS_TEXT)) {
                eventBus.goToDemandModule(null, Constants.DEMANDS_CLIENT_MY_DEMANDS);
            }
            if (param.equals(DEMANDS_CLIENT_OFFERS_TEXT)) {
                eventBus.goToDemandModule(null, Constants.DEMANDS_CLIENT_OFFERS);
            }
            if (param.equals(DEMANDS_CLIENT_ASSIGNED_DEMANDS_TEXT)) {
                eventBus.goToDemandModule(null, Constants.DEMANDS_CLIENT_ASSIGNED_DEMANDS);
            }
            if (param.equals(DEMANDS_SUPPLIER_MY_DEMANDS_TEXT)) {
                eventBus.goToDemandModule(null, Constants.DEMANDS_SUPPLIER_MY_DEMANDS);
            }
            if (param.equals(DEMANDS_SUPPLIER_OFFERS_TEXT)) {
                eventBus.goToDemandModule(null, Constants.DEMANDS_SUPPLIER_OFFERS);
            }
            if (param.equals(DEMANDS_SUPPLIER_ASSIGNED_DEMANDS_TEXT)) {
                eventBus.goToDemandModule(null, Constants.DEMANDS_SUPPLIER_ASSIGNED_DEMANDS);
            }
            if (param.equals(DEMANDS_NONE_TEXT)) {
                eventBus.goToDemandModule(null, Constants.NONE);
            }
        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
