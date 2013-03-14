package com.eprovement.poptavka.client.user.supplierdemands;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.service.demand.UserRPCServiceAsync;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;
import java.util.HashMap;

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
    @Inject
    protected UserRPCServiceAsync service = null;
    //
    public static final String NONE_STRING = "welcome";
    public static final String SUPPLIER_POTENTIAL_DEMANDS_STRING = "supplierPotentialDemands";
    public static final String SUPPLIER_OFFERS_STRING = "supplierOffers";
    public static final String SUPPLIER_ASSIGNED_DEMANDS_STRING = "supplierAssignedDemands";
    //kvoli vytvaraniu a aktualizovaniu tokenov, musim rozlysovat separatory, aby som dokazal
    //aktualizovat spravny podtokne
    public static final String TABLE_SEPARATOR = ">>";
    public static final String FILTER_SEPARATOR = ">>>";
    public static final String ITEM_SEPARATOR = ";";
    public static final String VALUE_SEPARATOR = "=";

    /**************************************************************************/
    /* Convert to token methods.                                              */
    /**************************************************************************/
    public String onGoToSupplierDemandsModule(SearchModuleDataHolder filtersHolder, int loadWidget) {
        StringBuilder token = new StringBuilder();
        if (filtersHolder == null) {
            token.append("widget");
            token.append(VALUE_SEPARATOR);
            //TODO LATER Martin - loadWidget - dat meno, nie id
            token.append(getWidgetName(loadWidget));
            if (loadWidget != Constants.NONE) {
                createTokenPart(token, 0, -1L);
            }
        } else {
            token.append(com.google.gwt.user.client.History.getToken());
            token.append(FILTER_SEPARATOR);
            token.append(filtersHolder.toStringWithIDs());
        }
        return token.toString();
    }

    public String onCreateTokenForHistory(int tablePage, long selectedId) {
        String oldToken = com.google.gwt.user.client.History.getToken();
        String oldParams = oldToken.substring(oldToken.indexOf("?") + 1, oldToken.length());
        StringBuilder newToken = new StringBuilder();
        int lastSeparatorIndex = oldParams.indexOf(TABLE_SEPARATOR);
        //toto robim preto, ze ked zavolam 2x po sebe createTokenForHistory,
        //tak sa mi aktualizuje naposledny pridana cast tokenu
        if (lastSeparatorIndex == -1) {
            newToken.append(oldParams);
        } else {
            newToken.append(oldParams.substring(0, lastSeparatorIndex));
        }
        createTokenPart(newToken, tablePage, selectedId);
        return newToken.toString();
    }

    /**************************************************************************/
    /* Convert from token method.                                             */
    /**************************************************************************/
    @Override
    public void convertFromToken(String historyName, String param, final SupplierDemandsModuleEventBus eventBus) {
        //If application is called by URL, log in user and forward him to overview (goToSupplierDemandModule.Welcome)
        if (Storage.isAppCalledByURL() != null && Storage.isAppCalledByURL()) {
            Storage.setAppCalledByURL(false);
            eventBus.setHistoryStoredForNextOne(false);
            eventBus.loginFromSession(Constants.NONE);
            return;
        }

        if (param == null) { //nikdy nebude null, predsa aspon widget=welcome minimalne
            eventBus.setHistoryStoredForNextOne(false);
            eventBus.goToSupplierDemandsModule(null, Constants.NONE);
        } else {
            HashMap<String, String> tokenParts = getTokenParts(param);
            String[] tableParts;

            switch (parseWidgetName(tokenParts.get("").split(VALUE_SEPARATOR)[1])) { //e.g: widget=supplierDemands
                case Constants.SUPPLIER_POTENTIAL_DEMANDS:
                    tableParts = tokenParts.get(TABLE_SEPARATOR).split(ITEM_SEPARATOR);

                    if (tokenParts.get(TABLE_SEPARATOR) != null) {
                        eventBus.initSupplierDemandsByHistory(
                                //parentTablePage
                                Integer.parseInt(tableParts[0].split(VALUE_SEPARATOR)[1]),
                                Long.parseLong(tableParts[1].split(VALUE_SEPARATOR)[1]),
                                SearchModuleDataHolder.parseSearchModuleDataHolder(tokenParts.get(FILTER_SEPARATOR)));
                    }
                    break;
                case Constants.SUPPLIER_OFFERS: //e.g: page=0;id=133
                    tableParts = tokenParts.get(TABLE_SEPARATOR).split(ITEM_SEPARATOR);

                    if (tokenParts.get(TABLE_SEPARATOR) != null) {
                        eventBus.initSupplierOffersByHistory(
                                Integer.parseInt(tableParts[0].split(VALUE_SEPARATOR)[1]),
                                Long.parseLong(tableParts[1].split(VALUE_SEPARATOR)[1]),
                                SearchModuleDataHolder.parseSearchModuleDataHolder(tokenParts.get(FILTER_SEPARATOR)));
                    }


                    break;
                case Constants.SUPPLIER_ASSIGNED_DEMANDS:
                    tableParts = tokenParts.get(TABLE_SEPARATOR).split(ITEM_SEPARATOR);

                    if (tokenParts.get(TABLE_SEPARATOR) != null) {
                        eventBus.initSupplierAssignedDemandsByHistory(
                                Integer.parseInt(tableParts[0].split(VALUE_SEPARATOR)[1]),
                                Long.parseLong(tableParts[1].split(VALUE_SEPARATOR)[1]),
                                SearchModuleDataHolder.parseSearchModuleDataHolder(tokenParts.get(FILTER_SEPARATOR)));
                    }
                    break;
                default:
                    eventBus.setHistoryStoredForNextOne(false);
                    eventBus.goToSupplierDemandsModule(
                            SearchModuleDataHolder.parseSearchModuleDataHolder(tokenParts.get(FILTER_SEPARATOR)),
                            Constants.NONE);
                    break;
            }
        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }

    /**************************************************************************/
    /* Helper methods.                                                        */
    /**************************************************************************/
    private String createTokenPart(StringBuilder token, int tablePage, long selectedId) {
        token.append(TABLE_SEPARATOR);
        token.append("page");
        token.append(VALUE_SEPARATOR);
        token.append(tablePage);
        token.append(ITEM_SEPARATOR);
        token.append("id");
        token.append(VALUE_SEPARATOR);
        token.append(selectedId);
        return token.toString();
    }

    private HashMap<String, String> getTokenParts(String token) {
        HashMap<String, String> tokenParts = new HashMap<String, String>();
        //Token like:
        //widget=10>page;id>>>filter
        /** WIDGET. **/
        //parse-> widget=10
        int tableSeparatorIndex = token.indexOf(TABLE_SEPARATOR);
        if (tableSeparatorIndex == -1) {
            tokenParts.put("", token);
        } else {
            tokenParts.put("", token.substring(0, tableSeparatorIndex));
            /** TABLE. **/
            //parse-> page;id
            int filterSeparatorIndex = token.indexOf(FILTER_SEPARATOR);
            if (filterSeparatorIndex == -1) {
                tokenParts.put(
                        TABLE_SEPARATOR,
                        token.substring(tableSeparatorIndex + TABLE_SEPARATOR.length(), token.length()));
            } else {
                tokenParts.put(
                        TABLE_SEPARATOR,
                        token.substring(tableSeparatorIndex + TABLE_SEPARATOR.length(),
                        filterSeparatorIndex));
                /** FILTER. **/
                //add last item - filter's part of token
                tokenParts.put(
                        FILTER_SEPARATOR,
                        token.substring(filterSeparatorIndex + FILTER_SEPARATOR.length(), token.length()));
            }
        }
        return tokenParts;
    }

    private String getWidgetName(int widgetConst) {
        switch (widgetConst) {
            case Constants.SUPPLIER_POTENTIAL_DEMANDS:
                return SUPPLIER_POTENTIAL_DEMANDS_STRING;
            case Constants.SUPPLIER_OFFERS:
                return SUPPLIER_OFFERS_STRING;
            case Constants.SUPPLIER_ASSIGNED_DEMANDS:
                return SUPPLIER_ASSIGNED_DEMANDS_STRING;
            default:
                return NONE_STRING;
        }
    }

    private int parseWidgetName(String widgetName) {
        if (widgetName.equals(SUPPLIER_POTENTIAL_DEMANDS_STRING)) {
            return Constants.SUPPLIER_POTENTIAL_DEMANDS;
        } else if (widgetName.equals(SUPPLIER_OFFERS_STRING)) {
            return Constants.SUPPLIER_OFFERS;
        } else if (widgetName.equals(SUPPLIER_ASSIGNED_DEMANDS_STRING)) {
            return Constants.SUPPLIER_ASSIGNED_DEMANDS;
        } else {
            return Constants.NONE;
        }
    }
}