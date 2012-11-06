package com.eprovement.poptavka.client.user.clientdemands;

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
 * History converter class. Handles history for ClientDemandsModule.
 *
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.DEFAULT, name = "clientDemands")
public class ClientDemandsModuleHistoryConverter implements HistoryConverter<ClientDemandsModuleEventBus> {

    public static final String DEMAND = "demand";
    //kvoli vytvaraniu a aktualizovaniu tokenov, musim rozlysovat separatory, aby som dokazal
    //aktualizovat spravny podtokne
    public static final String PARENT_TABLE_SEPARATOR = ">";
    public static final String CHILD_TABLE_SEPARATOR = ">>";
    public static final String FILTER_SEPARATOR = ">>>";
    public static final String ITEM_SEPARATOR = ";";
    public static final String VALUE_SEPARATOR = "=";
    @Inject
    protected UserRPCServiceAsync service = null;

    public String onGoToClientDemandsModule(SearchModuleDataHolder filtersHolder, int loadWidget) {
        StringBuilder token = new StringBuilder();
        if (filtersHolder == null) {
            token.append("widget");
            token.append(VALUE_SEPARATOR);
            //TODO Martin - loadWidget - dat meno, nie id
            token.append(loadWidget);

        } else {
            token.append(com.google.gwt.user.client.History.getToken());
            token.append(FILTER_SEPARATOR);
            token.append(filtersHolder.toStringWithIDs());
        }
        return token.toString();
    }

    /**
     * widget=..;page=..;id=..
     * @param loadedWidget
     * @param page1
     * @param objectID
     * @return
     */
    public String onCreateTokenForHistory1(int parentTablePage) {
        String oldToken = com.google.gwt.user.client.History.getToken();
        String oldParams = oldToken.substring(oldToken.indexOf("?") + 1, oldToken.length());
        StringBuilder newToken = new StringBuilder();
        int lastSeparatorIndex = oldParams.indexOf(PARENT_TABLE_SEPARATOR);
        //toto robim preto, ze ked zavolam 2x po sebe createTokenForHistory,
        //tak sa mi aktualizuje naposledny pridana cast tokenu
        if (lastSeparatorIndex == -1) {
            newToken.append(oldParams);
        } else {
            newToken.append(oldParams.substring(0, lastSeparatorIndex));
        }
        newToken.append(PARENT_TABLE_SEPARATOR);
        newToken.append("page1");
        newToken.append(VALUE_SEPARATOR);
        newToken.append(parentTablePage);
        return newToken.toString();
    }

    /**
     *
     * @param parentId
     * @param childTablePage
     * @param childId
     * @return
     */
    public String onCreateTokenForHistory2(long parentId, int childTablePage, long childId) {
        String oldToken = com.google.gwt.user.client.History.getToken();
        String oldParams = oldToken.substring(oldToken.indexOf("?") + 1, oldToken.length());
        StringBuilder newToken = new StringBuilder();
        int lastSeparatorIndex = oldParams.indexOf(CHILD_TABLE_SEPARATOR);
        if (lastSeparatorIndex == -1) {
            newToken.append(oldParams);
        } else {
            newToken.append(oldParams.substring(0, lastSeparatorIndex));
        }
        newToken.append(CHILD_TABLE_SEPARATOR);
        newToken.append("id1");
        newToken.append(VALUE_SEPARATOR);
        newToken.append(parentId);
        newToken.append(ITEM_SEPARATOR);
        newToken.append("page2");
        newToken.append(VALUE_SEPARATOR);
        newToken.append(childTablePage);
        newToken.append(ITEM_SEPARATOR);
        newToken.append("id2");
        newToken.append(VALUE_SEPARATOR);
        newToken.append(childId);
        return newToken.toString();
    }

    public String onCreateTokenForHistory3(int parentTablePage, long parentId) {
        String oldToken = com.google.gwt.user.client.History.getToken();
        String oldParams = oldToken.substring(oldToken.indexOf("?") + 1, oldToken.length());
        StringBuilder newToken = new StringBuilder();
        int lastSeparatorIndex = oldParams.indexOf(PARENT_TABLE_SEPARATOR);
        //toto robim preto, ze ked zavolam 2x po sebe createTokenForHistory,
        //tak sa mi aktualizuje naposledny pridana cast tokenu
        if (lastSeparatorIndex == -1) {
            newToken.append(oldParams);
        } else {
            newToken.append(oldParams.substring(0, lastSeparatorIndex));
        }
        newToken.append(PARENT_TABLE_SEPARATOR);
        newToken.append("page1");
        newToken.append(VALUE_SEPARATOR);
        newToken.append(parentTablePage);
        newToken.append(ITEM_SEPARATOR);
        newToken.append("id1");
        newToken.append(VALUE_SEPARATOR);
        newToken.append(parentId);
        return newToken.toString();
    }

    @Override
    public void convertFromToken(String historyName, String param, final ClientDemandsModuleEventBus eventBus) {
        if (Storage.isAppCalledByURL() != null && Storage.isAppCalledByURL()) {
            // login from session method
            eventBus.loginFromSession();
        }
        if (param == null) { //nikdy nebude null, predsa aspon widget=10 bude nie? --->>> upravit podmienky
            eventBus.goToHomeSuppliersModule(null);
        } else {
            HashMap<String, String> tokenParts = getTokenParts(param);
            String[] parentTableParts;
            String[] childTableParts;

            switch (Integer.parseInt(tokenParts.get("").split(VALUE_SEPARATOR)[1])) { //e.g: widget=10
                case Constants.CLIENT_DEMANDS:
                    //Child table
                    if (tokenParts.get(CHILD_TABLE_SEPARATOR) != null) {
                        childTableParts = tokenParts.get(CHILD_TABLE_SEPARATOR).split(ITEM_SEPARATOR);
                        eventBus.getClientDemandAndInitClientDemandConversationByHistory(
                                Long.parseLong(childTableParts[0].split(VALUE_SEPARATOR)[1]), //parentId
                                Integer.parseInt(childTableParts[1].split(VALUE_SEPARATOR)[1]), //childTablePage
                                Long.parseLong(childTableParts[2].split(VALUE_SEPARATOR)[1]), //childId
                                SearchModuleDataHolder.parseSearchModuleDataHolder(tokenParts.get(FILTER_SEPARATOR)));
                        return; //vs. break;
                    }
                    //Parent table
                    if (tokenParts.get(PARENT_TABLE_SEPARATOR) != null) {
                        eventBus.initClientDemandsByHistory(
                                //parentTablePage
                                Integer.parseInt(tokenParts.get(PARENT_TABLE_SEPARATOR).split(VALUE_SEPARATOR)[1]),
                                SearchModuleDataHolder.parseSearchModuleDataHolder(tokenParts.get(FILTER_SEPARATOR)));
                    }
                    break;
                case Constants.CLIENT_OFFERED_DEMANDS: //e.g: page=0;id=133
                    //Child table
                    if (tokenParts.get(CHILD_TABLE_SEPARATOR) != null) {
                        childTableParts = tokenParts.get(CHILD_TABLE_SEPARATOR).split(ITEM_SEPARATOR);
                        eventBus.getClientOfferedDemandAndInitClientOfferedDemandOffersByHistory(
                                Long.parseLong(childTableParts[0].split(VALUE_SEPARATOR)[1]), //parentId
                                Integer.parseInt(childTableParts[1].split(VALUE_SEPARATOR)[1]), //childTablePage
                                Long.parseLong(childTableParts[2].split(VALUE_SEPARATOR)[1]), //childId
                                SearchModuleDataHolder.parseSearchModuleDataHolder(tokenParts.get(FILTER_SEPARATOR)));
                    }
                    //Parent table
                    if (tokenParts.get(PARENT_TABLE_SEPARATOR) != null) {
                        eventBus.initClientOfferedDemandsByHistory(
                                Integer.parseInt(tokenParts.get(PARENT_TABLE_SEPARATOR).split(VALUE_SEPARATOR)[1]),
                                SearchModuleDataHolder.parseSearchModuleDataHolder(tokenParts.get(FILTER_SEPARATOR)));
                    }


                    break;
                case Constants.CLIENT_ASSIGNED_DEMANDS:
                    parentTableParts = tokenParts.get(PARENT_TABLE_SEPARATOR).split(ITEM_SEPARATOR);
                    //parse filter
                    eventBus.initClientAssignedDemandsByHistory(
                            Integer.parseInt(parentTableParts[0].split(VALUE_SEPARATOR)[1]),
                            Long.parseLong(parentTableParts[0].split(VALUE_SEPARATOR)[1]),
                            SearchModuleDataHolder.parseSearchModuleDataHolder(tokenParts.get(FILTER_SEPARATOR)));
                    break;
                default:
                    eventBus.goToHomeSuppliersModule(
                            SearchModuleDataHolder.parseSearchModuleDataHolder(tokenParts.get(FILTER_SEPARATOR)));
                    break;
            }
        }
    }

    private HashMap<String, String> getTokenParts(String token) {
        HashMap<String, String> tokenParts = new HashMap<String, String>();
        //Token like:
        //widget=10>page1>>id1;page2;id2>>>filter
        /** WIDGET. **/
        //parse-> widget=10
        int parentSeparatorIndex = token.indexOf(PARENT_TABLE_SEPARATOR);
        if (parentSeparatorIndex == -1) {
            tokenParts.put("", token);
        } else {
            tokenParts.put("", token.substring(0, parentSeparatorIndex));
            /** PARENT. **/
            //parse-> page1
            int childSeparatorIndex = token.indexOf(CHILD_TABLE_SEPARATOR);
            if (childSeparatorIndex == -1) {
                tokenParts.put(
                        PARENT_TABLE_SEPARATOR,
                        token.substring(parentSeparatorIndex + PARENT_TABLE_SEPARATOR.length(), token.length()));
            } else {
                tokenParts.put(
                        PARENT_TABLE_SEPARATOR,
                        token.substring(parentSeparatorIndex + PARENT_TABLE_SEPARATOR.length(), childSeparatorIndex));
                /** CHILD. **/
                //parse-> id1;page2;id2
                int filterSeparatorIndex = token.indexOf(FILTER_SEPARATOR);
                if (filterSeparatorIndex == -1) {
                    tokenParts.put(
                            CHILD_TABLE_SEPARATOR,
                            token.substring(childSeparatorIndex + CHILD_TABLE_SEPARATOR.length(), token.length()));
                } else {
                    tokenParts.put(
                            CHILD_TABLE_SEPARATOR,
                            token.substring(childSeparatorIndex + CHILD_TABLE_SEPARATOR.length(),
                            filterSeparatorIndex));
                    /** FILTER. **/
                    //add last item - filter's part of token
                    tokenParts.put(
                            FILTER_SEPARATOR,
                            token.substring(filterSeparatorIndex + FILTER_SEPARATOR.length(), token.length()));
                }
            }
        }
        return tokenParts;
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
