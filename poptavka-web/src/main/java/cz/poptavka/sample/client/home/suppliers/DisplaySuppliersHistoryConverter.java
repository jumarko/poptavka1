package cz.poptavka.sample.client.home.suppliers;

import java.util.logging.Logger;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

@History(type = HistoryConverterType.NONE, name = "DisplaySuppliers")
public class DisplaySuppliersHistoryConverter implements HistoryConverter<DisplaySuppliersEventBus> {

    private static final Logger LOGGER = Logger.getLogger(DisplaySuppliersHistoryConverter.class.getName());

    @Override
    public void convertFromToken(String historyName, String param, DisplaySuppliersEventBus eventBus) {
//        eventBus.displayMenu();

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
