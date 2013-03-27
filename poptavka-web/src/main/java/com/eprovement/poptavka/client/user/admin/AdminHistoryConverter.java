package com.eprovement.poptavka.client.user.admin;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;

/**
 * History converter class. Handles history for AministrationModule.
 *
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.DEFAULT, name = "administration")
public class AdminHistoryConverter implements HistoryConverter<AdminEventBus> {

    private static final String ADMIN_ACCESS_ROLE_TEXT = "adminAccessRole";
    private static final String ADMIN_CLIENTS_TEXT = "adminClients";
    private static final String ADMIN_DEMANDS_TEXT = "adminDemands";
    private static final String ADMIN_EMAILS_ACTIVATION_TEXT = "adminEmailsActivation";
    private static final String ADMIN_INVOICES_TEXT = "adminInvoices";
    private static final String ADMIN_MESSAGES_TEXT = "adminMessages";
    private static final String ADMIN_OFFERS_TEXT = "adminOffers";
    private static final String ADMIN_PAYMENT_METHODS_TEXT = "adminPaymentMethods";
    private static final String ADMIN_PERMISSIONS_TEXT = "adminPermissions";
    private static final String ADMIN_PREFERENCES_TEXT = "adminPreferences";
    private static final String ADMIN_PROBLEMS_TEXT = "adminProblems";
    private static final String ADMIN_SUPPLIERS_TEXT = "adminSuppliers";
    private static final String ADMIN_NONE_TEXT = "adminWelcome";

    /**
     * Created token(URL) for onGoToAdminModule method.
     *
     * @param searchDataHolder - Provided by search module. Holds data to filter.
     * @param loadWidget - Constant from class Constants. Tells which view to load.
     * @return token string like module/method?param, where param = adminAccessRole, ....
     */
    public String onGoToAdminModule(SearchModuleDataHolder searchDataHolder, int loadWidget) {
        //Nemusi byt, ale lepsie to vyzera ako ked tam mam dat do url len hodnotu ciselnej konstanty
//        switch (loadWidget) {
//            case Constants.ADMIN_ACCESS_ROLE:
//                return ADMIN_ACCESS_ROLE_TEXT;
//            case Constants.ADMIN_CLIENTS:
//                return ADMIN_CLIENTS_TEXT;
//            case Constants.ADMIN_DEMANDS:
//                return ADMIN_DEMANDS_TEXT;
//            case Constants.ADMIN_EMAILS_ACTIVATION:
//                return ADMIN_EMAILS_ACTIVATION_TEXT;
//            case Constants.ADMIN_INVOICES:
//                return ADMIN_INVOICES_TEXT;
//            case Constants.ADMIN_MESSAGES:
//                return ADMIN_MESSAGES_TEXT;
//            case Constants.ADMIN_OFFERS:
//                return ADMIN_OFFERS_TEXT;
//            case Constants.ADMIN_PAYMENT_METHODS:
//                return ADMIN_PAYMENT_METHODS_TEXT;
//            case Constants.ADMIN_PERMISSIONS:
//                return ADMIN_PERMISSIONS_TEXT;
//            case Constants.ADMIN_PREFERENCES:
//                return ADMIN_PREFERENCES_TEXT;
//            case Constants.ADMIN_PROBLEMS:
//                return ADMIN_PROBLEMS_TEXT;
//            case Constants.ADMIN_SUPPLIERS:
//                return ADMIN_SUPPLIERS_TEXT;
//            default:
//                return ADMIN_NONE_TEXT;
//        }
        return "widget=" + loadWidget;
    }

    /**
     * Called either when browser action <b>back</b> or <b>forward</b> is evocated,
     * or by clicking on <b>hyperlink</b> with set token.
     *
     * @param methodName - name of the called method
     * @param param - string behind '?' in url (module/method?param).
     *                URL creates onGoToAdminModule method in AdminModuleHistoryConverter class.
     * @param eventBus - AdminModuleEventBus
     */
    @Override
    public void convertFromToken(String historyName, String param, AdminEventBus eventBus) {
        if (Storage.isAppCalledByURL() != null && Storage.isAppCalledByURL()) {
            // login from session method
            Storage.setAppCalledByURL(false);
            eventBus.setHistoryStoredForNextOne(false);
            eventBus.loginFromSession(Constants.ADMIN_NEW_DEMANDS);
            return;
        }
        eventBus.goToAdminModule(null, Constants.ADMIN_NEW_DEMANDS);
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
