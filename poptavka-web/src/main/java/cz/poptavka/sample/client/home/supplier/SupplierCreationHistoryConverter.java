package cz.poptavka.sample.client.home.supplier;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;
import cz.poptavka.sample.client.main.Storage;

/**
 * History converter class. Handles history of supplier creation module.
 *
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.DEFAULT, name = "supplierCreation")
public class SupplierCreationHistoryConverter implements HistoryConverter<SupplierCreationEventBus> {

    /**
     * Creates token(URL) for goToCreateSupplierModule method.
     *
     * @return token string like module/method?param, where param = home / user
     */
    public String onGoToCreateSupplierModule() {
        /* Martin - Nemusi to byt, pretoze v convertFromToken to neberiem vobec do uvahy.
        Ale pre testovacie ucely ale vhodne. Potom moze odstranit */
        if (Storage.getUser() != null) {
            return "location=user";
        } else {
            return "location=home";
        }
    }

    /**
     * Called either when browser action <b>back</b> or <b>forward</b> is evocated,
     * or by clicking on <b>hyperlink</b> with set token.
     *
     * @param methodName - name of the called method
     * @param param - string behind '?' in url (module/method?param).
     *                URL creates onGoToCreateSupplierModule method in SupplierCreationHistoryConverter class.
     * @param eventBus - SupplierCreationEventBus
     */
    @Override
    public void convertFromToken(String methodName, String param, SupplierCreationEventBus eventBus) {
        if (methodName.equals("goToCreateSupplierModule")) {
            eventBus.goToCreateSupplierModule();
        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
