package cz.poptavka.sample.client.root;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.Window;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

import cz.poptavka.sample.shared.domain.CategoryDetail;

/**
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.SIMPLE, name = "root")
public class RootHistoryConverter implements HistoryConverter<RootEventBus> {

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    public String convertToToken(String tokenName) {
        return "Home";
    }

    public String convertToToken(String historyName, CategoryDetail category) {
        return Long.toString(category.getId());
    }

    @Override
    public void convertFromToken(String historyName, String param,
            RootEventBus eventBus) {
        Window.alert(historyName);

        if (historyName.contains("user")) {
            eventBus.atAccount();
        }

        if (historyName.equals("atHome")) {
            eventBus.atHome();
        }

        if (historyName.equals("initHomeSuppliersModule")) {
            eventBus.initHomeSuppliersModule(null, param.replace("Root", ""));
        }
    }

    @Override
    public boolean isCrawlable() {
        return true;
    }

}
