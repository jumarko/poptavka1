package cz.poptavka.sample.client.root;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

/**
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.DEFAULT, name = "root")
public class RootHistoryConverter implements HistoryConverter<RootEventBus> {

//    public String onAtHome(int loadModule) {
//        return Integer.toString(Storage.getCurrentlyLoadedModule());
//    }
//
//    public String onAtAccount(int loadModule) {
//        return Integer.toString(Storage.getCurrentlyLoadedModule());
//    }
//    public String onGoToHomeWelcomeModule(SearchModuleDataHolder filter) {
//        return "back";
//    }
//
//    public String onGoToDemandModule(SearchModuleDataHolder filter, int loadWidget) {
//        return "back";
//    }

//    public String onInitLoginWindow() {
//        return "login";
//    }
    @Override
    public void convertFromToken(String historyName, String param, RootEventBus eventBus) {
        /**
         * Problem je ten ze ked vytvorim token pre rootRventBus eventy, tak sa mi potom pri akciach
         * back & forward zavola dany token, ktory by mal riesit prave naloadovanie atHome, atAccount, teda
         * layout. Ale vznika tu problem, ktory ani sam neviem popisat :)
         */

//        boolean wasBackOrForward = false;
//        if (param.equals("back")) {
//            eventBus.getHistory().getToken().replace("back", "forward");
//            eventBus.getHistory().back();
//            wasBackOrForward = true;
//        }
//        if (param.equals("forward")) {
//            eventBus.getHistory().getToken().replace("forward", "back");
//            eventBus.getHistory().forward();
//            wasBackOrForward = true;
//        }
//        if (!wasBackOrForward) {
//            //
//            if (historyName.equals("goToHomeWelcomeModule")) {
////            eventBus.setHistoryStoredForNextOne(false);
//                eventBus.atHome();
//                eventBus.goToHomeWelcomeModule(null);
//            }
//            if (historyName.equals("goToDemandModule")) {
////            eventBus.getHistory().back();
////            eventBus.setHistoryStoredForNextOne(false);
//                eventBus.atAccount();
//                eventBus.goToDemandModule(null, Constants.USER_DEMANDS_MODULE);
//            }
//        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
