package com.eprovement.poptavka.client.user.admin;

//import com.google.gwt.user.client.Cookies;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

import com.eprovement.poptavka.client.main.common.search.SearchModuleDataHolder;
import java.util.logging.Logger;

@History(type = HistoryConverterType.DEFAULT)
public class AdminHistory implements HistoryConverter<AdminEventBus> {

    private static final Logger LOGGER = Logger.getLogger(AdminHistory.class.getName());
    private static final String DEMANDS = "initDemands";
    private static final String CLIENTS = "initClients";
    private static final String SUPPLIERS = "initSuppliers";
    private static final String OFFERS = "initOffers";
    private static final String ACCESSROLES = "initAccessRoles";
    private static final String EMAILACTIVATIONS = "initEmailsActivation";
    private static final String INVOICES = "initInvoices";
    private static final String MESSAGES = "initMessages";
//    private static final String OURPAYMENTDETAILS = "invokeOurPaymentDetails";
    private static final String PAYMENTMETHODS = "initPaymentMethods";
    private static final String PERMISSIONS = "initPermissions";
    private static final String PREFERENCES = "initPreferences";
    private static final String PROBLEMS = "initProblems";

    @Override
    public void convertFromToken(String historyName, String param, AdminEventBus eventBus) {
//        String cookie = Cookies.getCookie("admin-presenter");
//        LOGGER.fine(" +++++++++ ADMIN  Name: " + historyName + "\nParam: " + param);

        if (historyName.equals(DEMANDS)) {
            eventBus.initDemands(null);
        }

        if (historyName.equals(CLIENTS)) {
            eventBus.initClients(null);
        }

        if (historyName.equals(SUPPLIERS)) {
            eventBus.initSuppliers(null);
        }

        if (historyName.equals(OFFERS)) {
            eventBus.initOffers(null);
        }

        if (historyName.equals(ACCESSROLES)) {
            eventBus.initAccessRoles(null);
        }

        if (historyName.equals(EMAILACTIVATIONS)) {
            eventBus.initEmailsActivation(null);
        }

        if (historyName.equals(INVOICES)) {
            eventBus.initInvoices(null);
        }

        if (historyName.equals(MESSAGES)) {
            eventBus.initMessages(null);
        }

//        if (historyName.equals(OURPAYMENTDETAILS)) {
//            eventBus.invokeAdminOurPaymentDetails();
//        }

        if (historyName.equals(PAYMENTMETHODS)) {
            eventBus.initPaymentMethods(null);
        }

        if (historyName.equals(PERMISSIONS)) {
            eventBus.initPermissions(null);
        }

        if (historyName.equals(PREFERENCES)) {
            eventBus.initPreferences(null);
        }

        if (historyName.equals(PROBLEMS)) {
            eventBus.initProblems(null);
        }
    }

    @Override
    public boolean isCrawlable() {
        // TODO Auto-generated method stub
        return false;
    }

    public String onInitDemands(SearchModuleDataHolder searchDataHolder) {
        return "onInitDemands in AdminModuleHistory";
    }

    public String onInitClients(SearchModuleDataHolder searchDataHolder) {
        return "onInitClients in AdminModuleHistory";
    }

    public String onInitSuppliers(SearchModuleDataHolder searchDataHolder) {
        return "onInitSuppliers in AdminModuleHistory";
    }

    public String onInitOffers(SearchModuleDataHolder searchDataHolder) {
        return "onInitOffers in AdminModuleHistory";
    }

    public String onInitAccessRoles(SearchModuleDataHolder searchDataHolder) {
        return "onInitRoles in AdminModuleHistory";
    }

    public String onInitEmailsActivation(SearchModuleDataHolder searchDataHolder) {
        return "onInitActivation in AdminModuleHistory";
    }

    public String onInitInvoices(SearchModuleDataHolder searchDataHolder) {
        return "onInitInvoices in AdminModuleHistory";
    }

    public String onInitMessages(SearchModuleDataHolder searchDataHolder) {
        return "onInitMessages in AdminModuleHistory";
    }

    public String onInitPaymentMethods(SearchModuleDataHolder searchDataHolder) {
        return "onInitPaymentMethods in AdminModuleHistory";
    }

    public String onInitPermissions(SearchModuleDataHolder searchDataHolder) {
        return "onInitPermissions in AdminModuleHistory";
    }

    public String onInitPreferences(SearchModuleDataHolder searchDataHolder) {
        return "onInitPreferences in AdminModuleHistory";
    }

    public String onInitProblems(SearchModuleDataHolder searchDataHolder) {
        return "onInitProblems in AdminModuleHistory";
    }
}
