package cz.poptavka.sample.client.root;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

/**
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.DEFAULT, name = "root")
public class RootHistoryConverter implements HistoryConverter<RootEventBus> {

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    public String onAtHome() {
        return "Home";
    }
//    public String onAtAccount() {
//        return "User";
//    }

//    public String convertToToken(String tokenName) {
//        if (tokenName.equals("atHome")) {
//
//        }
//        if (tokenName.contains("user")) {
//            return "User";
//        }
//
//        return "";
//    }
    @Override
    public void convertFromToken(String historyName, String param, RootEventBus eventBus) {
        if (historyName.equals("atHome")) {
            eventBus.atHome();
        }

        //Ak zoberiem do uvahy && chcem aby po prihlaseni bol naloadovany Demand Module
        //znamena, ze ulozene tokeny su:   atHome, atAccount, initDemandModule
        // a ked dam v browseri spat, tak sa chce naloadovat atAccount, kt automaticku loaduje DemandModule, takze
        // sa prakticky nikdy nedostanem na atHome
//        if (historyName.contains("atAccount")) {
//            eventBus.atAccount();
//        }


    }

    @Override
    public boolean isCrawlable() {
        return true;
    }
}
