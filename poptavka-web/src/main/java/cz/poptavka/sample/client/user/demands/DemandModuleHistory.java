package cz.poptavka.sample.client.user.demands;

import com.google.gwt.user.client.Window;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;


@History(type = HistoryConverterType.DEFAULT)
public class DemandModuleHistory implements HistoryConverter<DemandModuleEventBus> {

    public String onInitSupplierList(SearchModuleDataHolder filter) {
        return "haha";
    }


    @Override
    public void convertFromToken(String historyName, String param,
            DemandModuleEventBus eventBus) {
        if (historyName.equals("new")) {
            eventBus.initSupplierList(null);
        } else {
            Window.alert(">" + historyName + "<\n" + param);
        }

    }

    @Override
    public boolean isCrawlable() {
        return false;
    }

}
