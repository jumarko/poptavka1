package cz.poptavka.sample.client.user.demands;

import com.google.gwt.user.client.Window;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;
import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;

@History(type = HistoryConverterType.DEFAULT, name = "demands")
public class DemandModuleHistoryConverter implements HistoryConverter<DemandModuleEventBus> {

    public String onInitDemandModule() {
        return "initDemandModule";
    }

    public String onInitClientList(SearchModuleDataHolder filter) {
        return "initClientList";
    }

    public String onInitSupplierList(SearchModuleDataHolder filter) {
        return "initSupplierList";
    }

    @Override
    public void convertFromToken(String historyName, String param, DemandModuleEventBus eventBus) {
        if (historyName.equals("initDemandModule")) {
            //TODO Martin -- vypitat prihlasenie ak nie je prihlaseny, co by uz ale mal byt ... este overit
            if (Storage.getUser() == null) {
                Window.alert("Please first log in");
            } else {
                eventBus.initDemandModule();
            }
        }

        if (historyName.equals("initClientList")) {
            eventBus.initClientList(null);
        }

        if (historyName.equals("initSupplierList")) {
            eventBus.initSupplierList(null);
        }

//        if (historyName.equals("new")) {
//            eventBus.initSupplierList(null);
//        } else {
//            Window.alert(">" + historyName + "<\n" + param);
//        }

    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
