package cz.poptavka.sample.client.user.admin;

//import com.google.gwt.user.client.Cookies;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

import java.util.logging.Logger;

@History(type = HistoryConverterType.DEFAULT)
public class AdminModuleHistory implements HistoryConverter<AdminModuleEventBus> {

    private static final Logger LOGGER = Logger.getLogger(AdminModuleHistory.class.getName());
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
    public void convertFromToken(String historyName, String param, AdminModuleEventBus eventBus) {
//        String cookie = Cookies.getCookie("admin-presenter");
//        LOGGER.fine(" +++++++++ ADMIN  Name: " + historyName + "\nParam: " + param);

        if (historyName.equals(DEMANDS)) {
            eventBus.initDemands();
        }

        if (historyName.equals(CLIENTS)) {
            eventBus.initClients();
        }

        if (historyName.equals(SUPPLIERS)) {
            eventBus.initSuppliers();
        }

        if (historyName.equals(OFFERS)) {
            eventBus.initOffers();
        }

        if (historyName.equals(ACCESSROLES)) {
            eventBus.initAccessRoles();
        }

        if (historyName.equals(EMAILACTIVATIONS)) {
            eventBus.initEmailsActivation();
        }

        if (historyName.equals(INVOICES)) {
            eventBus.initInvoices();
        }

        if (historyName.equals(MESSAGES)) {
            eventBus.initMessages();
        }

//        if (historyName.equals(OURPAYMENTDETAILS)) {
//            eventBus.invokeAdminOurPaymentDetails();
//        }

        if (historyName.equals(PAYMENTMETHODS)) {
            eventBus.initPaymentMethods();
        }

        if (historyName.equals(PERMISSIONS)) {
            eventBus.initPermissions();
        }

        if (historyName.equals(PREFERENCES)) {
            eventBus.initPreferences();
        }

        if (historyName.equals(PROBLEMS)) {
            eventBus.initProblems();
        }
    }

    @Override
    public boolean isCrawlable() {
        // TODO Auto-generated method stub
        return false;
    }

    public String onInitDemands() {
        return "onInitDemands in AdminModuleHistory";
    }

    public String onInitClients() {
        return "onInitClients in AdminModuleHistory";
    }

    public String onInitSuppliers() {
        return "onInitSuppliers in AdminModuleHistory";
    }

    public String onInitOffers() {
        return "onInitOffers in AdminModuleHistory";
    }

    public String onInitAccessRoles() {
        return "onInitRoles in AdminModuleHistory";
    }

    public String onInitEmailsActivation() {
        return "onInitActivation in AdminModuleHistory";
    }

    public String onInitInvoices() {
        return "onInitInvoices in AdminModuleHistory";
    }

    public String onInitMessages() {
        return "onInitMessages in AdminModuleHistory";
    }

    public String onInitPaymentMethods() {
        return "onInitPaymentMethods in AdminModuleHistory";
    }

    public String onInitPermissions() {
        return "onInitPermissions in AdminModuleHistory";
    }

    public String onInitPreferences() {
        return "onInitPreferences in AdminModuleHistory";
    }

    public String onInitProblems() {
        return "onInitProblems in AdminModuleHistory";
    }
}
