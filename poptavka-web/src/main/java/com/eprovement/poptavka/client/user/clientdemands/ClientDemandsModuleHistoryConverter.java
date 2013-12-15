/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.clientdemands;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

/**
 * Manages history for ClientDemands module.
 *
 * @author Martin Slavkovsky
 */
@History(type = HistoryConverterType.DEFAULT, name = "clientDemands")
public class ClientDemandsModuleHistoryConverter implements HistoryConverter<ClientDemandsModuleEventBus> {

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    public static final String NONE_STRING = "welcome";
    public static final String CLIENT_DEMANDS_STRING = "clientDemands";
    public static final String CLIENT_OFFERED_DEMANDS_STRING = "clientOfferedDemands";
    public static final String CLIENT_ASSIGNED_DEMANDS_STRING = "clientAssignedDemands";
    public static final String CLIENT_CLOSED_DEMANDS_STRING = "clientClosedDemands";
    public static final String CLIENT_RATINGS_STRING = "clientRatings";

    /**************************************************************************/
    /* Convert to token methods.                                              */
    /**************************************************************************/
    /**
     * Creates token with current loaded view's name in it.
     * @return history token
     */
    public String onCreateTokenForHistory() {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.CLIENT_DEMANDS:
                return CLIENT_DEMANDS_STRING;
            case Constants.CLIENT_OFFERED_DEMANDS:
                return CLIENT_OFFERED_DEMANDS_STRING;
            case Constants.CLIENT_ASSIGNED_DEMANDS:
                return CLIENT_ASSIGNED_DEMANDS_STRING;
            case Constants.CLIENT_CLOSED_DEMANDS:
                return CLIENT_CLOSED_DEMANDS_STRING;
            case Constants.CLIENT_RATINGS:
                return CLIENT_RATINGS_STRING;
            default:
                return NONE_STRING;
        }
    }

    /**************************************************************************/
    /* Convert from token method.                                             */
    /**************************************************************************/
    /**
     * Creates history token into action.
     */
    @Override
    public void convertFromToken(String historyName, String param, ClientDemandsModuleEventBus eventBus) {
        eventBus.menuStyleChange(Constants.USER_CLIENT_MODULE);
        eventBus.clientDemandsMenuStyleChange(getCurrentViewConstant(param));
        //If application is called by URL, log in user and forward him to overview (goToClientDemandModule.Welcome)
        if (Storage.isAppCalledByURL() != null && Storage.isAppCalledByURL()) {
            Storage.setAppCalledByURL(false);
            eventBus.setHistoryStoredForNextOne(false);
            eventBus.loginFromSession(getCurrentViewConstant(param));
            return;
        }
        eventBus.goToClientDemandsModule(null, Constants.NONE);
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }

    /**************************************************************************/
    /* Helper methods.                                                        */
    /**************************************************************************/
    /**
     * Converts history token to current view constant.
     * @param token
     * @return
     */
    private int getCurrentViewConstant(String token) {
        if (CLIENT_DEMANDS_STRING.equals(token)) {
            return Constants.CLIENT_DEMANDS;
        }
        if (CLIENT_OFFERED_DEMANDS_STRING.equals(token)) {
            return Constants.CLIENT_OFFERED_DEMANDS;
        }
        if (CLIENT_ASSIGNED_DEMANDS_STRING.equals(token)) {
            return Constants.CLIENT_ASSIGNED_DEMANDS;
        }
        if (CLIENT_CLOSED_DEMANDS_STRING.equals(token)) {
            return Constants.CLIENT_CLOSED_DEMANDS;
        }
        if (CLIENT_RATINGS_STRING.equals(token)) {
            return Constants.CLIENT_RATINGS;
        }
        return Constants.CLIENT_DEMANDS_WELCOME;
    }
}