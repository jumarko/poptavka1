package com.eprovement.poptavka.client.home.createSupplier;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;

/**
 * History converter class. Handles history of supplier creation module.
 *
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.DEFAULT, name = "supplierCreation")
public class SupplierCreationHistoryConverter implements HistoryConverter<SupplierCreationEventBus> {

    private static final String HOME = "home";
    private static final String USER = "user";

    public String onGoToCreateSupplierModule() {
        return Storage.getUser() == null ? HOME : USER;
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
        if (param.equals(HOME)) {
            eventBus.goToCreateSupplierModule();
        } else {
            eventBus.setHistoryStoredForNextOne(false);
            eventBus.loginFromSession(Constants.CREATE_SUPPLIER);
        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
