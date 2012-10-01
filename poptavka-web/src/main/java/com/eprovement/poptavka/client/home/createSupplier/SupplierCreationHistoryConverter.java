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

    /**
     * Creates token(URL) for goToCreateSupplierModule method.
     *
     * @return token string like module/method?param, where param = home / user
     */
    public String onGoToCreateSupplierModule() {
        /* Martin - Nemusi to byt, pretoze v convertFromToken to neberiem vobec do uvahy.
        Ale pre testovacie ucely ale vhodne. Potom moze odstranit */
        if (Storage.getUser() == null) {
            return "location=home";
        } else {
            return "location=user";
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
        if (Storage.getUser() == null) {
            eventBus.menuStyleChange(Constants.HOME_SUPPLIER_CREATION_MODULE);
        } else {
            eventBus.userMenuStyleChange(Constants.USER_DEMANDS_MODULE);
        }
        //if true => URL invocation, because
        //if app is running and module is called, that's module start method is called first
        //and then start method of root module is called.
        //If hisotryOnStart is called, it is the opposite.
        if (Storage.isAppCalledByURL()) {
            eventBus.goToCreateSupplierModule();
        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
