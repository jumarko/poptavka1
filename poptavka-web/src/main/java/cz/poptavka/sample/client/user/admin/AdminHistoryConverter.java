package cz.poptavka.sample.client.user.admin;

import com.google.gwt.user.client.Cookies;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.client.user.admin.tab.AdminDemandsPresenter;
import cz.poptavka.sample.client.user.admin.tab.AdminOffersPresenter;
import cz.poptavka.sample.client.user.admin.tab.AdminSuppliersPresenter;
import java.util.logging.Logger;

@History(type = HistoryConverterType.NONE)
public class AdminHistoryConverter implements HistoryConverter<UserEventBus> {

    private static final Logger LOGGER = Logger.getLogger(AdminHistoryConverter.class.getName());
    private static final String DEMANDS = "invokeAdminDemands";
    private static final String CLIENTS = "invokeAdminClients";
    private static final String SUPPLIERS = "invokeAdminSuppliers";
    private static final String OFFERS = "invokeAdminOffers";
    private static final String ACCESSROLES = "invokeAdminAccessRoles";
    private static final String EMAILACTIVATIONS = "invokeAdminEmailActivations";
    private static final String INVOICES = "invokeAdminInvoices";
    private static final String MESSAGES = "invokeAdminMessages";
    private static final String OURPAYMENTDETAILS = "invokeAdminOurPaymentDetails";
    private static final String PAYMENTMETHODS = "invokeAdminPaymentMethods";
    private static final String PERMISSIONS = "invokeAdminPermissions";
    private static final String PREFERENCES = "invokeAdminPreferences";
    private static final String PROBLEMS = "invokeAdminProblems";
//    private static final String USERS = "invokeAdminUsers";
    private AdminSuppliersPresenter adminSuppliersPresenter = null;
    private AdminDemandsPresenter adminDemandsPresenter = null;
    private AdminOffersPresenter adminOffersPresenter = null;

    @Override
    public void convertFromToken(String historyName, String param, UserEventBus eventBus) {
        String cookie = Cookies.getCookie("admin-presenter");
        LOGGER.fine(" +++++++++ ADMIN  Name: " + historyName + "\nParam: " + param);
//            normal behaviour
//            eventBus.dispatch(historyName);

//            eventBus.toggleLoading();

        if (historyName.equals(DEMANDS)) {
            eventBus.invokeAdminDemands();
        }

        if (historyName.equals(CLIENTS)) {
            eventBus.invokeAdminClients();
        }

        if (historyName.equals(SUPPLIERS)) {
            eventBus.invokeAdminSuppliers();
        }

        if (historyName.equals(OFFERS)) {
            eventBus.invokeAdminOffers();
        }

        if (historyName.equals(ACCESSROLES)) {
            eventBus.invokeAdminAccessRoles();
        }

        if (historyName.equals(EMAILACTIVATIONS)) {
            eventBus.invokeAdminEmailActivations();
        }

        if (historyName.equals(INVOICES)) {
            eventBus.invokeAdminInvoices();
        }

        if (historyName.equals(MESSAGES)) {
            eventBus.invokeAdminMessages();
        }

        if (historyName.equals(OURPAYMENTDETAILS)) {
            eventBus.invokeAdminOurPaymentDetails();
        }

        if (historyName.equals(PAYMENTMETHODS)) {
            eventBus.invokeAdminPaymentMethods();
        }

        if (historyName.equals(PERMISSIONS)) {
            eventBus.invokeAdminPermissions();
        }

        if (historyName.equals(PREFERENCES)) {
            eventBus.invokeAdminPreferences();
        }

        if (historyName.equals(PROBLEMS)) {
            eventBus.invokeAdminProblems();
        }
    }

    @Override
    public boolean isCrawlable() {
        // TODO Auto-generated method stub
        return false;
    }
}
