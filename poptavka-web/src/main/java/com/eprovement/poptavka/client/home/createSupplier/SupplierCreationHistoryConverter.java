/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.home.createSupplier;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.home.createSupplier.interfaces.ISupplierCreationModule;

/**
 * Manages history for supplier creation module.
 *
 * @author Martin Slavkovsky
 */
@History(type = HistoryConverterType.DEFAULT, name = ISupplierCreationModule.NAME)
public class SupplierCreationHistoryConverter implements HistoryConverter<SupplierCreationEventBus> {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private static final String HOME = "home";
    private static final String USER = "user";

    /**************************************************************************/
    /* ConvertToToken events                                                  */
    /**************************************************************************/
    /**
     * Creates history token for supplier creation module.
     * @return created history token
     */
    public String onGoToCreateSupplierModule() {
        return Storage.getUser() == null ? HOME : USER;
    }

    /**************************************************************************/
    /* ConvertFromToken                                                       */
    /**************************************************************************/
    /**
     * Convert history token into action.
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

    /**************************************************************************/
    /* Other events                                                           */
    /**************************************************************************/
    @Override
    public boolean isCrawlable() {
        return false;
    }
}
