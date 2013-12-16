/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.supplierdemands;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

/**
 * Manages history for SupplierDemands module.
 *
 * @author Martin Slavkovsky
 */
@History(type = HistoryConverterType.DEFAULT, name = "supplierDemands")
public class SupplierDemandsModuleHistoryConverter implements HistoryConverter<SupplierDemandsModuleEventBus> {

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    public static final String NONE_STRING = "welcome";
    public static final String SUPPLIER_POTENTIAL_DEMANDS_STRING = "supplierPotentialDemands";
    public static final String SUPPLIER_OFFERS_STRING = "supplierOffers";
    public static final String SUPPLIER_ASSIGNED_DEMANDS_STRING = "supplierAssignedDemands";
    public static final String SUPPLIER_CLOSED_DEMANDS_STRING = "supplierClosedDemands";
    public static final String SUPPLIER_RATINGS_STRING = "supplierRatings";

    /**************************************************************************/
    /* ConvertToToken events                                                  */
    /**************************************************************************/
    /**
     * Creates token with current loaded view's name in it.
     * @return history token
     */
    public String onCreateTokenForHistory() {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.SUPPLIER_POTENTIAL_DEMANDS:
                return SUPPLIER_POTENTIAL_DEMANDS_STRING;
            case Constants.SUPPLIER_OFFERS:
                return SUPPLIER_OFFERS_STRING;
            case Constants.SUPPLIER_ASSIGNED_DEMANDS:
                return SUPPLIER_ASSIGNED_DEMANDS_STRING;
            case Constants.SUPPLIER_CLOSED_DEMANDS:
                return SUPPLIER_CLOSED_DEMANDS_STRING;
            case Constants.SUPPLIER_RATINGS:
                return SUPPLIER_RATINGS_STRING;
            default:
                return NONE_STRING;
        }
    }

    /**************************************************************************/
    /* ConvertFromToken events                                                */
    /**************************************************************************/
    /**
     * Convert history token into action.
     */
    @Override
    public void convertFromToken(String historyName, String param, SupplierDemandsModuleEventBus eventBus) {
        eventBus.menuStyleChange(Constants.USER_SUPPLIER_MODULE);
        eventBus.supplierMenuStyleChange(getCurrentViewConstant(param));
        //If application is called by URL, log in user and forward him to overview (goToClientDemandModule.Welcome)
        if (Storage.isAppCalledByURL() != null && Storage.isAppCalledByURL()) {
            Storage.setAppCalledByURL(false);
            eventBus.setHistoryStoredForNextOne(false);
            eventBus.loginFromSession(getCurrentViewConstant(param));
            return;
        }
        eventBus.goToSupplierDemandsModule(null, Constants.NONE);
    }

    /**************************************************************************/
    /* Other history evetns                                                   */
    /**************************************************************************/
    @Override
    public boolean isCrawlable() {
        return false;
    }

    /**************************************************************************/
    /* Helper methods.                                                        */
    /**************************************************************************/
    /**
     * Converts history token to current view constant.
     * @param token to be converted
     * @return coresponding constant
     */
    private int getCurrentViewConstant(String token) {
        if (SUPPLIER_POTENTIAL_DEMANDS_STRING.equals(token)) {
            return Constants.SUPPLIER_POTENTIAL_DEMANDS;
        }
        if (SUPPLIER_OFFERS_STRING.equals(token)) {
            return Constants.SUPPLIER_OFFERS;
        }
        if (SUPPLIER_ASSIGNED_DEMANDS_STRING.equals(token)) {
            return Constants.SUPPLIER_ASSIGNED_DEMANDS;
        }
        if (SUPPLIER_CLOSED_DEMANDS_STRING.equals(token)) {
            return Constants.SUPPLIER_CLOSED_DEMANDS;
        }
        if (SUPPLIER_RATINGS_STRING.equals(token)) {
            return Constants.SUPPLIER_RATINGS;
        }
        return Constants.SUPPLIER_DEMANDS_WELCOME;
    }
}