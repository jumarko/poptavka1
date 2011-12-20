package cz.poptavka.sample.client.root;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.Window;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

import cz.poptavka.sample.shared.domain.CategoryDetail;

@History(type = HistoryConverterType.SIMPLE, name = "root")
public class RootHistoryConverter implements HistoryConverter<RootEventBus> {

    public String convertToToken(String tokenName) {
        return "";
    }

    public String convertToToken(String historyName, CategoryDetail category) {
        return Long.toString(category.getId());
    }

    private static final LocalizableMessages MSGS = GWT
            .create(LocalizableMessages.class);

    @Override
    public void convertFromToken(String historyName, String param,
            RootEventBus eventBus) {
        Window.alert(historyName);
        eventBus.setHistoryStoredForNextOne(false);
        eventBus.displayMenu();

        if (historyName.contains("user")) {
            eventBus.atAccount();
        }

        if (historyName.equals("atHome")) {
            eventBus.atHome();
        }
        if (historyName.equals("addToPath")) {
            eventBus.loadingShow(MSGS.loading());
            if (param.equals("root")) {
                eventBus.initHomeSupplierModule(null);
            }
        }

    }

    @Override
    public boolean isCrawlable() {
        return true;
    }

}
