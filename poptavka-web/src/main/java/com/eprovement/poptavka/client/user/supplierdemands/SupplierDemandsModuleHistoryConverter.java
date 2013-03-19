package com.eprovement.poptavka.client.user.supplierdemands;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

/**
 * History converter class. Handles history for SupplierDemandsModule.
 *
 * @author slavkovsky.martin
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
    /* Convert to token methods.                                              */
    /**************************************************************************/
    /**
     * Creates token with current loaded view's name in it.
     * @return
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
    /* Convert from token method.                                             */
    /**************************************************************************/
    @Override
    public void convertFromToken(String historyName, String param, SupplierDemandsModuleEventBus eventBus) {
        //If application is called by URL, log in user and forward him to overview (goToClientDemandModule.Welcome)
        if (Storage.isAppCalledByURL() != null && Storage.isAppCalledByURL()) {
            Storage.setAppCalledByURL(false);
            eventBus.setHistoryStoredForNextOne(false);
            eventBus.loginFromSession(getCurrentViewConstant(param));
            return;
        }
        eventBus.goToSupplierDemandsModule(null, Constants.NONE);
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