package cz.poptavka.sample.client.homesuppliers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import java.util.logging.Logger;

@History(type = HistoryConverterType.SIMPLE, name = "homeSuppliers")
public class HomeSuppliersHistoryConverter implements HistoryConverter<HomeSuppliersEventBus> {

    private static final Logger LOGGER = Logger.getLogger(HomeSuppliersHistoryConverter.class.getName());

    public String convertToToken(String tokenName) {
        return tokenName;
    }
    public String convertToToken(String historyName, CategoryDetail category) {
        return Long.toString(category.getId());
    }

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    @Override
    public void convertFromToken(String historyName, String param, HomeSuppliersEventBus eventBus) {
        eventBus.setHistoryStoredForNextOne(false);
//        eventBus.displayMenu();
//
//        if (historyName.equals("atHome")) {
//            eventBus.atHome();
//        }
        if (historyName.equals("addToPath")) {
            eventBus.loadingShow(MSGS.loading());
            if (param.equals("root")) {
                eventBus.rootWithSearchDataHolder(); //goToHomeSuppliers(null);
            } else {
                eventBus.setCategoryID(Long.valueOf(param));
                eventBus.removeFromPath(Long.valueOf(param));
                eventBus.getSubCategories(Long.valueOf(param));
            }
        }
//        if (historyName.equals("atCreateSupplier")) {
//            eventBus.atCreateSupplier();
//        }
//        if (historyName.equals("atSuppliers")) {
//            eventBus.atSuppliers();
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
