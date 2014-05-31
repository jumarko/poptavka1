/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.home.createDemand;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.home.createDemand.interfaces.IDemandCreationModule;

/**
 * Manages history for DemandCreation module.
 *
 * @author Martin Slavkovsky
 */
@History(type = HistoryConverterType.DEFAULT, name = IDemandCreationModule.NAME)
public class DemandCreationHistoryConverter implements HistoryConverter<DemandCreationEventBus> {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private static final String HOME = "home";
    private static final String USER = "user";

    /**************************************************************************/
    /* ConvertToToken events                                                  */
    /**************************************************************************/
    /*
     * Creates history token for DemandCreation module.
     */
    public String onGoToCreateDemandModule() {
        return Storage.getUser() == null ? HOME : USER;
    }

    /**************************************************************************/
    /* ConvertToToken events                                                  */
    /**************************************************************************/
    /**
     * Convert history token to action.
     * Called either when browser action <b>back</b> or <b>forward</b> is evocated
     * or by clicking on <b>hyperlink</b> with set token.
     *
     * @param methodName - name of the called method
     * @param param - string behind '?' in url (module/method?param).
     *                URL creates onGoToCreateDemandModule method in DemandCreationHistoryConverter class.
     * @param eventBus - DemandCreationEventBus
     */
    @Override
    public void convertFromToken(String methodName, String param, DemandCreationEventBus eventBus) {
        if (param.equals(HOME)) {
            eventBus.goToCreateDemandModule();
        } else {
            eventBus.setHistoryStoredForNextOne(false);
            eventBus.loginFromSession(Constants.CREATE_DEMAND);
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
