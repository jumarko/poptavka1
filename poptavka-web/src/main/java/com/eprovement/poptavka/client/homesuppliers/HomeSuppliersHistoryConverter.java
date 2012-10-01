package com.eprovement.poptavka.client.homesuppliers;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

/**
 * History converter class. Handles history for HomeSuppliersModule.
 *
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.DEFAULT, name = "homeSuppliers")
public class HomeSuppliersHistoryConverter implements HistoryConverter<HomeSuppliersEventBus> {

    public String onGoToHomeSuppliersModule(SearchModuleDataHolder searchModuleDataHolder, int homeSuppliersViewType) {
//        switch (homeSuppliersViewType) {
//            case Constants.HOME_SUPPLIERS_BY_DEFAULT:
        return "";
//            case Constants.HOME_SUPPLIERS_BY_SEARCH:
//                return "catId=";
//            default:
//                break;
//        }
    }

    public String onCreateTokenForHistory(String token) {
        return token;
    }

    /**
     * Called either when browser action <b>back</b> or <b>forward</b> is evocated,
     * or by clicking on <b>hyperlink</b> with set token.
     *
     * @param methodName - name of the called method
     * @param param - string behind '?' in url (module/method?param).
     *                URL creates onAddToPath method in HomeSuppliersHistoryConverter class.
     * @param eventBus - HomeSuppliersEventBus
     */
    @Override
    public void convertFromToken(String methodName, String param, HomeSuppliersEventBus eventBus) {
        if (Storage.getUser() == null) {
            eventBus.menuStyleChange(Constants.HOME_SUPPLIERS_MODULE);
        } else {
            eventBus.userMenuStyleChange(Constants.USER_SUPPLIER_MODULE);
        }
        if (Storage.isAppCalledByURL()) {
            eventBus.goToHomeSuppliersModule(null, Constants.HOME_SUPPLIERS_BY_DEFAULT);
        }
//        if (param != null) {
//            SearchModuleDataHolder filter = new SearchModuleDataHolder();
//            String[] idTab = param.split(";");
//            filter.getCategories().add(new CategoryDetail(Long.valueOf(idTab[0].split("=")[1]), ""));
//            if (idTab.length == 2) {
//                filter.getAttributes().add(new FilterItem(
//                        "id", FilterItem.OPERATION_EQUALS, idTab[1].split("=")[1]));
//            }
////            if (com.google.gwt.user.client.History.getToken().isEmpty()) {
//                eventBus.goToHomeSuppliersModule(filter, Constants.HOME_SUPPLIERS_BY_HISTORY);
////            }
//        } else {
//            if (com.google.gwt.user.client.History.getToken().isEmpty()) {
//                eventBus.goToHomeSuppliersModule(null, Constants.HOME_SUPPLIERS_BY_DEFAULT);
//            }
//        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
