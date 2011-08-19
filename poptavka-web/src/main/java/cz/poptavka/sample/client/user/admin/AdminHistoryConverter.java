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
    private static final String SUPPLIERS = "invokeAdminSuppliers";
    private static final String OFFERS = "invokeAdminOffers";
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

        if (historyName.equals(OFFERS)) {
            eventBus.invokeAdminOffers();
        }

        if (historyName.equals(SUPPLIERS)) {
            eventBus.invokeAdminSuppliers();
        }
    }

    @Override
    public boolean isCrawlable() {
        // TODO Auto-generated method stub
        return false;
    }
}
