package cz.poptavka.sample.client.home;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import java.util.logging.Logger;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;
import cz.poptavka.sample.shared.domain.CategoryDetail;

@History(type = HistoryConverterType.SIMPLE, name = "home")
public class HomeHistoryConverter implements HistoryConverter<HomeEventBus> {

    private static final Logger LOGGER = Logger.getLogger(HomeHistoryConverter.class.getName());

    public String convertToToken(String tokenName) {
        return tokenName;
    }
    public String convertToToken(String historyName, CategoryDetail category) {
        return Long.toString(category.getId());
    }

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    @Override
    public void convertFromToken(String historyName, String param, HomeEventBus eventBus) {
        eventBus.setHistoryStoredForNextOne(false);
        eventBus.displayMenu();

        if (historyName.equals("atHome")) {
            eventBus.atHome();
        }
        if (historyName.equals("addToPath")) {
            eventBus.loadingShow(MSGS.loading());
            if (param.equals("root")) {
                eventBus.goToHomeSuppliers();
            }
//            else {
                // TODO praso - add history management for homeSuppliers. I will
                // comment Martin's methods for now
//                eventBus.setCategoryID(Long.valueOf(param));
//                eventBus.removeFromPath(Long.valueOf(param));
//                eventBus.getSubCategories(Long.valueOf(param));
//            }
        }
        if (historyName.equals("atCreateDemand")) {
            eventBus.atCreateDemand();
        }
//        if (historyName.equals("atDemands")) {
//            eventBus.atDemands();
//        }
//        if (historyName.equals("atSuppliers")) {
//            eventBus.atSuppliers();
//        }
        if (historyName.equals("atRegisterSupplier")) {
            eventBus.atRegisterSupplier();
        }
    }

    @Override
    public boolean isCrawlable() {
        // TODO Auto-generated method stub
        return true;
    }
}
