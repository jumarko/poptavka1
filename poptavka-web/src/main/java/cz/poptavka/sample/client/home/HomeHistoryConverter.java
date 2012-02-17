package cz.poptavka.sample.client.home;


import java.util.logging.Logger;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

@History(type = HistoryConverterType.SIMPLE, name = "home")
public class HomeHistoryConverter implements HistoryConverter<HomeEventBus> {

    private static final Logger LOGGER = Logger.getLogger(HomeHistoryConverter.class.getName());

//    public String convertToToken(String tokenName) {
//        return "";
//    }
//
//    public String convertToToken(String historyName, CategoryDetail category) {
//        return Long.toString(category.getId());
//    }
//    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    @Override
    public void convertFromToken(String historyName, String param, HomeEventBus eventBus) {
//        Window.alert(historyName);
//        eventBus.setHistoryStoredForNextOne(false);
//        eventBus.displayMenu();

//        if (historyName.contains("user")) {
//            eventBus.atAccount();
//        }

//        if (historyName.equals("atHome")) {
//            eventBus.atHome();
//        }
//        if (historyName.equals("atCreateDemand")) {
//            eventBus.atCreateDemand();
//        }
//        if (historyName.equals("atDemands")) {
//            eventBus.atDemands();
//        }
//        if (historyName.equals("atSuppliers")) {
//            eventBus.atSuppliers();
//        }
//        if (historyName.equals("atRegisterSupplier")) {
//            eventBus.atRegisterSupplier();
//        }
    }

    @Override
    public boolean isCrawlable() {
        // TODO Auto-generated method stub
        return true;
    }
}
