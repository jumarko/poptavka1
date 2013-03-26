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

    public String onRegisterTabToken(int tab) {
        return "tab=" + (tab + 1);
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
        if (Storage.getUser() == null) {
            eventBus.menuStyleChange(Constants.CREATE_SUPPLIER);
        } else {
            eventBus.userMenuStyleChange(Constants.CREATE_SUPPLIER);
        }

        //if true => URL invocation, because
        //if app is running and module is called, that's module start method is called first
        //and then start method of root module is called.
        //If hisotryOnStart is called, it is the opposite.
        if (Storage.isAppCalledByURL() != null && Storage.isAppCalledByURL()) {
            eventBus.goToCreateSupplierModule();
        } else {
            int tab = Integer.parseInt(param.split("=")[1]);
            //case when supplier registered and back performed
            //--> forward to supplier creation module again
            if (tab == 4 && Storage.getUser() != null) {
                eventBus.goToCreateSupplierModule();
            //otherwise only select wanted tab
            } else {
                eventBus.goToCreateSupplierModuleByHistory(tab - 1);
            }
        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
